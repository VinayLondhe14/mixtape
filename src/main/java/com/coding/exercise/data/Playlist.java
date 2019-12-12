package com.coding.exercise.data;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

/**
 * POJO for JSON mapping of input playlists
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Playlist
{
    private String id;
    private String userId;
    private List<String> songIds;

    public Playlist()
    {
    }

    public Playlist(String id, String userId, List<String> songIds)
    {
        this.id = id;
        this.userId = userId;
        this.songIds = songIds;
    }

    public String getId()
    {
        return id;
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
