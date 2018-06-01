package com.mei.daam.soundsync;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;

/**
 * Created by D01 on 25/05/2018.
 */

public class JoinGroupPresenter {
    private final JoinGroupFragment fragment;
    private final Button nextButton;
    private final EditText editText;

    public JoinGroupPresenter(JoinGroupFragment fragment, Button nextButton, EditText editText) {

        this.fragment = fragment;
        this.nextButton = nextButton;
        this.editText = editText;
    }

    public void setListeners() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextButton.setEnabled(false);
                String groupName = editText.getText().toString();
                if (ConnectionHandler.hasNetworkConnection(fragment.getContext())) {
                    Group group = new Group(groupName);
                    FireBaseHandler fireBaseHandler = new FireBaseHandler(group);
                    fireBaseHandler.checkAndRightGroupOnDB();
                    handleFirebaseResponse(fireBaseHandler, group);
                } else {
                    fragment.showToast("Network not available");
                    nextButton.setEnabled(true);
                }
            }
        });
    }

    private void handleFirebaseResponse(FireBaseHandler fireBaseHandler, Group group) {
        fireBaseHandler.groupExists().doOnNext(exists -> {
            if (exists == ResultMapper.EXISTS) {
                Intent intent = createIntent(group);
                fragment.startActivity(intent);
            } else if (exists == ResultMapper.CREATE) {
                fragment.showToast("This group doesn't exist");
            } else {
                fragment.showToast("An unexpected error occured");
            }
            nextButton.setEnabled(true);
        }).subscribe();
    }

    private Intent createIntent(Group group) {
        Intent intent = new Intent(fragment.getContext(), HostYoutubeActivity.class);
        intent.putExtra(MainActivity.GROUP_NAME, group.getName());
        intent.putExtra("Group", (Serializable) group);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }
}
