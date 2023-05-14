package com.example.newnoteapp;

import static com.example.newnoteapp.R.layout.activity_notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.newnoteapp.Models.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesActivity extends AppCompatActivity {
    EditText editText_title, editText_notes;
    ImageView image_save;
    Notes notes;
    boolean is_old_note=false;
    //SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        image_save=findViewById(R.id.image_view_save);
        editText_title=findViewById(R.id.edit_text_title);
        editText_notes=findViewById(R.id.edit_text_notes);


        notes= new Notes();
        try {
            notes= (Notes) getIntent().getSerializableExtra("old_note");
            editText_title.setText(notes.getTitle());
            editText_notes.setText(notes.getNotes());
            is_old_note=true;

        } catch (Exception e){
            e.printStackTrace();
        }


        image_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=editText_title.getText().toString();
                String description=editText_notes.getText().toString();
                if (title.isEmpty()) {
                    Toast.makeText(NotesActivity.this, "Please Add Some Title!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (description.isEmpty()){
                    Toast.makeText(NotesActivity.this, "Please Add Some Notes !!", Toast.LENGTH_SHORT).show();
                return;
                }
                SimpleDateFormat format= new SimpleDateFormat("EEE, d MMM yyyy HH : mm a");
                Date date=  new Date();

                if (!is_old_note){
                    notes= new Notes();
                }
                notes.setTitle(title);
                notes.setNotes(description);
                notes.setDate(format.format(date));

                Intent intent= new Intent();
                intent.putExtra("note", notes);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }

        });

        //Swipe for Refresh
       /* swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code here
                Toast.makeText(getApplicationContext(), "Works!", Toast.LENGTH_LONG).show();
                // To keep animation for 4 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeLayout.setRefreshing(false);
                    }
                }, 4000); // Delay in millis
            }
        });*/
    }
}