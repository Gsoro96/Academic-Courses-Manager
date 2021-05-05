/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demorest.entities;

/**
 * An instance of Student Class is a POJO that represents a Student from the Database.
 * @author George
 */
public class Student {
    private String AM;
    private String SurName;
    private String FirstName;
    private String FathersName;
    private String MothersName;
    private String Sex;
    private String Phone1;
    private String Phone2;
    private int Semester;
    private String Status;
    private String OriginalCurriculumID;
    private String CurriculumID;

    @Override
    public String toString() {
        return "Student{" + "AM=" + AM + ", SurName=" + SurName + ", FirstName=" + FirstName + ", FathersName=" + FathersName + ", MothersName=" + MothersName + ", Sex=" + Sex + ", Phone1=" + Phone1 + ", Phone2=" + Phone2 + ", Semester=" + Semester + ", Status=" + Status + ", OriginalCurriculumID=" + OriginalCurriculumID + ", CurriculumID=" + CurriculumID + '}';
    }
    
    

    public String getOriginalCurriculumID() {
        return OriginalCurriculumID;
    }

    public void setOriginalCurriculumID(String OriginalCurriculumID) {
        this.OriginalCurriculumID = OriginalCurriculumID;
    }

   

    
    public String getCurriculumID() {
        return CurriculumID;
    }

    public void setCurriculumID(String CurriculumID) {
        this.CurriculumID = CurriculumID;
    }
    

    public String getAM() {
        return AM;
    }

    public void setAM(String AM) {
        this.AM = AM;
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

    public String getFathersName() {
        return FathersName;
    }

    public void setFathersName(String FatherName) {
        this.FathersName = FatherName;
    }

    public String getMothersName() {
        return MothersName;
    }

    public void setMothersName(String MotherName) {
        this.MothersName = MotherName;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String Sex) {
        this.Sex = Sex;
    }

    public String getPhone1() {
        return Phone1;
    }

    public void setPhone1(String Phone1) {
        this.Phone1 = Phone1;
    }

    public String getPhone2() {
        return Phone2;
    }

    public void setPhone2(String Phone2) {
        this.Phone2 = Phone2;
    }

    public int getSemester() {
        return Semester;
    }

    public void setSemester(int Semester) {
        this.Semester = Semester;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }
    
    
}
