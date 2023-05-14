package com.example.newnoteapp;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.newnoteapp.Adapters.NotesListAdapter;
import com.example.newnoteapp.Database.RoomDB;
import com.example.newnoteapp.Models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {


    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;
    List<Notes>notes= new ArrayList<>();
    RoomDB database;
    FloatingActionButton fab_btn;
    SearchView searchView;
    Notes selected_notes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        recyclerView=findViewById(R.id.recycler_view);
        fab_btn= findViewById(R.id.fab_add);
        searchView=findViewById(R.id.search_view);


        database=RoomDB.getInstance(this);
        notes=database.dao().getAll();
       updateRecycler(notes);
        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(MainActivity.this, NotesActivity.class);
                startActivityForResult(intent, 101);

            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

    }





    private void filter(String newText) {
        List<Notes>filter_list= new ArrayList<>();
        for (Notes single_notes:notes){
            if (single_notes.getTitle().toLowerCase().contains(newText.toLowerCase())
            || single_notes.getNotes().toLowerCase().contains(newText.toLowerCase())){
                filter_list.add(single_notes);
            }
        }
        notesListAdapter.filtered_list(filter_list);
        }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==101) {
            if (resultCode == Activity.RESULT_OK) {
                Notes new_note = (Notes) data.getSerializableExtra("note");
                database.dao().insert(new_note);
                notes.clear();
                notes.addAll(database.dao().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
            return;
        }
            else if (requestCode==102){
                if(resultCode == Activity.RESULT_OK){
                    Notes new_note = (Notes) data.getSerializableExtra("note");
                    database.dao().update(new_note.getID(), new_note.getTitle(), new_note.getNotes());
                    notes.clear();
                    notes.addAll(database.dao().getAll());
                    notesListAdapter.notifyDataSetChanged();
            }
            return;
        }
        Toast.makeText(MainActivity.this, "Cancelled!", Toast.LENGTH_SHORT).show();

    }

    private void updateRecycler(List<Notes> notes) {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter= new NotesListAdapter(MainActivity.this, notes,notesClick);
        recyclerView.setAdapter(notesListAdapter);
    }

    private final NotesClick notesClick= new NotesClick() {
        @Override
        public void onClick(Notes notes) {

            Intent intent= new Intent(MainActivity.this, NotesActivity.class);
            intent.putExtra("old_note", notes);
            startActivityForResult(intent, 102);

        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {

           // selected_notes= new Notes();
                selected_notes=notes;
                showPopup(cardView);

        }
    };

    private void showPopup(CardView cardView) {

        PopupMenu popupMenu= new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.pop_up);
        popupMenu.show();
    }
    //New code



    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pin:
                if (selected_notes.isPinned()) {
                    database.dao().pin(selected_notes.getID(), false);
                    Toast.makeText(this, "Unpinned Successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    database.dao().pin(selected_notes.getID(), true);
                    Toast.makeText(this, "Pinned Successfully", Toast.LENGTH_SHORT).show();
                }

                notes.clear();
                notes.addAll(database.dao().getAll());
                notesListAdapter.notifyDataSetChanged();
                return true;

            case R.id.del:
                database.dao().delete(selected_notes);
                notes.remove(selected_notes);
                notesListAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Notes Deleted Successfully", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return false;
        }
    }
}

