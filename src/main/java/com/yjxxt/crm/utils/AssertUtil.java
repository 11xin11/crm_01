package com.yjxxt.crm.utils;


import com.yjxxt.crm.exception.ParamsException;

public class AssertUtil {


    public  static void isTrue(Boolean flag,String msg){
        if(flag){
            throw  new ParamsException(msg);
        }
    }

}
