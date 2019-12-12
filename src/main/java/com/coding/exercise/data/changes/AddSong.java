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

    public String getSongId()
    {
        return songId;
    }

    public String getPlaylistId()
    {
        return playlistId;
    }
}
