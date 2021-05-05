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
import org.demorest.entities.Student;
import org.demorest.entities.TransitionPassed;
import org.demorest.entities.User;
import org.demorest.exceptions.MyException;

/**
 * StudentDB class implemets all the communication between the webservices and the students table of the database.
 * @author George
 */
public class StudentDB {
    
    /**
     * Method that returns an instance of type Student by AM.
     * @param AM String.The id of the Student.
     * @return Student.If the student was found it returns the Student Object else returns null.
     * @throws MyException when an SQLException or ClassNotFoundException occurs.
     */
    public Student getStudentByAM(User user,String AM) throws MyException{
        try {
            Student s = new Student();
            //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
            PreparedStatement st = con.prepareStatement("SELECT * FROM students WHERE AM=?");
            st.setString(1, AM);
            ResultSet results = st.executeQuery();
            //check if select return nothing
            if (!results.isBeforeFirst() ) {
                
                results.close();
                st.close();
                return null;
            }
            while(results.next()){
                s.setAM(results.getString(1));
                s.setSurName(results.getString(2));
                s.setFirstName(results.getString(3));
                s.setFathersName(results.getString(4));
                s.setMothersName(results.getString(5));
                s.setSex(results.getString(6));
                s.setPhone1(results.getString(7));
                s.setPhone2(results.getString(8));
                s.setSemester(results.getInt(9));
                s.setStatus(results.getString(10));
            }
            //Get Curricula of student
            st = con.prepareStatement("SELECT DiplomaType,OriginalCurriculum FROM transitionmain WHERE AM=?");
            st.setString(1, AM);
            results = st.executeQuery();
            
            //check if select return nothing
            if (!results.isBeforeFirst() ) {
                
                results.close();
                st.close();
                return null;
            }
            while(results.next()){
                if(results.getString(1).equalsIgnoreCase("ΠΑΝΕΠΙΣΤΗΜΙΟΥ")){
                    s.setCurriculumID("ΠΑΔΑ");
                    s.setOriginalCurriculumID(results.getString(2));
                }      
                else{
                    s.setCurriculumID(results.getString(2)); 
                    s.setOriginalCurriculumID(results.getString(2));
                }
                   
            }
            
            results.close();
            st.close();
            return s;
        } catch (ClassNotFoundException ex) {
           throw new MyException("ClassNotFoundException",ex);
        } catch (SQLException ex) {
           throw new MyException("SQLException",ex);
        }
    }
    
    /**
     * This method reuturns a list of students that their AM OR SurName is alike with the str parameter.
     * @param str String.
     * @param CourseID String. The id of the Course.
     * @param SemesterID int. The id of the Semester.
     * @return {@code ArrayList<Students>}. The list of Student.
     * @throws MyException when an SQLException or ClassNotFoundException occurs.
     */
    public ArrayList<Student> getStudentsBySemesterID(User user,String str,String CourseID,int SemesterID) throws MyException {
        try {
            ArrayList<Student> list = new ArrayList();
            ArrayList<Student> Final_list = new ArrayList();
            //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }

            PreparedStatement st = con.prepareStatement("SELECT * FROM students WHERE AM LIKE ? OR SurName LIKE ?");
            st.setString(1, str+"%");
            st.setString(2, str+"%");
            ResultSet results = st.executeQuery();
            //check if select return nothing
            if (!results.isBeforeFirst() ) {
                results.close();
                st.close();
                return list;
            }
            while(results.next()){
                Student s = new Student();
                s.setAM(results.getString(1));
                s.setSurName(results.getString(2));
                s.setFirstName(results.getString(3));
                s.setFathersName(results.getString(4));
                s.setMothersName(results.getString(5));
                s.setSex(results.getString(6));
                s.setPhone1(results.getString(7));
                s.setPhone2(results.getString(8));
                s.setSemester(results.getInt(9));
                s.setStatus(results.getString(10));
                list.add(s);
            }
            
            for(int i=0; i<list.size(); i++){
                st = con.prepareStatement("SELECT StudentAM FROM stud2course WHERE StudentAM=? AND CourseID=? AND SemesterID=?");
                st.setString(1, list.get(i).getAM());
                st.setString(2, CourseID);
                st.setInt(3, SemesterID);
                results = st.executeQuery();
                while(results.next()){
                    Final_list.add(list.get(i));
                }
            }
            
            for(int j=0; j<Final_list.size(); j++){
                Student s = new Student();
                    //Get Curricula of student
                st = con.prepareStatement("SELECT DiplomaType,OriginalCurriculum FROM transitionmain WHERE AM=?");
                st.setString(1, Final_list.get(j).getAM());
                results = st.executeQuery();

                //check if select return nothing
                if (!results.isBeforeFirst() ) {

                    results.close();
                    st.close();
                    return null;
                }
                while(results.next()){
                    s=Final_list.get(j);
                    if(results.getString(1).equalsIgnoreCase("ΠΑΝΕΠΙΣΤΗΜΙΟΥ")){ 
                       s.setCurriculumID("ΠΑΔΑ");
                       s.setOriginalCurriculumID(results.getString(2));                       
                    }  
                    else{
                        s.setCurriculumID(results.getString(2));
                        s.setOriginalCurriculumID(results.getString(2));
                    }
                      Final_list.set(j,s); 
                }
            }
            
            
            results.close();
            st.close();
            return Final_list;
        } catch (ClassNotFoundException ex) {
           throw new MyException("ClassNotFoundException",ex);
        } catch (SQLException ex) {
           throw new MyException("SQLException",ex);
        }
    }
    
