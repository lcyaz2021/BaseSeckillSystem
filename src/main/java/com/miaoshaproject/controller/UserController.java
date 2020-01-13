package com.miaoshaproject.controller;

import com.miaoshaproject.controller.viewobject.UserVO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommenReturnType;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;


@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", origins = {"*"})   // 处理跨域共享，session数据共享问题
public class UserController extends BaseController{

    /**
     *  session跨域问题：
     *  1、controller中注解 @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
     *  2、前端ajax中加上 contentType: "application/x-www-form-urlencoded" 和 xhrFields: {withCredentials: true},
     *  3、post方法注解上，加上 consumes = "application/x-www-form-urlencoded"
     *  参考 https://blog.csdn.net/L15810356216/article/details/89362965
     */

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

/*****************************************************
 接口方法
 ****************************************************/

    @RequestMapping("/get")
    @ResponseBody
    public CommenReturnType getUser(@RequestParam(name="id")Integer id) throws BusinessException {
        // 调用service获取对应id的用户对象 并返回前端
        UserModel userModel = userService.getUserById(id);
        if(userModel == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        // 将核心领域模型用户对象转换为 可供UI使用的viewobject
        UserVO userVO = convertFromModel(userModel);
        return CommenReturnType.create(userVO);
    }


    // 用户获取otp短信接口
    @RequestMapping(value = "/getotp", method = RequestMethod.POST)
    @ResponseBody
    public CommenReturnType getOtp(@RequestParam(name="telphone")String telphone){
        // 按照一定规则生成otp验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);
        // 将otp验证码与手机号关联;使用httpSession的方式绑定手机号和code
        HttpSession session = request.getSession();
        // 将otp验证码通过短信通道发送给用用户
        System.out.println("phone&otp:  " + telphone + " -> " + otpCode);
        return CommenReturnType.create("生成otp成功");
    }



    // 用户注册接口
    @RequestMapping(value = "/regist", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    @ResponseBody
    public CommenReturnType register(@RequestParam(name = "telphone")String telphone,
                                     @RequestParam(name = "otpCode")String otpCode,
                                     @RequestParam(name = "name")String name,
                                     @RequestParam(name="gender")Integer gender,
                                     @RequestParam(name = "age")Integer age,
                                     @RequestParam(name = "password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        // 验证手机号和对应的otpcode符合
        HttpSession session = this.request.getSession();
        String inSessionOtpCode = (String) session.getAttribute(telphone);
        if(inSessionOtpCode == null){
            System.out.println("session中取出的otpCode为空");
        }
        if(otpCode == null || otpCode == ""){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码为空");
        }
        // 用户注册流程
        UserModel userModel = new UserModel();
        userModel.setAge(age);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setName(name);
        userModel.setTelphone(telphone);
        userModel.setRegisterNode("byPhone");
        userModel.setEncriptPassword(this.encodeByMD5(password));
        userService.regist(userModel);
        return CommenReturnType.create(null);
    }


    // 用户登录接口
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    @ResponseBody
    public CommenReturnType login(@RequestParam(name = "telphone")String telphone,
                                  @RequestParam(name = "password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        // 入参校验
        if(org.apache.commons.lang3.StringUtils.isEmpty(telphone) ||
                org.apache.commons.lang3.StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "手机号或者密码不能为空");
        }
        // 用户登录服务， 校验登录合法
        String encryptPw = encodeByMD5(password);
        UserModel userModel = userService.validationLogin(telphone, encryptPw);

        // 将登陆凭证加入到用户登录成功的session内
        this.request.getSession().setAttribute("IS_LOGIN", true);
        this.request.getSession().setAttribute("LOGIN_USER", userModel);
        return CommenReturnType.create(null);
    }





/*****************************************************
私有辅助方法
 ****************************************************/
    private UserVO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }

    public String encodeByMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String asB64 = Base64.getEncoder().encodeToString(str.getBytes("utf-8"));
        return asB64;
    }

}
