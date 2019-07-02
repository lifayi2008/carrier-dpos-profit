# carrier-dpos-profit
Elastos Carrier SuperNode DPos profit tool


# 使用说明

## 配置文件说明

`common.startProfitBlock`: 设置开始处理的块高度  
`common.profitCircles`: 设置没次定时任务执行处理多少轮投票（一轮投票=36个块高度）

`common.ownerPublicKey`: 设置超级节点所有人公钥（`owner publickey`）  
`common.rewardAddress`: 超级节点所有人地址（即超级节点接受链分红的地址）  

`common.nodeURL`: `ELA`主链节点`JsonRPC URL`  
`common.nodeAccessSecret`: 访问上面节点RPC接口的加密后的密钥

`common.historyServiceURL`: 生态组小明的历史记录服务 (添加了一个接口建议先使用默认配置)

`profit.account.address`: 给投票用户发放奖励的账户地址  
`profit.account.privateKey`: 对应的私钥

`profit.memo`: 发放奖励的交易`memo`信息，建议使用这种格式 `type:text,msg:Elastos Carrier DPoS rewards for 20 circles`

`transaction.fee`: 发放奖励交易手续费

## 分红方式说明

工具分红计算主要在 `ProfitTask.voterProfitMethod` 方法中，参数的含义依次为:

* `superNodeVoteNum` : 超级节点本轮获得的投票数 
* `superNodeProfitValue` : 超级节点本轮获得的奖励（Sela）
* `result` : 超级节点本轮用户投票详情
````json
[
  {
    "Address": "EN686pe2r8CT12qQYCfC31i9yCNNrXNHfN",
    "Value": 2424324.2
  },
  {
    "Address": "ETJtqzZsKuFdxDwGCCyS25PePPKBd64YXi",
    "Value": 24243
   }
]
````
* `profitDetail` : 本轮计算之后所有人应付奖励（Sela）

```json
{
  "EN686pe2r8CT12qQYCfC31i9yCNNrXNHfN": 1231231,
  "ETJtqzZsKuFdxDwGCCyS25PePPKBd64YXi": 22422
}
```

## 注意事项

* 如果因维护或者运行异常导致程序停止，则需要手动更改 `startProfitBlock` 参数为已成功处理块之后的 `36` 个块  
* 一定不要同时运行多个程序实例，否则你可能会发双倍的奖励给投票者