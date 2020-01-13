package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.UserModel;

public interface UserService {

    UserModel getUserById(Integer id);

    void regist(UserModel userModel) throws BusinessException;

    /**
     *
     * @param telphone  用户手机号
     * @param encryptPassword   加密后的字符串密码
     * @throws BusinessException
     */
    UserModel validationLogin(String telphone, String encryptPassword) throws BusinessException;
}
