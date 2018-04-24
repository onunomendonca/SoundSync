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
                    //TENTAR ENTRAR NO GRUPO
                    Group group = new Group(groupName);
                    FireBaseHandler fireBaseHandler = new FireBaseHandler(group);
                    fireBaseHandler.groupExists().doOnNext(exists -> {
                        if (exists == ResultMapper.EXISTS) {
                            Intent intent = new Intent(getContext(), HostYoutubeActivity.class);
                            intent.putExtra(MainActivity.GROUP_NAME, groupName);
                            startActivity(intent);
                     } else if(exists == ResultMapper.CREATE){
                            Toast.makeText(getContext(),"O grupo não existe.",Toast.LENGTH_LONG).show();
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
