package com.mei.daam.soundsync;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private FragmentNavigator fragmentNavigator;
    private ImageView imageView;
    protected final static String GROUP_NAME = "GROUP_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentNavigator = new FragmentNavigator(getSupportFragmentManager(), R.id.fragment_placeholder);
        imageView = (ImageView) findViewById(R.id.sound_sync_img);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentNavigator.navigateToCleaningBackStack(new MainScreenFragment(),true);
            }
        });
        if(savedInstanceState == null){
        fragmentNavigator.navigateToWithoutBackSave(new MainScreenFragment(), true);
        }
    }


    public FragmentNavigator getFragmentNavigator() {
        return fragmentNavigator;
    }
}
