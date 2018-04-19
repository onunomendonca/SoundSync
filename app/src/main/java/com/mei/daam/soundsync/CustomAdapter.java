package com.mei.daam.soundsync;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filipa on 17-04-2018.
 */

public class CustomAdapter extends BaseAdapter {


        Context context;
        private  List<String> videoNames;
        private  List<String> videoDurations;
        private  List<String> videoThumbnails;
        private  List<String> videoIds;

        public CustomAdapter(Context context){
            //super(context, R.layout.single_list_app_item, utilsArrayList);
            this.context = context;
            this.videoNames = new ArrayList<>();
            this.videoDurations = new ArrayList<>();
            this.videoThumbnails = new ArrayList<>();
            this.videoIds = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return videoNames.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(final int position,  View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.custom_adapter,null);
            TextView videoName = (TextView) convertView.findViewById(R.id.videoName);
            TextView videoDuration = (TextView) convertView.findViewById(R.id.videoDuration);
            ImageView thumbnail = (ImageView) convertView.findViewById(R.id.videoThumbnail);
            videoName.setText(videoNames.get(position));
            videoDuration.setText(videoDurations.get(position));
            Picasso.get().load(videoThumbnails.get(position)).into(thumbnail);

            return convertView;
        }


    public void addSongName(String name) {
            videoNames.add(name);
    }

    public void addDuration(String name) {
            videoDurations.add(name);
    }

    public void addThumbnail(String s) {
            videoThumbnails.add(s);
    }

    public void addVideoId(String videoId) {
            videoIds.add(videoId);
    }
}

