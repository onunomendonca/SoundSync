package com.mei.daam.soundsync;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

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

