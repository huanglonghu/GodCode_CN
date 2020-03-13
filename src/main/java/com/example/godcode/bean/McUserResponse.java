package com.example.godcode.bean;

import java.util.List;

public class McUserResponse {

    /**
     * result : {"totalCount":8,"items":[{"fK_UserID":20,"toUserID":11,"nickName":"Dneal","toUserName":"113135","friendImgPath":"http://thirdwx.qlogo.cn/mmopen/vi_32/4rDU87Tvl7PRYiblHibW8mgfFTGxUgp1cl9aLGSicbV9cFHiaSEcUbcFPdSzcx8ia6UzFOBicHGL1jiblevQ4JuBRtwCw/132","addTime":"2018-09-17T21:46:50.7987252","isConcur":true,"id":27},{"fK_UserID":63,"toUserID":11,"nickName":"Nil","toUserName":"429762","friendImgPath":"/Files/Pictures/UserPortrait/dda16d44-f06a-48c5-ac86-8ef871d2a5bf.jpg","addTime":"2018-12-27T11:04:37.0917353","isConcur":true,"id":247},{"fK_UserID":5,"toUserID":11,"nickName":"150\u20267","toUserName":"999999","friendImgPath":"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83erCDKaa8yuPqtn4kJdeNIm0A6w6x8nOk41UCK4GasVicAu55IskSX4nicDhNMhxzRANtL04KL19uJ5A/132","addTime":"2019-03-04T18:08:47.8973651","isConcur":true,"id":284},{"fK_UserID":26,"toUserID":11,"nickName":"ying","toUserName":"111111","friendImgPath":null,"addTime":"2019-05-09T08:26:40.2539111","isConcur":true,"id":372},{"fK_UserID":29,"toUserID":11,"nickName":"13322","toUserName":"666666","friendImgPath":"http://thirdwx.qlogo.cn/mmopen/vi_32/YRr6oD4gXAGhxicIibho8mMdUfYvB4s12CjSnZicIC0zF1VaxEG9yDuOwViatWszHuuLLpjfpBahdkOgkLh76sdCHA/132","addTime":"2019-06-14T15:44:30.8969928","isConcur":true,"id":20459},{"fK_UserID":10232,"toUserID":11,"nickName":"18307200140","toUserName":"620781","friendImgPath":null,"addTime":"2019-06-25T17:20:22.8124953","isConcur":true,"id":20488},{"fK_UserID":21,"toUserID":11,"nickName":"口十金玉先森","toUserName":"114819","friendImgPath":"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKUmAVdySFxZWC4XkdjEDLictEDicUCD8xngHccTRXuvyl5bOM4sBNRRNvEJ5WAibHgCuBqYBIY0cujQ/132","addTime":"2019-07-01T16:06:29.8181782","isConcur":true,"id":30510},{"fK_UserID":116,"toUserID":11,"nickName":"13250554787","toUserName":"305126","friendImgPath":null,"addTime":"2019-07-03T14:59:19.0528726","isConcur":true,"id":30519}]}
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
         * totalCount : 8
         * items : [{"fK_UserID":20,"toUserID":11,"nickName":"Dneal","toUserName":"113135","friendImgPath":"http://thirdwx.qlogo.cn/mmopen/vi_32/4rDU87Tvl7PRYiblHibW8mgfFTGxUgp1cl9aLGSicbV9cFHiaSEcUbcFPdSzcx8ia6UzFOBicHGL1jiblevQ4JuBRtwCw/132","addTime":"2018-09-17T21:46:50.7987252","isConcur":true,"id":27},{"fK_UserID":63,"toUserID":11,"nickName":"Nil","toUserName":"429762","friendImgPath":"/Files/Pictures/UserPortrait/dda16d44-f06a-48c5-ac86-8ef871d2a5bf.jpg","addTime":"2018-12-27T11:04:37.0917353","isConcur":true,"id":247},{"fK_UserID":5,"toUserID":11,"nickName":"150\u20267","toUserName":"999999","friendImgPath":"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83erCDKaa8yuPqtn4kJdeNIm0A6w6x8nOk41UCK4GasVicAu55IskSX4nicDhNMhxzRANtL04KL19uJ5A/132","addTime":"2019-03-04T18:08:47.8973651","isConcur":true,"id":284},{"fK_UserID":26,"toUserID":11,"nickName":"ying","toUserName":"111111","friendImgPath":null,"addTime":"2019-05-09T08:26:40.2539111","isConcur":true,"id":372},{"fK_UserID":29,"toUserID":11,"nickName":"13322","toUserName":"666666","friendImgPath":"http://thirdwx.qlogo.cn/mmopen/vi_32/YRr6oD4gXAGhxicIibho8mMdUfYvB4s12CjSnZicIC0zF1VaxEG9yDuOwViatWszHuuLLpjfpBahdkOgkLh76sdCHA/132","addTime":"2019-06-14T15:44:30.8969928","isConcur":true,"id":20459},{"fK_UserID":10232,"toUserID":11,"nickName":"18307200140","toUserName":"620781","friendImgPath":null,"addTime":"2019-06-25T17:20:22.8124953","isConcur":true,"id":20488},{"fK_UserID":21,"toUserID":11,"nickName":"口十金玉先森","toUserName":"114819","friendImgPath":"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKUmAVdySFxZWC4XkdjEDLictEDicUCD8xngHccTRXuvyl5bOM4sBNRRNvEJ5WAibHgCuBqYBIY0cujQ/132","addTime":"2019-07-01T16:06:29.8181782","isConcur":true,"id":30510},{"fK_UserID":116,"toUserID":11,"nickName":"13250554787","toUserName":"305126","friendImgPath":null,"addTime":"2019-07-03T14:59:19.0528726","isConcur":true,"id":30519}]
         */

        private int totalCount;
        private List<ContactBean> items;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<ContactBean> getItems() {
            return items;
        }

        public void setItems(List<ContactBean> items) {
            this.items = items;
        }


    }
}
