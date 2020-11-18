package com.red_projects.mediateca.services.media;

import com.red_projects.mediateca.entities.media.Album;
import com.red_projects.mediateca.entities.media.Artist;
import com.red_projects.mediateca.entities.media.Song;
import com.red_projects.mediateca.repositories.AlbumRepository;
import com.red_projects.mediateca.repositories.ArtistRepository;
import com.red_projects.mediateca.repositories.SongRepository;
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
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AlbumService {


    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;
    private final String MUSIC_DIR = "H:/RedProjects/test/media/music/";
    private final String IMAGE_HOSTNAME = "http://localhost:8080/media/music/album/image/";


    @Autowired
    public AlbumService(AlbumRepository albumRepository, ArtistRepository artistRepository, SongRepository songRepository) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.songRepository = songRepository;

    }

    private String getArtistName(String artistId) throws NoSuchElementException {
        Optional<Artist> opArtist = artistRepository.findById(artistId);
        return opArtist.get().getName();

    }

    private boolean isArtistAlbumUnique(String title, String artistId) {
        ArrayList<Album> albumList = albumRepository.findByTitleAndArtistId(title, artistId);
        return albumList.size() == 0;
    }

    private boolean createAlbumDirectory(String directory) {
        File albumDirectory = new File(MUSIC_DIR + directory);
        return albumDirectory.mkdir();
    }

    public ActionStatus createAlbum(String title, String artistId, int numTracks, int year, MultipartFile photoFile) {
        String albumFileName = "/album_photo.jpg";
        ActionStatus status;
        try {
            String artistName = getArtistName(artistId);
            if (isArtistAlbumUnique(title, artistId)) {
                String albumDirectoryName = IOUtil.covertToValidName(title);
                String artistDirectoryName = IOUtil.covertToValidName(artistName);
                albumDirectoryName = artistDirectoryName + "/" + albumDirectoryName;
                boolean creationSuccess = createAlbumDirectory(albumDirectoryName); // add check
                if (creationSuccess) {
                    photoFile.transferTo(new File(MUSIC_DIR + albumDirectoryName + albumFileName));
                    Album album = new Album(title);
                    album.setArtistId(artistId);
                    album.setNumberOfTracks(numTracks);
                    album.setReleaseYear(year);
                    album.setImagePath("/" + albumDirectoryName + albumFileName);
                    album.setImageUrl(IMAGE_HOSTNAME + album.getId());
                    albumRepository.save(album);
                    status = ActionStatus.SUCCEEDED;
                }
                else {
                    throw new Exception("Error Creating Album Directory");
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

    public Album getAlbumInfo(String id) {
        Optional<Album> opAlbum = albumRepository.findById(id);
        if (opAlbum.isPresent()) {
            Album album = opAlbum.get();
            album.setImageUrl(IMAGE_HOSTNAME + album.getId());
            return album;
        }
        else {
            return null;
        }
    }

    public byte[] getAlbumImage(String path) {
        try {
            InputStream inputStream = new FileInputStream(MUSIC_DIR + path);
            return IOUtils.toByteArray(inputStream);
        }
        catch(IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public JsonArray getAllAlbums() {
        ArrayList<Album> albumList =  new ArrayList<>(albumRepository.findAll());
        JsonArrayBuilder albumListBuilder = Json.createArrayBuilder();
        for (Album album : albumList) {
            album.setImageUrl(IMAGE_HOSTNAME + album.getId());
            albumListBuilder.add(album.toJsonObject());
        }
        return albumListBuilder.build();
    }

    public JsonArray getAllArtistAlbums(String artistId) {
        ArrayList<Album> albumList = albumRepository.findByArtistId(artistId);
        JsonArrayBuilder albumListBuilder = Json.createArrayBuilder();
        for (Album album : albumList) {
            album.setImageUrl(IMAGE_HOSTNAME + album.getId());
            albumListBuilder.add(album.toJsonObject());
        }
        return albumListBuilder.build();
    }

    public ActionStatus deleteAlbum(String id) {
        Optional<Album> opAlbum = albumRepository.findById(id);
        if (opAlbum.isPresent()) {
            Album album = opAlbum.get();
            Optional<Artist> opArtist = artistRepository.findById(album.getArtistId());
            if (opArtist.isPresent()) {
                Artist artist = opArtist.get();
                ArrayList<Song> songList = songRepository.findByAlbumId(id);
                try {
                    if (songList.size() == 0) {
                        albumRepository.delete(album);
                        String albumPath =  IOUtil.covertToValidName(artist.getName()) + "/" + IOUtil.covertToValidName(album.getTitle());
                        File albumDirectory = new File(MUSIC_DIR + albumPath);
                        FileUtils.deleteDirectory(albumDirectory);
                        return ActionStatus.SUCCEEDED;
                    } else
                        return ActionStatus.FAILED;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return ActionStatus.FAILED;
                }
            }
            else
                return ActionStatus.FAILED;

        } else
            return ActionStatus.FAILED;
    }
}
