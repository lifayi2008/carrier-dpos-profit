package org.elastoscarrier.dposprofit.entity;

public class ResultHistory<T> {
    private T result;
    private Integer status;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
