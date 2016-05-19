package com.firebasedemo.arunangshupal.chatappfirebase.bean;

/**
 * Created by Arunangshu Pal on 5/15/2016.
 */
public class MessageBean {

    private Long fromPhoneNumber;
    private Long toPhoneNumber;
    private Long sysTime;

    public Long getToPhoneNumber() {
        return toPhoneNumber;
    }

    public void setToPhoneNumber(Long toPhoneNumber) {
        this.toPhoneNumber = toPhoneNumber;
    }

    public Long getSysTime() {
        return sysTime;
    }

    public void setSysTime(Long sysTime) {
        this.sysTime = sysTime;
    }

    private String message;




    public Long getFromPhoneNumber() {
        return fromPhoneNumber;
    }

    public void setFromPhoneNumber(Long fromPhoneNumber) {
        this.fromPhoneNumber = fromPhoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "fromPhoneNumber=" + fromPhoneNumber +
                ", toPhoneNumber=" + toPhoneNumber +
                ", sysTime=" + sysTime +
                ", message='" + message + '\'' +
                '}';
    }
}
