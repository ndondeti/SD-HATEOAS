/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.cse564.appealsclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import edu.asu.cse564.appealsclient.models.Appeal;
import edu.asu.cse564.appealsclient.models.AppealStatus;
import edu.asu.cse564.appealsclient.models.Grade;
import edu.asu.cse564.appealsclient.representation.AllAppealResponseMessage;
import edu.asu.cse564.appealsclient.representation.AppealRepresentation;
import edu.asu.cse564.appealsclient.representation.GradeRepresentation;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author Vivek
 */
public class Main {

    public static void main(String[] args) throws URISyntaxException {
        AppealsHttpClient client = new AppealsHttpClient();
        try {
            System.out.println("The grades are accessed through the student name and appeals through appealsId");
            System.out.println("First test case - Teacher Posts grade Student appeals and Teacher accepts and changes grades.");
            testCase1();
            System.out.println();
            System.out.println();
            client.clearServer();

            System.out.println("Second test case - Create an appeal and abandon the appeal");
            testCase2();
            System.out.println();
            System.out.println();
            client.clearServer();

            System.out.println("Third test case - Submit an appeal and then follow it up later when there is no response");
            testCase3();
            System.out.println();
            System.out.println();
            client.clearServer();

            System.out.println("Fourth test case - Submit the appeal to the wrong url");
            testCase4();
            System.out.println();
            System.out.println();
            client.clearServer();

            System.out.println("Fifth test case - Submits the appeal to the proper url but uses the wrong url while try to check on the appeal status");
            testCase5();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void testCase1() {
        AppealsHttpClient client = new AppealsHttpClient();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            System.out.println("1) Teacher Posting grade for student vivek, feedback-the 5th question was incorrect, grade-90");
            String student = "vivek";
            Grade grade = new Grade();
            grade.setGrade(90);
            grade.setFeedback("The 5th question was not attempted");
            client.addGrade(grade, student);
            System.out.println("Added grade successfully");

            System.out.println("2) Student getting the grade");
            URI getGradeUri = new URI(AppealsHttpClient.ENTRY_POINT_URI + student);
            GradeRepresentation gradeRep = client.getGrade(getGradeUri);
            JsonElement j = gson.toJsonTree(gradeRep);
            System.out.println(gson.toJson(j));

            System.out.println("3) Student appealing the grade through the link - " + gradeRep.getPostAppealLink().getUri());
            System.out.println("Student makes the following appeal - 5th question was in the last page. Please kindly review.");
            Appeal appeal = new Appeal();
            appeal.setStudent(student);
            appeal.setAppeals("5th question was in the last page. Please kindly review.");
            j = gson.toJsonTree(appeal);
            System.out.println(gson.toJson(j));
            System.out.println("The response from server");
            AppealRepresentation appealRep = client.makeAppeal(appeal, new URI(gradeRep.getPostAppealLink().getUri()));
            j = gson.toJsonTree(appealRep);
            System.out.println(gson.toJson(j));

            System.out.println("4) The teacher gets all the appeals in the system");
            AllAppealResponseMessage response = client.getAllAppeals();
            j = gson.toJsonTree(response);
            System.out.println(gson.toJson(j));

            System.out.println("5) Teacher accepts the appeal. He updates the grade and also the appeal");
            appeal = new Appeal();
            appeal.setStatus(AppealStatus.Accepted);
            j = gson.toJsonTree(appeal);
            System.out.println(gson.toJson(j));
            /* Setting the status of the appeal using the update link obtained from get all appeals */
            URI setAppealUri = new URI(response.getAppealRepresentations().get(0).getUpdateStatusLink().getUri());
            client.setAppealStatus(appeal, setAppealUri);
            System.out.println("Updated appeal with status Accepted");

            System.out.println("New Grade - 100, Feedback - Good job");
            grade = new Grade();
            grade.setGrade(100);
            grade.setFeedback("Good job");
            j = gson.toJsonTree(grade);
            System.out.println(gson.toJson(j));
            /* Updating the grtade using the update link obtained from get all appeals */
            URI updateGradeUri = new URI(response.getAppealRepresentations().get(0).getUpdateGradeLink().getUri());
            client.updateGrade(grade, updateGradeUri);
            System.out.println("Updated grade successfully");

            System.out.println("6) Student checks the appeal again to check for any updates");
            AppealRepresentation getAppealRep = client.getAppeal(new URI(appealRep.getAppealLink().getUri()));
            System.out.println("Student gets the following status back");
            j = gson.toJsonTree(getAppealRep);
            System.out.println(gson.toJson(j));
            System.out.println("7) Checking if the status is 'Accepted'. If so we get the grades.");
            if (getAppealRep.getAppeals().getStatus() == AppealStatus.Accepted) {
                GradeRepresentation updatedGradeRep = client.getGrade(new URI(getAppealRep.getGradeLink().getUri()));
                j = gson.toJsonTree(updatedGradeRep);
                System.out.println(gson.toJson(j));
                System.out.println("The students grade has been updated thanks to his appeal");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void testCase2() {
        AppealsHttpClient client = new AppealsHttpClient();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            System.out.println("1) Teacher Posting grade for student vivek, feedback-the 5th question was incorrect, grade-90");
            String student = "vivek";
            Grade grade = new Grade();
            grade.setGrade(90);
            grade.setFeedback("The 5th question was not attempted");
            client.addGrade(grade, student);
            System.out.println("Added grade successfully");

            System.out.println("2) Student getting the grade");
            URI getGradeUri = new URI(AppealsHttpClient.ENTRY_POINT_URI + student);
            GradeRepresentation gradeRep = client.getGrade(getGradeUri);
            JsonElement j = gson.toJsonTree(gradeRep);
            System.out.println(gson.toJson(j));

            System.out.println("3) Student appealing the grade through the link - " + gradeRep.getPostAppealLink().getUri());
            System.out.println("Student makes the following appeal - 5th question was in the last page. Please kindly review.");
            Appeal appeal = new Appeal();
            appeal.setStudent(student);
            appeal.setAppeals("I need to get a 'B' grade to graduate. Please kindly review.");
            j = gson.toJsonTree(appeal);
            System.out.println(gson.toJson(j));
            System.out.println("The response from server");
            AppealRepresentation appealRep = client.makeAppeal(appeal, new URI(gradeRep.getPostAppealLink().getUri()));
            j = gson.toJsonTree(appealRep);
            System.out.println(gson.toJson(j));

            System.out.println("The student abandons his appeal process.");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void testCase3() {
        AppealsHttpClient client = new AppealsHttpClient();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            System.out.println("1) Teacher Posting grade for student vivek, feedback-the 5th question was incorrect, grade-90");
            String student = "vivek";
            Grade grade = new Grade();
            grade.setGrade(90);
            grade.setFeedback("The 5th question was not attempted");
            client.addGrade(grade, student);
            System.out.println("Added grade successfully");

            System.out.println("2) Student getting the grade");
            URI getGradeUri = new URI(AppealsHttpClient.ENTRY_POINT_URI + student);
            GradeRepresentation gradeRep = client.getGrade(getGradeUri);
            JsonElement j = gson.toJsonTree(gradeRep);
            System.out.println(gson.toJson(j));

            System.out.println("3) Student appealing the grade through the link - " + gradeRep.getPostAppealLink().getUri());
            System.out.println("Student makes the following appeal - 5th question was in the last page. Please kindly review.");
            Appeal appeal = new Appeal();
            appeal.setStudent(student);
            appeal.setAppeals("5th question was in the last page. Please kindly review.");
            j = gson.toJsonTree(appeal);
            System.out.println(gson.toJson(j));
            System.out.println("The response from server");
            AppealRepresentation appealRep = client.makeAppeal(appeal, new URI(gradeRep.getPostAppealLink().getUri()));
            j = gson.toJsonTree(appealRep);
            System.out.println(gson.toJson(j));

            System.out.println("4) Student checks the appeal again to check for any updates");
            AppealRepresentation getAppealRep = client.getAppeal(new URI(appealRep.getAppealLink().getUri()));
            System.out.println("Student gets the following status back");
            j = gson.toJsonTree(getAppealRep);
            System.out.println(gson.toJson(j));

            System.out.println("The student seeing that the appeal is still in process sends a mail to the teahcer to do a follow up");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void testCase4() {
        AppealsHttpClient client = new AppealsHttpClient();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            System.out.println("1) Teacher Posting grade for student vivek, feedback-the 5th question was incorrect, grade-90");
            String student = "vivek";
            Grade grade = new Grade();
            grade.setGrade(90);
            grade.setFeedback("The 5th question was not attempted");
            client.addGrade(grade, student);
            System.out.println("Added grade successfully");

            System.out.println("2) Student getting the grade");
            URI getGradeUri = new URI(AppealsHttpClient.ENTRY_POINT_URI + student);
            GradeRepresentation gradeRep = client.getGrade(getGradeUri);
            JsonElement j = gson.toJsonTree(gradeRep);
            System.out.println(gson.toJson(j));

            System.out.println("3) Student appealing the grade");
            System.out.println("Student makes the following appeal - 5th question was in the last page. Please kindly review.");
            Appeal appeal = new Appeal();
            appeal.setStudent(student);
            appeal.setAppeals("5th question was in the last page. Please kindly review.");
            j = gson.toJsonTree(appeal);
            System.out.println(gson.toJson(j));

            //Here the student uses a wrong appeal id to post the appeal
            String uri = gradeRep.getPostAppealLink().getUri();
            String wrongUri = uri.substring(0, uri.length() - 1) + "5";
            System.out.println("The student posts to the following wrong url " + wrongUri);
            AppealRepresentation appealRep = client.makeAppeal(appeal, new URI(wrongUri));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void testCase5() {
        AppealsHttpClient client = new AppealsHttpClient();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            System.out.println("1) Teacher Posting grade for student vivek, feedback-the 5th question was incorrect, grade-90");
            String student = "vivek";
            Grade grade = new Grade();
            grade.setGrade(90);
            grade.setFeedback("The 5th question was not attempted");
            client.addGrade(grade, student);
            System.out.println("Added grade successfully");

            System.out.println("2) Student getting the grade");
            URI getGradeUri = new URI(AppealsHttpClient.ENTRY_POINT_URI + student);
            GradeRepresentation gradeRep = client.getGrade(getGradeUri);
            JsonElement j = gson.toJsonTree(gradeRep);
            System.out.println(gson.toJson(j));

            System.out.println("3) Student appealing the grade through the link - " + gradeRep.getPostAppealLink().getUri());
            System.out.println("Student makes the following appeal - 5th question was in the last page. Please kindly review.");
            Appeal appeal = new Appeal();
            appeal.setStudent(student);
            appeal.setAppeals("5th question was in the last page. Please kindly review.");
            j = gson.toJsonTree(appeal);
            System.out.println(gson.toJson(j));
            AppealRepresentation appealRep = client.makeAppeal(appeal, new URI(gradeRep.getPostAppealLink().getUri()));
            System.out.println("The response from server");
            j = gson.toJsonTree(appealRep);
            System.out.println(gson.toJson(j));

            System.out.println("4) The teacher gets all the appeals in the system");
            AllAppealResponseMessage response = client.getAllAppeals();
            j = gson.toJsonTree(response);
            System.out.println(gson.toJson(j));

            System.out.println("5) Teacher accepts the appeal. He updates the grade and also the appeal");
            appeal = new Appeal();
            appeal.setStatus(AppealStatus.Accepted);
            j = gson.toJsonTree(appeal);
            System.out.println(gson.toJson(j));
            /* Setting the status of the appeal using the update link obtained from get all appeals */
            URI setAppealUri = new URI(response.getAppealRepresentations().get(0).getUpdateStatusLink().getUri());
            client.setAppealStatus(appeal, setAppealUri);
            System.out.println("Updated appeal with status Accepted");

            System.out.println("New Grade - 100, Feedback - Good job");
            grade = new Grade();
            grade.setGrade(100);
            grade.setFeedback("Good job");
            j = gson.toJsonTree(grade);
            System.out.println(gson.toJson(j));
            /* Updating the grtade using the update link obtained from get all appeals */
            URI updateGradeUri = new URI(response.getAppealRepresentations().get(0).getUpdateGradeLink().getUri());
            client.updateGrade(grade, updateGradeUri);
            System.out.println("Updated grade successfully");

            String uri = gradeRep.getPostAppealLink().getUri();
            String wrongUri = uri.substring(0, uri.length() - 1) + "5";
            System.out.println("6) Student checks the appeal again to check for any updates but uses the wrong appealId to access the resource");
            System.out.println("the wrong url " + wrongUri);
            System.out.println();
            System.out.println("The response");
            AppealRepresentation getAppealRep = client.getAppeal(new URI(wrongUri));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
