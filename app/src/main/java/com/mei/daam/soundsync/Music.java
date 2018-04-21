package com.mei.daam.soundsync;

/**
 * Created by D01 on 21/04/2018.
 */

public class Music {

    private String name;
    private String duration;
    private String thumbnail;
    private String videoId;

    public Music (String name, String duration, String thumbnail, String videoId){

        this.name = name;
        this.duration = duration;
        this.thumbnail = thumbnail;
        this.videoId = videoId;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }
}
