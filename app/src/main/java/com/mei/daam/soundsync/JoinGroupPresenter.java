package com.mei.daam.soundsync;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                String groupName = editText.getText().toString();
                if(groupName.equals("")){
                    fragment.showToast("Invalid name");
                }
                else{
                    //TENTAR ENTRAR NO GRUPO
                    Group group = new Group(groupName);
                    FireBaseHandler fireBaseHandler = new FireBaseHandler(group);
                    fireBaseHandler.groupExists().doOnNext(exists -> {
                        if (exists == ResultMapper.EXISTS) {
                            Intent intent = new Intent(fragment.getContext(), HostYoutubeActivity.class);
                            intent.putExtra(MainActivity.GROUP_NAME, groupName);
                            fragment.startActivity(intent);
                        } else if(exists == ResultMapper.CREATE){
                            fragment.showToast("O grupo não existe.");
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
