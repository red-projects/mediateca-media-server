package com.red_projects.mediateca.controllers.user;

import com.red_projects.mediateca.communication.requests.RegisterRequest;
import com.red_projects.mediateca.communication.requests.ResetMethod;
import com.red_projects.mediateca.communication.requests.ResetPasswordRequest;
import com.red_projects.mediateca.communication.requests.VerificationRequest;
import com.red_projects.mediateca.communication.response.Messages;
import com.red_projects.mediateca.communication.response.Response;
import com.red_projects.mediateca.entities.utils.AccessLevel;
import com.red_projects.mediateca.security.JwtUtil;
import com.red_projects.mediateca.services.user.OpenUserService;
import com.red_projects.mediateca.utils.AuthStatus;
import com.red_projects.mediateca.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;



@RestController
@RequestMapping("/open-services")
public class OpenServicesController {

    private final OpenUserService openUserService;

    @Autowired
    public OpenServicesController(OpenUserService openUserService) {
        this.openUserService = openUserService;
    }

    // Open Services Endpoints
    // 1: Register User
    // 2: Authenticate User Credentials
    // 3: Verify User Email Address
    // 4: Reset User Password

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> registerUserEndpoint(@RequestBody RegisterRequest request) {
        Response response = new Response("Register New User");
        HttpStatus httpStatus = HttpStatus.CREATED;

        // check if username unique
        if (openUserService.isUsernameUnique(request.getUsername())) {
            // check if password meets requirement
            if (openUserService.isValidPassword(request.getPassword())) {
                // create new user
                boolean createUserSuccess = openUserService.createUser(request);
                if (!createUserSuccess) {
                    response.actionFailed();
                    response.setMessage(Messages.Failure.USER_CREATION);
                    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                }
                else {
                    response.setMessage(Messages.Success.USER_REGISTRATION);
                }
            }
            else {
                response.actionFailed();
                response.setMessage(Messages.Failure.PASSWORD_REQUIREMENT);
                httpStatus = HttpStatus.ACCEPTED;
            }
        }
        else {
            response.actionFailed();
            response.setMessage(Messages.Failure.INVALID_USERNAME);
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
        HttpStatus httpStatus = HttpStatus.ACCEPTED;
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
                AuthStatus status = openUserService.authenticateUser(username, password);
                if (status == AuthStatus.FAILED) {
                    response.actionFailed();
                    response.setMessage(Messages.Failure.INVALID_CREDENTIALS);
                }
                else if (status == AuthStatus.UNVERIFIED_DEACTIVATED) {
                    response.actionFailed();
                    response.setMessage(Messages.Failure.USER_STATUS);
                }
                else {
                    AccessLevel accessLevel;
                    if (status == AuthStatus.SUCCESS_ADMIN) accessLevel = AccessLevel.ADMIN;
                    else if (status == AuthStatus.SUCCESS_OWNER) accessLevel = AccessLevel.OWNER;
                    else accessLevel = AccessLevel.USER;
                    authToken = JwtUtil.generateJwt(username, accessLevel);
                }
            }
            else {
                response.actionFailed();
                response.setMessage(Messages.Failure.BAD_REQUEST);
                httpStatus = HttpStatus.BAD_REQUEST;
            }
        }
        else {
            response.actionFailed();
            response.setMessage(Messages.Failure.BAD_REQUEST);
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
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        // get credentials
        String emailAddress = request.getEmailAddress();
        String verificationCode = SecurityUtil.base64Decode(request.getVerificationCode());
        if (verificationCode != null) {
            AuthStatus status = openUserService.verifyUserEmail(emailAddress, verificationCode);
            if (status == AuthStatus.FAILED) {
                response.actionFailed();
                response.setMessage(Messages.Failure.EMAIL_VERIFICATION);
            }
            else {
                response.setMessage(Messages.Success.EMAIL_VERIFICATION);
            }
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


    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public ResponseEntity<String> resetUserPasswordEndpoint(@RequestBody ResetPasswordRequest request) {
        Response response = new Response("Reset User Password");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        // check for bad request
        if (request.getResetMethod() == ResetMethod.EMAIL) {
            String username = request.getUsername();

            // get user information
            String status = openUserService.authenticateCredentialsForEmail(username);
            if (!status.contentEquals(AuthStatus.FAILED.toString()) || !status.contentEquals(AuthStatus.UNVERIFIED_DEACTIVATED.toString())) {
                String email = status;
                // queue email
                response.setMessage(Messages.Success.RESET_PASSWORD_REQUEST);
            }
            else {
                response.actionFailed();
                response.setMessage(Messages.Failure.INVALID_CREDENTIALS);
            }
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

}
