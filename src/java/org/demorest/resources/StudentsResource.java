/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demorest.resources;

import com.google.gson.Gson;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.demorest.db.AuthentDB;
import org.demorest.db.StudentDB;
import org.demorest.entities.Student;
import org.demorest.entities.User;
import org.demorest.exceptions.MyException;
import org.demorest.entities.MyHttpStatus;

/**
 * REST Web Service
 *
 * @author George
 */
@Path("students")
public class StudentsResource {

    @Context
    private UriInfo context;
    StudentDB db;
    /**
     * Creates a new instance of StudentsResource
     */
    public StudentsResource() {
         db = new StudentDB();
    }

    /**
     * Retrieves a json representation of an instance of Student.
     * @param AM String. The id of the Student.
     * @param key String.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","CourseOptions").
     */
    @GET
    @Path("{AM}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response GetStudentByAM(@PathParam("AM") String AM,@QueryParam("key") String key) {
        String Rs;
        Gson gson = new Gson();
        Response.ResponseBuilder  rb;
        try {
            AuthentDB auth = AuthentDB.GetInstance();
            User my_user = auth.CheckKey(key);
            
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Students"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
                //check if the StudentAM match the AM of the User for whom the key was used(Only for Students)
                if(my_user.getRoleId()==1){ 
                    if(!AM.equalsIgnoreCase(my_user.getIDinKateg())){
                        Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Students"));
                        rb = Response.status(Response.Status.FORBIDDEN);
                        rb.entity(Rs);
                        AuthentDB.getLogger().log(Level.SEVERE,Rs);
                        return rb.build();
                    }  
                }
                
                //action 6 --> read students
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 6).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Students"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if sql returned nothing
                Student student = db.getStudentByAM(my_user,AM);
                if(student == null){
                    Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","Students"));
                    rb = Response.status(Response.Status.NOT_FOUND);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                Rs = gson.toJson(student);
                AuthentDB.getLogger().log(Level.INFO,Rs);
                return Response.ok(Rs, MediaType.APPLICATION_JSON).build();
            }
        } catch (MyException ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            AuthentDB.getLogger().log(Level.SEVERE,ex.getThrwbl()+" "+sStackTrace);
        }
        Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Students"));
        rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.entity(Rs);
        AuthentDB.getLogger().log(Level.SEVERE,Rs);
        return rb.build();
    }
    
    /**
     * Retrives all the students that their id or ther Surname is alike with the str parameter as a json representation of instance ArrayList of type Course.
     * @param key String.
     * @param str String. The text that we are searching for a Student id or a Student Surname to be alike.
     * @param CourseID Stirng. The id of the Course.
     * @param SemesterID int. The id of the Semester.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","CourseOptions").
     */
    @GET
    @Path("/like/{str}/course/{CourseID}/semester/{SemesterID}")
    @Produces (MediaType.APPLICATION_JSON)
    public Response GetStudentsBySemesterID(
            @QueryParam("key") String key,
            @PathParam("str") String str,
            @PathParam("CourseID") String CourseID,
            @PathParam("SemesterID") int SemesterID)
    {
        ArrayList<Student> list = new ArrayList<>();
        String Rs;
        Response.ResponseBuilder  rb;
        Gson gson = new Gson();
        
        try {
            AuthentDB auth = AuthentDB.GetInstance();
             User my_user = auth.CheckKey(key);
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Students"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
                //action 6 --> read Students
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 6).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Students"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build(); 
                }
                list=db.getStudentsBySemesterID(my_user,str,CourseID,SemesterID);
                
                //check if sql returned nothing
                if(list.isEmpty()){
                    Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","Students"));
                    rb = Response.status(Response.Status.NOT_FOUND);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                
                Rs = gson.toJson(list);
                AuthentDB.getLogger().log(Level.INFO,Rs);
                return Response.ok(Rs, MediaType.APPLICATION_JSON).build();
            }
        } catch (MyException ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            AuthentDB.getLogger().log(Level.SEVERE,ex.getThrwbl()+" "+sStackTrace);
        }
        Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Students"));
        rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.entity(Rs);
        AuthentDB.getLogger().log(Level.SEVERE,Rs);
        return rb.build();
    }
    
     /**
     * Retrives all the students that their id or ther Surname is alike with the str parameter as a json representation of instance ArrayList of type Course.
     * @param key String.
     * @param str String. The text that we are searching for a Student id or a Student Surname to be alike.
     * @param CourseID Stirng. The id of the Course.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","CourseOptions").
     */
    @GET
    @Path("/like/{str}/course/{CourseID}")
    @Produces (MediaType.APPLICATION_JSON)
    public Response GetStudents(
            @QueryParam("key") String key,
            @PathParam("str") String str,
            @PathParam("CourseID") String CourseID)
    {
        ArrayList<Student> list = new ArrayList<>();
        String Rs;
        Response.ResponseBuilder  rb;
        Gson gson = new Gson();
        
        try {
            AuthentDB auth = AuthentDB.GetInstance();
             User my_user = auth.CheckKey(key);
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Students"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
                //action 6 --> read Students
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 6).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Students"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build(); 
                }
                list=db.getStudents(my_user,str,CourseID);
                //check if sql returned nothing
                if(list.isEmpty()){
                    Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","Students"));
                    rb = Response.status(Response.Status.NOT_FOUND);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                
                Rs = gson.toJson(list);
                AuthentDB.getLogger().log(Level.INFO,Rs);
                return Response.ok(Rs, MediaType.APPLICATION_JSON).build();
            }
        } catch (MyException ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            AuthentDB.getLogger().log(Level.SEVERE,ex.getThrwbl()+" "+sStackTrace);
        }
        Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Students"));
        rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.entity(Rs);
        AuthentDB.getLogger().log(Level.SEVERE,Rs);
        return rb.build();
    }
    
    @GET
    @Path("/course/{CourseID}/semester/{SemesterID}")
    @Produces (MediaType.APPLICATION_JSON)
    public Response GetStudentsByCourseBySemester(
            @QueryParam("key") String key,
            @PathParam("CourseID") String CourseID,
            @PathParam("SemesterID") int SemesterID)
    {
        ArrayList<Student> list = new ArrayList<>();
        String Rs;
        Response.ResponseBuilder  rb;
        Gson gson = new Gson();
        
        try {
            AuthentDB auth = AuthentDB.GetInstance();
             User my_user = auth.CheckKey(key);
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Students"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
                //action 6 --> read Students
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 6).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Students"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build(); 
                }
                list=db.getStudentsByCourseBySemester(my_user,CourseID,SemesterID);
                //check if sql returned nothing
                if(list.isEmpty()){
                    Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","Students"));
                    rb = Response.status(Response.Status.NOT_FOUND);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                
                Rs = gson.toJson(list);
                AuthentDB.getLogger().log(Level.INFO,Rs);
                return Response.ok(Rs, MediaType.APPLICATION_JSON).build();
            }
        } catch (MyException ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            AuthentDB.getLogger().log(Level.SEVERE,ex.getThrwbl()+" "+sStackTrace);
        }
        Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Students"));
        rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.entity(Rs);
        AuthentDB.getLogger().log(Level.SEVERE,Rs);
        return rb.build();
    }
}
