package com.mei.daam.soundsync;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by D01 on 25/05/2018.
 */

public class CreateGroupPresenter {
    private final CreateGroupFragment fragment;
    private final EditText editText;
    private final Button nextButton;

    public CreateGroupPresenter(CreateGroupFragment fragment, EditText editText, Button nextButton) {
        this.fragment = fragment;
        this.editText = editText;
        this.nextButton = nextButton;
    }

    public void setListeners() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupName = editText.getText().toString();
                if (groupName.equals("")) {
                    fragment.showToast("Invalid name. Choose a new name!");
                } else {
                    Group group = new Group(groupName);
                     FireBaseHandler fireBaseHandler = new FireBaseHandler(group);
                     fireBaseHandler.writeGroupOnDB();
                     fireBaseHandler.groupExists().doOnNext(exists -> {
                     if (exists == ResultMapper.EXISTS) {
                     fragment.showToast("Group already exists! Choose a new name! ");
                     } else if(exists == ResultMapper.CREATE){
                    Intent intent = new Intent(fragment.getContext(), HostYoutubeActivity.class);
                    intent.putExtra(MainActivity.GROUP_NAME, groupName);
                    fragment.startActivity(intent);
                }
                else{
                 fragment.showToast("An unexpected error occured");
                 }
                 }).subscribe();
                 }
            }
        });
    }
}
