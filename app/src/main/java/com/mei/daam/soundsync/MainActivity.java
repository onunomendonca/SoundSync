package com.mei.daam.soundsync;

import android.os.Build;
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

        getWindow().setBackgroundDrawableResource(R.drawable.first_layer);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(R.drawable.first_layer);
        }
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

