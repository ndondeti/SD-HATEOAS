/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.cse564.appealsserver.resources;

import com.google.gson.Gson;
import edu.asu.cse564.appealsserver.models.AppealStatus;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import edu.asu.cse564.appealsserver.storage.AppealsStorage;
import edu.asu.cse564.appealsserver.models.Appeal;
import edu.asu.cse564.appealsserver.representation.AllAppealResponseMessage;
import edu.asu.cse564.appealsserver.representation.AppealRepresentation;
import edu.asu.cse564.appealsserver.representation.Link;
import edu.asu.cse564.appealsserver.representation.Representation;
import edu.asu.cse564.appealsserver.utilities.UriHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ws.rs.Consumes;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

/**
 *
 * @author Vivek
 */
@Path("/appeal")
@ApplicationScoped
public class AppealResource {
    //private static final Logger LOG = Logger.getLogger(GradeResource.class);

    private @Context
    UriInfo uriInfo;

    @Inject
    private AppealsStorage appealsStorage;

    private Gson gson;

    public AppealResource() {
        gson = new Gson();
    }

    @GET
    @Path("/allAppeals")
    @Produces(Representation.APPEALS_MEDIA_TYPE)
    public Response getAllAppeals() {
        Response response;
        try {
            Set<String> keys = appealsStorage.getAllAppeals();
            if (keys == null) {
                response = Response.status(Response.Status.NO_CONTENT).build();
            } else {
                ArrayList<AppealRepresentation> allAppeals = new ArrayList<>();
                for (String appealId : keys) {
                    Appeal appeal = appealsStorage.getAppeal(appealId);
                    List<Link> links = getLinkForAppealWhenTeacherViews(appealId, appeal.getStudent());
                    allAppeals.add(new AppealRepresentation(appeal, links));
                }
                AllAppealResponseMessage responseMessage = new AllAppealResponseMessage(allAppeals);
                String message = gson.toJson(responseMessage);
                response = Response.status(Response.Status.OK).entity(message).build();
            }
        } catch (Exception ex) {
            //LOG.debug("Something went wrong retriveing the Order");
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }

    @GET
    @Path("/{appealId}")
    @Produces(Representation.APPEALS_MEDIA_TYPE)
    public Response getAppeal(@PathParam("appealId") String appealId) {
        //LOG.info("Retrieving an grade resource");

        Response response;

        try {
            Appeal appeal = appealsStorage.getAppeal(appealId);
            if (appeal == null) {
                response = Response.status(Response.Status.NOT_FOUND).build();
            } else {
                List<Link> links = getLinksForGetAppeal(appeal.getStudent(), appeal.getStatus());
                AppealRepresentation appealRepresentation = new AppealRepresentation(appeal, links);
                String message = gson.toJson(appealRepresentation);
                response = Response.status(Response.Status.OK).entity(message).build();
            }

        } catch (Exception ex) {
            //LOG.debug("Something went wrong retriveing the Order");
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        //LOG.info("Retrieved the order resource");
        return response;
    }

    @POST
    @Path("/{appealId}")
    @Consumes(Representation.APPEALS_MEDIA_TYPE)
    @Produces(Representation.APPEALS_MEDIA_TYPE)
    public Response appealGrades(@PathParam("appealId") String appealId, String message) {
        Response response;
        try {
            if (appealsStorage.containsKey(appealId)) {
                Appeal existingAppeal = appealsStorage.getAppeal(appealId);
                if (existingAppeal == null) {
                    Appeal appeal = gson.fromJson(message, Appeal.class);
                    if (appeal != null || appeal.getAppeals() != null) {
                        appeal.setStatus(AppealStatus.UnderReview);
                        appealsStorage.addAppeal(appealId, appeal);
                        List<Link> links = getLinksForAppealGrades(appealId);
                        AppealRepresentation appealRepresentation = new AppealRepresentation(appeal, links);
                        String reesponseMessage = gson.toJson(appealRepresentation);
                        response = Response.status(Response.Status.CREATED).entity(reesponseMessage).build();
                    } else {
                        response = Response.status(Response.Status.BAD_REQUEST).build();
                    }
                } else {
                    response = Response.status(Response.Status.FORBIDDEN).entity("AppealExists").build();
                }
            } else {
                response = Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }

    @PUT
    @Path("/{studentName}")
    @Consumes(Representation.APPEALS_MEDIA_TYPE)
    public Response setAppealStatus(@PathParam("studentName") String studentName, String message) {
        Response response;
        try {
            Appeal existingAppeal = appealsStorage.getAppeal(studentName);
            if (existingAppeal != null) {
                Appeal appeal = gson.fromJson(message, Appeal.class);
                if (appeal == null || appeal.getStatus() == null) {
                    response = Response.status(Response.Status.BAD_REQUEST).build();
                } else {
                    existingAppeal.setStatus(appeal.getStatus());
                    response = Response.status(Response.Status.OK).build();
                }
            } else {
                response = Response.status(Response.Status.FORBIDDEN).build();
            }
        } catch (Exception e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }

    private List<Link> getLinksForGetAppeal(String studentName, AppealStatus status) {
        if (status != AppealStatus.Rejected) {
            List<Link> links = new ArrayList<>();
            Link link;
            if (status == AppealStatus.Accepted) {
                link = new Link();
                link.setMediaType(Representation.APPEALS_MEDIA_TYPE);
                link.setRel("getGrade");
                String appealsUri = UriHelper.getBaseUri(uriInfo.getRequestUri()) + "/grade/" + studentName;
                link.setUri(appealsUri);
                links.add(link);
            }
            link = new Link();
            link.setMediaType(Representation.APPEALS_MEDIA_TYPE);
            link.setRel(Representation.SELF_REL_VALUE);
            link.setUri(uriInfo.getRequestUri().toString());
            links.add(link);

            return links;
        }
        return null;
    }

    private List<Link> getLinksForAppealGrades(String studentName) {
        List<Link> links = new ArrayList<>();
        Link link = new Link();
        link.setMediaType(Representation.APPEALS_MEDIA_TYPE);
        link.setRel("getAppeal");
        String appealsUri = UriHelper.getBaseUri(uriInfo.getRequestUri()) + "/appeal/" + studentName;
        link.setUri(appealsUri);
        links.add(link);

        link = new Link();
        link.setMediaType(Representation.APPEALS_MEDIA_TYPE);
        link.setRel(Representation.SELF_REL_VALUE);
        link.setUri(uriInfo.getRequestUri().toString());
        links.add(link);
        return links;
    }

    private List<Link> getLinkForAppealWhenTeacherViews(String appealId, String student) {
        List<Link> links = new ArrayList<>();
        Link link = new Link();
        link.setMediaType(Representation.APPEALS_MEDIA_TYPE);
        link.setRel("setAppealStatus");
        String appealsUri = UriHelper.getBaseUri(uriInfo.getRequestUri()) + "/appeal/" + appealId;
        link.setUri(appealsUri);
        links.add(link);

        link = new Link();
        link.setMediaType(Representation.APPEALS_MEDIA_TYPE);
        link.setRel("updateGrade");
        String updateGtradeUri = UriHelper.getBaseUri(uriInfo.getRequestUri()) + "/grade/" + student;
        link.setUri(updateGtradeUri);
        links.add(link);

        link = new Link();
        link.setMediaType(Representation.APPEALS_MEDIA_TYPE);
        link.setRel(Representation.SELF_REL_VALUE);
        link.setUri(uriInfo.getRequestUri().toString());
        links.add(link);
        return links;
    }
}
