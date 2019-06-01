package com.example.godcode.handler;

import com.example.godcode.bean.WebSocketNews1;
import com.example.godcode.bean.WebSocketNews2;
import com.example.godcode.bean.WebSocketNews3;
import com.example.godcode.bean.WebSocketNews4;
import com.example.godcode.bean.WsHeart;
import com.example.godcode.constant.Constant;
import com.example.godcode.greendao.entity.Notification;
import com.example.godcode.greendao.option.NotificationOption;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WebSocketNewsHandler {

    private WebSocketNews1 webSocketNews1;
    private WebSocketNews2 webSocketNews2;
    private WebSocketNews3 webSocketNews3;
    private WebSocketNews4 webSocketNews4;
    private WsHeart wsHeart;
    private int handlerType;
    private String eventType;

    private WebSocketNewsHandler(Builder builder) {
        this.webSocketNews1 = builder.webSocketNews1;
        this.webSocketNews2 = builder.webSocketNews2;
        this.webSocketNews3 = builder.webSocketNews3;
        this.webSocketNews4 = builder.webSocketNews4;
        this.wsHeart = builder.wsHeart;
        this.handlerType = builder.handlerType;
        this.eventType = builder.eventType;
    }


    public void setNofication() {
        Notification notification = new Notification();
        switch (eventType) {
            case "1":
            case "2":
                notification.setContent(webSocketNews3.getData().getMsg());
                notification.setDate(webSocketNews3.getSendTime());
                notification.setType(3);
                break;
            case "13":
            case "14":
            case "15":
            case "16":
            case "17":
            case "18":
                notification.setContent(webSocketNews3.getData().getMsg());
                notification.setDate(webSocketNews3.getSendTime());
                notification.setType(4);
                break;
            case "3":
            case "7":
                notification.setContent(webSocketNews3.getData().getMsg());
                notification.setDate(webSocketNews3.getSendTime());
                notification.setType(2);
                break;
            case "4":
            case "5":
            case "6":
            case "8":
            case "10":
            case "11":
                notification.setContent(webSocketNews2.getData().getMsg());
                notification.setRelatedKey(webSocketNews2.getData().getRelatedKey());
                notification.setTransactionId(webSocketNews2.getData().getId());
                notification.setTransactionType(webSocketNews2.getData().getTransactionType());
                notification.setType(1);
                notification.setDate(webSocketNews2.getSendTime());
                break;
        }
        if (notification.getType() != 0) {
            notification.setUserId(Constant.userId);
            notification.setDate(getDate());
            int i = Integer.parseInt(eventType);
            String title = titleArray[i - 1];
            notification.setTitle(title);
            NotificationOption.getInstance().memoryNotification(notification);
        }
    }


    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date now = new Date();
        return sdf.format(now);
    }

    private String[] titleArray = {"好友请求", "好友同意", "银行卡审核成功", "提现成功", "好友转账", "二维码收款", "银行卡审核失败", "提现失败", "登录异常", "退款到账",
            "产品营收到账", "删除好友", "资产分成", "修改资产分成", "删除资产分成", "解除资产绑定", "产权归还", "产权转移"};


    public static class Builder {
        private WebSocketNews1 webSocketNews1;
        private WebSocketNews2 webSocketNews2;
        private WebSocketNews3 webSocketNews3;
        private WebSocketNews4 webSocketNews4;
        private WsHeart wsHeart;
        private int handlerType;
        private String eventType;

        public Builder eventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder handlerType(int handlerType) {
            this.handlerType = handlerType;
            return this;
        }

        public Builder webSocketNews1(WebSocketNews1 webSocketNews1) {
            this.webSocketNews1 = webSocketNews1;
            return this;
        }

        public Builder webSocketNews2(WebSocketNews2 webSocketNews2) {
            this.webSocketNews2 = webSocketNews2;
            return this;
        }

        public Builder webSocketNews3(WebSocketNews3 webSocketNews3) {
            this.webSocketNews3 = webSocketNews3;
            return this;
        }

        public Builder webSocketNews4(WebSocketNews4 webSocketNews4) {
            this.webSocketNews4 = webSocketNews4;
            return this;
        }


        public Builder wsHeart(WsHeart wsHeart) {
            this.wsHeart = wsHeart;
            return this;
        }


        public WebSocketNewsHandler build() {
            return new WebSocketNewsHandler(this);
        }


    }


    public WebSocketNews1 getWebSocketNews1() {
        return webSocketNews1;
    }

    public void setWebSocketNews1(WebSocketNews1 webSocketNews1) {
        this.webSocketNews1 = webSocketNews1;
    }

    public WebSocketNews2 getWebSocketNews2() {
        return webSocketNews2;
    }

    public void setWebSocketNews2(WebSocketNews2 webSocketNews2) {
        this.webSocketNews2 = webSocketNews2;
    }

    public WebSocketNews3 getWebSocketNews3() {
        return webSocketNews3;
    }

    public void setWebSocketNews3(WebSocketNews3 webSocketNews3) {
        this.webSocketNews3 = webSocketNews3;
    }

    public WebSocketNews4 getWebSocketNews4() {
        return webSocketNews4;
    }

    public void setWebSocketNews4(WebSocketNews4 webSocketNews4) {
        this.webSocketNews4 = webSocketNews4;
    }

    public WsHeart getWsHeart() {
        return wsHeart;
    }

    public void setWsHeart(WsHeart wsHeart) {
        this.wsHeart = wsHeart;
    }

    public int getHandlerType() {
        return handlerType;
    }

    public void setHandlerType(int handlerType) {
        this.handlerType = handlerType;
    }
}


