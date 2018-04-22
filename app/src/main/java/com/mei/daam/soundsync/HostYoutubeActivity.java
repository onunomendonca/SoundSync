package com.mei.daam.soundsync;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static io.reactivex.subjects.PublishSubject.create;

/**
 * Created by D01 on 26/03/2018.
 */

public class HostYoutubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener { //Implements Listeners here
    //TODO
    private final static String YOUTUBEKEY = "AIzaSyAXxFYaRA0v0YGRwhaVX45E-pnWMzOmn44";
    private final static String SEARCHTYPE = "video";
    private final static String DEFAULTERRORMESSAGE = "Error initializing youtube";
    private YouTubePlayerView youTubePlayerView;
    private PublishSubject<SearchResultObject> searchResultSubject;
    private PublishSubject<String> musicSearchSubject;
    private YouTubePlayer m_youTubePlayer;
    private String searchedMusic;
    private ListView listView;
    private SearchResultObject searchResultObject;
    private YouTube youtube;
    private Button addButton;
    private boolean stopped = false;
    private CustomAdapter listAdapter;
    private String currentVideoId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_youtube);

        getWindow().setBackgroundDrawableResource(R.drawable.first_layer);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(R.drawable.first_layer);
        }

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.GROUP_NAME);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        listView = (ListView) findViewById(R.id.list_view_host);
        listAdapter = new CustomAdapter(HostYoutubeActivity.this);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                loadVideo(listAdapter.getVideoId(position));
            }
        });
        // test.add("This");
        // test.add("is");
        // test.add("just");
        //test.add("a test");
        // test.add("scroll");
        // test.add("scroll");
        // test.add("scroll");
        //test.add("scroll");
        //test.add("scroll");
        //test.add("scroll");
        //test.add("scroll");


        searchResultSubject = create();
        musicSearchSubject = create();
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        //noVideoImage = (ImageView) findViewById(R.id.no_video_img);
        addButton = (Button) findViewById(R.id.add_button_host);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HostYoutubeActivity.this)
                        .setTitle("Add new song");
                final EditText input = new EditText(HostYoutubeActivity.this);
                input.setInputType((InputType.TYPE_CLASS_TEXT));
                builder.setView(input);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String inputWord = input.getText().toString();
                        if (inputWord.equals("")) {
                            Toast.makeText(HostYoutubeActivity.this, "Empty Input", Toast.LENGTH_SHORT).show();
                        } else {
                            musicSearchSubject.onNext(inputWord);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
        resultFromMusicSearch();
        resultFromSearchResult();
    }

    private class TestSearch extends AsyncTask<Void, Void, SearchResultObject> {

        @Override
        protected SearchResultObject doInBackground(Void... voids) {
            SearchResultObject m_searchResultObject = null;
            try {
                youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                    public void initialize(HttpRequest request) throws IOException {
                    }
                }).build();

                YouTube.Search.List search = youtube.search().list("id,snippet");
                search.setKey(YOUTUBEKEY);
                search.setQ(searchedMusic);
                search.setType(SEARCHTYPE);
                search.setFields("items(id/videoId,snippet/title,snippet/thumbnails/default/url, snippet/channelTitle),nextPageToken");
                search.setMaxResults((long) 3);

                // Call the API and print results.
                SearchListResponse searchResponse = search.execute();
                List<SearchResult> searchResultList = searchResponse.getItems();
                if (!searchResultList.isEmpty()) {
                    SearchResult result = searchResultList.get(0);
                    String videoId = result.getId().getVideoId();
                    String eTag = result.getEtag();
                    SearchResultSnippet searchResultSnippet = result.getSnippet();
                    String name = searchResultSnippet.getTitle();
                    String channelTitle=searchResultSnippet.getChannelTitle();
                    //getVideoDetails
                    /*YouTube.Videos.List videoRequest = youtube.videos().list("snippet, recordingDetails").setId(videoId);
                    VideoListResponse video = videoRequest.execute();
                    List<Video> videos = video.getItems();
                    String duration = videos.get(0).getContentDetails().getDuration();*/
                    if (!listAdapter.hasVideoId(videoId)) {
                        Music music = new Music(name, channelTitle, "https://img.youtube.com/vi/" + videoId + "/0.jpg", videoId);
                        listAdapter.addMusic(music);
                        m_searchResultObject = new SearchResultObject(videoId, eTag, searchResultSnippet);
                    } else {
                        m_searchResultObject = new SearchResultObject("-1", "-1", null);
                    }

                    //test.add(name);
                    Log.d("TAG123", "first result " + videoId);
                    Log.d("TAG123", "e-tag " + eTag);
                    Log.d("TAG123", "snippet " + searchResultSnippet);
                }

            } catch (GoogleJsonResponseException e) {
                Log.d("TAG123", "There was a service error: " + e.getDetails().getCode() + " : "
                        + e.getDetails().getMessage());
            } catch (IOException e) {
                Log.d("TAG123", "There was an IO error: " + e.getCause() + " : " + e.getMessage());
            } catch (Throwable t) {
                Log.d("TAG123", "stack " + t.toString());
            }
            return m_searchResultObject;
        }

        @Override
        protected void onPostExecute(SearchResultObject s) {
            if (s == null) {
                Toast.makeText(HostYoutubeActivity.this, "No Result found", Toast.LENGTH_LONG).show();
            } else {
                searchResultSubject.onNext(s);
            }
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean restored) {
        if (!restored) {
            //  noVideoImage.setVisibility(View.GONE);
            youTubePlayerView.setVisibility(View.VISIBLE);
            m_youTubePlayer = youTubePlayer;
            loadVideo(searchResultObject.getVideoId());
            m_youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                @Override
                public void onPlaying() {
                    stopped = false;
                    Log.d("TAG123", "HERE");
                }

                @Override
                public void onPaused() {

                }

                @Override
                public void onStopped() {

                }

                @Override
                public void onBuffering(boolean b) {

                }

                @Override
                public void onSeekTo(int i) {

                }
            });
            m_youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                @Override
                public void onLoading() {

                }

                @Override
                public void onLoaded(String s) {

                }

                @Override
                public void onAdStarted() {

                }

                @Override
                public void onVideoStarted() {
                    stopped = false;
                }

                @Override
                public void onVideoEnded() {
                    Log.d("TAG123", "STOPPED");
                    stopped = true;
                    int nextPosition = listAdapter.getPositionVideoId(currentVideoId) + 1;
                    if (nextPosition <= listAdapter.getCount() - 1) {
                        loadVideo(listAdapter.getVideoId(nextPosition));
                        stopped = false;
                    }
                }

                @Override
                public void onError(YouTubePlayer.ErrorReason errorReason) {

                }
            });
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, 1).show();
        } else {
            Toast.makeText(getApplicationContext(), DEFAULTERRORMESSAGE + youTubeInitializationResult.toString(), Toast.LENGTH_LONG).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            getYoutubePlayerProvider().initialize(YOUTUBEKEY, this); //Insert correct key
        }
    }

    protected YouTubePlayer.Provider getYoutubePlayerProvider() {
        return youTubePlayerView;
    }

    public Observable<String> musicSearchSubjectResult() {
        return musicSearchSubject;
    }

    public Observable<SearchResultObject> searchResultSubjectResult() {
        return searchResultSubject;
    }

    private void resultFromMusicSearch() {
        musicSearchSubjectResult().doOnNext(musicSearchInput -> searchedMusic = musicSearchInput)
                .doOnNext(__ -> new TestSearch().execute())
                .subscribe();
    }

    private void resultFromSearchResult() {
        searchResultSubjectResult().doOnNext(searchResult -> searchResultObject = searchResult)
                .doOnNext(__ -> hasResultsDecision())
                .subscribe();
    }

    private void hasResultsDecision() {
        if (searchResultObject.getVideoId().equals("-1")) {
            Toast.makeText(HostYoutubeActivity.this, "This video already exists", Toast.LENGTH_SHORT).show();
        } else {
            if (m_youTubePlayer == null) {
                youTubePlayerView.initialize(YOUTUBEKEY, this);
            } else {
                if (stopped) {
                    loadVideo(searchResultObject.getVideoId());
                }
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    private void loadVideo(String videoId) {
        currentVideoId = videoId;
        stopped = false;
        m_youTubePlayer.loadVideo(videoId);
        listAdapter.setSelectedItem(listAdapter.getPositionVideoId(videoId));
        listAdapter.notifyDataSetChanged();
    }
}
