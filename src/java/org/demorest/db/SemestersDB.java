/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demorest.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.demorest.connectionpool.ConnectionPool;
import org.demorest.entities.Semester;
import org.demorest.entities.User;
import org.demorest.exceptions.MyException;

/**
 * SemestersDB class implemets all the communication between the webservices and the semesters table of the database.
 * @author George
 */
public class SemestersDB {
    
    /**
     * Method that returns all the Semesters.
     * @return {@code ArrayList<String>}. The list of Semesters.
     * @throws MyException when an SQLException or ClassNotFoundException occurs.
     */
    public ArrayList<Semester> GetSemesters(User user) throws MyException {
        try {
            ArrayList<Semester> list = new ArrayList<>();
            
           //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
            Statement st = con.createStatement();
            ResultSet results = st.executeQuery("SELECT * FROM semesters");
            //check if select return nothing
            if (!results.isBeforeFirst() ) {
                results.close();
                st.close();
                return list;
            }
            while(results.next()){
                Semester s = new Semester();
                s.setID(results.getInt(1));
                s.setName(results.getString(2));
                list.add(s);
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
