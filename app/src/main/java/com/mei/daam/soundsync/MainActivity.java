package com.mei.daam.soundsync;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {

    private FragmentNavigator fragmentNavigator;
    private PublishSubject<String> contentSubject;
    private ImageView imageView;
    protected final static String GROUP_NAME = "GROUP_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentSubject = PublishSubject.create();
        getWindow().setBackgroundDrawableResource(R.drawable.background_blurred);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(R.drawable.first_layer);
        }
        fragmentNavigator = new FragmentNavigator(getSupportFragmentManager(), R.id.fragment_placeholder);
        imageView = (ImageView) findViewById(R.id.sound_sync_img);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentNavigator.navigateToCleaningBackStack(new MainScreenFragment());
            }
        });
        if (savedInstanceState == null) {
            fragmentNavigator.navigateToWithoutBackSave(new MainScreenFragment());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "You cancelled the scanning!", Toast.LENGTH_LONG).show();
            } else {
                contentSubject.onNext(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public Observable<String> contentSubjectResult() {
        return contentSubject;
    }

    public FragmentNavigator getFragmentNavigator() {
        return fragmentNavigator;
    }
}

