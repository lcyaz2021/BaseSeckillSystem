package com.miaoshaproject.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ValidationResult {

    private boolean hasError = false;

    private Map<String, String> errMsgMap = new HashMap<>();

    // 实现通用的 格式化字符串信息 获取错误结果的方法
    public String getErrMsg(){
        return StringUtils.join(errMsgMap.values().toArray(), "; ");
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public Map<String, String> getErrMsgMap() {
        return errMsgMap;
    }

    public void setErrMsgMap(Map<String, String> errMsgMap) {
        this.errMsgMap = errMsgMap;
    }
}
