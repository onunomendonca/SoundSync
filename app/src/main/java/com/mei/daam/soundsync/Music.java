package com.mei.daam.soundsync;

/**
 * Created by D01 on 21/04/2018.
 */

public class Music {

    private String name;
    private String channelTitle;
    private String thumbnail;
    private String videoId;

    public Music (String name, String channelTitle, String thumbnail, String videoId){

        this.name = name;
        this.channelTitle = channelTitle;
        this.thumbnail = thumbnail;
        this.videoId = videoId;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public String getName() {
        return name;
    }

    public Music(){}

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
