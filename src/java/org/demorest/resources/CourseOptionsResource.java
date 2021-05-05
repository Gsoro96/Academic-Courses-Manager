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
import java.util.ArrayList;
import java.util.logging.Level;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.demorest.db.AuthentDB;
import org.demorest.db.CourseOptionsDB;
import org.demorest.entities.CourseOption;
import org.demorest.entities.User;
import org.demorest.exceptions.MyException;
import org.demorest.entities.MyHttpStatus;

/**
 * REST Web Service
 * @author George
 */
@Path("courses/{CourseID}/professors/{ProfessorID}/course-options")
public class CourseOptionsResource {
    
    @Context
    private UriInfo context;
    CourseOptionsDB db;
    /**
     * Creates a new instance of CourseOptionsResource
     */
    public CourseOptionsResource() {
        db =new CourseOptionsDB();
    }

    /**
     * Retrieves all CourseOptions as a json represantation of an instance of ArrayList of type CourseOption.
     * @param key String.
     * @param CourseID String. The id of the Course.
     * @param ProfessorID String. The id of the Professor.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","CourseOptions").
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response GetCourseOptions(
            @QueryParam("key") String key,
            @PathParam("CourseID") String CourseID ,
            @PathParam("ProfessorID") String ProfessorID ) 
    {
        
            ArrayList<CourseOption> list = new ArrayList();
            String Rs;
            Gson gson = new Gson();
            Response.ResponseBuilder  rb;
        try {   
            AuthentDB auth = AuthentDB.GetInstance();
            User my_user = auth.CheckKey(key);
            
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","CourseOptions"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
                
                //action 7 --> read CourseOption
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 7).contentEquals("Permission Granted")){

                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","CourseOptions"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if the ProfessorID match the Logname of the User for whom the key was used
                if(!ProfessorID.equalsIgnoreCase(my_user.getIDinKateg())){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","CourseOptions"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if sql returned nothing
                list = db.getCourseOptions(my_user,CourseID, ProfessorID);
                if(list == null){
                    Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","CourseOptions"));
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
        Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","CourseOptions"));
        rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.entity(Rs);
        AuthentDB.getLogger().log(Level.INFO,Rs);
        return rb.build();
    }
    /**
     * Retrieves a json representation of an instance of CourseOption by SemesterID.
     * @param key String.
     * @param CourseID String. The id of the Course.
     * @param ProfessorID String. The id of the Professor.
     * @param SemesterID int.The id of the Semester.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","CourseOptions").
     */
    @GET
    @Path("/semester/{SemesterID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response GetCourseOptionBySemester(
            @QueryParam("key") String key,
            @PathParam("CourseID") String CourseID ,
            @PathParam("ProfessorID") String ProfessorID,
            @PathParam("SemesterID") int SemesterID
    ) 
    {
        
            String Rs;
            Gson gson = new Gson();
            Response.ResponseBuilder  rb;
        try {  
            AuthentDB auth = AuthentDB.GetInstance();
            User my_user = auth.CheckKey(key);
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","CourseOptions"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
               
                //action 7 --> read CourseOption
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 7).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","CourseOptions"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if the ProfessorID match the Logname of the User for whom the key was used
                if(!ProfessorID.equalsIgnoreCase(my_user.getIDinKateg())){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","CourseOptions"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if sql returned nothing
                CourseOption co = db.getCourseOptionBySemester(my_user,CourseID, ProfessorID, SemesterID);
                if(co == null){
                    Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","CourseOptions"));
                    rb = Response.status(Response.Status.NOT_FOUND);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                Rs = gson.toJson(co);
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
        Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","CourseOptions"));
        rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.entity(Rs);
        AuthentDB.getLogger().log(Level.INFO,Rs);
        return rb.build();
    }
    
    /**
     * Creates an instance of CourseOptions to the database and returns this CourseOption as a json represantation of type CourseOption.
     * @param key String.
     * @param CourseID String. The id of the Course.
     * @param ProfessorID String. The id of the Professor.
     * @param SemesterID int.The id of the Semester.
     * @param c String. The request body that includes the record that will be created.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","CourseOptions").
     */
    @POST
    @Path("/semester/{SemesterID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public Response CreateCourseOption(
            @QueryParam("key") String key,
            @PathParam("CourseID") String CourseID ,
            @PathParam("ProfessorID") String ProfessorID,
            @PathParam("SemesterID") int SemesterID,
            String c)
    {
            String Rs;
            Gson gson = new Gson();
            Response.ResponseBuilder rb;
        try {
            //Check if RequestBody is Empty
            if(c.isEmpty()){
                Rs= gson.toJson(new MyHttpStatus("400","Error: Bad Request","CourseOptions"));
                rb = Response.status(Response.Status.BAD_REQUEST);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            AuthentDB auth = AuthentDB.GetInstance();
            User my_user = auth.CheckKey(key);
            //if json is not a valid representation for object CourseOption throws JsonSyntaxException
            CourseOption obj = gson.fromJson(c, CourseOption.class);
            
            //check if the sum of the TheoryRate and LabRate is greater than 1
            float num = obj.getTheoryRate()+obj.getLabRate();
            if(Float.compare(num, 1)>0){
              Rs= gson.toJson(new MyHttpStatus("400","Error: Bad Request The sum of the TheoryRate and LabRate can't be greater than 1","CourseOptions"));
              rb = Response.status(Response.Status.BAD_REQUEST);
              rb.entity(Rs);
              AuthentDB.getLogger().log(Level.SEVERE,Rs);
              return rb.build();  
            }
            //check if user is logged in
            if(my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","CourseOptions"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
                //add pathparams to obj in order to create an instance of CourseOption in the database
                obj.setCourseID(CourseID);
                obj.setProfessorID(ProfessorID);
                obj.setSemesterID(SemesterID);
                //check if the ProfessorID match the Logname of the User for whom the key was used
                if(!ProfessorID.equalsIgnoreCase(my_user.getIDinKateg())){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","CourseOptions"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                
                //action 8 --> create CourseOption
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 8).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","CourseOptions"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if professor can create CourseOption for this course
                String prof2course = auth.CheckProf2Course(my_user,ProfessorID,CourseID);
                if(prof2course == null){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","CourseOptions"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if the ProfessorID match the Logname of the User for whom the key was used
                if(!ProfessorID.equalsIgnoreCase(my_user.getIDinKateg())){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","CourseOptions"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //Check if CourseOption already Exists
                if(db.getCourseOptionBySemester(my_user,CourseID, ProfessorID, SemesterID) != null){
                    Rs= gson.toJson(new MyHttpStatus("409","Error: Conflict Error","CourseOptions"));
                    rb = Response.status(Response.Status.CONFLICT);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if the request body has all the required parameters filled
                if(obj.isNull() == null){
                    Rs= gson.toJson(new MyHttpStatus("400","Error: Bad Request","CourseOptions"));
                    rb = Response.status(Response.Status.BAD_REQUEST);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if the request body has valid values
                if(obj.isValid() == null){
                  Rs= gson.toJson(new MyHttpStatus("400","Error: Bad Request Ivalid Values","CourseOptions"));
                  rb = Response.status(Response.Status.BAD_REQUEST);
                  rb.entity(Rs);
                  AuthentDB.getLogger().log(Level.SEVERE,Rs);
                  return rb.build();   
                }
                //check if sql returned nothing
                CourseOption co = db.createCourseOption(my_user,ProfessorID,CourseID,SemesterID,obj);
                if(co == null){
                    Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","CourseOptions"));
                    rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                Rs = gson.toJson(co);
                AuthentDB.getLogger().log(Level.INFO,Rs);
                return Response.ok(Rs,MediaType.APPLICATION_JSON).build();
            }
        }catch (MyException ex){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            AuthentDB.getLogger().log(Level.SEVERE,ex.getThrwbl()+" "+sStackTrace);   
            Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","CourseOptions"));
            rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            rb.entity(Rs);
        }catch (JsonSyntaxException ex){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            AuthentDB.getLogger().log(Level.SEVERE,sStackTrace);  
            Rs= gson.toJson(new MyHttpStatus("400","Error: Bad Request","CourseOptions"));
            rb = Response.status(Response.Status.BAD_REQUEST);
            rb.entity(Rs);
        }
        rb.entity(Rs);
        return rb.build();
    }
    
    /**
     * Updates an instance of CourseOptions to the database and returns this CourseOption as a json Represantation of type CourseOption.
     * @param key String.
     * @param CourseID String. The id of the Course.
     * @param ProfessorID String. The id of the Professor.
     * @param SemesterID int.The id of the Semester.
     * @param json String. The request body that includes the record that will be updated.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","CourseOptions").
     */  
    @PUT
    @Path("/semester/{SemesterID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public Response UpdateCourseOption(
            @QueryParam("key") String key,
            @PathParam("CourseID") String CourseID ,
            @PathParam("ProfessorID") String ProfessorID,
            @PathParam("SemesterID") int SemesterID,
            String json)
    {
            String Rs;
            Gson gson = new Gson();
            Response.ResponseBuilder rb;
        try {
            //Check if RequestBody is Empty
            if(json.isEmpty()){
                Rs= gson.toJson(new MyHttpStatus("400","Error: Bad Request","CourseOptions"));
                rb = Response.status(Response.Status.BAD_REQUEST);
                rb.entity(Rs);
                 AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            AuthentDB auth = AuthentDB.GetInstance();
            User my_user = auth.CheckKey(key);
            //if json is not a valid representation for object CourseOption throws JsonSyntaxException
            CourseOption obj = gson.fromJson(json, CourseOption.class);
            //check if the request body has valid values
            if(obj.isValid() == null){
              Rs= gson.toJson(new MyHttpStatus("400","Error: Bad Request Ivalid Values","CourseOptions"));
              rb = Response.status(Response.Status.BAD_REQUEST);
              rb.entity(Rs);
               AuthentDB.getLogger().log(Level.SEVERE,Rs);
              return rb.build();   
            }
            //check if the sum of the TheoryRate and LabRate is greater than 1
            float num = obj.getTheoryRate()+obj.getLabRate();
            if(Float.compare(num, 1)>0){
              Rs= gson.toJson(new MyHttpStatus("400","Error: Bad Request The sum of the TheoryRate and LabRate can't be greater than 1","CourseOptions"));
              rb = Response.status(Response.Status.BAD_REQUEST);
              rb.entity(Rs);
               AuthentDB.getLogger().log(Level.SEVERE,Rs);
              return rb.build();  
            }
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","CourseOptions"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                 AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {              
                //action 9 --> update CourseOption
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 9).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","CourseOptions"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                
                //check if the ProfessorID match the Logname of the User for whom the key was used
                if(!ProfessorID.equalsIgnoreCase(my_user.getIDinKateg())){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","CourseOptions"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if professor can update CourseOption for this course
                String prof2course = auth.CheckProf2Course(my_user,ProfessorID,CourseID);
                if(prof2course == null){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","CourseOptions"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //Check if CourseOption already Exists
                CourseOption co = db.getCourseOptionBySemester(my_user,CourseID, ProfessorID, SemesterID);
                if(co == null){
                    Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","CourseOptions"));
                    rb = Response.status(Response.Status.NOT_FOUND);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if the request body has valid values
                if(obj.isValid() == null){
                  Rs= gson.toJson(new MyHttpStatus("400","Error: Bad Request Ivalid Values","CourseOptions"));
                  rb = Response.status(Response.Status.BAD_REQUEST);
                  rb.entity(Rs);
                  AuthentDB.getLogger().log(Level.SEVERE,Rs);
                  return rb.build();   
                }
                //check if sql returned nothing
                if(db.updateCourseOption(my_user,CourseID,ProfessorID,SemesterID,obj) == null){
                    Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","CourseOptions"));
                    rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                co = db.getCourseOptionBySemester(my_user,CourseID, ProfessorID, SemesterID);
                Rs = gson.toJson(co);
                AuthentDB.getLogger().log(Level.INFO,Rs);
                return Response.ok(Rs,MediaType.APPLICATION_JSON).build();
            }
        }catch (MyException ex){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            AuthentDB.getLogger().log(Level.SEVERE,ex.getThrwbl()+" "+sStackTrace);   
            Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","CourseOptions"));
            rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            rb.entity(Rs);
        }catch (JsonSyntaxException ex){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            AuthentDB.getLogger().log(Level.SEVERE,sStackTrace);  
            Rs= gson.toJson(new MyHttpStatus("400","Error: Bad Request","CourseOptions"));
            rb = Response.status(Response.Status.BAD_REQUEST);
            rb.entity(Rs);
        }
        rb.entity(Rs);
        return rb.build();
    }
    
    /**
     * Deletes an instance of CourseOptions from the database and returns this CourseOption as a json represantation of type CourseOption.
     * @param key String.
     * @param CourseID String. The id of the Course.
     * @param ProfessorID String. The id of the Professor.
     * @param SemesterID int.The id of the Semester.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","CourseOptions").
     */
    @DELETE
    @Path("/semester/{SemesterID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public Response DeleteCourseOption(
            @QueryParam("key") String key,
            @PathParam("CourseID") String CourseID ,
            @PathParam("ProfessorID") String ProfessorID,
            @PathParam("SemesterID") int SemesterID) 
    {
            String Rs;
            Gson gson = new Gson();
            Response.ResponseBuilder  rb;
         try {   
            AuthentDB auth = AuthentDB.GetInstance();
            User my_user = auth.CheckKey(key);
            
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","CourseOptions"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
               
                //action 10 --> delete CourseOption
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 10).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","CourseOptions"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                 //check if the ProfessorID match the Logname of the User for whom the key was used
                if(!ProfessorID.equalsIgnoreCase(my_user.getIDinKateg())){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","CourseOptions"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if professor can delete CourseOption for this course
                String prof2course = auth.CheckProf2Course(my_user,ProfessorID,CourseID);
                if(prof2course == null){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","CourseOptions"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //Check if CourseOption already Exists
                CourseOption co = db.getCourseOptionBySemester(my_user,CourseID, ProfessorID, SemesterID) ;
                if(co == null){
                    Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","CourseOptions"));
                    rb = Response.status(Response.Status.NOT_FOUND);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if sql returned nothing
                if(db.deleteCourseOption(my_user,CourseID, ProfessorID, SemesterID) == null){
                    Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","CourseOptions"));
                    rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                Rs = gson.toJson(co);
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
        Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","CourseOptions"));
        rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.entity(Rs);
        AuthentDB.getLogger().log(Level.SEVERE,Rs);
        return rb.build();
    }
  
}
