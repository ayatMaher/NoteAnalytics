package com.example.noteanalyticsapplication;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.noteanalyticsapplication.adapter.CategoryAdapter;
import com.example.noteanalyticsapplication.model.Category;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements CategoryAdapter.ItemClickListener {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAnalytics mFirebaseAnalytics;
    ArrayList<Category> items;
    CategoryAdapter categoryAdapter;
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    RecyclerView rvCategory;
    Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR);
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvCategory = findViewById(R.id.rvCategory);
        items = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, items, this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        screenTrack("Category Screen");
        GetAllCategory();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void GetAllCategory() {
        db.collection("CategoryNotes").get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isEmpty()) {
                Log.e("Ayat", "onSuccess: LIST EMPTY");
            } else {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        String id = documentSnapshot.getId();
                        String name = documentSnapshot.getString("name");

                        Category e_Category = new Category(id, name);
                        items.add(e_Category);

                        rvCategory.setLayoutManager(layoutManager);
                        rvCategory.setHasFixedSize(true);
                        rvCategory.setAdapter(categoryAdapter);

                        categoryAdapter.notifyDataSetChanged();
                        Log.e("LogDATA", items.toString());

                    }
                }
            }
        }).addOnFailureListener(e -> Log.e("LogDATA", "get failed with "));
    }

    @Override
    public void onItemClick(int position, String id) {
        Intent intent = new Intent(this, NoteActivity.class);
        Category category = new Category(id, items.get(position).getCategoryId());
        intent.putExtra("Category", category);
        cardEvent(id, "Category", items.get(position).getName());
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
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "Main Activity");
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
        screens.put("name", "Main Activity");
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