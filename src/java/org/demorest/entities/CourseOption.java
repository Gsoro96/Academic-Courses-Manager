/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demorest.entities;

/**
 * An instance of CourseOption Class is a POJO that represents a CourseOption from the Database.
 * @author George
 */
public class CourseOption {
    private int ID;
    private String CourseID;
    private int SemesterID;
    private String ProfessorID;
    private int TheoryBiggerThan5;
    private int LabBiggerThan5;
    private float TheoryRate;
    private float LabRate;
    private int Parousies;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCourseID() {
        return CourseID;
    }

    public void setCourseID(String CourseID) {
        this.CourseID = CourseID;
    }

    public int getSemesterID() {
        return SemesterID;
    }

    public void setSemesterID(int SemesterID) {
        this.SemesterID = SemesterID;
    }

    public String getProfessorID() {
        return ProfessorID;
    }

    public void setProfessorID(String ProgessorID) {
        this.ProfessorID = ProgessorID;
    }

    public int getTheoryBiggerThan5() {
        return TheoryBiggerThan5;
    }

    public void setTheoryBiggerThan5(int TheoryBiggerThan5) {
        this.TheoryBiggerThan5 = TheoryBiggerThan5;
    }

    public int getLabBiggerThan5() {
        return LabBiggerThan5;
    }

    public void setLabBiggerThan5(int LabBiggerThan5) {
        this.LabBiggerThan5 = LabBiggerThan5;
    }

    public float getTheoryRate() {
        return TheoryRate;
    }

    public void setTheoryRate(float TheoryRate) {
        this.TheoryRate = TheoryRate;
    }

    public float getLabRate() {
        return LabRate;
    }

    public void setLabRate(float LabRate) {
        this.LabRate = LabRate;
    }

    public int getParousies() {
        return Parousies;
    }

    public void setParousies(int Parousies) {
        this.Parousies = Parousies;
    }

    @Override
    public String toString() {
        return "CourseOptions{" + "ID=" + ID + ", CourseID=" + CourseID + ", SemesterID=" + SemesterID + ", ProgessorID=" + ProfessorID + ", TheoryBiggerThan5=" + TheoryBiggerThan5 + ", LabBiggerThan5=" + LabBiggerThan5 + ", TheoryRate=" + TheoryRate + ", LabRate=" + LabRate + ", Parousies=" + Parousies + '}';
    }
    
    public String isNull(){
        if(getProfessorID() == null || getProfessorID().isEmpty() || getProfessorID().equalsIgnoreCase(" "))
            return null;
        if(getCourseID() == null || getCourseID().isEmpty() || getProfessorID().equalsIgnoreCase(" "))
            return null;
        if(getSemesterID() == 0)
            return null;
        return "OK";
    }
    
    public String isValid(){
        if(this.TheoryBiggerThan5 < 0 || this.TheoryBiggerThan5 > 1)
            return null;
        if(this.LabBiggerThan5 < 0 || this.LabBiggerThan5 > 1)
            return null;
        if((Float.compare(this.TheoryRate, 0)<0) || (Float.compare(this.TheoryRate, 1)>0))
            return null;
        if((Float.compare(this.LabRate, 0)<0) || (Float.compare(this.LabRate, 1)>0))
            return null;
        if(this.Parousies < 0 || this.Parousies > 1)
            return null;
        
         return "OK";
    }
}
