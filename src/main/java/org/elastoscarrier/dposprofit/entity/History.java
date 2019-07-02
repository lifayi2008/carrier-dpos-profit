package org.elastoscarrier.dposprofit.entity;

import java.util.ArrayList;

public class History {
    private String Txid;
    private String Type;
    private Long Value;
    private Long CreateTime;
    private Long Height;
    private ArrayList<String> Inputs;
    private ArrayList<String> Outputs;
    private String TxType;

    private String Address;
    private Long Fee;
    private String Memo;

    public String getTxid() {
        return Txid;
    }

    public void setTxid(String txid) {
        Txid = txid;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Long getValue() {
        return Value;
    }

    public void setValue(Long value) {
        Value = value;
    }

    public Long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Long createTime) {
        CreateTime = createTime;
    }

    public Long getHeight() {
        return Height;
    }

    public void setHeight(Long height) {
        Height = height;
    }

    public ArrayList<String> getInputs() {
        return Inputs;
    }

    public void setInputs(ArrayList<String> inputs) {
        Inputs = inputs;
    }

    public ArrayList<String> getOutputs() {
        return Outputs;
    }

    public void setOutputs(ArrayList<String> outputs) {
        Outputs = outputs;
    }

    public String getTxType() {
        return TxType;
    }

    public void setTxType(String txType) {
        TxType = txType;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public Long getFee() {
        return Fee;
    }

    public void setFee(Long fee) {
        Fee = fee;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }

    @Override
    public String toString() {
        return "History{" +
                "Txid='" + Txid + '\'' +
                ", Type='" + Type + '\'' +
                ", Value=" + Value +
                ", CreateTime=" + CreateTime +
                ", Height=" + Height +
                ", Inputs=" + Inputs +
                ", Outputs=" + Outputs +
                ", TxType='" + TxType + '\'' +
                ", Address='" + Address + '\'' +
                ", Fee=" + Fee +
                ", Memo='" + Memo + '\'' +
                '}';
    }
}
