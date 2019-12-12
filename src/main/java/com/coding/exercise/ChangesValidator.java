package com.coding.exercise;

import com.coding.exercise.data.Playlist;
import com.coding.exercise.data.Song;
import com.coding.exercise.data.User;
import com.coding.exercise.data.changes.AddPlaylist;
import com.coding.exercise.data.changes.AddSong;
import com.coding.exercise.data.changes.RemovePlaylist;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class ChangesValidator
{
    private static final Set<String> errors = new HashSet<>();

    static void validateChanges(List<User> users, List<Playlist> playlists, List<Song> songs,
        List<AddSong> songsToAdd, List<AddPlaylist> playlistsToAdd, List<RemovePlaylist> playlistsToRemove)
    {
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
