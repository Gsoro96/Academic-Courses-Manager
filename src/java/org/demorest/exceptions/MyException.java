/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demorest.exceptions;

/**
 * My Exception is the a class that extends Exception and its functionallity is to manage all the <br>
 * exceptions that this app throws.
 * @author George
 */
public class MyException extends Exception{
    
    String Source;
    String Date;
    Throwable thrwbl;

    public Throwable getThrwbl() {
        return thrwbl;
    }

    public void setThrwbl(Throwable thrwbl) {
        this.thrwbl = thrwbl;
    }

    public MyException(String Source, Throwable thrwbl) {
        super(Source, thrwbl);
        this.Source = Source;
        this.thrwbl = thrwbl;
        this.Date = getCurrentTimeStamp();
    }
    
     public MyException(String Source) {
        super(Source);
        this.Source = Source;
        this.Date = getCurrentTimeStamp();
    }
    
     public static String getCurrentTimeStamp() {
	java.util.Date today = new java.util.Date();
	return new java.sql.Timestamp(today.getTime()).toString();
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String Source) {
        this.Source = Source;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    @Override
    public String toString() {
        return "MyException{" + "Source=" + Source + ", Date=" + Date + '}';
    }
     
}
