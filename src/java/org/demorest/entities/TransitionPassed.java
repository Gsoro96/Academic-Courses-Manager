/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demorest.entities;

/**
 *
 * @author George
 */
public class TransitionPassed {
    private String CourseID;
    private String StudentAM;
    private String Grade;
    private String Type;

    public TransitionPassed(String CourseID, String StudentAM, String Grade,String Type) {
        this.CourseID = CourseID;
        this.StudentAM = StudentAM;
        this.Grade = Grade;
        this.Type = Type;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getCourseID() {
        return CourseID;
    }

    public void setCourseID(String CourseID) {
        this.CourseID = CourseID;
    }

    public String getStudentAM() {
        return StudentAM;
    }

    public void setStudentAM(String StudentAM) {
        this.StudentAM = StudentAM;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String Grade) {
        this.Grade = Grade;
    }

    @Override
    public String toString() {
        return "TransitionPassedClass{" + "CourseID=" + CourseID + ", StudentAM=" + StudentAM + ", Grade=" + Grade + '}';
    }
    
    
}
