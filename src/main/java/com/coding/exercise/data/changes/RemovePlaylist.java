package com.coding.exercise.data.changes;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RemovePlaylist
{
    private String playlistId;

    public RemovePlaylist()
    {
    }

    public String getPlaylistId()
    {
        return playlistId;
    }
}
