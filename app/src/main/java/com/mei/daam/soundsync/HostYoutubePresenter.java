package com.mei.daam.soundsync;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Created by D01 on 25/05/2018.
 */

public class HostYoutubePresenter {
    private final HostYoutubeActivity activity;
    private final ListView listView;
    private final CustomAdapter listAdapter;
    private final Button addButton;

    public HostYoutubePresenter(HostYoutubeActivity activity, ListView listView, CustomAdapter listAdapter, Button addButton) {

        this.activity = activity;
        this.listView = listView;
        this.listAdapter = listAdapter;
        this.addButton = addButton;
    }

    public void present() {
        handleListViewClick();
        handleAddButtonClick();
        resultFromMusicSearch();
        resultFromSearchResult();
    }

    private void handleListViewClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                activity.loadVideo(listAdapter.getVideoId(position));
            }
        });
    }

    private void handleAddButtonClick() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                        .setTitle("Add new song");
                final EditText input = new EditText(activity);
                input.setInputType((InputType.TYPE_CLASS_TEXT));
                builder.setView(input);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String inputWord = input.getText().toString();
                        if (inputWord.equals("")) {
                            activity.showToast("Empty Input");
                        } else {
                            activity.musicSearched(inputWord);
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
    }

    private void resultFromMusicSearch() {
        activity.musicSearchSubjectResult().doOnNext(musicSearchInput -> activity.setSearchedMusic(musicSearchInput))
                .doOnNext(__ -> activity.startThread())
                .subscribe();
    }

    private void resultFromSearchResult() {
        activity.searchResultSubjectResult().doOnNext(searchResult -> activity.setSearchResultObject(searchResult))
                .doOnNext(__ -> hasResultsDecision())
                .subscribe();
    }

    private void hasResultsDecision() {
        if (activity.getSearchResultObject().getVideoId().equals("-1")) {
            activity.showToast("This video already exists");
        } else {
            if (activity.getYouTubePlayer() == null) {
                activity.initializeYoutube();
            } else {
                if (activity.isStopped()) {
                    activity.loadVideo(activity.getSearchResultObject().getVideoId());
                }
            }
        }
    }
}
