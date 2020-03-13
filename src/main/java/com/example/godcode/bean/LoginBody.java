package com.example.godcode.bean;

/**
 * Created by Administrator on 2018/5/17.
 */

public class LoginBody {


    /**
     * userName : string
     * deviceToken : string
     * openID : string
     * verificationCode : string
     */

    private String userName;
    private String deviceToken;
    private String openID;
    private String verificationCode;
    private String password;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
