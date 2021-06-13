package ru.geekbrains.listofnotes.ui.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private OnNoteClicked onNoteClicked;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnNoteClicked) {
            onNoteClicked = (OnNoteClicked) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        noteRepository = new NoteRepositoryImpl();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_of_notes, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_list_of_notes, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.option_menu_add_note) {
            Toast.makeText(requireContext(), "Selected option menu \"Add\"", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout listOfNotes = view.findViewById(R.id.list_of_notes_container);

        List<Note> notes = noteRepository.getNotes();

        for (Note note : notes) {

            View itemView = LayoutInflater.from(requireContext()).inflate(R.layout.item_note, listOfNotes, false);

            itemView.setOnClickListener(v -> {
                if (onNoteClicked != null) {
                    onNoteClicked.onNoteClicked(note);
                }
            });

            TextView noteName = itemView.findViewById(R.id.note_caption);
            noteName.setText(note.getCaption());

            listOfNotes.addView(itemView);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onNoteClicked = null;
    }

    public interface OnNoteClicked {
        void onNoteClicked(Note note);
    }
}
