/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demorest.db;

import com.mysql.jdbc.Statement;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.demorest.connectionpool.ConnectionPool;
import org.demorest.entities.CourseOption;
import org.demorest.entities.Record;
import org.demorest.entities.User;
import org.demorest.exceptions.MyException;

/**
 * RecordDB class implemets all the communication between the webservices and the records table of the database.
 * @author George
 */
public class RecordDB {

 
     /**
      * Method that returns all the Records from the Database.
      * @param CourseID String. The id of the Course.
      * @param ProfessorID String. The id of the Professor.
      * @param SemesterID int. The id of the SemesterID.
      * @return Returns an ArrayList of type record.
      * @throws MyException when an SQLException or ClassNotFoundException accurs.
      */
     public ArrayList<Record> getRecordsBySemesterID(User user,String CourseID,String ProfessorID,int SemesterID) throws MyException {
         try {
             ArrayList<Record> list = new ArrayList<Record>();
            //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
             String sql="SELECT * FROM records WHERE CourseID= ? and ProfessorID = ? and SemesterID=?";
             PreparedStatement st = con.prepareStatement(sql);
             st.setString(1, CourseID);
             st.setString(2, ProfessorID);
             st.setInt(3, SemesterID);
             ResultSet results = st.executeQuery();
             //check if select return nothing
             if (!results.isBeforeFirst() ) {
                 results.close();
                 st.close();
                 return list;
             }
             while(results.next()){
                 Record c = new Record();
                 c.setId(results.getInt(1));
                 c.setProfessorID(results.getString(2));
                 c.setCourseID(results.getString(3));
                 c.setSemesterID(results.getInt(4));
                 c.setStudentAM(results.getString(5));
                 c.setTheoryGrade(results.getFloat(6));
                 c.setLabGrade(results.getFloat(7));
                 c.setFinalGrade(results.getFloat(8));
                 c.setTheoryComment(results.getString(9));
                 c.setLabComment(results.getString(10));
                 c.setComment(results.getString(11));
                 c.setDate(results.getString(12));
                 c.setParousies(results.getInt(13));
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
     
     public ArrayList<Record> getRecordsByStudentsBySemesterID(
            User user,
            String CourseID,
            String ProfessorID,
            String StudentAM,
            int SemesterID) throws MyException
     {
         try {
             ArrayList<Record> list = new  ArrayList<>();
            //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
             
             String sql = "SELECT * FROM records r1 where "
                     + "r1.ProfessorID=? AND "
                     + "r1.CourseID=? AND "
                     + "r1.SemesterID=? AND "
                     + "r1.StudentAM IN (?) AND "
                     + "Date = (SELECT MAX(Date) FROM records r2 WHERE r1.StudentAM = r2.StudentAM)";
             PreparedStatement st = con.prepareStatement(sql);
             st.setString(1, ProfessorID);
             st.setString(2, CourseID);
             st.setInt(3, SemesterID);
             st.setString(4, StudentAM);
             ResultSet results = st.executeQuery();
             //check if select return nothing
             if (!results.isBeforeFirst() ) {
                 results.close();
                 st.close();
                 return null;
             }
             
             while(results.next()){
                 Record c = new Record();
                 c.setId(results.getInt(1));
                 c.setProfessorID(results.getString(2));
                 c.setCourseID(results.getString(3));
                 c.setSemesterID(results.getInt(4));
                 c.setStudentAM(results.getString(5));
                 c.setTheoryGrade(results.getFloat(6));
                 c.setLabGrade(results.getFloat(7));
                 c.setFinalGrade(results.getFloat(8));
                 c.setTheoryComment(results.getString(9));
                 c.setLabComment(results.getString(10));
                 c.setComment(results.getString(11));
                 c.setDate(results.getString(12));
                 c.setParousies(results.getInt(13));
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
      * Method that returns an instance o type record by id from the Database.
      * @param RecordID int. The id of the Record.
      * @return Record.If the record was found the it returns the record else returns null.
      * @throws MyException when an SQLException or ClassNotFoundException accurs.
      */
     public Record getRecordByID(User user,int RecordID) throws MyException {
         try {
             //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
             String sql="SELECT * FROM records WHERE ID=?";
             PreparedStatement st = con.prepareStatement(sql);
             st.setInt(1, RecordID);
             ResultSet results = st.executeQuery();
             Record c = new Record();
             //check if select return nothing
             if (!results.isBeforeFirst() ) {
                 results.close();
                 st.close();
                 return null;
             }
             while(results.next()){
                 c.setId(results.getInt(1));
                 c.setProfessorID(results.getString(2));
                 c.setCourseID(results.getString(3));
                 c.setSemesterID(results.getInt(4));
                 c.setStudentAM(results.getString(5));
                 c.setTheoryGrade(results.getFloat(6));
                 c.setLabGrade(results.getFloat(7));
                 c.setFinalGrade(results.getFloat(8));
                 c.setTheoryComment(results.getString(9));
                 c.setLabComment(results.getString(10));
                 c.setComment(results.getString(11));
                 c.setDate(results.getString(12));
                 c.setParousies(results.getInt(13));
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
      * Method that returns a list with all the records by StudentAM.
      * @param CourseID String. The id of the Course.
      * @param ProfessorID String. The id of the Professor.
      * @param StudentAM String. The id of the Student.
      * @return {@code ArrayList<Records>}. The list of records.
      * @throws MyException when an SQLException or ClassNotFoundException accurs.
      */
     public ArrayList<Record> getRecordByStudent(
            User user,
            String CourseID,
            String ProfessorID,
            String StudentAM) throws MyException
     {
         try {
             ArrayList<Record> list = new  ArrayList<>();
             //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
             
             String sql = "SELECT * FROM records where "
                     + "ProfessorID=? AND "
                     + "CourseID=? AND "
                     + "StudentAM=?";
             PreparedStatement st = con.prepareStatement(sql);
             st.setString(1, ProfessorID);
             st.setString(2, CourseID);
             st.setString(3, StudentAM);
             ResultSet results = st.executeQuery();
             //check if select return nothing
             if (!results.isBeforeFirst() ) {
                 results.close();
                 st.close();
                 return null;
             }
             
             while(results.next()){
                 Record c = new Record();
                 c.setId(results.getInt(1));
                 c.setProfessorID(results.getString(2));
                 c.setCourseID(results.getString(3));
                 c.setSemesterID(results.getInt(4));
                 c.setStudentAM(results.getString(5));
                 c.setTheoryGrade(results.getFloat(6));
                 c.setLabGrade(results.getFloat(7));
                 c.setFinalGrade(results.getFloat(8));
                 c.setTheoryComment(results.getString(9));
                 c.setLabComment(results.getString(10));
                 c.setComment(results.getString(11));
                 c.setDate(results.getString(12));
                 c.setParousies(results.getInt(13));
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
      * Method that returns a list with all the records by StudentAM.
      * @param CourseID String. The id of the Course.
      * @param ProfessorID String. The id of the Professor.
      * @param StudentAM String. The id of the Student.
      * @param SemesterID int. The id of the SemesterID.
      * @return {@code ArrayList<Records>}. The list of records.
      * @throws MyException when an SQLException or ClassNotFoundException accurs.
      */
     public ArrayList<Record> getRecordByStudentBySemesterID(
            User user,
            String CourseID,
            String ProfessorID,
            String StudentAM,
            int SemesterID) throws MyException
     {
         try {
             ArrayList<Record> list = new  ArrayList<>();
            //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
             
             String sql = "SELECT * FROM records where "
                     + "ProfessorID=? AND "
                     + "CourseID=? AND "
                     + "SemesterID=? AND "
                     + "StudentAM=?";
             PreparedStatement st = con.prepareStatement(sql);
             st.setString(1, ProfessorID);
             st.setString(2, CourseID);
             st.setInt(3, SemesterID);
             st.setString(4, StudentAM);
             ResultSet results = st.executeQuery();
             //check if select return nothing
             if (!results.isBeforeFirst() ) {
                 results.close();
                 st.close();
                 return null;
             }
             
             while(results.next()){
                 Record c = new Record();
                 c.setId(results.getInt(1));
                 c.setProfessorID(results.getString(2));
                 c.setCourseID(results.getString(3));
                 c.setSemesterID(results.getInt(4));
                 c.setStudentAM(results.getString(5));
                 c.setTheoryGrade(results.getFloat(6));
                 c.setLabGrade(results.getFloat(7));
                 c.setFinalGrade(results.getFloat(8));
                 c.setTheoryComment(results.getString(9));
                 c.setLabComment(results.getString(10));
                 c.setComment(results.getString(11));
                 c.setDate(results.getString(12));
                 c.setParousies(results.getInt(13));
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
      * Method that creates an instance of type Record in the Database.
      * @param obj Record. The body of the record that will be created.
      * @return Record.If the insertion was done sucessffully the it returns the Record that was created<br>
      * else returns null.
      * @throws MyException when an SQLException or ClassNotFoundException accurs.
      */
     public Record createRecord(User user,Record obj) throws MyException{
         try {

            //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
             
             obj.setTheoryGrade(round(obj.getTheoryGrade(),1));
             obj.setLabGrade(round(obj.getLabGrade(),1));
             obj.setFinalGrade(round(obj.getFinalGrade(),1));
             
             String sql="INSERT INTO  records "
                     + "( ProfessorID ,  CourseID ,  SemesterID , "
                     + " StudentAM ,  TheoryGrade ,  LabGrade ,  FinalGrade ,"
                     + "  TheoryComment ,  LabComment ,  Comment ,  Date ,  Parousies ) "
                     + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
             PreparedStatement st = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
             st.setString(1, obj.getProfessorID());
             st.setString(2, obj.getCourseID());
             st.setInt(3, obj.getSemesterID());
             st.setString(4, obj.getStudentAM());
             st.setFloat(5, obj.getTheoryGrade());
             st.setFloat(6, obj.getLabGrade());
             st.setFloat(7, obj.getFinalGrade());
             st.setString(8, obj.getTheoryComment());
             st.setString(9, obj.getLabComment());
             st.setString(10, obj.getComment());
             st.setTimestamp(11, getCurrentTimeStamp());
             st.setInt(12, obj.getParousies());
             int count = st.executeUpdate();
             if(count>0){
                 ResultSet generatedKeys = st.getGeneratedKeys();
                 if (generatedKeys.next()) {
                    obj.setId(generatedKeys.getInt(1));
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
      * Method that creates a new Record to the database with FinalGrade attribute<br>
      * auto calculated.
      * @param obj Record. The body of the record that will be created.
      * @return Record.If the insterion was done successfully returns the record that was created else retunrs null.
      * @throws MyException when an SQLException or ClassNotFoundException accurs.
      */
     public Record createRecordAutoFinalGrade(User user,Record obj) throws MyException{
         try {

            //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
             float FinalGrade = CalculateFinalGrade(user,obj);
             
             obj.setTheoryGrade(round(obj.getTheoryGrade(),1));
             obj.setLabGrade(round(obj.getLabGrade(),1));
             FinalGrade=(round(FinalGrade,1));
             
             if(Float.compare(FinalGrade, -1)==0){
                 throw new MyException("No CourseOption Found");
             }
             String sql="INSERT INTO  records "
                     + "( ProfessorID ,  CourseID ,  SemesterID , "
                     + " StudentAM ,  TheoryGrade ,  LabGrade ,  FinalGrade ,"
                     + "  TheoryComment ,  LabComment ,  Comment ,  Date ,  Parousies ) "
                     + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
             PreparedStatement st = con.prepareStatement(sql);
             st.setString(1, obj.getProfessorID());
             st.setString(2, obj.getCourseID());
             st.setInt(3, obj.getSemesterID());
             st.setString(4, obj.getStudentAM());
             st.setFloat(5, obj.getTheoryGrade());
             st.setFloat(6, obj.getLabGrade());
             st.setFloat(7, FinalGrade);
             st.setString(8, obj.getTheoryComment());
             st.setString(9, obj.getLabComment());
             st.setString(10, obj.getComment());
             st.setTimestamp(11, getCurrentTimeStamp());
             st.setInt(12, obj.getParousies());
             int count = st.executeUpdate();
             if(count>0){
                 st.close();
                 obj.setFinalGrade(FinalGrade);
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
      * Method that updates Record from the database.
      * @param obj Record. The body of the record that will be updated.
      * @return String.If the update was done successfully returns "OK" else retunrs null.
      * @throws MyException when an SQLException or ClassNotFoundException accurs.
      */
     public String updateRecord(User user,Record obj) throws MyException {
         try {
             
            //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
             
             String sql="UPDATE records "
                     + "SET  TheoryGrade  = ?, LabGrade = ?, FinalGrade = ?,"
                     + "TheoryComment = ?, LabComment = ? , Comment = ?,"
                     + "Parousies  = ? , Date = ? WHERE ID = ?";
             
             PreparedStatement st = con.prepareStatement(sql);
             st.setFloat(1, obj.getTheoryGrade());
             st.setFloat(2, obj.getLabGrade());
             st.setFloat(3, obj.getFinalGrade());
             st.setString(4, obj.getTheoryComment());
             st.setString(5, obj.getLabComment());
             st.setString(6, obj.getComment());
             st.setInt(7, obj.getParousies());
             st.setTimestamp(8, getCurrentTimeStamp());
             st.setInt(9, obj.getId());
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
      * Method that deletes an instance of type Record from the Database.
      * @param RecordID int. The id of the Record.
      * @return String.If the deletion was done successfully returns "OK" else retunrs null.
      * @throws MyException when an SQLException or ClassNotFoundException accurs. 
      */
     public String deleteRecord(User user,int RecordID) throws MyException {
         try {
            //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
             
             String sql="DELETE FROM records WHERE ID=?";
             //check if record existis
             
             PreparedStatement st = con.prepareStatement(sql);
             st.setInt(1, RecordID);
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
      * CheckCourseOption checks if the professor enabled view mode for Students.
      * @param role int. The id of the user's Role.
      * @param CourseID String. The id of the course.
      * @param ProfessorID Stirng. The id of the Professor.
      * @return String. if the given role is not 1 returns "1".If the given role is 1 and the view mode is on<br>
      * returns "1" else returns 0.
      * @throws MyException when an SQLException or ClassNotFoundException accurs. 
      */
     public String CheckCourseOption(User user,int role,String CourseID,String ProfessorID) throws MyException{
        //check if the user is Student
        if(role == 1)
        {
            try {
                //get connection Object from user
                Connection con = user.getCon();
                if(!con.isValid(0)){
                     ConnectionPool pool = ConnectionPool.getInstance();
                     con = pool.getConnection();
                     user.setCon(con);
                     con=user.getCon();
                }
                
                String sql="SELECT viewable FROM prof2course WHERE CourseID=? and ProfessorID=?";
                PreparedStatement st = con.prepareStatement(sql);
                st.setString(1, CourseID);
                st.setString(2, ProfessorID);
                ResultSet results = st.executeQuery();
                int viewable = 0;
                //check if select return nothing
                if (!results.isBeforeFirst() ) {
                    results.close();
                    st.close();
                    return null;
                }
                while(results.next()){
                    viewable=results.getInt(1);
                }
                results.close();
                st.close();
                
                if(viewable == 0)
                    return null;
                else
                    return String.valueOf(viewable);
                
            } catch (ClassNotFoundException ex) {
                throw new MyException("ClassNotFoundException",ex);
            } catch (SQLException ex) {
                throw new MyException("SQLException",ex);
            }
        }
        else 
            return "1";
     }
     /**
      * This method updates an instance of Record to the database by auto calulating the FinaleGrade.
      * @param rec Record. The body of the record that will be updated. 
      * @return String.If the update was done successfully returns "OK" else retunrs null.
      * @throws MyException when an SQLException or ClassNotFoundException accurs. 
      */
     public String updateFinalGrade(User user,Record rec) throws MyException {
         try {
             float FinalGrade;
             FinalGrade=CalculateFinalGrade(user,rec);
            
            //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
             
             String sql="UPDATE records "
                     + "SET FinalGrade = ? WHERE ID = ?";
             
             PreparedStatement st = con.prepareStatement(sql);
             st.setFloat(1, FinalGrade);
             st.setInt(2, rec.getId());
             
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
      * Method that calculates the final grade according to the course option and given record.
      * @param rec Record. The body of the Record which will be used to calculate the FinalGrade.
      * @return float FinaleGrade. The auto calculated grade.
      * @throws MyException when an SQLException or ClassNotFoundException accurs. 
      */
     public float CalculateFinalGrade(User user,Record rec) throws MyException  {

             float FinalGrade;
             CourseOptionsDB co_db = new CourseOptionsDB();
             CourseOption co =  co_db.getCourseOptionBySemester(user,rec.getCourseID(), rec.getProfessorID(), rec.getSemesterID());
             if(co==null) return -1;
             int CourseOption_Parousies=co.getParousies();
             int CourseOption_TheoryBiggerThan5 = co.getTheoryBiggerThan5();
             int CourseOption_LabBiggerThan5 = co.getLabBiggerThan5();
             float CourseOption_TheoryRate = co.getTheoryRate();
             float CourseOption_LabRate = co.getLabRate();
             
             FinalGrade=0;
             if(CourseOption_Parousies == 0){
                if(CourseOption_TheoryBiggerThan5==0 && CourseOption_LabBiggerThan5==0){
                    FinalGrade = CourseOption_TheoryRate*rec.getTheoryGrade()+ CourseOption_LabRate*rec.getLabGrade();
                }else if(CourseOption_TheoryBiggerThan5==1 && CourseOption_LabBiggerThan5==0){
                    if(Float.compare(rec.getTheoryGrade(), 5)>=0)
                        FinalGrade = CourseOption_TheoryRate*rec.getTheoryGrade()+ CourseOption_LabRate*rec.getLabGrade();  
                }else if(CourseOption_TheoryBiggerThan5==0 && CourseOption_LabBiggerThan5==1){
                    if(Float.compare(rec.getLabGrade(), 5)>=0)
                        FinalGrade = CourseOption_TheoryRate*rec.getTheoryGrade()+ CourseOption_LabRate*rec.getLabGrade();  
                }else{
                    if((Float.compare(rec.getTheoryGrade(), 5)>=0) && Float.compare(rec.getLabGrade(), 5)>=0)
                        FinalGrade = CourseOption_TheoryRate*rec.getTheoryGrade()+ CourseOption_LabRate*rec.getLabGrade();  
                }
             }else{
                 if(rec.getParousies()==1){
                    if(CourseOption_TheoryBiggerThan5==0 && CourseOption_LabBiggerThan5==0){
                        FinalGrade = CourseOption_TheoryRate*rec.getTheoryGrade()+ CourseOption_LabRate*rec.getLabGrade();
                    }else if(CourseOption_TheoryBiggerThan5==1 && CourseOption_LabBiggerThan5==0){
                        if(Float.compare(rec.getTheoryGrade(), 5)>=0)
                            FinalGrade = CourseOption_TheoryRate*rec.getTheoryGrade()+ CourseOption_LabRate*rec.getLabGrade();  
                    }else if(CourseOption_TheoryBiggerThan5==0 && CourseOption_LabBiggerThan5==1){
                        if(Float.compare(rec.getLabGrade(), 5)>=0)
                            FinalGrade = CourseOption_TheoryRate*rec.getTheoryGrade()+ CourseOption_LabRate*rec.getLabGrade();  
                    }else{
                        if(((Float.compare(rec.getTheoryGrade(), 5)>=0) && Float.compare(rec.getLabGrade(), 5)>=0))
                            FinalGrade = CourseOption_TheoryRate*rec.getTheoryGrade()+ CourseOption_LabRate*rec.getLabGrade();  
                    } 
              }
             }
       return FinalGrade;
     }
    
    /**
     * This method round a float number to the given decimalPlace. e.g round(3.15,1) - result = 3.1
     * @param d. The float number that we want to round.
     * @param decimalPlace. The number of decimal digit tha we want to round to.
     * @return float.The rounded number.
     */
     public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
     
     /**
      * This method returns the current Timestamp.
      * @return Timstamp. The Current Timestamp.
      */
     private static java.sql.Timestamp getCurrentTimeStamp() {
	java.util.Date today = new java.util.Date();
	return new java.sql.Timestamp(today.getTime());
    }
    
}
