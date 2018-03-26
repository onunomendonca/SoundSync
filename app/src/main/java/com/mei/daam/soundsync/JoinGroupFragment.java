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

/**
 * Created by D01 on 26/03/2018.
 */

public class JoinGroupFragment extends Fragment {
//STILL NEEDS THE QRCODE AND NFC IMAGES

    private Button nextButton;
    private EditText editText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.join_group_screen,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = (EditText) view.findViewById(R.id.name_of_group_join);
        nextButton = (Button) view.findViewById(R.id.next_join_btn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupName = editText.getText().toString();
                if(groupName.equals("") || groupName == null){
                    Toast.makeText(getContext(),"Invalid name",Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(getContext(), GuestYoutubeActivity.class);
                    intent.putExtra(MainActivity.GROUP_NAME,groupName);
                    startActivity(intent);
                }

            }
        });
    }

}
