/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demorest.entities;

/**
 * An instance of MyHttpStatus Class is a POJO that is returned when an MyException is thrown.
 * @author George
 */
public class MyHttpStatus {
    private String code;
    private String message;
    private String sourse;

    public MyHttpStatus(String code, String message, String sourse) {
        this.code = code;
        this.message = message;
        this.sourse = sourse;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSourse() {
        return sourse;
    }

    public void setSourse(String sourse) {
        this.sourse = sourse;
    }
    
    
}
