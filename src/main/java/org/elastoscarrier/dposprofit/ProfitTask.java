package org.elastoscarrier.dposprofit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.elastos.util.HttpKit;
import org.elastoscarrier.dposprofit.Utils.Constants;
import org.elastoscarrier.dposprofit.Utils.ELAUtils;
import org.elastoscarrier.dposprofit.entity.ELAResultGenTx;
import org.elastoscarrier.dposprofit.entity.History;
import org.elastoscarrier.dposprofit.entity.HistoryResult;
import org.elastoscarrier.dposprofit.entity.ResultHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProfitTask {
    private static final Logger log = LoggerFactory.getLogger(ProfitTask.class);

    private static Long nextProfitBlock;
    private static Long nextQueryPage = 1L;

    @Value("${common.startProfitBlock}")
    public void setNextProfitBlock(Long startProfitBlock) {
        nextProfitBlock = startProfitBlock;
    }

    @Value("${common.profitCircles}")
    private Integer profitCircles;

    @Value("${common.historyServiceURL}")
    private String historyServiceURL;

    @Value("${common.ownerPublicKey}")
    private String ownerPublicKey;

    @Value("${common.rewardAddress}")
    private String rewardAddress;

    @Value("${profit.account.address}")
    private String profitAccountAddress;

    @Value("${profit.account.privateKey}")
    private String profitAccountPrivateKey;


    @Scheduled(cron = "0 0 0/2 * * *")
    public void profit() {

        long thisTimeStartProfitBlock = nextProfitBlock;
        long thisTimeStartQueryPage = nextQueryPage;
        int i = 0;

        Map<String, Long> profitDetail = new LinkedHashMap<>(1000);

        profitDetail.put(Constants.MAINTAINER_ADDRESS, 0L);
        profitDetail.put(Constants.OWNER_ADDRESS, 0L);

        end:
        while(true) {

            String result = HttpKit.get(historyServiceURL + "/api/1/history/" + rewardAddress + "?pageSize=" + profitCircles + "&pageNum=" + nextQueryPage);
            Type type = new TypeReference<ResultHistory<HistoryResult>>() {}.getType();
            ResultHistory<HistoryResult> resultHistory = JSON.parseObject(result, type);

            if(resultHistory.getStatus() != 200) {
                log.warn("获取地址 [{}] 历史记录失败", rewardAddress);
                break;
            }

            for(History history : resultHistory.getResult().getHistory()) {

                long currentProfitBlock = history.getHeight();

                //如果还没到要分红处理的块 或者 当前块不是CoinBase交易则跳过
                if(currentProfitBlock < nextProfitBlock || !history.getTxType().equals("CoinBase")) {
                    continue;
                }

                if(i >= profitCircles) {
                    nextProfitBlock = currentProfitBlock;
                    break end;
                }

                log.info("当前处理的块为 [{}]", currentProfitBlock);
                log.debug("超级节点从高度为 [{}] 的块中获取投票总奖励为: {}", currentProfitBlock, history.getValue());

                //DO profit
                try {
                    doProfit(currentProfitBlock,history.getValue(), profitDetail);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                i++;
            }
            if(resultHistory.getResult().getHistory().size() == profitCircles) {
                nextQueryPage++;
            } else {
                log.info("当前未处理的块不足 [{}] 下次开始处理的块为 [{}]", profitCircles, nextProfitBlock);
                nextQueryPage = thisTimeStartQueryPage;
                return;
            }
        }

        if(profitDetail.size() == 0) {
            log.info("本次统计结果没有投票者");
            return;
        }

        try {
            String rawTransactionStr = ELAUtils.generateTransaction(ELAUtils.getUTXOs(profitAccountAddress), profitDetail, profitAccountPrivateKey, profitAccountAddress);
            ELAResultGenTx elaResultGenTx = JSON.parseObject(rawTransactionStr, ELAResultGenTx.class);
            ELAUtils.sendTransaction(elaResultGenTx.getResult().get("rawTx"));
            log.info("下次开始处理的块为 [{}]", nextProfitBlock);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发送交易异常，下次开始处理的块为 [{}]", thisTimeStartProfitBlock);
            nextProfitBlock = thisTimeStartProfitBlock;
            nextQueryPage = thisTimeStartQueryPage;
        }
    }

    private void doProfit(long currentProfitBlock,long superNodeProfitValue, Map<String, Long> profitDetail) throws Exception {
        //使用上上上轮最后一个块的排名和投票情况
        long profitDependsBlock = currentProfitBlock - 73;
        long superNodeVoteNum = 0;

        //获取特定块的超级节点排名
        String resultRank = HttpKit.get(historyServiceURL +"/api/1/dpos/rank/height/" + profitDependsBlock);
        Type type = new TypeReference<ResultHistory<List<Map<String, String>>>>() {}.getType();
        ResultHistory<List<Map<String, String>>> resultHistory = JSON.parseObject(resultRank, type);

        if(resultHistory.getStatus() != 200) {
            log.error("获取块 [{}] 超级节点排名失败", profitDependsBlock);
            throw new Exception("获取块超级节点排名失败");
        }

        for(Map<String, String> entry : resultHistory.getResult()) {
            long voteValue = Double.valueOf(entry.get("Votes")).longValue();
            if(entry.get("Producer_public_key").equals(ownerPublicKey)) {
                superNodeVoteNum = voteValue;
            }
        }
        log.debug("超级节点在高度 [{}] 获得的总投票: {}", profitDependsBlock, superNodeVoteNum);

        //获取特定块超级节点投票详情
        String resultVoteStatics = HttpKit.get(historyServiceURL + "/api/1/dpos/producer/" + ownerPublicKey + "/height/" + profitDependsBlock);
        Type type2 = new TypeReference<ResultHistory<List<Map<String, String>>>>() {}.getType();
        ResultHistory<List<Map<String, String>>> resultHistory2 = JSON.parseObject(resultVoteStatics, type2);

        if(resultHistory2.getStatus() != 200) {
            log.error("获取块 [{}] 超级节点投票详情失败", profitDependsBlock);
            throw new Exception("获取块超级节点投票详情失败");
        }

        voterProfitMethod(superNodeVoteNum, superNodeProfitValue, resultHistory2.getResult(), profitDetail);
    }

    private void voterProfitMethod(long superNodeVoteNum, long superNodeProfitValue, List<Map<String, String>> result, Map<String, Long> profitDetail) {

        superNodeVoteNum += 360000;
//        long profitValuePerVote = (superNodeProfitValue - 1800000) * 8 / 10 / superNodeVoteNum;
        long profitValuePerVote = (superNodeProfitValue - 1800000)  / superNodeVoteNum;
        long reservedValue = profitValuePerVote * 180000;

        profitDetail.put(Constants.MAINTAINER_ADDRESS, profitDetail.get(Constants.MAINTAINER_ADDRESS) + reservedValue);
        profitDetail.put(Constants.OWNER_ADDRESS, profitDetail.get(Constants.OWNER_ADDRESS) + reservedValue);

        for(Map<String, String> entry : result) {
            String voteAddress = entry.get("Address");
            if(profitDetail.containsKey(voteAddress)) {
                profitDetail.put(voteAddress, profitDetail.get(voteAddress) + Double.valueOf(entry.get("Value")).longValue() * profitValuePerVote);
            } else {
                profitDetail.put(voteAddress, Double.valueOf(entry.get("Value")).longValue() * profitValuePerVote);
            }
        }
    }
}
