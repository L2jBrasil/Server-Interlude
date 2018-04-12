package com.l2jbr.gameserver.model.dao;

import org.springframework.data.annotation.Id;

public class account_data {


    @Id  String  account_name;
    private String  var;
    private String value;


    public String getAccountName() {
        return account_name;
    }

    public void setAccountName(String accountName) {
        this.account_name = accountName;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