    /**
     * This method reuturns a list of students that their AM OR SurName is alike with the str parameter.
     * @param str String.
     * @param CourseID String. The id of the Course.
     * @return {@code ArrayList<Students>}. The list of Student.
     * @throws MyException when an SQLException or ClassNotFoundException occurs.
     */
    public ArrayList<Student> getStudents(User user,String str,String CourseID) throws MyException {
        try {
            ArrayList<Student> list = new ArrayList();
            ArrayList<Student> Final_list = new ArrayList();
            ArrayList<Student> result = new ArrayList();
            //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }

            PreparedStatement st = con.prepareStatement("SELECT * FROM students WHERE AM LIKE ? OR SurName LIKE ?");
            st.setString(1, str+"%");
            st.setString(2, str+"%");
            ResultSet results = st.executeQuery();
            //check if select return nothing
            if (!results.isBeforeFirst() ) {
                results.close();
                st.close();
                return list;
            }
            while(results.next()){
                Student s = new Student();
                s.setAM(results.getString(1));
                s.setSurName(results.getString(2));
                s.setFirstName(results.getString(3));
                s.setFathersName(results.getString(4));
                s.setMothersName(results.getString(5));
                s.setSex(results.getString(6));
                s.setPhone1(results.getString(7));
                s.setPhone2(results.getString(8));
                s.setSemester(results.getInt(9));
                s.setStatus(results.getString(10));
                list.add(s);
            }
            for(int i=0; i<list.size(); i++){
                st = con.prepareStatement("SELECT StudentAM FROM stud2course WHERE StudentAM=? AND CourseID=?");
                st.setString(1, list.get(i).getAM());
                st.setString(2, CourseID);
                results = st.executeQuery();
                while(results.next()){
                    Final_list.add(list.get(i));
                }
            }
            
            for(int j=0; j<Final_list.size(); j++){
                Student s = new Student();
                    //Get Curricula of student
                st = con.prepareStatement("SELECT DiplomaType,OriginalCurriculum FROM transitionmain WHERE AM=?");
                st.setString(1, Final_list.get(j).getAM());
                results = st.executeQuery();

                //check if select return nothing
                if (!results.isBeforeFirst() ) {

                    results.close();
                    st.close();
                    return null;
                }
                while(results.next()){
                    s=Final_list.get(j);
                    if(results.getString(1).equalsIgnoreCase("ΠΑΝΕΠΙΣΤΗΜΙΟΥ")){ 
                       s.setCurriculumID("ΠΑΔΑ");
                       s.setOriginalCurriculumID(results.getString(2));                       
                    }  
                    else{
                        s.setCurriculumID(results.getString(2));
                        s.setOriginalCurriculumID(results.getString(2));
                    }
                      Final_list.set(j,s); 
                }
            }
            
            if(!Final_list.isEmpty()){
                result.add(Final_list.get(0));
                for(int i=0; i<Final_list.size(); i++){
                    boolean flag=false;
                    for(int j=0; j<result.size(); j++){
                        if(Final_list.get(i).getAM().equalsIgnoreCase(result.get(j).getAM()))
                            flag=true;
                    }
                    if(flag==false)
                        result.add(Final_list.get(i));
                }
            }
            
            
            results.close();
            st.close();
            return result;
        } catch (ClassNotFoundException ex) {
           throw new MyException("ClassNotFoundException",ex);
        } catch (SQLException ex) {
           throw new MyException("SQLException",ex);
        }
    }
    
    
    public ArrayList<String> getMatchings(User user,String CourseID,String CurrID) throws MyException{
        try {
            ArrayList<String> Course_List = new ArrayList();
            //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
            PreparedStatement st;
            if(CurrID.equalsIgnoreCase("ΠΑΔΑ"))
                 st = con.prepareStatement("SELECT OCID FROM matchings WHERE NCID=?");
            else
                 st = con.prepareStatement("SELECT NCID FROM matchings WHERE OCID=?");
            
            st.setString(1, CourseID);
            ResultSet results = st.executeQuery();
            //check if select return nothing
            if (!results.isBeforeFirst() ) {
                
                results.close();
                st.close();
                return null;
            }
            while(results.next()){
                Course_List.add(results.getString(1));
            }
           
            results.close();
            st.close();
            return Course_List;
        } catch (ClassNotFoundException ex) {
           throw new MyException("ClassNotFoundException",ex);
        } catch (SQLException ex) {
           throw new MyException("SQLException",ex);
        }
    }
    
