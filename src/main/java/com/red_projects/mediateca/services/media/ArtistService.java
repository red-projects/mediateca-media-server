package com.red_projects.mediateca.services.media;

import com.red_projects.mediateca.entities.media.Album;
import com.red_projects.mediateca.entities.media.Artist;
import com.red_projects.mediateca.repositories.AlbumRepository;
import com.red_projects.mediateca.repositories.ArtistRepository;
import com.red_projects.mediateca.utils.ActionStatus;
import com.red_projects.mediateca.utils.io.IOUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;

    private final String MUSIC_DIR = "H:/RedProjects/test/media/music/";
    private final String IMAGE_HOSTNAME = "http://localhost:8080/media/music/artist/image/";


    @Autowired
    public ArtistService(ArtistRepository artistRepository, AlbumRepository albumRepository) {
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
        //ArrayList<Environment> environments = environmentRepository.findByIsActiveTrue();
    }

    public boolean isArtistNameUnique(String name) {
        Optional<Artist> opArtist = artistRepository.findByName(name);
        return opArtist.isEmpty();
    }

    public boolean createArtistDirectory(String artistName) {
        File artistDirectory = new File(MUSIC_DIR + artistName);
        return artistDirectory.mkdir();
    }

    public ActionStatus createArtist(String name, String description, MultipartFile photoFile) {
        String aritstFileName = "/artist_photo.jpg";
        ActionStatus status;
        try {
            if (isArtistNameUnique(name)) {
                // create artist directory
                String directoryName = IOUtil.covertToValidName(name);
                boolean creationSuccess = createArtistDirectory(directoryName);
                if (creationSuccess) {
                    photoFile.transferTo(new File(MUSIC_DIR + directoryName + aritstFileName));
                    Artist artist = new Artist(name);
                    artist.setDescription(description);
                    artist.setImagePath("/" + directoryName + aritstFileName);
                    artist.setImageUrl(IMAGE_HOSTNAME + artist.getId());
                    artistRepository.save(artist);
                    status = ActionStatus.SUCCEEDED;
                }
                else {
                    throw new Exception("Error Creating Artist Directory");
                }
            }
            else
                status = ActionStatus.FAILED;
        }
        catch (Exception ex) {
            status = ActionStatus.FAILED;
            ex.printStackTrace();
        }
        return status;
    }

    public Artist getArtistInfo(String id) {
        Optional<Artist> opArtist = artistRepository.findById(id);
        if (opArtist.isPresent()) {
            Artist artist = opArtist.get();
            artist.setImageUrl(IMAGE_HOSTNAME + artist.getId());
            return artist;
        }
        else {
            return null;
        }
    }

    public byte[] getArtistImage(String path) {
        try {
            InputStream inputStream = new FileInputStream(MUSIC_DIR + path);
            return IOUtils.toByteArray(inputStream);
        }
        catch(IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public JsonArray getAllArtist() {
        ArrayList<Artist> artistList =  new ArrayList<>(artistRepository.findAll());
        JsonArrayBuilder artistListBuilder = Json.createArrayBuilder();
        for (Artist artist : artistList) {
            artist.setImageUrl(IMAGE_HOSTNAME + artist.getId());
            artistListBuilder.add(artist.toJsonObject());
        }
        return artistListBuilder.build();
    }

    public ActionStatus deleteArtist(String id) {
        Optional<Artist> opArtist = artistRepository.findById(id);
        if (opArtist.isPresent()) {
            Artist artist = opArtist.get();
            ArrayList<Album> albumList = albumRepository.findByArtistId(id);
            try {
                if (albumList.size() == 0) {
                    artistRepository.delete(artist);
                    File artistDirectory = new File(MUSIC_DIR + IOUtil.covertToValidName(artist.getName()));
                    FileUtils.deleteDirectory(artistDirectory);
                    return ActionStatus.SUCCEEDED;
                }
                else
                    return ActionStatus.FAILED;
            }
            catch (IOException ex) {
                ex.printStackTrace();
                return ActionStatus.FAILED;
            }
        }
        else
            return ActionStatus.FAILED;
    }
}
