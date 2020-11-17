package com.example.android.forplaymusic;

public class SongFile {
    private String songName;
    private int songResourceId;

    public SongFile(String name, int id) {
        songName = name;
        songResourceId = id;
    }

    public String getSongName() {
        return songName;
    }

    public int getSongResourceId() {
        return songResourceId;
    }
}
