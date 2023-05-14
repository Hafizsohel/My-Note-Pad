package com.example.newnoteapp.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newnoteapp.Models.Notes;
import com.example.newnoteapp.NotesClick;
import com.example.newnoteapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class NotesListAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    Context context;
    List<Notes> list;
    NotesClick notesClick;

    public NotesListAdapter(Context context, List<Notes> list, NotesClick notesClick) {
        this.context = context;
        this.list = list;
        this.notesClick = notesClick;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {

        holder.textView_title.setText(list.get(position).getTitle());
        holder.textView_title.setSelected(true);
        holder.textView_notes.setText(list.get(position).getNotes());
        holder.textView_date.setText(list.get(position).getDate());
        holder.textView_date.setSelected(true);

        if (list.get(position).isPinned()) {
            holder.pin_image.setImageResource(R.drawable.pin_24);
        } else {
            holder.pin_image.setImageResource(0);
        }
        int color_code = getRandomColor();
        holder.notes_container.setCardBackgroundColor(holder.itemView.getResources().getColor(color_code, null));


        holder.notes_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesClick.onClick(list.get(holder.getAdapterPosition()));

            }
        });
        holder.notes_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                notesClick.onLongClick(list.get(holder.getAdapterPosition()), holder.notes_container);
                return true;
            }
        });


    }

    private int getRandomColor() {
        List<Integer> colorCode = new ArrayList<>();

        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);
        colorCode.add(R.color.color6);


        Random random = new Random();
        int random_color = random.nextInt(colorCode.size());
        return colorCode.get(random_color);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filtered_list(List<Notes> filter_list) {
        list = filter_list;
        notifyDataSetChanged();
    }

}
class NotesViewHolder extends RecyclerView.ViewHolder{


    CardView notes_container;
    TextView textView_title, textView_notes, textView_date;
    ImageView pin_image;


    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        notes_container= itemView.findViewById(R.id.notes_card_container);
        textView_title= itemView.findViewById(R.id.title_text);
        textView_notes= itemView.findViewById(R.id.textView_notes);
        pin_image= itemView.findViewById(R.id.image_view_pin);
        textView_date= itemView.findViewById(R.id.textView_date);

    }
}
