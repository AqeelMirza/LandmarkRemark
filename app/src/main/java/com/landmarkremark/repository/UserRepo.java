package com.landmarkremark.repository;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.landmarkremark.models.MarkedNote;
import com.landmarkremark.models.User;
import com.landmarkremark.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UserRepo {

    FirebaseDatabase firebaseDatabase;

    public UserRepo() {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void findUserNotes(String userId, Consumer<MarkedNote> onSuccess) {
        DatabaseReference databaseReference = firebaseDatabase.getReference("users").getRef().child(userId).child("markedNote");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    Log.i("User notes", noteSnapshot.toString());
                    onSuccess.accept(noteSnapshot.getValue(MarkedNote.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw new RuntimeException("Failed to find user notes", databaseError.toException());
            }
        });
    }

    public void findAllNotes(Consumer<MarkedNote> markedNoteConsumer) {
        final DatabaseReference userRef = firebaseDatabase.getReference().child(SharedPref.Users);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = (String) ds.getKey();
                    Log.d("TAG", "Key" + key);
                    DatabaseReference notesRef = userRef.child(key);
                    Log.d("TAG", "Key reference: " + notesRef);
                    notesRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot childSnapshot : dataSnapshot.child("markedNote").getChildren()) {
                                MarkedNote markedNote = childSnapshot.getValue(MarkedNote.class);
                                markedNoteConsumer.accept(markedNote);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("TAG", "Read failed");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "Read failed");
            }
        });
    }

    public void addNotesToDb(User user, MarkedNote markedNote) {
        markedNote.setId(String.valueOf(SharedPref.createRandomInt()));
        markedNote.setName(user.getName());

        //Creating a new Note
        DatabaseReference databaseReference = firebaseDatabase.getReference(SharedPref.Users);
        databaseReference.child(user.getId()).child(SharedPref.MarkedNote).child(markedNote.getId()).setValue(markedNote);

    }

    public void findUser(String userId, Consumer<User> onUserFound) {
        //getting the reference of all users
        DatabaseReference databaseReference = firebaseDatabase.getReference(SharedPref.Users);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User userById = getUserById(dataSnapshot, userId);
                if (userById == null) {
                    throw new RuntimeException("No User Found in DB");
                }
                onUserFound.accept(userById);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private User getUserById(DataSnapshot userData, String id) {
        for (DataSnapshot userSnapshot : userData.getChildren()) {
            User user = userSnapshot.getValue(User.class);
            if (user.getId().equalsIgnoreCase(id)) {
                return user;
            }
        }
        return null;
    }

}
