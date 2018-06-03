package com.mei.daam.soundsync;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.subjects.PublishSubject;

public class FireBaseHandler {

    private Group group;
    private String key;
    private PublishSubject<ResultMapper> groupExists;

    public FireBaseHandler(Group group) {
        this.group = group;
        key = "groups/" + group.getName();
        groupExists = PublishSubject.create();
    }

    //Method that checks if the group name already exists in the DB. If it exists, then gives an error
    //In case it doesn't exist, then create the group.
    public void checkAndRightGroupOnDB(boolean isCreate) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        DatabaseReference myRef = database.getReference(key);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(key)) {
                    //No caso de já existir um grupo
                    groupExists.onNext(ResultMapper.EXISTS);
                } else {
                    //Registar o novo grupo na base de dados dentro do grupo "groups"
                    if (isCreate) {
                        myRef.setValue(group);
                    }
                    groupExists.onNext(ResultMapper.CREATE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                groupExists.onNext(ResultMapper.ERROR);
            }
        });
    }

    public io.reactivex.Observable<ResultMapper> groupExists() {
        return groupExists;
    }
}