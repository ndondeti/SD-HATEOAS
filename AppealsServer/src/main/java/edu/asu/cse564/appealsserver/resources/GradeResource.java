/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.cse564.appealsserver.resources;

import com.google.gson.Gson;
import edu.asu.cse564.appealsserver.models.Grade;
import edu.asu.cse564.appealsserver.representation.GradeRepresentation;
import edu.asu.cse564.appealsserver.representation.Link;
import edu.asu.cse564.appealsserver.representation.Representation;
import edu.asu.cse564.appealsserver.storage.GradesStorage;
import edu.asu.cse564.appealsserver.utilities.UriHelper;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.log4j.Logger;

/**
 *
 * @author Vivek
 */
@Path("/grade")
@ApplicationScoped
public class GradeResource {

    //private static final Logger LOG = Logger.getLogger(GradeResource.class);
    private @Context
    UriInfo uriInfo;

    @Inject
    private GradesStorage gradeStorage;
    private Gson gson;
    public GradeResource() {
        //LOG.info("GradeResource constructor");
        gson = new Gson();
    }

    @GET
    @Path("/{studentName}")
    @Produces(Representation.APPEALS_MEDIA_TYPE)
    public Response getGrade(@PathParam("studentName") String studentName) {
        //LOG.info("Retrieving an grade resource");

        Response response = null;

        try {
            Grade grade = gradeStorage.getGrade(studentName);
            if (grade == null) {
                response = Response.status(Response.Status.NOT_FOUND).build();
            } else {
                List<Link> links = getLinksForGetGrade(studentName);
                GradeRepresentation gradeRepresentation = new GradeRepresentation(grade, links);
                String message = gson.toJson(gradeRepresentation);
                response = Response.status(Response.Status.OK).entity(message).build();
            }

        } catch (Exception ex) {
            //LOG.info("Something went wrong retriveing the Order");
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } finally {
            //LOG.info("Retrieved the order resource");
            return response;
        }
    }
    
    @POST
    @Path("/{studentName}")
    @Consumes(Representation.APPEALS_MEDIA_TYPE)
    public Response addGrade(@PathParam("studentName") String studentName, String message) {
        //LOG.info("Retrieving an grade resource");

        Response response = null;

        try {
            Grade grade = gson.fromJson(message, Grade.class);
            if (grade == null || grade.getFeedback() == null || grade.getGrade() < 0) {
                response = Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                gradeStorage.addGrade(studentName, grade);
                response = Response.status(Response.Status.CREATED).build();
            }

        } catch (Exception ex) {
            //LOG.info("Something went wrong retriveing the Order");
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } finally {
            //LOG.info("Retrieved the order resource");
            return response;
        }
    }

    private List<Link> getLinksForGetGrade(String studentName) {
        List<Link> links = new ArrayList<Link>();
        Link appealLink = new Link();
        appealLink.setMediaType(Representation.APPEALS_MEDIA_TYPE);
        appealLink.setRel("postAppeal");
        String appealsUri = UriHelper.getBaseUri(uriInfo.getRequestUri()) + "/appeal/" + studentName;
        appealLink.setUri(appealsUri);        
        links.add(appealLink);
        
        appealLink = new Link();
        appealLink.setMediaType(Representation.APPEALS_MEDIA_TYPE);
        appealLink.setRel(Representation.SELF_REL_VALUE );
        appealLink.setUri(uriInfo.getRequestUri().toString());
        links.add(appealLink);
        
        return links;
    }
}
