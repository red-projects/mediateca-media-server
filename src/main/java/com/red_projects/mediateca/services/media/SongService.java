package com.red_projects.mediateca.services.media;

import com.red_projects.mediateca.entities.media.Album;
import com.red_projects.mediateca.entities.media.Artist;
import com.red_projects.mediateca.entities.media.Song;
import com.red_projects.mediateca.repositories.AlbumRepository;
import com.red_projects.mediateca.repositories.ArtistRepository;
import com.red_projects.mediateca.repositories.SongRepository;
import com.red_projects.mediateca.utils.ActionStatus;
import com.red_projects.mediateca.utils.io.IOUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SongService {

    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final String MUSIC_DIR = "H:/RedProjects/test/media/music/";
    private final String FILE_HOSTNAME = "http://localhost:8080/media/music/song/stream/";

    @Autowired
    public SongService(SongRepository songRepository,
                       AlbumRepository albumRepository, ArtistRepository artistRepository) {
        this.songRepository = songRepository;
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
    }

    private String numberToString(int number) {
        if (number < 10) {
            return "0" + number;
        }
        return Integer.toString(number);
    }

    private String getAlbumName(String albumId) throws NoSuchElementException {
        Optional<Album> opAlbum = albumRepository.findById(albumId);
        return opAlbum.get().getTitle();

    }

    private String getArtistName(String artistId) throws NoSuchElementException {
        Optional<Artist> opArtist = artistRepository.findById(artistId);
        return opArtist.get().getName();

    }

    private boolean isAlbumSongUnique(String title, String albumId) {
        ArrayList<Song> songList = songRepository.findByTitleAndAlbumId(title, albumId);
        return songList.size() == 0;
    }

    public ActionStatus createSong(String title, String artistId, String albumId,
                                   int trackNum, String genre, MultipartFile file) {
        ActionStatus status;
        try {
            String artistName = getArtistName(artistId);
            String albumName = getAlbumName(albumId);
            if (isAlbumSongUnique(title, albumId)) {
                String albumDirectory = IOUtil.covertToValidName(albumName);
                String artistDirectory = IOUtil.covertToValidName(artistName);
                String fileName = numberToString(trackNum) + "_" +  title + ".mp3";
                albumDirectory = artistDirectory + "/" + albumDirectory + "/";
                file.transferTo(new File(MUSIC_DIR + albumDirectory + fileName));
                Song song = new Song(title);
                song.setArtistId(artistId);
                song.setAlbumId(albumId);
                song.setTrackNumber(trackNum);
                song.setGenre(genre);
                song.setFilePath("/" + albumDirectory + fileName);
                song.setFileUrl(FILE_HOSTNAME + song.getId());
                songRepository.save(song);
                status = ActionStatus.SUCCEEDED;
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

    public Song getSongInfo(String id) {
        Optional<Song> opSong = songRepository.findById(id);
        if (opSong.isPresent()) {
            Song song = opSong.get();
            song.setFileUrl(FILE_HOSTNAME + song.getId());
            return song;
        }
        else {
            return null;
        }
    }

    public byte[] getSongFile(String path) {
        try {
            InputStream inputStream = new FileInputStream(MUSIC_DIR + path);
            return IOUtils.toByteArray(inputStream);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public JsonArray getAllSongs() {
        ArrayList<Song> songList =  new ArrayList<>(songRepository.findAll());
        JsonArrayBuilder songListBuilder = Json.createArrayBuilder();
        for (Song song : songList) {
            song.setFileUrl(FILE_HOSTNAME + song.getId());
            songListBuilder.add(song.toJsonObject());
        }
        return songListBuilder.build();
    }

    public JsonArray getAllArtistSongs(String artistId) {
        ArrayList<Song> songList = new ArrayList<>(songRepository.findByArtistId(artistId));
        JsonArrayBuilder songListBuilder = Json.createArrayBuilder();
        for (Song song: songList) {
            song.setFileUrl(FILE_HOSTNAME + song.getId());
            songListBuilder.add(song.toJsonObject());
        }
        return songListBuilder.build();
    }

    public JsonArray getAllAlbumSongs(String albumId) {
        ArrayList<Song> songList = new ArrayList<>(songRepository.findByAlbumId(albumId));
        JsonArrayBuilder songListBuilder = Json.createArrayBuilder();
        for (Song song: songList) {
            song.setFileUrl(FILE_HOSTNAME + song.getId());
            songListBuilder.add(song.toJsonObject());
        }
        return songListBuilder.build();
    }

    public ActionStatus deleteSong(String id) {
        Optional<Song> opSong = songRepository.findById(id);
        if (opSong.isPresent()) {
            Song song = opSong.get();
            File songFile = new File(MUSIC_DIR + song.getFilePath());
            if (songFile.delete()) {
                songRepository.delete(opSong.get());
            }
            else {
                System.out.println("ERROR: Encountered Error Deleting Song File: " + song.getFilePath());
            }
            return ActionStatus.SUCCEEDED;
        }
        else
            return ActionStatus.FAILED;
    }

    public ResponseEntity<byte[]> getContent(String id, String range, String contentTypePrefix) {
        long rangeStart = 0;
        long rangeEnd;
        byte[] data;
        Long fileSize;
        Song song = getSongInfo(id);
        if (song != null) {
            data = getSongFile(song.getFilePath());
            fileSize = (long) data.length;
            if (range == null) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .header("Content-Type", contentTypePrefix + "/mp3")
                        .header("Content-Length", String.valueOf(fileSize))
                        .body(readByteRange(data, rangeStart, fileSize - 1));
            }
            String[] ranges = range.split("-");
            rangeStart = Long.parseLong(ranges[0].substring(6));
            if (ranges.length > 1) {
                rangeEnd = Long.parseLong(ranges[1]);
            } else {
                rangeEnd = fileSize - 1;
            }
            if (fileSize < rangeEnd) {
                rangeEnd = fileSize - 1;
            }
            data = readByteRange(data, rangeStart, rangeEnd);
            String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .header("Content-Type", contentTypePrefix + "/mp3")
                    .header("Accept-Ranges", "bytes")
                    .header("Content-Range", "bytes" + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
                    .body(data);
        }
        return null;
    }

    private byte[] readByteRange(byte[] file, long start, long end) {
        byte[] fileRange = new byte[(int) (end - start) + 1];
        System.arraycopy(file, (int) start, fileRange, 0, fileRange.length);
        return fileRange;
    }

}
