/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demorest.resources;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author George
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(org.demorest.resources.CourseOptionsResource.class);
        resources.add(org.demorest.resources.CoursesResource.class);
        resources.add(org.demorest.resources.LoginResource.class);
        resources.add(org.demorest.resources.ProfessorsResourse.class);
        resources.add(org.demorest.resources.RecordsResource.class);
        resources.add(org.demorest.resources.SemestersResource.class);
        resources.add(org.demorest.resources.StudentsResource.class);
        resources.add(org.demorest.resources.TransitionPassedResource.class);
    }
    
}
