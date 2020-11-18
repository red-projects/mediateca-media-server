package com.red_projects.mediateca.controllers.user;

import com.red_projects.mediateca.communication.response.Messages;
import com.red_projects.mediateca.communication.response.Response;
import com.red_projects.mediateca.entities.utils.UserData;
import com.red_projects.mediateca.entities.utils.UserDataList;
import com.red_projects.mediateca.utils.ActionStatus;
import com.red_projects.mediateca.services.user.AdminUserService;
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
    // 3: Reset User Password
    // 4: Reactivate User
    // 5: Deactivate User
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
        if (detailLevel.contentEquals("condensed") || detailLevel.contentEquals("full")) {
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


    @RequestMapping(value = "/user/reactivate/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> reactivateUserEndpoint(@PathVariable(value = "id") String id) {
        Response response = new Response("Reactivate User");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        ActionStatus status = adminUserService.reactivateUser(id);
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


    @RequestMapping(value = "/user/deactivate/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> deactivateUserEndpoint(@PathVariable(value = "id") String id) {
        Response response = new Response("Deactivate User");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        ActionStatus status = adminUserService.deactivateUser(id);
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


    @RequestMapping(value = "/user/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUserEndpoint(@PathVariable(value = "id") String id) {
        Response response = new Response("Delete User");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        ActionStatus status = adminUserService.deleteUser(id);
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
