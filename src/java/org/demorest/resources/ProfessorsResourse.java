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
import org.demorest.db.ProfessorDB;
import org.demorest.entities.Professor;
import org.demorest.entities.User;
import org.demorest.exceptions.MyException;
import org.demorest.entities.MyHttpStatus;

/**
 * REST Web Service
 *
 * @author George
 */
@Path("professors")
public class ProfessorsResourse {

    @Context
    private UriInfo context;
    ProfessorDB db;

    /**
     * Creates a new instance of Professors
     */
    public ProfessorsResourse() {
        db = new ProfessorDB();
    }

    /**
     * Retrieves a json representation of an instance of ProfessorsResourse.
     * @param key String.
     * @param ProfessorID String. The id of the Professor.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","Professors").
     */
    @GET
    @Path("{ProfessorID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response GetProfessorByID(
            @QueryParam("key") String key,
            @PathParam("ProfessorID") String ProfessorID) 
    {
        String Rs;
        Response.ResponseBuilder  rb;
        Gson gson = new Gson();
        
        try {
            AuthentDB auth = AuthentDB.GetInstance();
            User my_user = auth.CheckKey(key);
            
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Professors"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
                //action 11 --> read professors
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 11).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Professors"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                //check if sql returned nothing
                Professor p = db.getProfessorByID(my_user,ProfessorID);
                if(p == null){
                    Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","Professors"));
                    rb = Response.status(Response.Status.NOT_FOUND);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                Rs = gson.toJson(p);
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
        Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Professors"));
        rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.entity(Rs);
        AuthentDB.getLogger().log(Level.SEVERE,Rs);
        return rb.build();
    }
    
    /**
     * Retrives all the Professors as a json representation of instance ArrayList of type Professor by Course.
     * @param key String.
     * @param CourseID String. The id of the Course.
     * @param SemesterID int. The id of the Semester.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","Professors").
     */
    @GET
    @Path("courses/{CourseID}/semester/{SemesterID}")
    @Produces (MediaType.APPLICATION_JSON)
    public Response GetProfByCourse(
            @QueryParam("key") String key,
            @PathParam("CourseID") String CourseID,
            @PathParam("SemesterID") int SemesterID)
    {
        ArrayList<Professor> list = new ArrayList<>();
        String Rs;
        Response.ResponseBuilder  rb;
        Gson gson = new Gson();
        
        try {
            AuthentDB auth = AuthentDB.GetInstance();
             User my_user = auth.CheckKey(key);
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Professors"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
                //action 11 --> read professors
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 11).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Professors"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build(); 
                }
               
                list=db.getProfByCourse(my_user,CourseID, SemesterID);
                //check if sql returned nothing
                if(list.isEmpty()){
                    Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","Professors"));
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
        Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Professors"));
        rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.entity(Rs);
        return rb.build();
    }

   
}
