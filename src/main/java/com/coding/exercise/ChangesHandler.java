package com.coding.exercise;

import com.coding.exercise.data.Playlist;
import com.coding.exercise.data.changes.AddPlaylist;
import com.coding.exercise.data.changes.AddSong;
import com.coding.exercise.data.changes.RemovePlaylist;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.UUID;

class ChangesHandler
{
    static void addSongsToPlaylists(List<Playlist> playlists, List<AddSong> songsToAdd)
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

    static void addPlaylists(List<Playlist> playlists, List<AddPlaylist> playlistsToAdd)
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

    static void removePlayLists(List<Playlist> playlists, List<RemovePlaylist> playlistsToRemove)
    {
        for(RemovePlaylist playlistToRemove : playlistsToRemove)
        {
            playlists.removeIf(playlist -> playlist.getId().equals(playlistToRemove.getPlaylistId()));
        }
    }
}