    public TransitionPassed getTransitionPassed(User user,String CourseID,String CourseCurr,String StudentAM) throws MyException{
        String res = null;
        String course = null;
        
        Student s = getStudentByAM(user,StudentAM);
        if(s!=null){ 
            // Student DyplomaType NOT PADA
            if(!s.getCurriculumID().equalsIgnoreCase("ΠΑΔΑ")){ 
                try {
                   //get connection Object from user
                    Connection con = user.getCon();
                    if(!con.isValid(0)){
                         ConnectionPool pool = ConnectionPool.getInstance();
                         con = pool.getConnection();
                         user.setCon(con);
                         con=user.getCon();
                    }
                     
                    PreparedStatement st = con.prepareStatement("SELECT Grade FROM transitionpassed WHERE AM=? AND CID=?");
                    st.setString(1, StudentAM);
                    st.setString(2, CourseID);
                    ResultSet results = st.executeQuery();
                    
                    if (!results.isBeforeFirst()){
                        results.close();
                        st.close();
                        return null;
                    }
                    while(results.next()){
                        res=results.getString(1);
                    }
                    results.close();
                    st.close();
                    return new TransitionPassed(CourseID,StudentAM,res,"Final Grade");
                } catch (ClassNotFoundException ex) {
                    throw new MyException("ClassNotFoundException",ex);
                 } catch (SQLException ex) {
                    throw new MyException("SQLException",ex);
                 }
               
            }else{
                 // Course from PADA
                 if(CourseCurr.equalsIgnoreCase("ΠΑΔΑ")){
                     try {
                         ArrayList<String> matchings = new ArrayList();
                         //Get matchings for Course from PADA
                         matchings = getMatchings(user,CourseID,"ΠΑΔΑ");
                         if(matchings.isEmpty())
                             return null;
                         
                         if(s.getOriginalCurriculumID().equalsIgnoreCase("ΑΕΙΠ")){
                            for(int i=0; i<matchings.size(); i++){
                                if(matchings.get(i).startsWith("244")){
                                    course = matchings.get(i);
                                    
                                }
                            }
                        }else if(s.getOriginalCurriculumID().equalsIgnoreCase("ΤΕΙΑ-Ν1")){
                            for(int i=0; i<matchings.size(); i++){
                                if(matchings.get(i).startsWith("Ν1")){
                                    course = matchings.get(i);
                                    
                                }
                            }
                        }else{
                            for(int i=0; i<matchings.size(); i++){
                                if(matchings.get(i).startsWith("Ν2")){
                                    course = matchings.get(i);
                                   
                                } 
                            }
                        }        
                         
                         
                       //get connection Object from user
                        Connection con = user.getCon();
                        if(!con.isValid(0)){
                             ConnectionPool pool = ConnectionPool.getInstance();
                             con = pool.getConnection();
                             user.setCon(con);
                             con=user.getCon();
                        }
                         
                         PreparedStatement st = con.prepareStatement("SELECT Grade FROM transitionpassed WHERE AM=? AND CID=?");
                         st.setString(1, StudentAM);
                         st.setString(2, course);
                         ResultSet results = st.executeQuery();
                         //Complete Course grade not found
                         if (!results.isBeforeFirst()) {
                             //Search for TheoryGrade
                             st = con.prepareStatement("SELECT Grade FROM transitionpassed WHERE AM=? AND CID=?");
                             st.setString(1, StudentAM);
                             st.setString(2, course+"-Θ");
                             results = st.executeQuery();
                             if(!results.isBeforeFirst()){
                                 //Search for LabGrade
                                 st = con.prepareStatement("SELECT Grade FROM transitionpassed WHERE AM=? AND CID=?");
                                 st.setString(1, StudentAM);
                                 st.setString(2, course+"-Ε");
                                 results = st.executeQuery();
                                 if(!results.isBeforeFirst()){
                                     results.close();
                                     st.close();
                                     return null;
                                 }
                                 while(results.next()){
                                     res=results.getString(1);
                                 }
                                 //Lab Grade Found
                                 results.close();
                                 st.close();
                                 return new TransitionPassed(CourseID,StudentAM,res,"Lab Grade");
                             }
                             while(results.next()){
                                 res=results.getString(1);
                             }
                             //Theory Grade Found
                             results.close();
                             st.close();
                             return new TransitionPassed(CourseID,StudentAM,res,"Theory Grade");
                         }
                         while(results.next()){
                             res=results.getString(1);
                         }
                         //Grade for complete Course found
                         results.close();
                         st.close();
                         return new TransitionPassed(CourseID,StudentAM,res,"Final Grade");
                     } catch (ClassNotFoundException ex) {
                        throw new MyException("ClassNotFoundException",ex);
                     } catch (SQLException ex) {
                        throw new MyException("SQLException",ex);
                     }
                    
                    
                 }else{
                     try {
                         //get connection Object from user
                        Connection con = user.getCon();
                        if(!con.isValid(0)){
                             ConnectionPool pool = ConnectionPool.getInstance();
                             con = pool.getConnection();
                             user.setCon(con);
                             con=user.getCon();
                        }
                         
                         PreparedStatement st = con.prepareStatement("SELECT Grade FROM transitionpassed WHERE AM=? AND CID=?");
                         st.setString(1, StudentAM);
                         st.setString(2, CourseID);
                         ResultSet results = st.executeQuery();
                        
                         if (!results.isBeforeFirst() ) {
                             results.close();
                             st.close();
                             return null;
                         }
                         
                        while(results.next()){
                             res=results.getString(1);
                         }
                         results.close();
                         st.close();
                         return new TransitionPassed(CourseID,StudentAM,res,"Final Grade");
                           
                         
                     } catch (ClassNotFoundException ex) {
                        throw new MyException("ClassNotFoundException",ex);
                     } catch (SQLException ex) {
                        throw new MyException("SQLException",ex);
                     }
                 }
            }
        }
      return null;  
    }
    
