/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demorest.db;

import com.mysql.jdbc.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.demorest.connectionpool.ConnectionPool;
import org.demorest.entities.CourseOption;
import org.demorest.entities.User;
import org.demorest.exceptions.MyException;

/**
 * CourseDB class implemets all the communication between the webservices and the courseoptions table of the database.
 * @author George
 */
public class CourseOptionsDB {
    
    /**
     * Method that returns all CourseOptions from the Database.
     * @param CourseID String. The id of the Course.
     * @param ProfessorID String. The id of the Professor.
     * @return {@code ArrayList<Course>}. The list of CourseOptions.
     * @throws MyException when an SQLException or ClassNotFoundException occurs.
     */
    public ArrayList<CourseOption> getCourseOptions(User user,String CourseID , String ProfessorID) throws MyException {
        try {
            ArrayList<CourseOption> list = new ArrayList();
            //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
            PreparedStatement st = con.prepareStatement("SELECT * FROM courseoptions WHERE CourseID=? and ProfessorID=?");
            st.setString(1, CourseID);
            st.setString(2, ProfessorID);
            ResultSet results = st.executeQuery();
            
            //check if select return nothing
            if (!results.isBeforeFirst() ) {
                results.close();
                st.close();
                return list;
            }
            while(results.next()){
                CourseOption c = new CourseOption();
                c.setID(results.getInt(1));
                c.setCourseID(results.getString(2));
                c.setSemesterID(results.getInt(3));
                c.setProfessorID(results.getString(4));
                c.setTheoryBiggerThan5(results.getInt(5));
                c.setLabBiggerThan5(results.getInt(6));
                c.setTheoryRate(results.getFloat(7));
                c.setLabRate(results.getFloat(8));
                c.setParousies(results.getInt(9));
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
     * Method that returns an instance of CourseOption by SemesterID from the Database.
     * @param CourseID String. The id of the Course.
     * @param ProfessorID String. The id of the Professor.
     * @param SemesterID int. The id of the SemesterID.
     * @return {@code ArrayList<Course>}. The list of CourseOptions.
     * @throws MyException when an SQLException or ClassNotFoundException occurs.
     */
    public CourseOption getCourseOptionBySemester(User user,String CourseID , String ProfessorID, int SemesterID) throws MyException{
        try {
            //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
            PreparedStatement st = con.prepareStatement("SELECT * FROM courseoptions WHERE CourseID=? and ProfessorID=? and SemesterID=?");
            st.setString(1, CourseID);
            st.setString(2, ProfessorID);
            st.setInt(3, SemesterID);
            ResultSet results = st.executeQuery();
            
            //check if select return nothing
            if (!results.isBeforeFirst() ) {
                results.close();
                st.close();
                return null;
            }
            CourseOption c = new CourseOption();
            while(results.next()){
                
                c.setID(results.getInt(1));
                c.setCourseID(results.getString(2));
                c.setSemesterID(results.getInt(3));
                c.setProfessorID(results.getString(4));
                c.setTheoryBiggerThan5(results.getInt(5));
                c.setLabBiggerThan5(results.getInt(6));
                c.setTheoryRate(results.getFloat(7));
                c.setLabRate(results.getFloat(8));
                c.setParousies(results.getInt(9));
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
     * Method that creates an instance of CourseOption int the Database.
     * @param CourseID String. The id of the Course.
     * @param ProfessorID String. The id of the Professor.
     * @param SemesterID int. The id of the SemesterID.
     * @param obj String Request Body.
     * @return CourseOption. If the insertion was sucessfull then it returns the CourseOption that was given to create<br>
     * else returns null.
     * @throws MyException when an SQLException or ClassNotFoundException occurs.
     */
    public CourseOption createCourseOption(User user,String ProfessorID,String CourseID,int SemesterID ,CourseOption obj) throws MyException {
        try {
            obj.setCourseID(CourseID);
            obj.setProfessorID(ProfessorID);
            obj.setSemesterID(SemesterID);
           //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
            String sql="INSERT INTO `courseoptions`(`CourseID`, `SemesterID`, `ProfessorID`, `TheoryBiggerThan5`, `LabBiggerThan5`,"
                    + " `TheoryRate`, `LabRate`, `Parousies`) "
                    + "VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement st = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            st.setString(1, CourseID);
            st.setInt(2, obj.getSemesterID());
            st.setString(3, obj.getProfessorID());
            st.setInt(4, obj.getTheoryBiggerThan5());
            st.setInt(5, obj.getLabBiggerThan5());
            st.setFloat(6, obj.getTheoryRate());
            st.setFloat(7, obj.getLabRate());
            st.setInt(8, obj.getParousies());
            int count = st.executeUpdate();
            if(count>0){
                ResultSet generatedKeys = st.getGeneratedKeys();
                 if (generatedKeys.next()) {
                    obj.setID(generatedKeys.getInt(1));
                }else
                     throw new SQLException();
                st.close();

                return obj;
            }
            else{
                st.close();
                return null;
            }
        } catch (ClassNotFoundException ex) {
            throw new MyException("ClassNotFoundException",ex);
        } catch (SQLException ex) {
            throw new MyException("SQLException",ex);
        }
     }
    /**
     * Method that updates an instance of type CourseOption in the Database.
     * @param CourseID String. The id of the Course.
     * @param ProfessorID String. The id of the Professor.
     * @param SemesterID int. The id of the SemesterID. 
     * @param obj CourseOption Request Body
     * @return String. If the update was done successfully then it returns "OK" else returns null.
     * @throws MyException when an SQLException or ClassNotFoundException occurs.
     */ 
    public String updateCourseOption(User user,String CourseID,String ProfessorID,int SemesterID,CourseOption obj) throws MyException {
        try {
            obj.setCourseID(CourseID);
            obj.setProfessorID(ProfessorID);
            obj.setSemesterID(SemesterID);
           //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }

            String sql="UPDATE courseoptions "
                    + "SET TheoryBiggerThan5 = ?, LabBiggerThan5 = ? , TheoryRate = ?,"
                    + " LabRate  = ? , Parousies = ? WHERE CourseID = ? AND ProfessorID = ? AND SemesterID = ? ";
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, obj.getTheoryBiggerThan5());
            st.setInt(2, obj.getLabBiggerThan5());
            st.setFloat(3, obj.getTheoryRate());
            st.setFloat(4, obj.getLabRate());
            st.setInt(5, obj.getParousies());
            st.setString(6, obj.getCourseID());
            st.setString(7, obj.getProfessorID());
            st.setInt(8, obj.getSemesterID());
            int count = st.executeUpdate();
            if(count>0){
                st.close();
                return "OK";
            }
            else{
                st.close();
                return null;
            }
        } catch (ClassNotFoundException ex) {
           throw new MyException("ClassNotFoundException",ex);
        } catch (SQLException ex) {
           throw new MyException("SQLException",ex);
        }
    }
    /**
     * Method that deletes an instance of type CourseOption from the Database.
     * @param CourseID String. The id of the Course.
     * @param ProfessorID String. The id of the Professor.
     * @param SemesterID int. The id of the Semester.
     * @return String. If the Deletion was done seccessfully the it returns "OK" else returns null.
     * @throws MyException when an SQLException or ClassNotFoundException occurs
     */
    public String deleteCourseOption(User user,String CourseID,String ProfessorID,int SemesterID) throws MyException {
        try {
            //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
            String sql="DELETE FROM courseoptions WHERE CourseID=? AND ProfessorID=? AND SemesterID=?";
            //check if record existis
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, CourseID);
            st.setString(2, ProfessorID);
            st.setInt(3, SemesterID);
            int count = st.executeUpdate();
            if(count>0){
                st.close();
                return "OK";
            }
            else{
                st.close();
                return null;
            }
        } catch (ClassNotFoundException ex) {
           throw new MyException("ClassNotFoundException",ex);
        } catch (SQLException ex) {
           throw new MyException("SQLException",ex);
        }
    }
     
}
