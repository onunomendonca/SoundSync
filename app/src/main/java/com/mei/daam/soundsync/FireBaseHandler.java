package com.mei.daam.soundsync;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FireBaseHandler {

    private Group g;
    private String key;

    public FireBaseHandler(){
        g = new Group("Oio453df","Tudo Becenaddsf");
        key = g.getName();
        writeGroupOnDB();
    }

    private void writeGroupOnDB() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(key);


        ValueEventListener responseListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Log.d("TAG123", "Não criei um grupo novo.");
                } else {
                    myRef.setValue(g);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG123", "Não criei um grupo novo323.");

            }
        };

        myRef.addValueEventListener(responseListener);


    }

}
