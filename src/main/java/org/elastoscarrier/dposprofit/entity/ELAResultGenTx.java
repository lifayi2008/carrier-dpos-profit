package org.elastoscarrier.dposprofit.entity;

import java.util.Map;

public class ELAResultGenTx {
    private String Desc;
    private String Action;
    private Map<String, String> Result;

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }

    public Map<String, String> getResult() {
        return Result;
    }

    public void setResult(Map<String, String> result) {
        Result = result;
    }
}
