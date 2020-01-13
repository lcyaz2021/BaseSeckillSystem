package com.miaoshaproject.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ValidatorImpl implements InitializingBean {

    private Validator validator;

    public ValidationResult validate(Object bean){
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<Object>> constraintViolationSet=  validator.validate(bean);
        if(constraintViolationSet.size()>0){
            result.setHasError(true);
            for(ConstraintViolation<Object> con : constraintViolationSet){
                String errMsg = con.getMessage();
                String propertyName = con.getPropertyPath().toString();
                result.getErrMsgMap().put(propertyName, errMsg);
            }
        }
        return result;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        // 将hibernate validator通过工厂的方式实例化
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
