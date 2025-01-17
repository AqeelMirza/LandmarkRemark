package com.landmarkremark.repository;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.landmarkremark.models.MarkedNote;
import com.landmarkremark.models.User;
import com.landmarkremark.utils.Utils;

import java.util.function.Consumer;

public class UserRepo {

    FirebaseDatabase firebaseDatabase;

    public UserRepo() {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
    }

    //For creating new user
    public void createNewUser(User user) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Utils.users);
        //Insert user in database
        databaseReference.child(user.getId()).setValue(user);
    }

    //Finding Notes of a specific User with its userId
    public void findUserNotes(String userId, Consumer<MarkedNote> onSuccess, Runnable noNotesFound) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Utils.users).getRef().child(userId).child(Utils.MarkedNote);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.i("sdfsddf", "sdfsfdsfdsfdsfsd");
                    for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                        Log.i("User notes", noteSnapshot.toString());
                        onSuccess.accept(noteSnapshot.getValue(MarkedNote.class));
                    }
                } else {
                    noNotesFound.run();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw new RuntimeException("Failed to find user notes", databaseError.toException());
            }
        });
    }

    //Finding all notes from all users
    public void findAllNotes(Consumer<MarkedNote> markedNoteConsumer) {
        final DatabaseReference userRef = firebaseDatabase.getReference().child(Utils.users);
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
                            throw new RuntimeException("Failed to find All notes", databaseError.toException());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw new RuntimeException("Failed to find All notes", databaseError.toException());
            }
        });
    }

    //adding notes
    public void addNotesToDb(MarkedNote markedNote) {
        markedNote.setId(String.valueOf(Utils.createRandomInt()));

        //Creating a new Note
        DatabaseReference databaseReference = firebaseDatabase.getReference(Utils.users);
        databaseReference.child(Utils.getLoggedInUserId()).child(Utils.MarkedNote).child(markedNote.getId()).setValue(markedNote);

    }

    //Finding a user to check its existence
    public void findUser(String userId, User user, Consumer<User> onUserFound) {
        //getting the reference of all users
        DatabaseReference databaseReference = firebaseDatabase.getReference(Utils.users);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User userById = getUserById(dataSnapshot, userId);
                if (userById == null) {
                    //no User found, create new user
                    createNewUser(user);
                    return;
                }
                onUserFound.accept(userById);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //getting a user with its Id
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
