package ru.geekbrains.listofnotes.ui.mainscreen.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.listofnotes.R;
import ru.geekbrains.listofnotes.domain.Note;

public class ListOfNotesAdapter extends RecyclerView.Adapter<ListOfNotesAdapter.NoteViewHolder> {

    private final List<Note> notes = new ArrayList<>();

    private OnNoteClickedListener onNoteClickedListener;

    public void setData(List<Note> toSet) {
        notes.clear();
        notes.addAll(toSet);
    }

    public void setOnNoteClickedListener(OnNoteClickedListener onNoteClickedListener) {
        this.onNoteClickedListener = onNoteClickedListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfNotesAdapter.NoteViewHolder holder, int position) {

        Note note = notes.get(position);
        holder.noteName.setText(note.getCaption());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public interface OnNoteClickedListener {
        void onNoteClick(@NonNull Note note);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView noteName;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(v -> {
                if (onNoteClickedListener != null) {
                    onNoteClickedListener.onNoteClick(notes.get(getAdapterPosition()));
                }
            });

            noteName = itemView.findViewById(R.id.note_caption);
        }
    }
}
