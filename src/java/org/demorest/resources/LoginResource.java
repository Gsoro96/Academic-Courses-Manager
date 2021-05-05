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
import org.demorest.db.AuthentDB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.demorest.entities.MyHttpStatus;
import org.demorest.entities.SafeUser;
import org.demorest.entities.User;
import org.demorest.exceptions.MyException;

/**
 * REST Web Service
 *
 * @author George
 */
@Path("login")
public class LoginResource {
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of LoginResource
     */
    public LoginResource() {
    }

    /**
     * This WebService is used in order to perform Login operation.Returns a jsonRepresantation of type SafeUser.
     * @param Logname String. The username of the user.
     * @param Password String. The password of the user.
     * @return Response object.This object includes the response body(the generated json) the status of the request etc.<br>
     * in case that the request was not done sucessfully then it returns a json representation of type MyHttpStatus <br>
     * with attributes String code, String message,String sourse  e.g new MyHttpStatus("403","Error: Forbidden Access","CourseOptions").
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response Login(@QueryParam("Ln") String Logname,@QueryParam("Pass") String Password ) 
    {
        String Rs;
        Response.ResponseBuilder  rb;
        Gson gson = new Gson();
        
        try {
            AuthentDB auth = AuthentDB.GetInstance();
            SafeUser my_user = auth.Login(Logname, Password);
            //check if user is logged in
            if (my_user.getMessage().equalsIgnoreCase("LoginFailed")) {
                Rs= gson.toJson(my_user);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return Response.ok(Rs,MediaType.APPLICATION_JSON).build();
            }
            else {
                 Rs= gson.toJson(my_user);
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
        Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Authent"));
        rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.entity(Rs);
        AuthentDB.getLogger().log(Level.SEVERE,Rs);
        return rb.build();
    }
    /**
     * This WebService is used in order to perform Logout operation.
     * @param Logname String. The username of the user.
     * @return String. Returns Logour User+ Logname if the Logout wa done Sucessfully else returns Logout Failed.
     */
    @GET
    @Path("Logout")
    @Produces (MediaType.APPLICATION_JSON)
    public Response Logout(@QueryParam("Ln") String Logname,
                           @QueryParam("key") String key) {
        String Rs;
        Response.ResponseBuilder  rb;
        Gson gson = new Gson();
        try {
            AuthentDB auth = AuthentDB.GetInstance();
            User my_user = auth.CheckKeyWithLogname(key,Logname);
            if (my_user == null) {
                Rs= gson.toJson(new MyHttpStatus("403","Error: Forbidden Access","Authent"));
                rb = Response.status(Response.Status.FORBIDDEN);
                rb.entity(Rs);
                AuthentDB.getLogger().log(Level.SEVERE,Rs);
                return rb.build();
            }
            String result = auth.Logout(Logname);
            if(result == null){
                 Rs= gson.toJson("Logout Failed");
                 rb = Response.status(Response.Status.OK);
                 rb.entity(Rs);
                 AuthentDB.getLogger().log(Level.SEVERE,Rs);
                 return rb.build(); 
            }
            else {
                Rs = gson.toJson("Logout User:"+Logname);
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
        Rs= gson.toJson(new MyHttpStatus("500","Error: Internal Server Error","Authent"));
        rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.entity(Rs);
        return rb.build();
    }

   
}
