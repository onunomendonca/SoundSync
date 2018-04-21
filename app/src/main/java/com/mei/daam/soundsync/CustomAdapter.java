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
        private List<Music> musicList;

        public CustomAdapter(Context context){
            //super(context, R.layout.single_list_app_item, utilsArrayList);
            this.context = context;
            this.musicList = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return musicList.size();
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
            Music selectedMusic = musicList.get(position);
            videoName.setText(selectedMusic.getName());
            videoDuration.setText(selectedMusic.getDuration());
            Picasso.get().load(selectedMusic.getThumbnail()).into(thumbnail);

            return convertView;
        }
    public void addMusic(Music music){
        musicList.add(music);
    }

    public String getVideoId(int position){
        return musicList.get(position).getVideoId();
    }

    public boolean hasVideoId(String videoId){
        for(Music music : musicList){
            if(music.getVideoId().equals(videoId)){
                return true;
            }
        }
        return false;
    }

    public int getPositionVideoId(String videoId){
        for(int i=0;i<musicList.size();i++){
            if(musicList.get(i).getVideoId().equals(videoId))
                return i;
        }
        return 0;
    }
}

