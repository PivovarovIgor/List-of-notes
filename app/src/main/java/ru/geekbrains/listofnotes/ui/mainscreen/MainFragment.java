package ru.geekbrains.listofnotes.ui.mainscreen;


import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.geekbrains.listofnotes.R;
import ru.geekbrains.listofnotes.domain.Note;
import ru.geekbrains.listofnotes.domain.NoteRepository;
import ru.geekbrains.listofnotes.domain.NoteRepositoryImpl;
import ru.geekbrains.listofnotes.ui.mainscreen.list.ListOfNotesFragment;

public class MainFragment extends Fragment implements ListOfNotesFragment.OnNoteClicked, EditNoteHolder {

    private MainFragmentRouter mainFragmentRouter;
    private NoteRepository noteRepository;

    public MainFragmentRouter getRouter() {
        return mainFragmentRouter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noteRepository = NoteRepositoryImpl.SINGLE_INSTANCE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainFragmentRouter = new MainFragmentRouter(getChildFragmentManager(),
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onNoteClicked(Note note) {
        mainFragmentRouter.showDetailNote(note);
    }

    @Override
    public void beginEditingNote(Note note) {
        mainFragmentRouter.beginEditingNote(note);
    }

    @Override
    public void applyEditedNote(Note note, boolean isNewNote) {
        int noteIndex;
        if (isNewNote) {
            noteIndex = noteRepository.addNote(note);
        } else {
            noteIndex = noteRepository.updateNote(note);
        }
        if (noteIndex == noteRepository.NO_NOTE) {
            Toast.makeText(requireContext(), "No note", Toast.LENGTH_LONG).show();
            return;
        }
        mainFragmentRouter.showListOfNotes(note, isNewNote);
    }

    @Override
    public void abortEditingNote() {

    }

}