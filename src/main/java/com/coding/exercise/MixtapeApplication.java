package com.coding.exercise;

import com.coding.exercise.data.Output;
import com.coding.exercise.data.Playlist;
import com.coding.exercise.data.Song;
import com.coding.exercise.data.User;
import com.coding.exercise.data.AddPlaylist;
import com.coding.exercise.data.AddSong;
import com.coding.exercise.data.RemovePlaylist;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

/**
 * Entrypoint of the entire application. The main method controls the execution flow
 */
public class MixtapeApplication
{
    public static void main(String[] args) throws Exception
    {
        if(args.length != 3)
        {
            throw new IllegalArgumentException("3 arguments (mixtape.json, changes,json and output.json) must be passed in to the application");
        }

        // If the app is running in a Docker environment then it expects the JSON files to be in "/app/data" on the container (local/data on the host machine)
        String inputFileLocation = "";
        if(isRunningInsideDocker())
        {
            inputFileLocation = "/app/data/";
        }
        else
        {
            // If the app is not running in a Docker environment then it expects the JSON files to be one directory up from its current location on the host machine
            inputFileLocation = System.getProperty("user.dir") + "/../";
        }

        // Utility class to read from the input files and write to output file
        JsonFileReaderWriter jsonReaderWriter = new JsonFileReaderWriter(inputFileLocation + args[0], inputFileLocation + args[1], inputFileLocation + args[2]);

        // Get users, playlists and songs
        List<User> users = jsonReaderWriter.readUsers();
        List<Playlist> playlists = jsonReaderWriter.readPlaylists();
        List<Song> songs = jsonReaderWriter.readSongs();

        // Get changes to be made to the input
        List<AddSong> songsToAdd = jsonReaderWriter.readSongsToAdd();
        List<AddPlaylist> playlistsToAdd = jsonReaderWriter.readPlaylistsToAdd();
        List<RemovePlaylist> playlistsToRemove = jsonReaderWriter.readPlaylistsToRemove();

        // Validate the changes. If the change file is invalid, the entire operation is aborted. Also, all the invalid changes are sent back to the client
        ChangesValidator.validateChanges(users, playlists, songs, songsToAdd, playlistsToAdd, playlistsToRemove);

        // Make the changes!
        ChangesHandler.applyChanges(playlists, songsToAdd, playlistsToAdd, playlistsToRemove);

        // Write JSON output to output file
        jsonReaderWriter.writeOutput(new Output(users, playlists, songs));
    }

    private static Boolean isRunningInsideDocker() {

        try (Stream<String> stream = Files.lines(Paths.get("/proc/1/cgroup")))
        {
            return stream.anyMatch(line -> line.contains("/docker"));
        }
        catch (IOException e) {
            return false;
        }
    }
}
