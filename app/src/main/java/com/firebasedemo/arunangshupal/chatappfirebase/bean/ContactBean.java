package com.firebasedemo.arunangshupal.chatappfirebase.bean;

/**
 * Created by Arunangshu Pal on 5/15/2016.
 */
public class ContactBean {
    String userName;
    Long phoneNumber;

    public ContactBean() {
    }

    public ContactBean(String userName, Long phoneNumber) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactBean that = (ContactBean) o;

        return phoneNumber != null ? phoneNumber.equals(that.phoneNumber) : that.phoneNumber == null;

    }

    @Override
    public int hashCode() {
        return phoneNumber != null ? phoneNumber.hashCode() : 0;
    }

    public String getUserName() {

        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
