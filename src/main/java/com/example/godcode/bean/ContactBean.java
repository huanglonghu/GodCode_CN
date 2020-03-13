package com.example.godcode.bean;

public class ContactBean {
    /**
     * fK_UserID : 20
     * toUserID : 11
     * nickName : Dneal
     * toUserName : 113135
     * friendImgPath : http://thirdwx.qlogo.cn/mmopen/vi_32/4rDU87Tvl7PRYiblHibW8mgfFTGxUgp1cl9aLGSicbV9cFHiaSEcUbcFPdSzcx8ia6UzFOBicHGL1jiblevQ4JuBRtwCw/132
     * addTime : 2018-09-17T21:46:50.7987252
     * isConcur : true
     * id : 27
     */

    private int fK_UserID;
    private int toUserID;
    private String nickName;
    private String toUserName;
    private String friendImgPath;
    private String addTime;
    private boolean isConcur;
    private int id;

    public int getFK_UserID() {
        return fK_UserID;
    }

    public void setFK_UserID(int fK_UserID) {
        this.fK_UserID = fK_UserID;
    }

    public int getToUserID() {
        return toUserID;
    }

    public void setToUserID(int toUserID) {
        this.toUserID = toUserID;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFriendImgPath() {
        return friendImgPath;
    }

    public void setFriendImgPath(String friendImgPath) {
        this.friendImgPath = friendImgPath;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public boolean isIsConcur() {
        return isConcur;
    }

    public void setIsConcur(boolean isConcur) {
        this.isConcur = isConcur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}


