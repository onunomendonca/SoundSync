package com.mei.daam.soundsync;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class HostYoutubePresenter {
    private final HostYoutubeActivity activity;
    private final ListView listView;
    private final CustomAdapter listAdapter;
    private final Button addButton;
    private ImageButton imageButton;
    private String groupName;

    public HostYoutubePresenter(HostYoutubeActivity activity, ListView listView, CustomAdapter listAdapter,
                                Button addButton, ImageButton imageButton, String groupName) {

        this.activity = activity;
        this.listView = listView;
        this.listAdapter = listAdapter;
        this.addButton = addButton;
        this.imageButton = imageButton;
        this.groupName = groupName;
    }

    public void present() {
        handleCreate();
        handleListViewClick();
        handleAddButtonClick();
        addPeopleToGroupListener();
        resultFromMusicSearch();
        resultFromSearchResult();
    }

    private void handleCreate() {
        listAdapter.dataChangeSubjectResult()
                .doOnNext(changed -> {
                    if (activity.getYouTubePlayer() == null && listAdapter.getCount() > 0) {
                        activity.initializeYoutube();
                    }
                }).subscribe();
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
            }
        }
    }

    private void addPeopleToGroupListener() {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(activity, Pop.class);
                intent.putExtra(MainActivity.GROUP_NAME, groupName);
                activity.startActivity(intent);
            }
        });

    }
}
