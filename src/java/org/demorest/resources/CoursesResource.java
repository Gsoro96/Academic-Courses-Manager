
package org.demorest.resources;

import com.google.gson.Gson;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.demorest.db.AuthentDB;
import org.demorest.entities.User;
import org.demorest.entities.Course;
import org.demorest.db.CourseDB;
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
import javax.ws.rs.core.Response.ResponseBuilder;
import org.demorest.exceptions.MyException;
import org.demorest.entities.MyHttpStatus;

/**
 * REST Web Service
 *
 * @author George
 */
@Path("courses")
public class CoursesResource  {
    
    @Context
    private UriInfo context;
    CourseDB db;
    /**
     * Creates a new instance of CoursesResource
     */
    public CoursesResource() {
         db = new CourseDB();
    }

    /**
     * Retrives all the courses as a json representation of instance ArrayList of type Course.
     * @param key String.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","Courses").
     */
    @GET
    @Produces (MediaType.APPLICATION_JSON)
    public Response GetCourses(@QueryParam("key") String key){
        ArrayList<Course> list = new ArrayList<Course>();
        String Rs;
        ResponseBuilder  rb;
        Gson gson = new Gson();   
        try {
            AuthentDB auth = AuthentDB.GetInstance();         
            User my_user = auth.CheckKey(key);
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Courses"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
                //action 1 --> read courses
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 1).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Courses"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build(); 
                }
                list=db.getCourses(my_user);
                //check if sql returned nothing
                if(list.isEmpty()){
                    Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","Courses"));
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
        Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Courses"));
        rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.entity(Rs);
        AuthentDB.getLogger().log(Level.SEVERE,Rs);
        return rb.build();
    }
    
    /**
     * Retrieves a json representation of an instance of Course by id.
     * @param id String. The id of the Course.
     * @param key String.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","Courses").
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response GetCourseByID(@PathParam("id") String id,@QueryParam("key") String key){
        String Rs;
        ResponseBuilder  rb;
        Gson gson = new Gson();
        try {
            AuthentDB auth = AuthentDB.GetInstance();
            User my_user = auth.CheckKey(key);
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Courses"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
                //action 1 --> read courses
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 1).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Courses"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build(); 
                }
                //check if sql returned nothing
                Course course = db.getCourseByID(my_user,id);
                if(course == null){
                    Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","Courses"));
                    rb = Response.status(Response.Status.NOT_FOUND);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                AuthentDB.getLogger().log(Level.INFO,gson.toJson(course));
                return Response.ok(course, MediaType.APPLICATION_JSON).build();
            }
        } catch (MyException ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            AuthentDB.getLogger().log(Level.SEVERE,ex.getThrwbl()+" "+sStackTrace); 
        }
        Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Courses"));
        rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.entity(Rs);
        AuthentDB.getLogger().log(Level.SEVERE,Rs);
        return rb.build();
    }
    
    /**
     * Retrives all the courses as a json representation of instance ArrayList of type Course by Professor.
     * @param key String.
     * @param ProfessorID String. The id of the Professor.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","Courses").
     */
    @GET
    @Path("professors/{ProfessorID}")
    @Produces (MediaType.APPLICATION_JSON)
    public Response GetCoursesByProf(
            @QueryParam("key") String key,
            @PathParam("ProfessorID") String ProfessorID)
    {
        ArrayList<Course> list = new ArrayList<Course>();
        String Rs;
        ResponseBuilder  rb;
        Gson gson = new Gson();
        
        try {
            AuthentDB auth = AuthentDB.GetInstance();
             User my_user = auth.CheckKey(key);
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Courses"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
                //action 1 --> read courses
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 1).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Courses"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build(); 
                }
                 //check if the ProfessorID match the Logname of the User for whom the key was used
                if(!ProfessorID.equalsIgnoreCase(my_user.getIDinKateg())){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Courses"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                list=db.getCoursesByProf(my_user,ProfessorID);
                //check if sql returned nothing
                if(list.isEmpty()){
                    Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","Courses"));
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
        Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Courses"));
        rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.entity(Rs);
         AuthentDB.getLogger().log(Level.SEVERE,Rs);
        return rb.build();
    }
    
     /**
     * Retrives all the courses as a json representation of instance ArrayList of type Course by Professor.
     * @param key String.
     * @param ProfessorID String. The id of the Professor.
     * @param SemesterID int. The id of the Semester.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","Courses").
     */
    @GET
    @Path("professors/{ProfessorID}/semester/{SemesterID}")
    @Produces (MediaType.APPLICATION_JSON)
    public Response GetCoursesByProfBySemesterID(
            @QueryParam("key") String key,
            @PathParam("ProfessorID") String ProfessorID,
            @PathParam("SemesterID") int SemesterID)
    {
        ArrayList<Course> list = new ArrayList<Course>();
        String Rs;
        ResponseBuilder  rb;
        Gson gson = new Gson();
        
        try {
            AuthentDB auth = AuthentDB.GetInstance();
             User my_user = auth.CheckKey(key);
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Courses"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
                //action 1 --> read courses
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 1).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Courses"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build(); 
                }
                 //check if the ProfessorID match the Logname of the User for whom the key was used
                if(!ProfessorID.equalsIgnoreCase(my_user.getIDinKateg())){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Courses"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                list=db.getCoursesByProfBySemester(my_user,ProfessorID,SemesterID);
                //check if sql returned nothing
                if(list.isEmpty()){
                    Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","Courses"));
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
        Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Courses"));
        rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.entity(Rs);
        AuthentDB.getLogger().log(Level.SEVERE,Rs);
        return rb.build();
    }
    
    /**
     * 
     * Retrives all the courses as a json representation of instance ArrayList of type Course by Student.
     * @param key String.
     * @param StudentAM String. The id of the Student.
     * @param SemesterID int. The id of the Semester.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","Courses").
     */
    @GET
    @Path("students/{StudentAM}/semester/{SemesterID}")
    @Produces (MediaType.APPLICATION_JSON)
    public Response GetCoursesByStud(
            @QueryParam("key") String key,
            @PathParam("StudentAM") String StudentAM,
            @PathParam("SemesterID") int SemesterID)
    {
        ArrayList<Course> list = new ArrayList<>();
        String Rs;
        ResponseBuilder  rb;
        Gson gson = new Gson();
        
        try {
            AuthentDB auth = AuthentDB.GetInstance();
             User my_user = auth.CheckKey(key);
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Courses"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
                //action 1 --> read courses
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 1).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Courses"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build(); 
                }
                //check if the StudentAM match the Logname of the User for whom the key was used(ONLY FOR STUDENTS)
                if(my_user.getRoleId()==1){
                    if(!StudentAM.equalsIgnoreCase(my_user.getIDinKateg())){
                      Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Courses"));
                      rb = Response.status(Response.Status.FORBIDDEN);
                      rb.entity(Rs);
                      AuthentDB.getLogger().log(Level.SEVERE,Rs);
                      return rb.build();
                    } 
                }
                list=db.getCoursesByStud(my_user,StudentAM, SemesterID);
                //check if sql returned nothing
                if(list.isEmpty()){
                    Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","Courses"));
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
        Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Courses"));
        rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.entity(Rs);
        AuthentDB.getLogger().log(Level.SEVERE,Rs);
        return rb.build();
    }
    


}
