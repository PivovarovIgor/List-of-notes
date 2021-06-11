package ru.geekbrains.listofnotes.ui.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import ru.geekbrains.listofnotes.R;
import ru.geekbrains.listofnotes.domain.Note;
import ru.geekbrains.listofnotes.domain.NoteRepository;
import ru.geekbrains.listofnotes.domain.NoteRepositoryImpl;

public class ListOfNotesFragment extends Fragment {

    private NoteRepository noteRepository;
    private OnNoteCliched onNoteCliched;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnNoteCliched) {
            onNoteCliched = (OnNoteCliched) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noteRepository = new NoteRepositoryImpl();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_of_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout listOfNotes = view.findViewById(R.id.list_of_notes_container);

        List<Note> notes = noteRepository.getNotes();

        for (Note note : notes) {

            View itemView = LayoutInflater.from(requireContext()).inflate(R.layout.item_note, listOfNotes, false);

            itemView.setOnClickListener(v -> {
                if (onNoteCliched != null) {
                    onNoteCliched.onNoteClicked(note);
                }
            });

            TextView noteName = itemView.findViewById(R.id.note_caption);
            noteName.setText(note.getCaption());

            listOfNotes.addView(itemView);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onNoteCliched = null;
    }

    public interface OnNoteCliched {
        void onNoteClicked(Note note);
    }
}
