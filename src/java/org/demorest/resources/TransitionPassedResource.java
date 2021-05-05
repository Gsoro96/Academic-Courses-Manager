/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demorest.resources;

import com.google.gson.Gson;
import java.io.PrintWriter;
import java.io.StringWriter;
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
import org.demorest.entities.MyHttpStatus;
import org.demorest.entities.TransitionPassed;
import org.demorest.entities.User;
import org.demorest.exceptions.MyException;

/**
 * REST Web Service
 *
 * @author George
 */
@Path("TransitionPassed")
public class TransitionPassedResource {

    @Context
    private UriInfo context;
    StudentDB db;
    /**
     * Creates a new instance of TransitionPassed
     */
    public TransitionPassedResource() {
         db = new StudentDB();
    }

    @GET
    @Path("/students/{StudentAM}/courses/{CourseID}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response GetTransitionPassed(
            @PathParam("StudentAM") String StudentAM,
            @PathParam("CourseID") String CourseID,
            @QueryParam("key") String key) {
        String Rs;
        Gson gson = new Gson();
        Response.ResponseBuilder  rb;
        TransitionPassed res;
        try {
            AuthentDB auth = AuthentDB.GetInstance();
            User my_user = auth.CheckKey(key);
            
            //check if user is logged in
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","TransitionPassed"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            else {
                //check if the StudentAM match the StudentAM of the User for whom the key was used(Only for Students)
                if(my_user.getRoleId()==1){ 
                    if(!StudentAM.equalsIgnoreCase(my_user.getIDinKateg())){
                        Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","TransitionPassed"));
                        rb = Response.status(Response.Status.FORBIDDEN);
                        rb.entity(Rs);
                        AuthentDB.getLogger().log(Level.SEVERE,Rs);
                        return rb.build();
                    }  
                }
                
                //action 6 --> read students
                //check if user has permission to access
                if(!auth.CheckPermission(my_user, 6).contentEquals("Permission Granted")){
                    Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","TransitionPassed"));
                    rb = Response.status(Response.Status.FORBIDDEN);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                
                String CourseCurr = auth.getCourseCurr(CourseID);
                if(CourseCurr == null){
                  Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","TransitionPassed Course Curricculum"));
                  rb = Response.status(Response.Status.NOT_FOUND);
                  rb.entity(Rs);
                  AuthentDB.getLogger().log(Level.SEVERE,Rs);
                  return rb.build();  
                }
                //check if sql returned nothing
                res = db.getTransitionPassed(my_user,CourseID,CourseCurr,StudentAM);
                if(res == null){
                    Rs= gson.toJson(new MyHttpStatus("404","Error: Not Found","TransitionPassed returned null"));
                    rb = Response.status(Response.Status.NOT_FOUND);
                    rb.entity(Rs);
                    AuthentDB.getLogger().log(Level.SEVERE,Rs);
                    return rb.build();
                }
                Rs = gson.toJson(res);
                return Response.ok(Rs, MediaType.APPLICATION_JSON).build();
            }
        } catch (MyException ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            AuthentDB.getLogger().log(Level.SEVERE,ex.getThrwbl()+" "+sStackTrace);
        }
        Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","TransitionPassed"));
        rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.entity(Rs);
        AuthentDB.getLogger().log(Level.SEVERE,Rs);
        return rb.build();
    }

   
}
