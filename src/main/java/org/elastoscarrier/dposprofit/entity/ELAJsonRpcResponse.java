package org.elastoscarrier.dposprofit.entity;

public class ELAJsonRpcResponse<T> {
    private String error;
    private String id;
    private String jsonrpc;
    private T result;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ELAJsonRpcResponse{" +
                "error='" + error + '\'' +
                ", id='" + id + '\'' +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", result=" + result +
                '}';
    }
}
