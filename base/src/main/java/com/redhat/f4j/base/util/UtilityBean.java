package com.redhat.f4j.base.util;

/**
 * UtilityBean
 */
public class UtilityBean {

    public static boolean isEmptyString(String text){
        return (text == null) ? true : ( "".equals(text.trim()));
    }

    
}