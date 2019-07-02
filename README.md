# carrier-dpos-profit
Elastos Carrier SuperNode DPos profit tool


# 使用说明

## 配置文件说明

`common.startProfitBlock`: 设置开始处理的块高度  
`common.profitCircles`: 设置没次定时任务执行处理多少轮投票（一轮投票=36个块高度）

`common.ownerPublicKey`: 设置超级节点所有人公钥（owner publickey）  
`common.rewardAddress`: 超级节点所有人地址（即超级节点接受链分红的地址
）
`common.nodeURL`: ELA主链节点JsonRPC URL  
`common.nodeAccessSecret`: 访问上面节点RPC接口的加密后的密钥

`common.historyServiceURL`: 生态组小明的历史记录服务 (添加了一个接口建议先使用默认配置)

`profit.account.address`: 给投票用户发放奖励的账户地址  
`profit.account.privateKey`: 对应的私钥

`profit.memo`: 发放奖励的交易memo信息，建议使用这种格式 type:text,msg:Elastos Carrier DPoS rewards for 20 circles

`transaction.fee`: 发放奖励交易手续费
