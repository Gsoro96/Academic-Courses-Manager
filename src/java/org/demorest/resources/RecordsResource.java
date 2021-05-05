/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demorest.resources;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.demorest.db.AuthentDB;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.demorest.db.CourseDB;
import org.demorest.entities.User;
import org.demorest.db.RecordDB;
import org.demorest.entities.Course;
import org.demorest.entities.Record;
import org.demorest.exceptions.MyException;
import org.demorest.entities.MyHttpStatus;

/**
 * REST Web Service
 *
 * @author George
 */
@Path("courses/{CourseID}/professors/{ProfessorID}/records")
public class RecordsResource {
    
    @Context
    private UriInfo context;
    RecordDB db;
    /**
     * Constructor of Class RecordsResource.
     */
    public RecordsResource()  { 
        db=new RecordDB();
    }

    
    /**
     * Retrives all the records as a json representation of instance ArrayList of type Record.
     * @param key String.
     * @param CourseID String. The id of the Course.
     * @param ProfessorID String. The id of the Professor.
     * @param SemesterID int. The id of the Semester.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","Records").
     */
    @GET
    @Path("/semester/{SemesterID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response GetRecordsBySemesterID(
            @QueryParam("key") String key,
            @PathParam("CourseID") String CourseID,
            @PathParam("ProfessorID") String ProfessorID,
            @PathParam("SemesterID") int SemesterID) 
    {
         ArrayList<Record> list = new ArrayList<>();
         String Rs;
         Gson gson = new Gson();
         Response.ResponseBuilder  rb;
        try {
           
            AuthentDB auth = AuthentDB.GetInstance();
            User my_user = auth.CheckKey(key);
            
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
                //action 2 --> read records
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 2).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();                
                }
                
