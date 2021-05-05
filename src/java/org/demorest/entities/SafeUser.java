/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demorest.entities;

/**
 * An instance of SafeUser Class is a POJO that that is returned to an user when he/she logins.
 * @author George
 */
public class SafeUser {
    private String Logname;
    private String RoleId;
    private String Key;
    private String message;

    public String getLogname() {
        return Logname;
    }

    public void setLogname(String Logname) {
        this.Logname = Logname;
    }

    public String getRoleId() {
        return RoleId;
    }

    public void setRoleId(String RoleId) {
        this.RoleId = RoleId;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String Key) {
        this.Key = Key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
