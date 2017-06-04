package com.example.shagil.ninjalike.data;

/**
 * Created by Siddiqui on 6/2/2017.
 */

public class FeedItem {
    private int id;
    private String name,status;

    public FeedItem() {
    }

    public FeedItem(int id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
