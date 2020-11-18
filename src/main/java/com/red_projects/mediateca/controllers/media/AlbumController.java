package com.red_projects.mediateca.controllers.media;

import com.red_projects.mediateca.communication.response.Response;
import com.red_projects.mediateca.entities.media.Album;
import com.red_projects.mediateca.services.media.AlbumService;
import com.red_projects.mediateca.utils.ActionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.json.JsonArray;

@RestController
@RequestMapping("/media/music/album")
public class AlbumController {

    private final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    // Album Endpoints
    // 1: Create Album
    // 2: Get Album Info
    // 3: Get Album Photo
    // 4: Get All Albums
    // 5: Get All Albums By Artist Id

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<String> createAlbumEndpoint(@RequestParam("title") String title,
                                                      @RequestParam("artistId") String artistId,
                                                      @RequestParam("numberOfTracks") int numTracks,
                                                      @RequestParam("releaseYear") int year,
                                                      @RequestParam("photo") MultipartFile file) {
        Response response = new Response("Adding New Album");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        ActionStatus status = albumService.createAlbum(title, artistId, numTracks, year, file);
        if (status == ActionStatus.SUCCEEDED) {
            response.actionSucceeded();
            response.setMessage("Successfully Added New Album");
        }
        else {
            response.actionFailed();
            response.setMessage("Could Not Add New Album");
        }

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }

    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getArtistInfoEndpoint(@PathVariable("id") String id) {
        Response response = new Response("Get Album Information");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        Album album = albumService.getAlbumInfo(id);
        if (album != null) {
            response.actionSucceeded();
            response.setMessage("Retrieved Album Information");
            response.addBodyElement("album", album.toJsonObject());
        }
        else {
            httpStatus = HttpStatus.BAD_REQUEST;
            response.setMessage("Could Not Find Album");
            response.actionFailed();
        }

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }

    @RequestMapping(value = "/info/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateArtistInfoEndpoint(@PathVariable("id") String id) {
        Response response = new Response("Get Album Information");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }

    @RequestMapping(value = "/image/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getAlbumImageEndpoint(@PathVariable("id") String id) {
        Response response = new Response("Get Album Photo");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        byte[] imageBytes;
        Album album = albumService.getAlbumInfo(id);
        if (album != null) {
            imageBytes = albumService.getAlbumImage(album.getImagePath());
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
    public ResponseEntity<String> getAlbumListEndpoint() {
        Response response = new Response("List All Albums Information");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        JsonArray albumList = albumService.getAllAlbums();
        response.addBodyElement("albumList", albumList);
        response.actionSucceeded();
        response.setMessage("Retrieved List of Albums");

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }

    @RequestMapping(value = "/list/{artistId}", method = RequestMethod.GET)
    public ResponseEntity<String> getArtistAlbumListEndpoint(@PathVariable("artistId") String artistId) {
        Response response = new Response("List All Artist Album Information");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        JsonArray albumList = albumService.getAllArtistAlbums(artistId);
        response.addBodyElement("albumList", albumList);
        response.actionSucceeded();
        response.setMessage("Retrieved List of Albums");

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }

}
