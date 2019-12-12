package com.coding.exercise;

import com.coding.exercise.data.Output;
import com.coding.exercise.data.Playlist;
import com.coding.exercise.data.Song;
import com.coding.exercise.data.User;
import com.coding.exercise.data.AddPlaylist;
import com.coding.exercise.data.AddSong;
import com.coding.exercise.data.RemovePlaylist;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

class JsonFileReaderWriter
{
    private final String mixTapeFile;
    private final String changesFile;
    private final String outputFile;
    private final JsonNode mixTapeRootNode;
    private final JsonNode changesRootNode;
    private final ObjectMapper mapper = new ObjectMapper();

    JsonFileReaderWriter(String mixTapeFile, String changesFile, String outputFile) throws Exception
    {
        this.mixTapeFile = mixTapeFile;
        this.changesFile = changesFile;
        this.outputFile = outputFile;

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String mixTapeJson = readFile(mixTapeFile);
        this.mixTapeRootNode = mapper.readTree(mixTapeJson);

        String changesJson = readFile(changesFile);
        changesRootNode = mapper.readTree(changesJson);
    }

    List<User> readUsers() throws IOException
    {
        return mapper.readerFor(new TypeReference<List<User>>() {}).readValue(mixTapeRootNode.get("users"));
    }

    List<Playlist> readPlaylists() throws IOException
    {
        return mapper.readerFor(new TypeReference<List<Playlist>>() {}).readValue(mixTapeRootNode.get("playlists"));
    }

    List<Song> readSongs() throws IOException
    {
        return mapper.readerFor(new TypeReference<List<Song>>() {}).readValue(mixTapeRootNode.get("songs"));
    }

    List<AddSong> readSongsToAdd() throws IOException
    {
        return mapper.readerFor(new TypeReference<List<AddSong>>() {}).readValue(changesRootNode.get("add_song"));
    }

    List<AddPlaylist> readPlaylistsToAdd() throws IOException
    {
        return mapper.readerFor(new TypeReference<List<AddPlaylist>>() {}).readValue(changesRootNode.get("add_playlist"));
    }

    List<RemovePlaylist> readPlaylistsToRemove() throws IOException
    {
        return mapper.readerFor(new TypeReference<List<RemovePlaylist>>() {}).readValue(changesRootNode.get("remove_playlist"));
    }

    void writeOutput(Output output) throws Exception
    {
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputFile), output);
    }

    private String readFile(String filename) throws Exception
    {
        String result = "";
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null)
            {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
        }
        catch(Exception e)
        {
            throw new Exception("Exception occurred while reading file: " + filename, e);
        }
        return result;
    }
}
