package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.UserDOMapper;
import com.miaoshaproject.dao.UserPasswordDOMapper;
import com.miaoshaproject.dataObject.UserDO;
import com.miaoshaproject.dataObject.UserPasswordDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.ValidatorImpl;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Autowired
    private ValidatorImpl validator;



    @Override
    public UserModel getUserById(Integer id) {
        // 调用userDOMapper获取对应用户的dataobject
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if(userDO == null){
            return null;
        }
        // 通过用户id获取用户加密密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(id);
        return convertFromUserDO(userDO, userPasswordDO);
    }

    @Override
    @Transactional
    public void regist(UserModel userModel) throws BusinessException {
        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //使用专门的校验器
        // 只需要在需要校验的model上添加校验注解即可
        ValidationResult result = validator.validate(userModel);
        if(result.isHasError()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }

        UserDO userDO = convertFromModel(userModel);
        try{
            userDOMapper.insertSelective(userDO);
        }catch (DuplicateKeyException ex){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号不能重复注册");
        }
        userModel.setId(userDO.getId());
        UserPasswordDO passwordDO = convertPdoFromModel(userModel);
        userPasswordDOMapper.insertSelective(passwordDO);
    }

    @Override
    public UserModel validationLogin(String telphone, String encryptPassword) throws BusinessException {
        // 通过用户手机获取用户信息
        UserDO userDO = userDOMapper.selectByTelphone(telphone);
        if(userDO == null ){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromUserDO(userDO,userPasswordDO);
        // 验证从数据库中取出的密码是否与传入的密码相匹配
        if(!StringUtils.equals(encryptPassword, userModel.getEncriptPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }


    private UserDO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
    }

    private UserPasswordDO convertPdoFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserPasswordDO passwordDO = new UserPasswordDO();
        passwordDO.setEncriptPassword(userModel.getEncriptPassword());
        passwordDO.setUserId(userModel.getId());
        return passwordDO;
    }


    private UserModel convertFromUserDO(UserDO userDO, UserPasswordDO userPasswordDO){
        if(userDO == null){
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO, userModel);
        if(userPasswordDO != null){
            userModel.setEncriptPassword(userPasswordDO.getEncriptPassword());
        }

        return userModel;
    }
}
