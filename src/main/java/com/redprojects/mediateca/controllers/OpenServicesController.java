package com.redprojects.mediateca.controllers;

import com.redprojects.mediateca.communication.Response;
import com.redprojects.mediateca.communication.requests.RegisterRequest;
import com.redprojects.mediateca.communication.requests.ResetMethod;
import com.redprojects.mediateca.communication.requests.ResetPasswordRequest;
import com.redprojects.mediateca.communication.requests.VerificationRequest;
import com.redprojects.mediateca.security.JwtUtil;
import com.redprojects.mediateca.services.UserService;
import com.redprojects.mediateca.utils.AuthStatus;
import com.redprojects.mediateca.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;


@RestController
@RequestMapping("/open-services")
public class OpenServicesController {

    public UserService userService;

    @Autowired
    public OpenServicesController(UserService userService) {
        this.userService = userService;
    }

    // Open Services Endpoints
    // 1: Register User
    // 2: Authenticate User Credentials
    // 3: Reset User Password
    //

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> registerUserEndpoint(@RequestBody RegisterRequest request) {
        Response response = new Response("Register New User");
        HttpStatus httpStatus = HttpStatus.CREATED;

        // check if username unique
        if (userService.isUsernameUnique(request.getUsername())) {
            // check if password meets requirement
            if (userService.isValidPassword(request.getPassword())) {
                // create new user
                boolean createUserSuccess = userService.createUser(request);
                if (!createUserSuccess) {
                    response.actionFailed();
                    response.setMessage("Error Creating New User");
                    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                }
            }
            else {
                response.actionFailed();
                response.setMessage("Password Does Not Meet Requirements");
                httpStatus = HttpStatus.ACCEPTED;
            }
        }
        else {
            response.actionFailed();
            response.setMessage("Username Is Already Associate With Another Account");
            httpStatus = HttpStatus.ACCEPTED;
        }
        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<String> authenticateUserEndpoint(HttpServletRequest request) {
        Response response = new Response("Authenticate User Credentials");
        HttpStatus httpStatus = HttpStatus.OK;
        String authToken = null;

        // get credentials
        String authorizationHeader = request.getHeader("Authorization");
        if (!authorizationHeader.isBlank()) {
            String combinedCredentials = SecurityUtil.base64Decode(authorizationHeader.substring(6));
            if (combinedCredentials != null) {
                String[] credentials = combinedCredentials.split(":");
                String username = credentials[0];
                String password = credentials[1];

                // authenticate user credentials
                AuthStatus status = userService.authenticateUser(username, password);
                if (status == AuthStatus.FAILED) {
                    response.actionFailed();
                    response.setMessage("Invalid Credentials");
                }
                else if (status == AuthStatus.UNVERIFIED_DEACTIVATED) {
                    response.actionFailed();
                    response.setMessage("Account Has Not Been Verified Or Is Deactivated");
                }
                else {
                    boolean isAdmin = status == AuthStatus.SUCCESS_ADMIN;
                    authToken = JwtUtil.generateJwt(username, isAdmin);
                }
            }
            else {
                response.actionFailed();
                response.setMessage("Bad Request!");
                httpStatus = HttpStatus.BAD_REQUEST;
            }
        }
        else {
            response.actionFailed();
            response.setMessage("Bad Request!");
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        if (response.getActionSuccess()) {
            response.addBodyElement("authToken", authToken);
        }

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public ResponseEntity<String> verifyUserEmailEndpoint(@RequestBody VerificationRequest request) {
        Response response = new Response("Verify User Email");
        HttpStatus httpStatus = HttpStatus.OK;

        // get credentials
        String emailAddress = request.getEmailAddress();
        String verificationCode = SecurityUtil.base64Decode(request.getVerificationCode());
        if (verificationCode != null) {
            AuthStatus status = userService.verifyUserEmail(emailAddress, verificationCode);
            if (status == AuthStatus.FAILED) {
                response.actionFailed();
                response.setMessage("Verification Code Is Invalid Or Incorrect");
            }
        }
        else {
            response.actionFailed();
            response.setMessage("Bad Request");
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public ResponseEntity<String> resetUserPasswordEndpoint(@RequestBody ResetPasswordRequest request) {
        Response response = new Response("Reset User Password");
        HttpStatus httpStatus = HttpStatus.OK;

        // check for bad request
        if (request.getResetMethod() == ResetMethod.EMAIL) {
            String username = request.getUsername();

            // get user information
            String status = userService.authenticateCredentialsForEmail(username);
            if (!status.contentEquals(AuthStatus.FAILED.toString()) || !status.contentEquals(AuthStatus.UNVERIFIED_DEACTIVATED.toString())) {
                String email = status;
                // queue email

            }
            else {
                response.actionFailed();
                response.setMessage("Incorrect Credentials");
            }
        }
        else {
            response.actionFailed();
            response.setMessage("Bad Request!");
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }
}
