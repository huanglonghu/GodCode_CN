package com.example.godcode.bean;

/**
 * Created by Administrator on 2018/8/3.
 */

public class ZzIncome {

    /**
     * result : {"fK_UserIDDisburs":10052,"disbursUserName":"(sy1532708747)","fK_UserIDIncome":10046,"incomeUserName":"(sy1532704800)","feeType":"cny","incomeAmount":0.01,"incomeGenre":2,"incomeTime":"2018-08-01T20:33:48.7695951","incomeMode":2}
     * targetUrl : null
     * success : true
     * error : null
     * unAuthorizedRequest : false
     * __abp : true
     */

    private ResultBean result;
    private Object targetUrl;
    private boolean success;
    private Object error;
    private boolean unAuthorizedRequest;
    private boolean __abp;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public Object getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(Object targetUrl) {
        this.targetUrl = targetUrl;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public boolean isUnAuthorizedRequest() {
        return unAuthorizedRequest;
    }

    public void setUnAuthorizedRequest(boolean unAuthorizedRequest) {
        this.unAuthorizedRequest = unAuthorizedRequest;
    }

    public boolean is__abp() {
        return __abp;
    }

    public void set__abp(boolean __abp) {
        this.__abp = __abp;
    }

    public static class ResultBean {
        /**
         * fK_UserIDDisburs : 10052
         * disbursUserName : (sy1532708747)
         * fK_UserIDIncome : 10046
         * incomeUserName : (sy1532704800)
         * feeType : cny
         * incomeAmount : 0.01
         * incomeGenre : 2
         * incomeTime : 2018-08-01T20:33:48.7695951
         * incomeMode : 2
         */

        private int fK_UserIDDisburs;
        private String disbursUserName;
        private int fK_UserIDIncome;
        private String incomeUserName;
        private String feeType;
        private double incomeAmount;
        private int incomeGenre;
        private String incomeTime;
        private int incomeMode;

        public int getFK_UserIDDisburs() {
            return fK_UserIDDisburs;
        }

        public void setFK_UserIDDisburs(int fK_UserIDDisburs) {
            this.fK_UserIDDisburs = fK_UserIDDisburs;
        }

        public String getDisbursUserName() {
            return disbursUserName;
        }

        public void setDisbursUserName(String disbursUserName) {
            this.disbursUserName = disbursUserName;
        }

        public int getFK_UserIDIncome() {
            return fK_UserIDIncome;
        }

        public void setFK_UserIDIncome(int fK_UserIDIncome) {
            this.fK_UserIDIncome = fK_UserIDIncome;
        }

        public String getIncomeUserName() {
            return incomeUserName;
        }

        public void setIncomeUserName(String incomeUserName) {
            this.incomeUserName = incomeUserName;
        }

        public String getFeeType() {
            return feeType;
        }

        public void setFeeType(String feeType) {
            this.feeType = feeType;
        }

        public double getIncomeAmount() {
            return incomeAmount;
        }

        public void setIncomeAmount(double incomeAmount) {
            this.incomeAmount = incomeAmount;
        }

        public int getIncomeGenre() {
            return incomeGenre;
        }

        public void setIncomeGenre(int incomeGenre) {
            this.incomeGenre = incomeGenre;
        }

        public String getIncomeTime() {
            return incomeTime;
        }

        public void setIncomeTime(String incomeTime) {
            this.incomeTime = incomeTime;
        }

        public int getIncomeMode() {
            return incomeMode;
        }

        public void setIncomeMode(int incomeMode) {
            this.incomeMode = incomeMode;
        }
    }
}
