package com.red_projects.mediateca.controllers.user;

import com.red_projects.mediateca.communication.response.Messages;
import com.red_projects.mediateca.communication.response.Response;
import com.red_projects.mediateca.utils.ActionStatus;
import com.red_projects.mediateca.services.user.OwnerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/owner-services")
public class OwnerServicesController {

    private final OwnerUserService ownerUserService;

    @Autowired
    public OwnerServicesController(OwnerUserService ownerUserService) {
        this.ownerUserService = ownerUserService;
    }

    // Owner Services Endpoints
    // 1: Promote To Admin User
    // 2: Demote Admin User
    // 3: Transfer Ownership

    @RequestMapping(value = "/user/promote/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> promoteUserEndpoint(@PathVariable(value = "id") String id) {
        Response response = new Response("Promote User To Admin");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        ActionStatus status = ownerUserService.promoteUser(id);
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

    @RequestMapping(value = "/user/demote/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> demoteUserEndpoint(@PathVariable(value = "id") String id) {
        Response response = new Response("Demote Admin User");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        ActionStatus status = ownerUserService.demoteUser(id);
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

}
