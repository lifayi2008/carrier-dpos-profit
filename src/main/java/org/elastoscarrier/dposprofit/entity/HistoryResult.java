package org.elastoscarrier.dposprofit.entity;

import java.util.List;

public class HistoryResult {
    private List<History> History;
    private String TotalNum;

    public List<org.elastoscarrier.dposprofit.entity.History> getHistory() {
        return History;
    }

    public void setHistory(List<org.elastoscarrier.dposprofit.entity.History> history) {
        History = history;
    }

    public String getTotalNum() {
        return TotalNum;
    }

    public void setTotalNum(String totalNum) {
        TotalNum = totalNum;
    }
}