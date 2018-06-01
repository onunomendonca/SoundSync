package com.mei.daam.soundsync;

import android.util.Log;

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
    public void checkAndRightGroupOnDB() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        DatabaseReference myRef = database.getReference(key);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(key)) {
                    //NO CASO DE JA EXISTIR O GRUPO NA BD.
                    groupExists.onNext(ResultMapper.EXISTS);
                    Log.d("TAG123", "Este " + key + " já existe... Nao foi criado um novo.");
                } else {
                    //Registar o novo grupo na base de dados dentro do grupo "groups"
                    myRef.setValue(group);
                    groupExists.onNext(ResultMapper.CREATE);
                    Log.d("TAG123", "Foi criado " + key + " com sucesso!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG123", "Ocorreu um erro na criacao/verificacao do grupo...");
                groupExists.onNext(ResultMapper.ERROR);
            }
        });
    }

    public io.reactivex.Observable<ResultMapper> groupExists() {
        return groupExists;
    }
}