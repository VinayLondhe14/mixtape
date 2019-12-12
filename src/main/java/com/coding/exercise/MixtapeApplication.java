package com.coding.exercise;

import com.coding.exercise.data.Output;
import com.coding.exercise.data.Playlist;
import com.coding.exercise.data.Song;
import com.coding.exercise.data.User;
import com.coding.exercise.data.changes.AddPlaylist;
import com.coding.exercise.data.changes.AddSong;
import com.coding.exercise.data.changes.RemovePlaylist;

import java.util.List;

public class MixtapeApplication
{
    public static void main(String[] args) throws Exception
    {
        if(args.length != 3)
        {
            throw new IllegalArgumentException("3 arguments (mixtape.json, changes,json and output.json) must be passed in to the application");
        }

        String inputFileLocation = System.getProperty("user.dir") + "/../";
        JsonFileReaderWriter jsonReaderWriter = new JsonFileReaderWriter(inputFileLocation + args[0], inputFileLocation + args[1], inputFileLocation + args[2]);

        List<User> users = jsonReaderWriter.readUsers();
        List<Playlist> playlists = jsonReaderWriter.readPlaylists();
        List<Song> songs = jsonReaderWriter.readSongs();

        List<AddSong> songsToAdd = jsonReaderWriter.readSongsToAdd();
        List<AddPlaylist> playlistsToAdd = jsonReaderWriter.readPlaylistsToAdd();
        List<RemovePlaylist> playlistsToRemove = jsonReaderWriter.readPlaylistsToRemove();

        ChangesValidator.validateChanges(users, playlists, songs, songsToAdd, playlistsToAdd, playlistsToRemove);

        ChangesHandler.addSongsToPlaylists(playlists, songsToAdd);
        ChangesHandler.addPlaylists(playlists, playlistsToAdd);
        ChangesHandler.removePlayLists(playlists, playlistsToRemove);

        jsonReaderWriter.writeOutput(new Output(users, playlists, songs));
    }
}
