package com.coding.exercise;

import com.coding.exercise.data.Output;
import com.coding.exercise.data.Playlist;
import com.coding.exercise.data.Song;
import com.coding.exercise.data.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class MixtapeApplication
{
    private static ObjectMapper mapper = new ObjectMapper();
    public static void main(String[] args) throws Exception
    {
        String mixTapeJson = readFile(System.getProperty("user.dir") + "/../" + args[0]);
        JsonNode rootNode = mapper.readTree(mixTapeJson);
        List<User> users = mapper.readerFor(new TypeReference<List<User>>() {}).readValue(rootNode.get("users"));
        List<Playlist> playlists = mapper.readerFor(new TypeReference<List<Playlist>>() {}).readValue(rootNode.get("playlists"));
        List<Song> songs = mapper.readerFor(new TypeReference<List<Song>>() {}).readValue(rootNode.get("songs"));

        mapper.writeValue(new File(System.getProperty("user.dir") + "/../" + args[1]), new Output(users, playlists, songs));

        System.out.println(users.toString());
    }

    public static String readFile(String filename) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
