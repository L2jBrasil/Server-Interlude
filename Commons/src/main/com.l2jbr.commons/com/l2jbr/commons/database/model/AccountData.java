package com.l2jbr.commons.database.model;

import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("account_data")
public class AccountData extends Model<String> {

    @Id
    private String  var;
    private String value;

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

    @Override
    public String getId() {
        return  var;
    }

}
