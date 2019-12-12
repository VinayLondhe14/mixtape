package com.coding.exercise;

import com.coding.exercise.data.Playlist;
import com.coding.exercise.data.Song;
import com.coding.exercise.data.User;
import com.coding.exercise.data.AddPlaylist;
import com.coding.exercise.data.AddSong;
import com.coding.exercise.data.RemovePlaylist;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Responsible for validating changes file.
 */
class ChangesValidator
{
    private static final Set<String> errors = new HashSet<>();

    /**
     * Calls appropriate methods to validate the changes. Returns an empty set if changes are valid. Otherwise throws an IllegalArgumentException with all the violations
     * @param users
     * @param playlists
     * @param songs
     * @param songsToAdd
     * @param playlistsToAdd
     * @param playlistsToRemove
     */
    static void validateChanges(List<User> users, List<Playlist> playlists, List<Song> songs,
        List<AddSong> songsToAdd, List<AddPlaylist> playlistsToAdd, List<RemovePlaylist> playlistsToRemove)
    {
        // Sets of userIds, playlistIds and songIds so that we can easily locate them when needed for validations
        Set<String> userIds = users.stream().map(User::getId).collect(Collectors.toSet());
        Set<String> playlistIds = playlists.stream().map(Playlist::getId).collect(Collectors.toSet());
        Set<String> songIds = songs.stream().map(Song::getId).collect(Collectors.toSet());

        validateSongsToAdd(songsToAdd, songIds, playlistIds, playlists);
        validatePlaylistsToAdd(playlistsToAdd, userIds, songIds, playlistIds);
        validatePlaylistsToRemove(playlistsToRemove, playlistIds);

        if(errors.size() > 0)
        {
            throw new IllegalArgumentException(String.join("\n", errors));
        }
    }

    /**
     * Iterates over songsToAdd and validates the following
     * 1) songToAdd does not have an empty songId
     * 2) songToAdd does not have an empty playlistId
     * 3) songToAdd has songId which already exists
     * 4) songToAdd has playlistId which already exists
     * 5) songToAdd does not add a songId which already exists in playlist's songIds
     * @param songsToAdd
     * @param songIds
     * @param playlistIds
     * @param playlists
     */
    private static void validateSongsToAdd(List<AddSong> songsToAdd, Set<String> songIds, Set<String> playlistIds, List<Playlist> playlists)
    {
        for(AddSong songToAdd : songsToAdd)
        {
            if(StringUtils.isBlank(songToAdd.getSongId()))
            {
                errors.add("song_id cannot be empty while adding song to playlist");
            }
            else if(StringUtils.isBlank(songToAdd.getPlaylistId()))
            {
                errors.add("playlist_id cannot be empty while adding song to playlist");
            }
            else
            {
                if(!songIds.contains(songToAdd.getSongId()))
                {
                    errors.add("song with id " + songToAdd.getSongId() + " cannot be added to playlist with id " + songToAdd.getPlaylistId() + " because the song does not exist");
                }

                if(!playlistIds.contains(songToAdd.getPlaylistId()))
                {
                    errors.add("song with id " + songToAdd.getSongId() + " cannot be added to playlist with id " + songToAdd.getPlaylistId() + " because the playlist does not exist");
                }
                else
                {
                    checkIfSongAlreadyExistsInPlaylist(songToAdd, playlists);
                }
            }
        }
    }

    private static void checkIfSongAlreadyExistsInPlaylist(AddSong songToAdd, List<Playlist> playlists)
    {
        Playlist playlist = playlists.stream().filter(pl -> songToAdd.getPlaylistId().equals(pl.getId())).findAny().orElse(null);
        if(playlist != null && playlist.getSongIds().stream().anyMatch(s -> s.equals(songToAdd.getSongId())))
        {
            errors.add("song with id " + songToAdd.getSongId() + " is already present in playlist with id " + playlist.getId());
        }
    }

    /**
     * Iterates over playlistsToAdd and validates the following
     * 1) playlistToAdd has a unique playlistId if it it set
     * 2) playlistToAdd does not have an empty userId
     * 3) playlistToAdd has userId which already exists
     * 4) playlistToAdd does not have null or empty songIds
     * 5) playlistToAdd does not have duplicate songIds
     * 6) playlistToAdd does not have empty song in songIds
     * 7) playListToAdd has song in songIds which already exists
     * @param playlistsToAdd
     * @param userIds
     * @param songIds
     * @param playlistIds
     */
    private static void validatePlaylistsToAdd(List<AddPlaylist> playlistsToAdd, Set<String> userIds, Set<String> songIds, Set<String> playlistIds)
    {
        for(AddPlaylist playlistToAdd : playlistsToAdd)
        {
            if(!StringUtils.isBlank(playlistToAdd.getPlaylistId()) && playlistIds.contains(playlistToAdd.getPlaylistId()))
            {
                errors.add("cannot create new playlist because playlist id " + playlistToAdd.getPlaylistId() + " already exists");
            }

            if(StringUtils.isBlank(playlistToAdd.getUserId()))
            {
                errors.add("user_id cannot be empty while creating new playlist");
            }
            else if(!userIds.contains(playlistToAdd.getUserId()))
            {
                errors.add("playlist with user " + playlistToAdd.getUserId() + " cannot be created because the user does not exist");
            }

            List<String> songsToAddInPlaylist = playlistToAdd.getSongIds();
            if(songsToAddInPlaylist == null)
            {
                errors.add("song_ids cannot be null while creating new playlist");
            }
            else if(songsToAddInPlaylist.size() == 0)
            {
                errors.add("song_ids cannot be empty while creating new playlist");
            }
            else
            {
                if(new HashSet<>(songsToAddInPlaylist).size() < songsToAddInPlaylist.size())
                {
                    errors.add("song_ids cannot have duplicates creating adding new playlist");
                }

                for(String song : songsToAddInPlaylist)
                {
                    if(StringUtils.isBlank(song))
                    {
                        errors.add("song id in songs ids list cannot be empty while creating new playlist");
                    }
                    else if(!songIds.contains(song))
                    {
                        errors.add("song id " + song + " not found while creating new playlist");
                    }
                }
            }
        }
    }

    /**
     * Iterates over playlistsToRemove and validates the following
     * 1) playlistToRemove does not have an empty playlistId
     * 2) playlistToRemove has playlistId which already exists
     * @param playlistsToRemove
     * @param playlistIds
     */
    private static void validatePlaylistsToRemove(List<RemovePlaylist> playlistsToRemove, Set<String> playlistIds)
    {
        for(RemovePlaylist playlistToRemove : playlistsToRemove)
        {
            if(StringUtils.isBlank(playlistToRemove.getPlaylistId()))
            {
                errors.add("id cannot be empty while removing playlist");
            }
            else if(!playlistIds.contains(playlistToRemove.getPlaylistId()))
            {
                errors.add("playlist with id " + playlistToRemove.getPlaylistId() + " cannot be removed because the playlist does not exist");
            }
        }
    }
}
