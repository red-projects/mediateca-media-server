package com.red_projects.mediateca.controllers.media;

import com.red_projects.mediateca.communication.response.Response;
import com.red_projects.mediateca.entities.media.Artist;
import com.red_projects.mediateca.services.media.ArtistService;
import com.red_projects.mediateca.utils.ActionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.json.JsonArray;

@RestController
@RequestMapping("/media/music/artist")
public class ArtistController {

    private final ArtistService artistService;

    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    // Artist Endpoints
    // 1: Upload Artist
    // 2: Get Artist Info
    // 3: Get Artist Photo
    // 4: Get All Artists Info
    // 5: Delete Artist

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<String> createArtistEndpoint(@RequestParam("name") String name,
                                                       @RequestParam("description") String description,
                                                       @RequestParam("photo") MultipartFile file) {
        Response response = new Response("Adding New Artist");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        ActionStatus status = artistService.createArtist(name, description, file);
        if (status == ActionStatus.SUCCEEDED) {
            response.actionSucceeded();
            response.setMessage("Successfully Added New Artist");
        }
        else {
            response.actionFailed();
            response.setMessage("Could Not Add New Artist");
        }

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }

    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getArtistInfoEndpoint(@PathVariable("id") String id) {
        Response response = new Response("Get Artist Information");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        Artist artist = artistService.getArtistInfo(id);
        if (artist != null) {
            response.actionSucceeded();
            response.setMessage("Retrieved Artist Information");
            response.addBodyElement("artist", artist.toJsonObject());
        }
        else {
            httpStatus = HttpStatus.BAD_REQUEST;
            response.setMessage("Could Not Find Artist");
            response.actionFailed();
        }

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }

    @RequestMapping(value = "/info/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateArtistInfoEndpoint(@PathVariable("id") String id) {
        Response response = new Response("Update Artist Information");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }

    @RequestMapping(value = "/image/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getArtistImageEndpoint(@PathVariable("id") String id) {
        Response response = new Response("Get Artist Photo");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        byte[] imageBytes;
        Artist artist = artistService.getArtistInfo(id);
        if (artist != null) {
            imageBytes = artistService.getArtistImage(artist.getImagePath());
        }
        else {
            imageBytes = null;
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<String> getArtistListEndpoint() {
        Response response = new Response("List All Artist Information");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        JsonArray artistList = artistService.getAllArtist();
        response.addBodyElement("artistList", artistList);
        response.actionSucceeded();
        response.setMessage("Retrieved List of Artists");

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteArtistEndpoint(@PathVariable("id") String id) {
        Response response = new Response("Delete Artist");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        ActionStatus status = artistService.deleteArtist(id);
        if (status == ActionStatus.SUCCEEDED) {
            response.actionSucceeded();
            response.setMessage("Successfully Deleted Song");
        }
        else {
            response.actionFailed();
            response.setMessage("Error Deleting Song");
        }


        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }

}
