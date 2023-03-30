package com.example.noteanalyticsapplication;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.noteanalyticsapplication.adapter.NoteAdapter;
import com.example.noteanalyticsapplication.model.Category;
import com.example.noteanalyticsapplication.model.Note;


import com.google.firebase.analytics.FirebaseAnalytics;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class NoteActivity extends AppCompatActivity implements NoteAdapter.ItemClickListener {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAnalytics mFirebaseAnalytics;
    ArrayList<Note> items;
    NoteAdapter noteAdapter;
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    RecyclerView rvNote;
    String categoryId;
    Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR);
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        rvNote = findViewById(R.id.rvNote);
        items = new ArrayList<>();
        noteAdapter = new NoteAdapter(this, items, this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        screenTrack("Note Screen");
        Intent intent = getIntent();
        Category category = (Category) intent.getSerializableExtra("Category");
        categoryId = category.getCategoryId();
        GetAllNotes();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void GetAllNotes() {
        db.collection("CategoryNotes").document(categoryId).collection("Note").get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isEmpty()) {
                Log.e("Ayat", "onSuccess: LIST EMPTY");
            } else {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        String id = documentSnapshot.getId();
                        String name = documentSnapshot.getString("name");
                        String description = documentSnapshot.getString("description");
                        String image = documentSnapshot.getString("image");
                        Note e_note = new Note(id, name, description, image);
                        items.add(e_note);

                        rvNote.setLayoutManager(layoutManager);
                        rvNote.setHasFixedSize(true);
                        rvNote.setAdapter(noteAdapter);

                        noteAdapter.notifyDataSetChanged();
                        Log.e("LogDATA", items.toString());

                    }
                }
            }
        }).addOnFailureListener(e -> Log.e("LogDATA", "get failed with "));
    }

    @Override
    public void onItemClick(int position, String id) {
        Intent intent = new Intent(this, DetailsActivity.class);
        Note note = new Note(id, items.get(position).getNoteId());
        intent.putExtra("Note", note);
        intent.putExtra("Category", categoryId);
        cardEvent(id, "Note", items.get(position).getName());
        Log.e("LogDATAAyT", "get failed with " + categoryId);
        startActivity(intent);
    }

    public void cardEvent(String id, String name, String content) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, content);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

    }

    public void screenTrack(String screenName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "Note Activity");
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
        screens.put("name", "Note Activity");
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