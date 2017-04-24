package com.example.chen.oamanager.bean;

import com.jaydenxiao.common.baserx.IModel;

import java.util.List;


/**
 * Author:Hikin
 * Data:2016/12/12
 */
public class MeizhiData  implements IModel{

    private boolean isError;
    private List<Meizhi> results;

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public void setResults(List<Meizhi> results) {
        this.results = results;
    }

    public List<Meizhi> getResults() {
        return results;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isAuthError() {
        return false;
    }

    @Override
    public boolean isBizError() {
        return false;
    }

    @Override
    public String getErrorMsg() {
        return null;
    }
}
