/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demorest.entities;


/**
 * An instance of Record Class is a POJO that represents a Record from the Database.
 * @author George
 */

public class Record {
    private int id;
    private String ProfessorID;
    private String CourseID;
    private int SemesterID;
    private String StudentAM;
    private float TheoryGrade;
    private float LabGrade;
    private float FinalGrade;
    private String TheoryComment;
    private String LabComment;
    private String Comment;
    private String Date;
    private int Parousies;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfessorID() {
        return ProfessorID;
    }

    public void setProfessorID(String ProfessorID) {
        this.ProfessorID = ProfessorID;
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

    public String getStudentAM() {
        return StudentAM;
    }

    public void setStudentAM(String StudentAM) {
        this.StudentAM = StudentAM;
    }

    public float getTheoryGrade() {
        return TheoryGrade;
    }

    public void setTheoryGrade(float TheoryGrade) {
        this.TheoryGrade = TheoryGrade;
    }

    public float getLabGrade() {
        return LabGrade;
    }

    public void setLabGrade(float LabGrade) {
        this.LabGrade = LabGrade;
    }

    public float getFinalGrade() {
        return FinalGrade;
    }

    public void setFinalGrade(float FinalGrade) {
        this.FinalGrade = FinalGrade;
    }

    public String getTheoryComment() {
        return TheoryComment;
    }

    public void setTheoryComment(String TheoryComment) {
        this.TheoryComment = TheoryComment;
    }

    public String getLabComment() {
        return LabComment;
    }

    public void setLabComment(String LabComment) {
        this.LabComment = LabComment;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String Comment) {
        this.Comment = Comment;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public int getParousies() {
        return Parousies;
    }

    public void setParousies(int Parousies) {
        this.Parousies = Parousies;
    }

    @Override
    public String toString() {
        return "Record{" + "id=" + id + ", ProfessorID=" + ProfessorID + ", CourseID=" + CourseID + ", SemesterID=" + SemesterID + ", StudentAM=" + StudentAM + ", TheoryGrade=" + TheoryGrade + ", LabGrade=" + LabGrade + ", FinalGrade=" + FinalGrade + ", TheoryComment=" + TheoryComment + ", LabComment=" + LabComment + ", Comment=" + Comment + ", Date=" + Date + ", Parousies=" + Parousies + '}';
    }
    
    
    public String isNull(){
        
        if(getProfessorID() == null || getProfessorID().isEmpty() || getProfessorID().equalsIgnoreCase(" "))
            return null;
        if(getCourseID() == null || getCourseID().isEmpty() || getProfessorID().equalsIgnoreCase(" "))
            return null;
        if(getSemesterID() == 0)
            return null;
        if(getStudentAM() == null || getStudentAM().isEmpty() || getStudentAM().equalsIgnoreCase(" "))
            return null;
        
        return "OK";
    }
    
    public String isValid(){    
        if((Float.compare(this.TheoryGrade, 0)<0) || (Float.compare(this.TheoryGrade, 10)>0))
            return null;
        if((Float.compare(this.LabGrade, 0)<0) || (Float.compare(this.LabGrade, 10)>0))
            return null;
        if((Float.compare(this.FinalGrade, 0)<0) || (Float.compare(this.FinalGrade, 10)>0))
           return null;
        if(this.Parousies < 0 || this.Parousies > 1)
            return null;
        return "OK";
    }
    
}
