package com.mei.daam.soundsync;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import java.io.Serializable;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import static com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import static com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import static com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import static com.google.android.youtube.player.YouTubePlayer.Provider;
import static io.reactivex.subjects.PublishSubject.create;

public class HostYoutubeActivity extends YouTubeBaseActivity implements OnInitializedListener { //Implements Listeners here
    private final static String YOUTUBEKEY = "";
    private final static String SEARCHTYPE = "video";
    private final static String DEFAULTERRORMESSAGE = "Error initializing youtube";
    private final static String ISFIRSTVIDEOKEY = "FIRSTVIDEO";
    private final static String POSITIONKEY = "POSITION";
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
    private String currentVideoId = "";
    private ImageView noVideoImage;
    private TextView groupNameTextView;
    private Group group;
    private boolean firstVideo;
    private int currentVideoPosition;
    private boolean isHost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            firstVideo = savedInstanceState.getBoolean(ISFIRSTVIDEOKEY);
            currentVideoPosition = savedInstanceState.getInt(POSITIONKEY);
        } else {
            firstVideo = true;
            currentVideoPosition = -1;
        }

        setContentView(R.layout.host_youtube);
        getWindow().setBackgroundDrawableResource(R.drawable.background_blurred);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(R.drawable.first_layer);
        }

        Intent intent = getIntent();
        String groupName = intent.getStringExtra(MainActivity.GROUP_NAME);
        isHost = intent.getBooleanExtra("isHost", false);

        if (firstVideo) {
            Toast.makeText(this, groupName, Toast.LENGTH_LONG).show();
        }
        group = (Group) intent.getSerializableExtra("Group");

        groupNameTextView = (TextView) findViewById(R.id.group_name);
        groupNameTextView.setText(group.getName());
        groupNameTextView.setVisibility(View.VISIBLE);

        //Prepares Adapter
        listView = (ListView) findViewById(R.id.list_view_host);
        CustomAdapter listAdapter = new CustomAdapter(HostYoutubeActivity.this, group);
        group.setMusicList(listAdapter);
        listView.setAdapter(listAdapter);

        //Creates subjects
        searchResultSubject = create();
        musicSearchSubject = create();

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        noVideoImage = (ImageView) findViewById(R.id.no_video_img);
        addButton = (Button) findViewById(R.id.add_button_host);
        addButton.bringToFront();
        new HostYoutubePresenter(this, listView, listAdapter, addButton).present();

        addPeopleToGroupListener();
    }



    public void addPeopleToGroupListener() {

        ImageButton imageButton = (ImageButton) findViewById(R.id.sound_sync_img);

        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(HostYoutubeActivity.this, Pop.class);
                intent.putExtra(MainActivity.GROUP_NAME, group.getName());
                startActivity(intent);
            }

        });

    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean(ISFIRSTVIDEOKEY, firstVideo);
        bundle.putInt(POSITIONKEY, currentVideoPosition);
    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer youTubePlayer, boolean restored) {
        noVideoImage.setVisibility(View.GONE);
        youTubePlayerView.setVisibility(View.VISIBLE);
        m_youTubePlayer = youTubePlayer;
        if (currentVideoPosition != -1) {
            loadVideo(group.getMusicList().getVideoId(currentVideoPosition));
        } else if (group.getMusicList().getCount() > 0 && currentVideoId.equals("")) {
            loadVideo(group.getMusicList().getVideoId(0));
        } else if (searchResultObject != null) {
            loadVideo(searchResultObject.getVideoId());
        }
        m_youTubePlayer.setPlaybackEventListener(new PlaybackEventListener() {
            @Override
            public void onPlaying() {
                stopped = false;
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
        m_youTubePlayer.setPlayerStateChangeListener(new PlayerStateChangeListener() {
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
                stopped = true;
                int nextPosition = group.getMusicList().getPositionVideoId(currentVideoId) + 1;
                if (nextPosition <= group.getMusicList().getCount() - 1) {
                    loadVideo(group.getMusicList().getVideoId(nextPosition));
                    stopped = false;
                }
            }

            @Override
            public void onError(ErrorReason errorReason) {

            }
        });

    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
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

    protected Provider getYoutubePlayerProvider() {
        return youTubePlayerView;
    }

    public Observable<String> musicSearchSubjectResult() {
        return musicSearchSubject;
    }

    public Observable<SearchResultObject> searchResultSubjectResult() {
        return searchResultSubject;
    }

    public void loadVideo(String videoId) {
        currentVideoId = videoId;
        stopped = false;
        if (m_youTubePlayer != null) {
            if (!isHost && firstVideo) {
                m_youTubePlayer.cueVideo(videoId);
                firstVideo = false;
            } else {
                m_youTubePlayer.loadVideo(videoId);
            }
        } else {
            initializeYoutube();
        }
        currentVideoPosition = group.getMusicList().getPositionVideoId(videoId);
        group.getMusicList().setSelectedItem(group.getMusicList().getPositionVideoId(videoId));
        group.getMusicList().notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HostYoutubeActivity.this)
                .setTitle("Do you want to leave the group?").setMessage("Are you sure you want to leave this group?");
        builder.setPositiveButton("Leave", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                HostYoutubeActivity.super.onBackPressed();
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

    public void initializeYoutube() {
        youTubePlayerView.initialize(YOUTUBEKEY, this);
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    //Presenter methods

    public void musicSearched(String inputWord) {
        musicSearchSubject.onNext(inputWord);
    }

    public void startThread() {
        new HostYoutubeActivity.SearchVideo().execute();
    }

    public void setSearchedMusic(String musicSearchInput) {
        searchedMusic = musicSearchInput;
    }

    public SearchResultObject getSearchResultObject() {
        return searchResultObject;
    }

    public void setSearchResultObject(SearchResultObject searchResult) {
        searchResultObject = searchResult;
    }

    public YouTubePlayer getYouTubePlayer() {
        return m_youTubePlayer;
    }

    public boolean isStopped() {
        return stopped;
    }

    private class SearchVideo extends AsyncTask<Void, Void, SearchResultObject> {
        private ProgressDialog progressbar = new ProgressDialog(HostYoutubeActivity.this);

        @Override
        protected void onPreExecute() {
            progressbar.setMessage("Searching...");
            progressbar.show();
        }

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
                    String channelTitle = searchResultSnippet.getChannelTitle();
                    if (!group.getMusicList().hasVideoId(videoId)) {
                        Music music = new Music(name, channelTitle, "https://img.youtube.com/vi/" + videoId + "/0.jpg", videoId);
                        group.getMusicList().addMusic(music);
                        m_searchResultObject = new SearchResultObject(videoId, eTag, searchResultSnippet);
                    } else {
                        m_searchResultObject = new SearchResultObject("-1", "-1", null);
                    }
                }
            } catch (GoogleJsonResponseException e) {
                Log.d("Error", "There was a service error: " + e.getDetails().getCode() + " : "
                        + e.getDetails().getMessage());
            } catch (IOException e) {
                Log.d("Error", "There was an IO error: " + e.getCause() + " : " + e.getMessage());
            } catch (Throwable t) {
                Log.d("Error", "stack " + t.toString());
            }
            return m_searchResultObject;
        }

        @Override
        protected void onPostExecute(SearchResultObject s) {
            progressbar.dismiss();
            if (s == null) {
                Toast.makeText(HostYoutubeActivity.this, "No Result found", Toast.LENGTH_LONG).show();
            } else {
                searchResultSubject.onNext(s);
            }
        }


    }
}
