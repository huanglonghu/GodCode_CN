package com.example.godcode.bean;

import java.util.ArrayList;

public class ContactData {
    private String charcter;
    private ArrayList<ContactBean> contacts;

    public String getCharcter() {
        return this.charcter;
    }

    public void setCharcter(String charcter) {
        this.charcter = charcter;
    }

    public ArrayList<ContactBean> getContacts() {
        return this.contacts;
    }

    public void setContacts(ArrayList<ContactBean> contacts) {
        this.contacts = contacts;
    }
}
