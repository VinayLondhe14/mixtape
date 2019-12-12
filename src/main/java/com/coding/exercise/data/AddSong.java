package com.coding.exercise.data;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * POJO for JSON mapping of add_song changes
 */
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
