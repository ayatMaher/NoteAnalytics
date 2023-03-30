package com.example.noteanalyticsapplication;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.noteanalyticsapplication.model.Note;


import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;

public class DetailsActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAnalytics mFirebaseAnalytics;
    TextView textViewName;
    TextView textViewDescription;
    ImageView imageViewNote;
    String noteId;

    String categoryId;
    Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR);
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        textViewName = findViewById(R.id.txtNoteName);
        textViewDescription = findViewById(R.id.txtNoteDescription);
        imageViewNote = findViewById(R.id.imageNote);
        Intent intent = getIntent();
        Log.e("Ayat", "onSuccess: LIST EMPTY" + intent);
        categoryId = intent.getStringExtra("Category");
        Note note = (Note) intent.getSerializableExtra("Note");
        noteId = note.getNoteId();
        Log.e("Ayat", "onSuccess: LIST EMPTY" + noteId);
        Log.e("Ayat", "onSuccess: LIST EMPTY" + categoryId);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        screenTrack("Details Screen");
        GetAllNotes();
    }

    public void GetAllNotes() {
        Log.e("Ayat", "onSuccess: LIST EMPTY");
        DocumentReference docRef = db.collection("CategoryNotes").document(categoryId)
                .collection("Note")
                .document(noteId);
        docRef.get().addOnCompleteListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isSuccessful()) {
                Log.e("Ayat", "onSuccess: LIST EMPTY" + noteId);
                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getResult();
                if (documentSnapshot.exists()) {

                    String name = documentSnapshot.getString("name");
                    String description = documentSnapshot.getString("description");
                    String image = documentSnapshot.getString("image");
                    Picasso.get().load(image).into(imageViewNote);
                    textViewName.setText(name);
                    textViewDescription.setText(description);

                } else {
                    Log.d("tag", "onSuccess: LIST EMPTY");
                }

            } else {
                Log.e("LogDATA", "get failed with ");
            }
        });
    }

    public void screenTrack(String screenName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "Details Activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Calendar calendar = Calendar.getInstance();
        int hour2 = calendar.get(Calendar.HOUR);
        int minute2 = calendar.get(Calendar.MINUTE);
        int second2 = calendar.get(Calendar.SECOND);

        int h = hour2 - hour;
        int m = minute2 - minute;
        int s = second2 - second;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        HashMap<String, Object> screens = new HashMap<>();
        screens.put("name", "Details Activity");
        screens.put("hours", h);
        screens.put("minute", m);
        screens.put("seconds", s);

        db.collection("Track Time").add(screens)
                .addOnSuccessListener(documentReference -> {

                })
                .addOnFailureListener(e -> {

                });
        Log.e("Hours", String.valueOf(h));
        Log.e("Minutes", String.valueOf(m));
        Log.e("Seconds", String.valueOf(s));
    }
}





