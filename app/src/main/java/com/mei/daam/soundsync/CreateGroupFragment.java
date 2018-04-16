package com.mei.daam.soundsync;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateGroupFragment extends Fragment {

    private Button nextButton;
    private EditText editText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_group_screen, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = (EditText) view.findViewById(R.id.name_of_group);
        nextButton = (Button) view.findViewById(R.id.next_btn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupName = editText.getText().toString();
                if (groupName.equals("")) {
                    Toast.makeText(getContext(), "Invalid name. Choose a new name!", Toast.LENGTH_LONG).show();
                } else {
                    Group group = new Group(groupName);
                    FireBaseHandler fireBaseHandler = new FireBaseHandler(group);
                    fireBaseHandler.writeGroupOnDB();
                    fireBaseHandler.groupExists().doOnNext(exists -> {
                        if (exists == ResultMapper.EXISTS) {
                            Toast.makeText(getContext(), "Group already exists! Choose a new name! ", Toast.LENGTH_LONG).show();
                        } else if(exists == ResultMapper.CREATE){
                            Intent intent = new Intent(getContext(), HostYoutubeActivity.class);
                            intent.putExtra(MainActivity.GROUP_NAME, groupName);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getContext(), "An unexpected error occured", Toast.LENGTH_LONG).show();
                        }
                    }).subscribe();
                }
            }
        });
    }
}