    public ArrayList<Student> getStudentsByCourseBySemester(User user,String CourseID,int SemesterID) throws MyException{
        try{
            ArrayList <Student> StudentsByCourse = new ArrayList();
            
            //get connection Object from user
            Connection con = user.getCon();
            if(!con.isValid(0)){
                 ConnectionPool pool = ConnectionPool.getInstance();
                 con = pool.getConnection();
                 user.setCon(con);
                 con=user.getCon();
            }
            PreparedStatement st = con.prepareStatement("select students.AM , students.SurName, students.FirstName, students.FatherName, students.MotherName, students.Sex, students.Phone1, students.Phone2, students.Semester, students.Status, transitionmain.DiplomaType, transitionmain.OriginalCurriculum from students\n" +
"INNER JOIN transitionmain ON students.AM=transitionmain.AM\n" +
"where students.AM IN (select StudentAM from stud2course where CourseID =? and SemesterID=?);");
            st.setString(1, CourseID);
            st.setInt(2, SemesterID);
            ResultSet results = st.executeQuery();
            //check if select return nothing
            if (!results.isBeforeFirst() ) {
                results.close();
                st.close();
                return StudentsByCourse;
            }
            while(results.next()){
                Student temp = new Student();
                temp.setAM(results.getString(1));
                temp.setSurName(results.getString(2));
                temp.setFirstName(results.getString(3));
                temp.setFathersName(results.getString(4));
                temp.setMothersName(results.getString(5));
                temp.setSex(results.getString(6));
                temp.setPhone1(results.getString(7));
                temp.setPhone2(results.getString(8));
                temp.setSemester(results.getInt(9));
                temp.setStatus(results.getString(10));
                if(results.getString(11).equalsIgnoreCase("ΠΑΝΕΠΙΣΤΗΜΙΟΥ")){
                    temp.setCurriculumID("ΠΑΔΑ");                    
                }else{
                    temp.setCurriculumID(results.getString(11));      
                }  
                temp.setOriginalCurriculumID(results.getString(12));
                StudentsByCourse.add(temp);
                
            }
            results.close();
            st.close();
            return StudentsByCourse;
        } catch (ClassNotFoundException ex) {
           throw new MyException("ClassNotFoundException",ex);
        } catch (SQLException ex) {
           throw new MyException("SQLException",ex);
        }
    }
}
