package com.yjxxt.crm.query;

import com.yjxxt.crm.base.BaseQuery;

public class SaleChanceQuery extends BaseQuery {
    //客户名称
    private String customerName;
    private String creatMan;
    private String state;

    public SaleChanceQuery() {
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreatMan() {
        return creatMan;
    }

    public void setCreatMan(String creatMan) {
        this.creatMan = creatMan;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "SaleChanceQuery{" +
                "customerName='" + customerName + '\'' +
                ", creatMan='" + creatMan + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
