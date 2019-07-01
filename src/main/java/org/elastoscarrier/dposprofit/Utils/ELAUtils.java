package org.elastoscarrier.dposprofit.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import net.sf.json.JSONObject;
import org.elastos.api.SingleSignTransaction;
import org.elastos.util.HttpKit;
import org.elastoscarrier.dposprofit.ProfitTask;
import org.elastoscarrier.dposprofit.entity.ELAJsonRpcRequest;
import org.elastoscarrier.dposprofit.entity.ELAJsonRpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;


public class ELAUtils {

    private static final Logger log = LoggerFactory.getLogger(ProfitTask.class);

    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("application");

    private final static String nodeURL = resourceBundle.getString("common.nodeURL");

    private final static String memo = resourceBundle.getString("profit.memo");

    private final static Long fee = Long.parseLong(resourceBundle.getString("transaction.fee"));

    private final static String secret = resourceBundle.getString("common.nodeAccessSecret");

    private static Map<String, String> JSONRpcRequestHeader = new HashMap<>(2);

    static {
        JSONRpcRequestHeader.put("Authorization", "Basic " + secret);
        JSONRpcRequestHeader.put("Content-Type", "application/json");
    }

    public static List<Map<String, String>> getUTXOs(String address) throws Exception {
        Map<String, String[]> requestData = new HashMap<>(1);
        requestData.put("addresses", new String[] {address});

        ELAJsonRpcRequest<Map<String, String[]>> elaJsonRpcRequest = new ELAJsonRpcRequest<>();
        elaJsonRpcRequest.setMethod("listunspent");
        elaJsonRpcRequest.setParams(requestData);

        String result = HttpKit.post(nodeURL, JSON.toJSONString(elaJsonRpcRequest), JSONRpcRequestHeader);
        Type type = new TypeReference<ELAJsonRpcResponse<List<Map<String, String>>>>() {}.getType();
        ELAJsonRpcResponse<List<Map<String, String>>> elaJsonRpcResponse = JSON.parseObject(result, type);

        if(elaJsonRpcResponse.getError() != null) {
            log.error("获取地址 [{}] UTXO失败 [{}]", address, elaJsonRpcResponse);
            throw new Exception("获取UTXO失败");
        }
        return elaJsonRpcResponse.getResult();
    }

    public static String generateTransaction(List<Map<String, String>> utxos, Map<String, Long> receivers, String privateKey, String address) throws Exception {
        log.debug("账户可用的UTXO为: {}", utxos);

        long totalSpend = 0, utxoValue = 0;

        List<Map<String, Object>> utxoOutputs = new ArrayList<>();
        for(Map.Entry<String, Long> entry : receivers.entrySet()) {
            if(entry.getValue() > 0) {
                Map<String,Object> utxoOutputDetail = new HashMap<>();
                utxoOutputDetail.put("address", entry.getKey());
                utxoOutputDetail.put("amount", entry.getValue());
                utxoOutputs.add(utxoOutputDetail);

                totalSpend += entry.getValue();
            }
        }

        List<Map<String, Object>> utxoInputs = new ArrayList<>();
        for(Map<String, String> entry : utxos) {
            Map<String,Object> utxoInputDetail = new HashMap<>();
            utxoInputDetail.put("txid",  entry.get("txid"));
            utxoInputDetail.put("index",  Long.parseLong(entry.get("vout")));
            utxoInputDetail.put("privateKey",  privateKey);
            utxoInputs.add(utxoInputDetail);

            utxoValue += new BigDecimal(entry.get("amount")).multiply(new BigDecimal(100000000)).longValue();
            if(utxoValue >= totalSpend + fee) {
                break;
            }
        }

        if(utxoValue < totalSpend  + fee) {
            log.error("分红账户余额不足");
            throw new Exception("分红账户余额不足");
        }

        long leftValue = utxoValue - totalSpend - fee;
        if(leftValue > 0) {
            Map<String,Object> utxoOutputDetail = new HashMap<>();
            utxoOutputDetail.put("address", address);
            utxoOutputDetail.put("amount", leftValue);
            utxoOutputs.add(utxoOutputDetail);
        }

        log.debug("总输入: [{}]  总输出: [{}]  手续费: [{}]  剩余: [{}]", utxoValue, totalSpend, fee, leftValue);

        Map<String,Object> txListMap = new HashMap<>();
        txListMap.put("Memo", memo);
        txListMap.put("UTXOInputs", utxoInputs);
        txListMap.put("Outputs", utxoOutputs);

        List<Map<String, Object>> txList = new ArrayList<>();
        txList.add(txListMap);

        Map<String,Object> paraListMap = new HashMap<>();
        paraListMap.put("Transactions", txList);

        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulateAll(paraListMap);
        return SingleSignTransaction.genRawTransaction(jsonObject);
    }

    public static void sendTransaction(String rawTxData) throws Exception {
        ELAJsonRpcRequest<String[]> elaJsonRpcRequest = new ELAJsonRpcRequest<>();
        elaJsonRpcRequest.setMethod("sendrawtransaction");
        elaJsonRpcRequest.setParams(new String[] {rawTxData});

        String result = HttpKit.post(nodeURL, JSON.toJSONString(elaJsonRpcRequest), JSONRpcRequestHeader);
        Type type = new TypeReference<ELAJsonRpcResponse<String>>() {}.getType();
        ELAJsonRpcResponse<String> elaJsonRpcResponse = JSON.parseObject(result, type);
        if(elaJsonRpcResponse.getError() != null) {
            log.error("Error to send transaction [{}]", elaJsonRpcResponse);
            throw new Exception("发送交易失败");
        }
        log.info("SendRawTransaction Result: {}", elaJsonRpcResponse);
    }
}
