package com.red_projects.mediateca.controllers.media;

import com.red_projects.mediateca.communication.response.Response;
import com.red_projects.mediateca.entities.media.Song;
import com.red_projects.mediateca.services.media.SongService;
import com.red_projects.mediateca.utils.ActionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import javax.json.JsonArray;
import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/media/music/song")
public class SongController {

    private final SongService songService;

    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }

    // Song Endpoints
    // 1: Upload Song
    // 2: Get Song Info
    // 3: Get Song File
    // 4: Get All Songs Info
    // 5: Get All Songs Info By Artist Id
    // 6: Get All Songs Info By Album Id
    // 7: Delete Song

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<String> uploadSongEndpoint(@RequestParam("title") String title,
                                                     @RequestParam("artistId") String artistId,
                                                     @RequestParam("albumId") String albumId,
                                                     @RequestParam("trackNumber") int trackNum,
                                                     @RequestParam("genre") String genre,
                                                     @RequestParam("file")MultipartFile file) {
        Response response = new Response("Upload New Song");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        ActionStatus status = songService.createSong(title, artistId, albumId, trackNum, genre, file);
        if (status == ActionStatus.SUCCEEDED) {
            response.actionSucceeded();
            response.setMessage("Successfully Uploaded Song");
        }
        else {
            response.actionFailed();
            response.setMessage("Could Not Upload Song");
        }

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }

    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getSongInfoEndpoint(@PathVariable("id") String id) {
        Response response = new Response("Get Song Information");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        Song song = songService.getSongInfo(id);
        if (song != null) {
            response.actionSucceeded();
            response.setMessage("Retrieved Song Information");
            response.addBodyElement("song", song.toJsonObject());
        }
        else {
            httpStatus = HttpStatus.BAD_REQUEST;
            response.setMessage("Could Not Find Song");
            response.actionFailed();
        }

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }

    @RequestMapping(value = "/info/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateSongInfoEndpoint(@PathVariable("id") String id) {
        Response response = new Response("Get Song Information");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }

    @RequestMapping(value = "/stream/{id}", method = RequestMethod.GET)
    public Mono<ResponseEntity<byte[]>> getSongStreamEndpoint(@RequestHeader(value = "Range", required = false) String httpRangeList,
                                                              @PathVariable("id") String id) {
        ResponseEntity<byte[]> responseEntity = songService.getContent(id, httpRangeList, "audio");
        return Mono.just(Objects.requireNonNullElseGet(responseEntity, () -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @RequestMapping(value = "/artist/list/{artistId}", method = RequestMethod.GET)
    public ResponseEntity<String> getArtistSongListEndpoint(@PathVariable("artistId") String artistId) {
        Response response = new Response("List All Artist Song Information");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        JsonArray songList = songService.getAllArtistSongs(artistId);
        response.addBodyElement("songList", songList);
        response.actionSucceeded();
        response.setMessage("Retrieved List of Songs");

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }

    @RequestMapping(value = "/album/list/{albumId}", method = RequestMethod.GET)
    public ResponseEntity<String> getAlbumSongListEndpoint(@PathVariable("albumId") String albumId) {
        Response response = new Response("List All Album Song Information");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        JsonArray songList = songService.getAllAlbumSongs(albumId);
        response.addBodyElement("songList", songList);
        response.actionSucceeded();
        response.setMessage("Retrieved List of Songs");

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<String> getSongListEndpoint() {
        Response response = new Response("List All Song Information");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        JsonArray songList = songService.getAllSongs();
        response.addBodyElement("songList", songList);
        response.actionSucceeded();
        response.setMessage("Retrieved List of Songs");

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson());
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<String> deleteSongEndpoint(@PathVariable("id") String id) {
        Response response = new Response("Delete Song");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;

        ActionStatus status = songService.deleteSong(id);
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
