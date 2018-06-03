package com.mei.daam.soundsync;

import java.io.Serializable;

public class Group implements Serializable {

    private String name;
    private CustomAdapter musicList;

    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public CustomAdapter getMusicList() {
        return musicList;
    }

    public void setMusicList(CustomAdapter musicList) {
        this.musicList = musicList;
    }
}
