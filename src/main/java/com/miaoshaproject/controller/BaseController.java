package com.miaoshaproject.controller;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommenReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

    public static final String CONSUME = "";


    // 定义exceptionHandler解决未被controller层吸收的异常
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object exceptionHandler(HttpServletRequest request, Exception ex){
        Map<String, Object> resultData = new HashMap<>();
        if(ex instanceof BusinessException){
            BusinessException businessException = (BusinessException)ex;
            resultData.put("errCode", businessException.getErrCode());
            resultData.put("errMsg", businessException.getErrMsg());
        }else{
            resultData.put("errCode", EmBusinessError.UNKNOW_ERROR.getErrCode());
            resultData.put("errMsg", EmBusinessError.UNKNOW_ERROR.getErrMsg());
        }

        return CommenReturnType.create(resultData, "fail");
    }

}
