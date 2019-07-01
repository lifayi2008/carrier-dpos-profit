package org.elastoscarrier.dposprofit.entity;

public class ELAJsonRpcRequest<T> {
    private String method;
    private T params;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public T getParams() {
        return params;
    }

    public void setParams(T params) {
        this.params = params;
    }
}
