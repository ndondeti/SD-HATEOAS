/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.cse564.appealsclient;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

import edu.asu.cse564.appealsclient.models.Appeal;
import edu.asu.cse564.appealsclient.models.Grade;
import edu.asu.cse564.appealsclient.representation.AllAppealResponseMessage;
import edu.asu.cse564.appealsclient.representation.AppealRepresentation;
import edu.asu.cse564.appealsclient.representation.GradeRepresentation;
import java.net.URI;
import java.util.List;
import javax.ws.rs.core.Response;

/**
 *
 * @author Vivek
 */
public class AppealsHttpClient {

    private Gson gson;
    public static final String ENTRY_POINT_URI = "http://localhost:8080/AppealsServer/CSE564/grade/";
    private static final String RESTBUCKS_MEDIA_TYPE = "application/vnd-cse564-appeals+json";

    public AppealsHttpClient() {
        gson = new Gson();
    }

    public void updateGrade(Grade grade, URI updateGradeUri) throws Exception {
        Client client = Client.create();
        String gradeMessage = gson.toJson(grade);
        ClientResponse response = client.resource(updateGradeUri)
                .type(RESTBUCKS_MEDIA_TYPE)
                .post(ClientResponse.class, gradeMessage);
        if (response.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
            throw new Exception("The server returned internal server error while adding grade");
        } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            throw new Exception("The grade sent was not a valid");
        } else if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
            return;
        }
        throw new RuntimeException(String.format("Unexpected response [%d] while updating grade resource [%s]", response.getStatus(), updateGradeUri.toString()));
    }

    public void addGrade(Grade grade, String student) throws Exception {
        Client client = Client.create();
        URI addGradeUri = new URI(ENTRY_POINT_URI + student);
        String gradeMessage = gson.toJson(grade);
        ClientResponse response = client.resource(addGradeUri)
                .type(RESTBUCKS_MEDIA_TYPE)
                .post(ClientResponse.class, gradeMessage);
        if (response.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
            throw new Exception("The server returned internal server error while adding grade");
        } else if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            throw new Exception("The grade sent was not a valid");
        } else if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
            return;
        }
        throw new RuntimeException(String.format("Unexpected response [%d] while adding grade resource [%s]", response.getStatus(), addGradeUri.toString()));
    }

    public GradeRepresentation getGrade(URI getGradeUri) throws Exception {
        Client client = Client.create();
        ClientResponse response = client.resource(getGradeUri).accept(RESTBUCKS_MEDIA_TYPE).get(ClientResponse.class);

        int status = response.getStatus();
        if (status == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
            throw new Exception("The server returned internal server error while adding grade");
        } else if (status == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new Exception("The grade you requested is not present");
        } else if (status == Response.Status.OK.getStatusCode()) {
            String message = response.getEntity(String.class);
            GradeRepresentation gradeRepresentation = gson.fromJson(message, GradeRepresentation.class);
            return gradeRepresentation;
        }
        throw new RuntimeException(String.format("Unexpected response [%d] while getting grade resource [%s]", response.getStatus(), getGradeUri.toString()));
    }

    public AppealRepresentation getAppeal(URI getAppealUri) throws Exception {
        Client client = Client.create();
        ClientResponse response = client.resource(getAppealUri).accept(RESTBUCKS_MEDIA_TYPE).get(ClientResponse.class);

        int status = response.getStatus();
        if (status == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
            throw new Exception("The server returned internal server error while adding grade");
        } else if (status == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new Exception("The appeal you requested is not present");
        } else if (status == Response.Status.OK.getStatusCode()) {
            String message = response.getEntity(String.class);
            AppealRepresentation appealRepresentation = gson.fromJson(message, AppealRepresentation.class);
            return appealRepresentation;
        }
        throw new RuntimeException(String.format("Unexpected response [%d] while getting appeal resource [%s]", response.getStatus(), getAppealUri.toString()));
    }

    public AppealRepresentation makeAppeal(Appeal appeal, URI makeAppealUri) throws Exception {
        Client client = Client.create();
        String appealMessage = gson.toJson(appeal);
        ClientResponse response = client.resource(makeAppealUri)
                .accept(RESTBUCKS_MEDIA_TYPE).type(RESTBUCKS_MEDIA_TYPE)
                .post(ClientResponse.class, appealMessage);
        int status = response.getStatus();
        if (status == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
            throw new Exception("The server returned internal server error while adding grade");
        } else if (status == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new Exception("The appealId you used for the grade is wrong");
        } else if (status == Response.Status.BAD_REQUEST.getStatusCode()) {
            throw new Exception("The appeal sent was not a valid");
        } else if (status == Response.Status.FORBIDDEN.getStatusCode()) {
            throw new Exception("You have already made an appeal");
        } else if (status == Response.Status.CREATED.getStatusCode()) {
            String message = response.getEntity(String.class);
            AppealRepresentation appealRepresentation = gson.fromJson(message, AppealRepresentation.class);
            return appealRepresentation;
        }
        throw new RuntimeException(String.format("Unexpected response [%d] while making an appeal via resource [%s]", response.getStatus(), makeAppealUri.toString()));
    }

    public void setAppealStatus(Appeal appeal, URI appealUri) throws Exception {
        Client client = Client.create();
        String appealMessage = gson.toJson(appeal);
        ClientResponse response = client.resource(appealUri)
                .type(RESTBUCKS_MEDIA_TYPE)
                .put(ClientResponse.class, appealMessage);
        int status = response.getStatus();
        if (status == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
            throw new Exception("The server returned internal server error while adding grade");
        } else if (status == Response.Status.BAD_REQUEST.getStatusCode()) {
            throw new Exception("The appeal sent was not a valid");
        } else if (status == Response.Status.OK.getStatusCode()) {
            return;
        }
        throw new RuntimeException(String.format("Unexpected response [%d] while setting appeal status via resource [%s]", response.getStatus(), appealUri.toString()));
    }

    public AllAppealResponseMessage getAllAppeals() throws Exception {
        Client client = Client.create();
        URI allAppealsUri = new URI("http://localhost:8080/AppealsServer/CSE564/appeal/allAppeals");
        ClientResponse response = client.resource(allAppealsUri).accept(RESTBUCKS_MEDIA_TYPE).get(ClientResponse.class);
        int status = response.getStatus();
        if (status == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
            throw new Exception("The server returned internal server error while adding grade");
        } else if (status == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new Exception("No appeal");
        } else if (status == Response.Status.OK.getStatusCode()) {
            String message = response.getEntity(String.class);
            AllAppealResponseMessage responseMessage = gson.fromJson(message, AllAppealResponseMessage.class);
            return responseMessage;
        }
        throw new RuntimeException(String.format("Unexpected response [%d] while getting all appeal via uri [%s]", response.getStatus(), allAppealsUri.toString()));
    }
    
    public void clearServer() throws Exception {
        Client client = Client.create();
        URI clearServer = new URI("http://localhost:8080/AppealsServer/CSE564/grade/all");
        ClientResponse response = client.resource(clearServer).delete(ClientResponse.class);
        int status = response.getStatus();
        if (status == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
            throw new Exception("The server returned internal server error while adding grade");
        } else if (status == Response.Status.OK.getStatusCode()) {
            return;
        }
        throw new RuntimeException(String.format("Unexpected response [%d] while getting all appeal via uri [%s]", response.getStatus(), clearServer.toString()));
    }
}
