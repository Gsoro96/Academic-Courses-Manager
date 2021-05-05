/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demorest.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.demorest.connectionpool.ConnectionPool;
import org.demorest.entities.Course;
import org.demorest.entities.User;
import org.demorest.exceptions.MyException;


/**
 * CourseDB class implemets all the communication between the webservices and the course table of the database.
 * @author George
 */
public class CourseDB {


    /**
     * Method that returns all the Courses.
     * @return {@code ArrayList<Course>}. All the Courses.
     * @throws MyException when an SQLException or ClassNotFoundException occurs.
     */
    public ArrayList<Course> getCourses(User user) throws MyException {
        try {
            ArrayList<Course> list = new ArrayList<Course>();
            
            //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
            Statement st = con.createStatement();
            ResultSet results = st.executeQuery("SELECT * FROM courses");
            //check if select return nothing
            if (!results.isBeforeFirst() ) {
                results.close();
                st.close();      
                return list;
            }
            while(results.next()){
                Course c = new Course();
                c.setId(results.getString(1));
                c.setName(results.getString(2));
                c.setCurr(results.getString(3));
                list.add(c);
            }
            results.close();
            st.close();
            return list;
        } catch (ClassNotFoundException ex) {
           throw new MyException("ClassNotFoundException",ex);
        } catch (SQLException ex) {
           throw new MyException("SQLException",ex);
        }
    }
    
    /**
     * Method that returns a course by a given id.
     * @param id String, The id of the Course.
     * @return Returns Course if the spesific course was found else returns null.
     * @throws MyException when an SQLException or ClassNotFoundException occurs.
     */
    public Course getCourseByID(User user,String id) throws MyException {
        try {
            Course c = new Course(); 
            
           //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
            PreparedStatement st = con.prepareStatement("SELECT * FROM courses WHERE ID=?");
            st.setString(1, id);
            ResultSet results = st.executeQuery();
            //check if select return nothing
            if (!results.isBeforeFirst() ) {
                
                results.close();
                st.close();
                return null;
            }
            while(results.next()){
                c.setId(results.getString(1));
                c.setName(results.getString(2));
                c.setCurr(results.getString(3));
            }
            results.close();
            st.close();
            return c;
        } catch (ClassNotFoundException ex) {
            throw new MyException("ClassNotFoundException",ex);
        } catch (SQLException ex) {
            throw new MyException("SQLException",ex);
        }
    }
    
        /**
     * This method returns a list of Courses by ProfessorID and SemesterID.
     * @param ProfessorID String. The id of the Professor.
     * @return {@code ArrayList<Course>}. The list with the Courses.
     * @throws MyException when an SQLException or ClassNotFoundException occurs.
     */
     public ArrayList<Course> getCoursesByProf(User user,String ProfessorID) throws MyException {
        try {
            ArrayList<Course> list = new ArrayList<>();
            ArrayList<String> list_courseID = new ArrayList<>();
           
            //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
            
            PreparedStatement st = con.prepareStatement("SELECT DISTINCT CourseID FROM prof2course WHERE ProfessorID=? AND (NOT (CourseID LIKE \"%-Θ\" OR CourseID LIKE \"%-Ε\"))");
            st.setString(1, ProfessorID);
            ResultSet results = st.executeQuery();
            //check if select return nothing
            if (!results.isBeforeFirst() ) {
                results.close();
                st.close();              
                return list;
            }
            
            while(results.next()){
                String CourseID=results.getString(1);
                list_courseID.add(CourseID);
            }
            for(int i=0; i<list_courseID.size(); i++){
                Course c = new Course();
                c=getCourseByID(user,list_courseID.get(i));
                if(c!=null)
                    list.add(c);
            }
            results.close();
            st.close();
            return list;
            
        } catch (ClassNotFoundException ex) {
           throw new MyException("ClassNotFoundException",ex);
        } catch (SQLException ex) {
           throw new MyException("SQLException",ex);
        }
     }
    
    /**
     * This method returns a list of Courses by ProfessorID and SemesterID.
     * @param ProfessorID String. The id of the Professor.
     * @param SemesterID int. The id of the Semester.
     * @return {@code ArrayList<Course>}. The list with the Courses.
     * @throws MyException when an SQLException or ClassNotFoundException occurs.
     */
     public ArrayList<Course> getCoursesByProfBySemester(User user,String ProfessorID,int SemesterID) throws MyException{
        try {
            ArrayList<Course> list = new ArrayList<>();
            ArrayList<String> list_courseID = new ArrayList<>();
            //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
            PreparedStatement st = con.prepareStatement("SELECT CourseID FROM prof2course WHERE ProfessorID=? AND semesterID=? AND (NOT (CourseID LIKE \"%-Θ\" OR CourseID LIKE \"%-Ε\"))");
            st.setString(1, ProfessorID);
            st.setInt(2, SemesterID);
            ResultSet results = st.executeQuery();
            //check if select return nothing
            if (!results.isBeforeFirst() ) {
                results.close();
                st.close();
                return list;
            }
            while(results.next()){
                String CourseID=results.getString(1);
                list_courseID.add(CourseID);
            }
            for(int i=0; i<list_courseID.size(); i++){
                Course c = new Course();
                c=getCourseByID(user,list_courseID.get(i));
                if(c!=null)
                    list.add(c);
            }
            results.close();
            st.close();
            return list;
        } catch (ClassNotFoundException ex) {
           throw new MyException("ClassNotFoundException",ex);
        } catch (SQLException ex) {
           throw new MyException("SQLException",ex);
        }
     } 

    /**
     * This method returns a list of Courses bu given StudentAm and SemesterID.
     * @param StudentAM String. The id of the Student.
     * @param SemesterID int. The id of the semester.
     * @return {@code ArrayList<Course>}. The list with the Courses.
     * @throws MyException when an SQLException or ClassNotFoundException occurs.
     */
    public ArrayList<Course> getCoursesByStud(User user,String StudentAM,int SemesterID) throws MyException {
        try {
            ArrayList<Course> list = new ArrayList<>();
            ArrayList<String> list_courseID = new ArrayList<>();
            
            //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
            PreparedStatement st = con.prepareStatement("SELECT CourseID FROM stud2course WHERE StudentAM=? AND semesterID=? AND (NOT (CourseID LIKE \"%-Θ\" OR CourseID LIKE \"%-Ε\"))");
            st.setString(1, StudentAM);
            st.setInt(2, SemesterID);
            ResultSet results = st.executeQuery();
            //check if select return nothing
            if (!results.isBeforeFirst() ) {
                results.close();
                st.close();
                return list;
            }
            while(results.next()){
                String CourseID=results.getString(1);
                list_courseID.add(CourseID);
            }
            for(int i=0; i<list_courseID.size(); i++){
                Course c = new Course();
                c=getCourseByID(user,list_courseID.get(i));
                if(c!=null)
                    list.add(c);
            }
            results.close();
            st.close();
            return list;
        } catch (ClassNotFoundException ex) {
           throw new MyException("ClassNotFoundException",ex);
        } catch (SQLException ex) {
           throw new MyException("SQLException",ex);
        }
    }
    
}
