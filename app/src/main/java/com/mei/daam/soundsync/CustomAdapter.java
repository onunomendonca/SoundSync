package com.mei.daam.soundsync;

import android.content.Context;
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

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class CustomAdapter extends BaseAdapter {


    private Context context;
    private List<Music> musicList;
    private DatabaseReference database_music_list;
    private int selectedItem;
    private PublishSubject<String> dataChangeSubject;

    public CustomAdapter(Context context, Group group) {
        //super(context, R.layout.single_list_app_item, utilsArrayList);
        this.context = context;
        this.musicList = new ArrayList<>();
        dataChangeSubject = PublishSubject.create();
        database_music_list = FirebaseDatabase.getInstance().getReference().child("groups/" + group.getName() + "/music_list");
        setMusicChangeListener();
        setMusicChangeSubject();
    }

    private void setMusicChangeSubject() {
        dataChangeSubject.doOnNext(dataChanged -> this.notifyDataSetChanged()).subscribe();
    }

    public Observable<String> dataChangeSubjectResult() {
        return dataChangeSubject;
    }

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.custom_adapter, null);
        TextView videoName = (TextView) convertView.findViewById(R.id.videoName);
        TextView channelTitle = (TextView) convertView.findViewById(R.id.channelTitle);
        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.videoThumbnail);
        ImageView deleteImg = (ImageView) convertView.findViewById(R.id.ic_delete);
        deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!musicList.isEmpty() && musicList.get(position) != null) {
                    musicList.remove(position);
                    database_music_list.removeValue();
                    addMusicList(musicList);
                }
            }
        });
        Music selectedMusic = musicList.get(position);
        videoName.setText(selectedMusic.getName());
        channelTitle.setText(selectedMusic.getChannelTitle());
        Picasso.get().load(selectedMusic.getThumbnail()).into(thumbnail);
        if (position == selectedItem) {
            convertView.setBackgroundResource(R.color.colorBGPrimaryDark);
        }
        return convertView;
    }

    public void addMusic(Music music) {
        database_music_list.push().setValue(music);
    }

    private void addMusicList(List<Music> musicList) {
        for (Music music : musicList) {
            database_music_list.push().setValue(music);
        }
    }

    public String getVideoId(int position) {
        return musicList.get(position).getVideoId();
    }

    public boolean hasVideoId(String videoId) {
        for (Music music : musicList) {
            if (music.getVideoId().equals(videoId)) {
                return true;
            }
        }
        return false;
    }

    public int getPositionVideoId(String videoId) {
        for (int i = 0; i < musicList.size(); i++) {
            if (musicList.get(i).getVideoId().equals(videoId))
                return i;
        }
        if (musicList.size() > selectedItem) {
            return selectedItem - 1;
        }

        return -1;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    public void setMusicChangeListener() {
        database_music_list.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Music> listMusic = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    listMusic.add(postSnapshot.getValue(Music.class));
                }
                musicList = listMusic;
                dataChangeSubject.onNext("changed");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

