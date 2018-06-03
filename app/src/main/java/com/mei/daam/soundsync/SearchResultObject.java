package com.mei.daam.soundsync;

import com.google.api.services.youtube.model.SearchResultSnippet;

public class SearchResultObject {

    private String videoId;
    private String eTag;
    private SearchResultSnippet searchResultSnippet;

    public SearchResultObject(String videoId, String eTag, SearchResultSnippet searchResultSnippet){

        this.videoId = videoId;
        this.eTag = eTag;
        this.searchResultSnippet = searchResultSnippet;
    }

    public String getVideoId() {
        return videoId;
    }

    public String geteTag() {
        return eTag;
    }

    public SearchResultSnippet getSearchResultSnippet() {
        return searchResultSnippet;
    }
}
