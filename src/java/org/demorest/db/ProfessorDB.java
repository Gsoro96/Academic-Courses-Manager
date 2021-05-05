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
import java.util.ArrayList;
import org.demorest.connectionpool.ConnectionPool;
import org.demorest.entities.Professor;
import org.demorest.entities.User;
import org.demorest.exceptions.MyException;


/**
 * ProfessorDB class implemets all the communication between the webservices and the professors table of the database.
 * @author George
 */
public class ProfessorDB {
    /**
     * Method that returns an instance of type Professor from the Database.
     * @param ID String. The id of the Professor.
     * @return Professor. If the professor exists returns a Professor object else returns null.
     * @throws MyException when an SQLException or ClassNotFoundException occur.
     */
    public Professor getProfessorByID(User user,String ID) throws MyException {
        try {
            Professor p = new Professor();
            //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
            PreparedStatement st = con.prepareStatement("SELECT * FROM professors WHERE ID=?");
            st.setString(1, ID);
            ResultSet results = st.executeQuery();
            //check if select return nothing
            if (!results.isBeforeFirst() ) {
                results.close();
                st.close();
                return null;
            }
            while(results.next()){
                p.setID(results.getString(1));
                p.setSurName(results.getString(2));
                p.setFirstName(results.getString(3));
                p.setEmail(results.getString(4));
                p.setPhone1(results.getString(5));
                
            }
            results.close();
            st.close();
            return p;
        } catch (ClassNotFoundException ex) {
           throw new MyException("ClassNotFoundException",ex);
        } catch (SQLException ex) {
           throw new MyException("SQLException",ex);
        }
    }
    
    /**
     * This method return a list of Professors by CourseID and SemesterID.
     * @param CourseID String. The id of the Course.
     * @param SemesterID int. The id of the Semester.
     * @return {@code ArrayList<Professor>}. The list of Professors.
     * @throws MyException when an SQLException or ClassNotFoundException occurs.
     */
    public ArrayList<Professor> getProfByCourse(User user,String CourseID,int SemesterID) throws MyException{
        try {
            ArrayList<Professor> list = new ArrayList<>();
            ArrayList<String> list_ProfessorID = new ArrayList<>();
            //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
            PreparedStatement st = con.prepareStatement("SELECT ProfessorID FROM prof2course WHERE CourseID=? AND semesterID=?");
            st.setString(1, CourseID);
            st.setInt(2, SemesterID);
            ResultSet results = st.executeQuery();
            //check if select return nothing
            if (!results.isBeforeFirst() ) {
                results.close();
                st.close();
                return list;
            }
            while(results.next()){
                String ProfessorID=results.getString(1);
                list_ProfessorID.add(ProfessorID);
            }
            for(int i=0; i<list_ProfessorID.size(); i++){
                Professor p = new Professor();
                p=getProfessorByID(user,list_ProfessorID.get(i));
                if(p!=null)
                    list.add(p);
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
