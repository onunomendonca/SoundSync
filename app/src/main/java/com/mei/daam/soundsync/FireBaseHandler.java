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

    public FireBaseHandler(Group g) {
        this.g = g;
        key = "groups/" + g.getName();
    }

    //Method that checks if the group name already exists in the DB. If it exists, then gives an error
    //In case it doesn't exist, then create the group.
    public boolean writeGroupOnDB() {
        boolean[] groupCreated = new boolean[1];
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        DatabaseReference myRef = database.getReference(key);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.hasChild(key)) {
                        //NO CASO DE JA EXISTIR O GRUPO NA BD.
                        Log.d("TAG123", "Este " + key + " já existe... Nao foi criado um novo.");
                        groupCreated[0] = false;
                    } else {
                        groupCreated[0] = true;
                        //Registar o novo grupo na base de dados dentro do grupo "groups"
                        myRef.setValue(g);
                        Log.d("TAG123", "Foi criado " + key + " com sucesso!");
                        Log.d("TAG123", "1:" + groupCreated[0]);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("TAG123", "Ocorreu um erro na criacao/verificacao do grupo...");

                }
            });

            Log.d("TAG123", "2:" + groupCreated[0]);
            return groupCreated[0];
    }


}