package com.redprojects.mediateca.controllers.user;

import com.redprojects.mediateca.communication.response.Messages;
import com.redprojects.mediateca.communication.response.Response;
import com.redprojects.mediateca.entities.UserData;
import com.redprojects.mediateca.entities.UserDataList;
import com.redprojects.mediateca.services.user.AdminUserService;
import com.redprojects.mediateca.utils.ActionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin-services")
public class AdminServicesController {

    private final AdminUserService adminUserService;

    @Autowired
    public AdminServicesController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    // Admin Services Endpoints
    // 1: Get User Information
    // 2: List User Information
    // 3: Promote To Admin User
    // 4: Reset User Password
    // 5: Demote Admin User
    // 6: Delete User

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getUserInformationEndpoint(@PathVariable(value = "id") String id) {
        Response response = new Response("Get User Information");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        UserData userData = adminUserService.getUserInfo(id);
        if (userData != null) {
            response.addBodyElement("userData", userData.toJsonObject());
        }
        else {
            response.actionFailed();
            response.setMessage(Messages.Failure.GET_USER_INFO);
        }

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }


    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public ResponseEntity<String> getUserListInformationEndpoint(@RequestParam String detailLevel) {
        Response response = new Response("Get List Of Users");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        UserDataList userDataList = adminUserService.getUserList();
        if (detailLevel.contentEquals("Condensed") || detailLevel.contentEquals("Full")) {
            response.addBodyElement("userDataList", userDataList.toJsonArray(detailLevel));
        }
        else {
            response.actionFailed();
            response.setMessage(Messages.Failure.BAD_REQUEST);
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }


    @RequestMapping(value = "/user/promote/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> promoteUserEndpoint(@PathVariable(value = "id") String id) {
        Response response = new Response("Promote User To Admin");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        ActionStatus status = adminUserService.promoteUser(id);
        if (status == ActionStatus.FAILED) {
            response.actionFailed();
            response.setMessage(Messages.Failure.PROMOTE_USER);
        }
        else {
            response.setMessage(Messages.Success.PROMOTE_USER);
        }

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }


    @RequestMapping(value = "/user/reset-password/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> resetUserPasswordEndpoint(@PathVariable(value = "id") String id) {
        Response response = new Response("Reset User Password");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        ActionStatus status = adminUserService.resetUserPassword(id);
        if (status == ActionStatus.FAILED) {
            response.actionFailed();
            response.setMessage(Messages.Failure.RESET_PASSWORD);
        }
        else {
            // queue reset password email
            System.out.print("TODO: SEND REQUEST TO EMAIL SERVICE QUEUE");
            response.setMessage(Messages.Success.RESET_PASSWORD);
        }

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }


    @RequestMapping(value = "/user/demote/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> demoteUserEndpoint(@PathVariable(value = "id") String id) {
        Response response = new Response("Demote Admin User");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        ActionStatus status = adminUserService.demoteUser(id);
        if (status == ActionStatus.FAILED) {
            response.actionFailed();
            response.setMessage(Messages.Failure.DEMOTE_USER);
        }
        else {
            response.setMessage(Messages.Success.DEMOTE_USER);
        }

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }


    @RequestMapping(value = "/user/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUserEndpoint(@PathVariable(value = "id") String id) {
        Response response = new Response("Delete User");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        ActionStatus status = adminUserService.demoteUser(id);
        if (status == ActionStatus.FAILED) {
            response.actionFailed();
            response.setMessage(Messages.Failure.DELETE_USER);
        }
        else {
            response.setMessage(Messages.Success.DELETE_USER);
        }

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }

}
