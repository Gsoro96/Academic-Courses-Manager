/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demorest.entities;

import java.sql.Connection;
import java.util.Date;

/**
 * An instance of User Class is a POJO that represents a User from the Database.
 * @author George
 */
public class User {
    private String Logname;
    private String Password;
    private String Kategory;
    private String IDinKateg;
    private int RoleId;
    private Date LoginTime;
    private String Key;
    private Connection Con;

    public Connection getCon() {
        return Con;
    }

    public void setCon(Connection con) {
        this.Con = con;
    }

    public Date getLoginTime() {
        return LoginTime;
    }

    public void setLoginTime() {
        this.LoginTime= new Date(getCurrentTimeStamp().getTime());
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String Key) {
        this.Key = Key;
    }

    public User(String Logname, String Password, String Kategory, String IDinKateg ,int RoleId) {
        this.RoleId = RoleId;
        this.Logname = Logname;
        this.Password = Password;
        this.Kategory = Kategory;
        this.IDinKateg = IDinKateg;
    }

    public int getRoleId() {
        return RoleId;
    }

    public void setRoleId(int RoleId) {
        this.RoleId = RoleId;
    }

    

    public String getLogname() {
        return Logname;
    }

    public void setLogname(String Logname) {
        this.Logname = Logname;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getKategory() {
        return Kategory;
    }

    public void setKategory(String Kategory) {
        this.Kategory = Kategory;
    }

    public String getIDinKateg() {
        return IDinKateg;
    }

    public void setIDinKateg(String IDinKateg) {
        this.IDinKateg = IDinKateg;
    }

    @Override
    public String toString() {
        return "User{" + "Logname=" + Logname + ", Password=" + Password + ", Kategory=" + Kategory + ", IDinKateg=" + IDinKateg + ", RoleId=" + RoleId + '}';
    }
    
    /**
      * This method returns the current Timestamp.
      * @return Timstamp. The Current Timestamp.
      */
     public  java.sql.Timestamp getCurrentTimeStamp() {
	java.util.Date today = new java.util.Date();
	return new java.sql.Timestamp(today.getTime());
    }
}
