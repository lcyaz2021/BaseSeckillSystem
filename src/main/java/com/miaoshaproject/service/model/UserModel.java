package com.miaoshaproject.service.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserModel {

    private Integer id;

    @NotBlank(message = "用户名不能为空")
    private String name;

    @NotNull(message = "性别必须填写")
    private Byte gender;

    @NotNull(message = "年龄必须填写")
    @Min(value = 0, message = "年龄必须大于零")
    @Max(value = 150, message = "年龄必须小于150")
    private Integer age;

    @NotNull(message = "手机号不能为空")
    private String telphone;

    @NotNull(message = "注册方式不能为空")
    private String registerNode;

    private String thirdPartyId;

    private String encriptPassword;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getRegisterNode() {
        return registerNode;
    }

    public void setRegisterNode(String registerNode) {
        this.registerNode = registerNode;
    }

    public String getThirdPartyId() {
        return thirdPartyId;
    }

    public void setThirdPartyId(String thirdPartyId) {
        this.thirdPartyId = thirdPartyId;
    }

    public String getEncriptPassword() {
        return encriptPassword;
    }

    public void setEncriptPassword(String encriptPassword) {
        this.encriptPassword = encriptPassword;
    }
}
