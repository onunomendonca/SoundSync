package com.mei.daam.soundsync;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;

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
                nextButton.setEnabled(false);
                fragment.toggleProgressBar();
                String groupName = editText.getText().toString();
                if (groupName.equals("")) {
                    fragment.showToast("Invalid name. Choose a new name!");
                    nextButton.setEnabled(true);
                    fragment.toggleProgressBar();
                } else {
                    if (ConnectionHandler.hasNetworkConnection(fragment.getContext())) {
                        Group group = new Group(groupName);
                        FireBaseHandler fireBaseHandler = new FireBaseHandler(group);
                        fireBaseHandler.checkAndRightGroupOnDB(true);
                        handleFirebaseResponse(fireBaseHandler, group);
                    } else {
                        nextButton.setEnabled(true);
                        fragment.toggleProgressBar();
                        fragment.showToast("Network not available");
                    }
                }
            }
        });
    }

    private void handleFirebaseResponse(FireBaseHandler fireBaseHandler, Group group) {
        fireBaseHandler.groupExists().doOnNext(exists -> {
            if (exists == ResultMapper.EXISTS) {
                fragment.showToast("Group already exists! Choose a new name! ");
            } else if (exists == ResultMapper.CREATE) {
                Intent intent = createIntent(group);
                fragment.startActivity(intent);
            } else {
                fragment.showToast("An unexpected error occured");
            }
            nextButton.setEnabled(true);
            fragment.toggleProgressBar();
        }).subscribe();
    }

    private Intent createIntent(Group group) {
        Intent intent = new Intent(fragment.getContext(), HostYoutubeActivity.class);
        intent.putExtra(MainActivity.GROUP_NAME, group.getName());
        intent.putExtra("Group", (Serializable) group);
        intent.putExtra("isHost", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }
}
