package com.coding.exercise.data.changes;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AddSong
{
    private String songId;
    private String playlistId;

    public AddSong()
    {
    }

    public AddSong(String songId, String playlistId)
    {
        this.songId = songId;
        this.playlistId = playlistId;
    }

    public String getSongId()
    {
        return songId;
    }

    public void setSongId(String songId)
    {
        this.songId = songId;
    }

    public String getPlaylistId()
    {
        return playlistId;
    }

    public void setPlaylistId(String playlistId)
    {
        this.playlistId = playlistId;
    }
}
