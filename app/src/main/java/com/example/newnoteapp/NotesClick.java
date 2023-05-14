package com.example.newnoteapp;

import androidx.cardview.widget.CardView;

import com.example.newnoteapp.Models.Notes;

public interface NotesClick {
    void onClick (Notes notes);
    void onLongClick(Notes notes, CardView cardView);

}
