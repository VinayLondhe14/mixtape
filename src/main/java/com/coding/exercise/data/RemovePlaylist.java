package com.coding.exercise.data;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * POJO for JSON mapping of remove_playlist changes
 */
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
