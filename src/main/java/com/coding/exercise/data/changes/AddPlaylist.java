package com.coding.exercise.data.changes;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AddPlaylist
{
    private String playlistId;
    private String userId;
    private List<String> songIds;

    public AddPlaylist()
    {
    }

    public String getPlaylistId()
    {
        return playlistId;
    }

    public String getUserId()
    {
        return userId;
    }

    public List<String> getSongIds()
    {
        return songIds;
    }
}
