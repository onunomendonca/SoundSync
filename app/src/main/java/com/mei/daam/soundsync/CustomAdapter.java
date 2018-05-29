package com.mei.daam.soundsync;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.PublishSubject;

public class CustomAdapter extends BaseAdapter {


        Context context;
        private List<Music> musicList;
        private DatabaseReference database_music_list;
        private int selectedItem;




        public CustomAdapter(Context context, Group group){
            //super(context, R.layout.single_list_app_item, utilsArrayList);
            this.context = context;
            this.musicList = new ArrayList<>();
            database_music_list = FirebaseDatabase.getInstance().getReference().child("groups/"+group.getName()+"/music_list");
        }

        public int getCount() {
            getMusicFromDB();
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
            getMusicFromDB();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.custom_adapter,null);
            TextView videoName = (TextView) convertView.findViewById(R.id.videoName);
            TextView channelTitle = (TextView) convertView.findViewById(R.id.channelTitle);
            ImageView thumbnail = (ImageView) convertView.findViewById(R.id.videoThumbnail);
            ImageView deleteImg = (ImageView) convertView.findViewById(R.id.ic_delete);
            deleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!musicList.isEmpty() && musicList.get(position) != null) {
                        //musicList.remove(position);

                            // TODO APLICAR O PUBLISHER AQUI
                            removeMusicFromDB(position);

                    }
                }
            });
            Music selectedMusic = musicList.get(position);
            videoName.setText(selectedMusic.getName());
            channelTitle.setText(selectedMusic.getChannelTitle());
            Picasso.get().load(selectedMusic.getThumbnail()).into(thumbnail);
            if(position == selectedItem){
                convertView.setBackgroundResource(R.color.colorBGPrimaryDark);
            }
            return convertView;
        }

    public void addMusic(Music music){
            database_music_list.push().setValue(music);
            getMusicFromDB();
            //this.notifyDataSetChanged();
    }

    public String getVideoId(int position){
        getMusicFromDB();
        return musicList.get(position).getVideoId();
    }

    public boolean hasVideoId(String videoId){
        getMusicFromDB();
        for(Music music : musicList){
            if(music.getVideoId().equals(videoId)){
                return true;
            }
        }
        return false;
    }

    public int getPositionVideoId(String videoId){
        getMusicFromDB();
        for(int i=0;i<musicList.size();i++){
            if(musicList.get(i).getVideoId().equals(videoId))
                return i;
        }
        return -1;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    public void getMusicFromDB(){
        database_music_list.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                musicList.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    musicList.add(postSnapshot.getValue(Music.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void removeMusicFromDB(int i){
        database_music_list.addValueEventListener(new ValueEventListener() {
            private int counter = 0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                musicList.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        if (i == counter) {
                            dataSnapshot.getRef().removeValue();
                            Log.d("TAG123", "Removida a musica");
                        }
                    counter++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

