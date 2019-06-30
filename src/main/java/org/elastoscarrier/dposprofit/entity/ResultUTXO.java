package org.elastoscarrier.dposprofit.entity;

import java.util.List;
import java.util.Map;

public class ResultUTXO {
    private String Desc;
    private Integer Error;
    private List<UTXOResult> Result;

    public static class UTXOResult {
        private String AssetId;
        private String AssetName;
        private List<Map<String, String>> Utxo;

        public String getAssetId() {
            return AssetId;
        }

        public void setAssetId(String assetId) {
            AssetId = assetId;
        }

        public String getAssetName() {
            return AssetName;
        }

        public void setAssetName(String assetName) {
            AssetName = assetName;
        }

        public List<Map<String, String>> getUtxo() {
            return Utxo;
        }

        public void setUtxo(List<Map<String, String>> utxo) {
            Utxo = utxo;
        }
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public Integer getError() {
        return Error;
    }

    public void setError(Integer error) {
        Error = error;
    }

    public List<UTXOResult> getResult() {
        return Result;
    }

    public void setResult(List<UTXOResult> result) {
        Result = result;
    }
}
