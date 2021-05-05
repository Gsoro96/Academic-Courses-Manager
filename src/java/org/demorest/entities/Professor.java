/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demorest.entities;

/**
 * An instance of Professor Class is a POJO that represents a Professor from the Database.
 * @author George
 */
public class Professor {
    private String ID;
    private String SurName;
    private String FirstName;
    private String Email;
    private String Phone1;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSurName() {
        return SurName;
    }

    public void setSurName(String SurName) {
        this.SurName = SurName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPhone1() {
        return Phone1;
    }

    public void setPhone1(String Phone1) {
        this.Phone1 = Phone1;
    }

    @Override
    public String toString() {
        return "Professor{" + "ID=" + ID + ", SurName=" + SurName + ", FirstName=" + FirstName + ", Email=" + Email + ", Phone1=" + Phone1 + '}';
    }
    
    
}