                 //check if the professorID match the Logname of the User for whom the key was used
                if(!ProfessorID.equalsIgnoreCase(my_user.getIDinKateg())){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if sql returned nothing
                list=db.getRecordsBySemesterID(my_user,CourseID,ProfessorID,SemesterID);
                if(list.isEmpty()){
                    Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","Records"));
                    rb = Response.status(Response.Status.NOT_FOUND);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                Rs = gson.toJson(list);
                AuthentDB.getLogger().log(Level.INFO,Rs);
                return Response.ok(Rs,MediaType.APPLICATION_JSON).build();
            }
        } catch (MyException ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            AuthentDB.getLogger().log(Level.SEVERE,ex.getThrwbl()+" "+sStackTrace); 
        }
        Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Records"));
        rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.entity(Rs);
        AuthentDB.getLogger().log(Level.SEVERE,Rs);
        return rb.build();
    }
    
    /**
     * Retrieves a json representation of an instance of Record by id.
     * @param key String.
     * @param CourseID String. The id of the Course.
     * @param ProfessorID String. The id of the Professor.
     * @param ID int. THe id of the record.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","Records").
     */
    @GET
    @Path("{id}")
    @Produces (MediaType.APPLICATION_JSON)
    public Response GetRecordsByID(
            @QueryParam("key") String key,
            @PathParam("CourseID") String CourseID,
            @PathParam("ProfessorID") String ProfessorID,
            @PathParam("id") int ID) 
    {
        
            String Rs;
            Gson gson = new Gson();
            Response.ResponseBuilder  rb;
        try {    
            AuthentDB auth = AuthentDB.GetInstance();
            User my_user = auth.CheckKey(key);
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
                //action 2 --> read records
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 2).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();   
                }
                 //check if the professorID match the Logname of the User for whom the key was used
                if(!ProfessorID.equalsIgnoreCase(my_user.getIDinKateg())){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if the professor has enabled the view action for this course(only for Students)
                Rs=db.CheckCourseOption(my_user,my_user.getRoleId(), CourseID, ProfessorID);
                if(Rs==null){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if sql returned nothing
                Record record = db.getRecordByID(my_user,ID);
                if(record == null){
                    Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","Records"));
                    rb = Response.status(Response.Status.NOT_FOUND);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                Rs = gson.toJson(record);
                AuthentDB.getLogger().log(Level.INFO,Rs);
                return Response.ok(Rs,MediaType.APPLICATION_JSON).build();
            }
        } catch (MyException ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            AuthentDB.getLogger().log(Level.SEVERE,ex.getThrwbl()+" "+sStackTrace); 
        }
        Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Records"));
        rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.entity(Rs);
        AuthentDB.getLogger().log(Level.SEVERE,Rs);
        return rb.build();
    }
    
    /**
     * Retrives all the records as a json representation of instance ArrayList of type Record by Student.
     * @param key String.
     * @param CourseID String. The id of the Course.
     * @param ProfessorID String. The id of the Professor.
     * @param StudentAM String. The id of the Student.
     * @param SemesterID int. The id of the Semester.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","Records").
     */
    @GET
    @Path("/student/{StudentAM}")
    @Produces (MediaType.APPLICATION_JSON)
    public Response GetRecordsByStudent(
            @QueryParam("key") String key,
            @PathParam("CourseID") String CourseID,
            @PathParam("ProfessorID") String ProfessorID,
            @PathParam("StudentAM") String StudentAM)
    {
        String Rs;
        Gson gson = new Gson();
        Response.ResponseBuilder  rb;
        try {
            ArrayList<Record> list = new ArrayList<>();
            AuthentDB auth = AuthentDB.GetInstance();
            User my_user = auth.CheckKey(key);
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
                //action 2 --> read records
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 2).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if the professor has enabled the view action for this course(only for Students)
                Rs=db.CheckCourseOption(my_user,my_user.getRoleId(), CourseID, ProfessorID);
                if(Rs.equalsIgnoreCase(null)){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: View Option is Disabled","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if the professorID match the Logname of the User for whom the key was used
                if(my_user.getRoleId() == 1){
                   if(!StudentAM.equalsIgnoreCase(my_user.getIDinKateg())){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                  } 
                }else{
                    if(!ProfessorID.equalsIgnoreCase(my_user.getIDinKateg())){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                  } 
                }
                
                
                //check if sql returned nothing
                list = db.getRecordByStudent(my_user,CourseID, ProfessorID, StudentAM);
                if(list == null){
                    Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","Records"));
                    rb = Response.status(Response.Status.NOT_FOUND);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                Rs = gson.toJson(list);
                AuthentDB.getLogger().log(Level.INFO,Rs);
                return Response.ok(Rs,MediaType.APPLICATION_JSON).build();
            }
        } catch (MyException ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            AuthentDB.getLogger().log(Level.SEVERE,ex.getThrwbl()+" "+sStackTrace);
        }
        Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Records"));
        rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.entity(Rs);
        AuthentDB.getLogger().log(Level.SEVERE,Rs);
        return rb.build();
    }
    
    /**
     * Retrives all the records as a json representation of instance ArrayList of type Record by Student.
     * @param key String.
     * @param CourseID String. The id of the Course.
     * @param ProfessorID String. The id of the Professor.
     * @param StudentAM String. The id of the Student.
     * @param SemesterID int. The id of the Semester.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","Records").
     */
    @GET
    @Path("/semester/{SemesterID}/student/{StudentAM}")
    @Produces (MediaType.APPLICATION_JSON)
    public Response GetRecordsByStudentBySemester(
            @QueryParam("key") String key,
            @PathParam("CourseID") String CourseID,
            @PathParam("ProfessorID") String ProfessorID,
            @PathParam("StudentAM") String StudentAM,
            @PathParam("SemesterID") int SemesterID)
    {
        String Rs;
        Gson gson = new Gson();
        Response.ResponseBuilder  rb;
        try {
            ArrayList<Record> list = new ArrayList<>();
            AuthentDB auth = AuthentDB.GetInstance();
            User my_user = auth.CheckKey(key);
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
                //action 2 --> read records
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 2).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if the professor has enabled the view action for this course(only for Students)
                Rs=db.CheckCourseOption(my_user,my_user.getRoleId(), CourseID, ProfessorID);
                if(Rs.equalsIgnoreCase(null)){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: View Option is Disabled","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if the professorID match the Logname of the User for whom the key was used
                if(my_user.getRoleId() == 1){
                   if(!StudentAM.equalsIgnoreCase(my_user.getIDinKateg())){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                  } 
                }else{
                    if(!ProfessorID.equalsIgnoreCase(my_user.getIDinKateg())){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                  } 
                }  
                //check if sql returned nothing
                list = db.getRecordByStudentBySemesterID(my_user,CourseID, ProfessorID, StudentAM, SemesterID);
                if(list == null){
                    Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","Records"));
                    rb = Response.status(Response.Status.NOT_FOUND);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                Rs = gson.toJson(list);
                AuthentDB.getLogger().log(Level.INFO,Rs);
                return Response.ok(Rs,MediaType.APPLICATION_JSON).build();
            }
        } catch (MyException ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            AuthentDB.getLogger().log(Level.SEVERE,ex.getThrwbl()+" "+sStackTrace);
        }
        Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Records"));
        rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.entity(Rs);
        AuthentDB.getLogger().log(Level.SEVERE,Rs);
        return rb.build();
    }
    
   
    /**
     * Creates an instance of Record in the database and returns a json represantation of this record.
     * @param json String. The request body the includes the record that will be created.
     * @param key String.
     * @param CourseID String. The id of the Course.
     * @param ProfessorID String. The id of the Professor.
     * @param SemesterID int. The id of the Semester.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","Records").
     */
    @POST
    @Path("/semester/{SemesterID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response CreateRecord(
            String json,
            @QueryParam("key") String key,
            @PathParam("CourseID") String CourseID,
            @PathParam("ProfessorID") String ProfessorID,
            @PathParam("SemesterID") int SemesterID
            ) 
    {
        
        String Rs;
        Gson gson = new Gson();
        Response.ResponseBuilder  rb;
        
        try {
            //Check if RequestBody is Empty
            if(json.isEmpty()){
                Rs= gson.toJson(new MyHttpStatus("400","Error: Bad Request","Records"));
                rb = Response.status(Response.Status.BAD_REQUEST);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            AuthentDB auth = AuthentDB.GetInstance();
            User my_user = auth.CheckKey(key);
            //if json is not a valid representation for object Record throws JsonSyntaxException
            Record obj = gson.fromJson(json, Record.class);
            obj.setCourseID(CourseID);
            obj.setProfessorID(ProfessorID);
            obj.setSemesterID(SemesterID);
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
               
                //action 3 --> create records
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 3).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                 //check if the professorID match the Logname of the User for whom the key was used
                if(!ProfessorID.equalsIgnoreCase(my_user.getIDinKateg())){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if professor can create record for this course
                String prof2course = auth.CheckProf2Course(my_user,obj.getProfessorID(),obj.getCourseID());
                if(prof2course == null){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if student is regestered at the course that the Profosess wants to create Record
                CourseDB course_db = new CourseDB();
                ArrayList<Course> course_list = new ArrayList();
                boolean flag = false;
                course_list = course_db.getCoursesByStud(my_user,obj.getStudentAM(), SemesterID);
                for(int j=0; j<course_list.size(); j++){
                    if( course_list.get(j).getId().equalsIgnoreCase(obj.getCourseID())){
                        flag=true;
                        j=course_list.size()+1;
                    }
                }
                if (flag == false){
                  Rs= gson.toJson(new MyHttpStatus("400","Error: Bad Request Student : "+obj.getStudentAM()+" is not regestered to the Course:"+CourseID+"","Records"));
                  rb = Response.status(Response.Status.BAD_REQUEST);
                  rb.entity(Rs);
                  AuthentDB.getLogger().log(Level.SEVERE,Rs);
                  return rb.build();  
                }
                //check if the request body has all the required parameters filled
                if(obj.isNull() == null){
                    Rs= gson.toJson(new MyHttpStatus("400","Error: Bad Request","Records"));
                    rb = Response.status(Response.Status.BAD_REQUEST);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if the request body has valid values
                if(obj.isValid() == null){
                  Rs= gson.toJson(new MyHttpStatus("400","Error: Bad Request Ivalid Values","Records"));
                  rb = Response.status(Response.Status.BAD_REQUEST);
                  rb.entity(Rs);
                  AuthentDB.getLogger().log(Level.SEVERE,Rs);
                  return rb.build();   
                }
                //check if sql returned nothing
                Record record = db.createRecord(my_user,obj);
                if(record == null){
                    Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Records"));
                    rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                Rs = gson.toJson(record);
                AuthentDB.getLogger().log(Level.INFO,Rs);
                return Response.ok(Rs,MediaType.APPLICATION_JSON).build();
            }
        }catch (MyException ex){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            AuthentDB.getLogger().log(Level.SEVERE,ex.getThrwbl()+" "+sStackTrace);
            Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Records"));
            rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            AuthentDB.getLogger().log(Level.SEVERE,Rs);
            rb.entity(Rs);
        }catch (JsonSyntaxException ex){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            AuthentDB.getLogger().log(Level.SEVERE,sStackTrace);
            Rs= gson.toJson(new MyHttpStatus("400","Error: Bad Request","Records"));
            rb = Response.status(Response.Status.BAD_REQUEST);
            AuthentDB.getLogger().log(Level.SEVERE,Rs);
            rb.entity(Rs);
        }
        return rb.build();
    }
    
    /**
     * Creates an instance of Record (auto calculates the Final Grade) in the database and returns a json represantation of this record.
     * @param json String. The request body the includes the record that will be created.
     * @param key String.
     * @param CourseID String. The id of the Course.
     * @param ProfessorID String. The id of the Professor.
     * @param SemesterID int. The id of the Semester.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","Records").
     */
    @POST
    @Path("/semester/{SemesterID}/AUTO")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response CreateRecordWithAuto(
            String json,
            @QueryParam("key") String key,
            @PathParam("CourseID") String CourseID,
            @PathParam("ProfessorID") String ProfessorID,
            @PathParam("SemesterID") int SemesterID
            ) 
    {
        String Rs;
        Gson gson = new Gson();
        Response.ResponseBuilder  rb;
        try {
            //Check if RequestBody is Empty
            if(json.isEmpty()){
                Rs= gson.toJson(new MyHttpStatus("400","Error: Bad Request","Records"));
                rb = Response.status(Response.Status.BAD_REQUEST);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            AuthentDB auth = AuthentDB.GetInstance();
            User my_user = auth.CheckKey(key);
            //if json is not a valid representation for object Record throws JsonSyntaxException
            Record obj = gson.fromJson(json, Record.class);           
            obj.setCourseID(CourseID);
            obj.setProfessorID(ProfessorID);
            obj.setSemesterID(SemesterID);
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
               
                //action 3 --> create records
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 3).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                 //check if the professorID match the Logname of the User for whom the key was used
                if(!ProfessorID.equalsIgnoreCase(my_user.getIDinKateg())){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if professor can create record for this course
                String prof2course = auth.CheckProf2Course(my_user,obj.getProfessorID(),obj.getCourseID());
                if(prof2course == null){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if the request body has all the required parameters filled
                if(obj.isNull() == null){
                    Rs= gson.toJson(new MyHttpStatus("400","Error: Bad Request","Records"));
                    rb = Response.status(Response.Status.BAD_REQUEST);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if the request body has valid values
                if(obj.isValid() == null){
                  Rs= gson.toJson(new MyHttpStatus("400","Error: Bad Request Ivalid Values","Records"));
                  rb = Response.status(Response.Status.BAD_REQUEST);
                  rb.entity(Rs);
                  AuthentDB.getLogger().log(Level.SEVERE,Rs);
                  return rb.build();   
                }
                //check if sql returned nothing
                Record record = db.createRecordAutoFinalGrade(my_user,obj);
                if(record == null){
                     Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Records"));
                    rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                Rs = gson.toJson(record);
                AuthentDB.getLogger().log(Level.INFO,Rs);
                return Response.ok(Rs,MediaType.APPLICATION_JSON).build();
            }
        }catch (MyException ex){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            AuthentDB.getLogger().log(Level.SEVERE,ex.getThrwbl()+" "+sStackTrace);
            if(ex.getMessage().equalsIgnoreCase("No CourseOption Found")){
                Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","Records"));
                rb = Response.status(Response.Status.NOT_FOUND);
                rb.entity(Rs);  
            }else{
                Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Records"));
                rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                rb.entity(Rs);
            }
            AuthentDB.getLogger().log(Level.SEVERE,Rs);
        }catch (JsonSyntaxException ex){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            AuthentDB.getLogger().log(Level.SEVERE,sStackTrace);
            Rs= gson.toJson(new MyHttpStatus("400","Error: Bad Request","Records"));
            rb = Response.status(Response.Status.BAD_REQUEST);
            AuthentDB.getLogger().log(Level.SEVERE,sStackTrace);
            rb.entity(Rs);
        }
        return rb.build();
    }
    /**
     * Updates an instance of Record in the database and returns a json represantation of this record.
     * @param json String. The request body the includes the record that will be updated.
     * @param key String.
     * @param CourseID String. The id of the Course.
     * @param ProfessorID String. The id of the Professor.
     * @param RecordID int. The id of the Record.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","Records").
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response UpdateRecord(
            @QueryParam("key") String key,
            @PathParam("CourseID") String CourseID,
            @PathParam("ProfessorID") String ProfessorID,
            @PathParam("id") int RecordID, 
            String json) 
    {
        
        String Rs;
        Gson gson = new Gson();
        Response.ResponseBuilder rb;
        try {
            //Check if RequestBody is Empty
            if(json.isEmpty()){
                Rs= gson.toJson(new MyHttpStatus("400","Error: Bad Request","Records"));
                rb = Response.status(Response.Status.BAD_REQUEST);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            AuthentDB auth = AuthentDB.GetInstance();
            User my_user = auth.CheckKey(key);
            //if json is not a valid representation for object Record throws JsonSyntaxException
            Record obj = gson.fromJson(json, Record.class);
            obj.setCourseID(CourseID);
            obj.setProfessorID(ProfessorID);
            obj.setId(RecordID);
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
                //action 4 --> update records
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 4).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }              
                  //check if the professorID match the Logname of the User for whom the key was used
                if(!ProfessorID.equalsIgnoreCase(my_user.getIDinKateg())){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if professor can update record for this course
                String prof2course = auth.CheckProf2Course(my_user,obj.getProfessorID(),obj.getCourseID());
                if(prof2course == null){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if record exists
                Record record=db.getRecordByID(my_user,RecordID);
                if(record == null){
                    Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","Records"));
                    rb = Response.status(Response.Status.NOT_FOUND);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if the request body has valid values
                if(obj.isValid() == null){
                  Rs= gson.toJson(new MyHttpStatus("400","Error: Bad Request Ivalid Values","Records"));
                  rb = Response.status(Response.Status.BAD_REQUEST);
                  rb.entity(Rs);
                  AuthentDB.getLogger().log(Level.SEVERE,Rs);
                  return rb.build();   
                }
                //check if sql returned nothing
                if(db.updateRecord(my_user,obj) == null){   
                     Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Records"));
                    rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                record=db.getRecordByID(my_user,RecordID);
                Rs = gson.toJson(record);
                AuthentDB.getLogger().log(Level.INFO,Rs);
                return Response.ok(Rs,MediaType.APPLICATION_JSON).build();
            }
        }catch (MyException ex){
             StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            AuthentDB.getLogger().log(Level.SEVERE,ex.getThrwbl()+" "+sStackTrace);
            Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Records"));
            rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            AuthentDB.getLogger().log(Level.SEVERE,Rs);
            rb.entity(Rs);
        }catch (JsonSyntaxException ex){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            AuthentDB.getLogger().log(Level.SEVERE,sStackTrace);
            Rs= gson.toJson(new MyHttpStatus("400","Error: Bad Request","Records"));
            rb = Response.status(Response.Status.BAD_REQUEST);
            AuthentDB.getLogger().log(Level.SEVERE,Rs);
            rb.entity(Rs);
        }
        rb.entity(Rs);
        return rb.build();
    }
    /**
     * Deletes an instance of Record from the database and returns a json represantation of this record.
     * @param key String
     * @param CourseID String. The id of the Course.
     * @param ProfessorID String. The id of the Professor.
     * @param RecordID int. The id of the Record.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","Records").
     */
    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public Response DeleteRecord(
            @QueryParam("key") String key,
            @PathParam("CourseID") String CourseID,
            @PathParam("ProfessorID") String ProfessorID,
            @PathParam("id") int RecordID )
    {
       
        String Rs;
        Gson gson = new Gson();
        Response.ResponseBuilder  rb;
        try {
            AuthentDB auth = AuthentDB.GetInstance();
            User my_user = auth.CheckKey(key);
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
                //action 10 --> delete record
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 5).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if the professorID match the Logname of the User for whom the key was used
                if(!ProfessorID.equalsIgnoreCase(my_user.getIDinKateg())){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if professor can delete Record for this course
                String prof2course = auth.CheckProf2Course(my_user,ProfessorID,CourseID);
                if(prof2course == null){

                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Records"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if record exists
                Record json = db.getRecordByID(my_user,RecordID);
                if(json == null){
                    Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","Records"));
                    rb = Response.status(Response.Status.NOT_FOUND);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if sql returned nothing
                if(db.deleteRecord(my_user,RecordID) == null){
                     Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Records"));
                    rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                Rs = gson.toJson(json);
                AuthentDB.getLogger().log(Level.INFO,Rs);
                return Response.ok(Rs,MediaType.APPLICATION_JSON).build();
            }
        } catch (MyException ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            AuthentDB.getLogger().log(Level.SEVERE,ex.getThrwbl()+" "+sStackTrace);
        }
        Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Records"));
        rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.entity(Rs);
        AuthentDB.getLogger().log(Level.SEVERE,Rs);
        return rb.build();
    }
    
}
