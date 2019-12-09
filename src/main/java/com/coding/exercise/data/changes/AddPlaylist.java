package com.coding.exercise.data.changes;

import java.util.List;

public class AddPlaylist
{
    private String id;
    private String userId;
    private List<String> songIds;

    public AddPlaylist()
    {
    }

    public AddPlaylist(String id, String userId, List<String> songIds)
    {
        this.id = id;
        this.userId = userId;
        this.songIds = songIds;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public List<String> getSongIds()
    {
        return songIds;
    }

    public void setSongIds(List<String> songIds)
    {
        this.songIds = songIds;
    }
}
