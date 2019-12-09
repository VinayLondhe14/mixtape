package com.coding.exercise.data.changes;

public class RemovePlaylist
{
    private String id;

    public RemovePlaylist()
    {
    }

    public RemovePlaylist(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
