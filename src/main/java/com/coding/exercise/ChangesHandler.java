package com.coding.exercise;

import com.coding.exercise.data.Playlist;
import com.coding.exercise.data.AddPlaylist;
import com.coding.exercise.data.AddSong;
import com.coding.exercise.data.RemovePlaylist;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.UUID;

/**
 * Responsible for applying changes to the input. Assumes that the input and changes have been already validated
 */
class ChangesHandler
{
    /**
     * Calls appropriate methods to make the changes
     * @param playlists
     * @param songsToAdd
     * @param playlistsToAdd
     * @param playlistsToRemove
     */
    static void applyChanges(List<Playlist> playlists, List<AddSong> songsToAdd, List<AddPlaylist> playlistsToAdd, List<RemovePlaylist> playlistsToRemove)
    {
        addSongsToPlaylists(playlists, songsToAdd);
        addPlaylists(playlists, playlistsToAdd);
        removePlayLists(playlists, playlistsToRemove);
    }

    /**
     * Iterates over all the elements in songsToAdd, finds the playlist in which the current song should be added and then adds in its songIds list
     */
    private static void addSongsToPlaylists(List<Playlist> playlists, List<AddSong> songsToAdd)
    {
        for(AddSong songToAdd : songsToAdd)
        {
            Playlist playlist = playlists.stream().filter(pl -> songToAdd.getPlaylistId().equals(pl.getId())).findAny().orElse(null);
            if(playlist != null)
            {
                List<String> songs = playlist.getSongIds();
                songs.add(songToAdd.getSongId());
            }
        }
    }

    /**
     * Iterates over all the elements in playlistsToAdd and adds it to playlists. If any element in playlistsToAdd does not have an id, a random UUID is generated and set as the
     * id
     * @param playlists
     * @param playlistsToAdd
     */
    private static void addPlaylists(List<Playlist> playlists, List<AddPlaylist> playlistsToAdd)
    {
        for(AddPlaylist playlistToAdd : playlistsToAdd)
        {
            if(StringUtils.isBlank(playlistToAdd.getPlaylistId()))
            {
                playlists.add(new Playlist(UUID.randomUUID().toString(), playlistToAdd.getUserId(), playlistToAdd.getSongIds()));
            }
            else
            {
                playlists.add(new Playlist(playlistToAdd.getPlaylistId(), playlistToAdd.getUserId(), playlistToAdd.getSongIds()));
            }

        }
    }

    /**
     * Iterates over all the elements in playlistsToRemove and removes the corresponding playlist
     * @param playlists
     * @param playlistsToRemove
     */
    private static void removePlayLists(List<Playlist> playlists, List<RemovePlaylist> playlistsToRemove)
    {
        for(RemovePlaylist playlistToRemove : playlistsToRemove)
        {
            playlists.removeIf(playlist -> playlist.getId().equals(playlistToRemove.getPlaylistId()));
        }
    }
}
