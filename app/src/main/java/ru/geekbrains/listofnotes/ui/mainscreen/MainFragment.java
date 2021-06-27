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
import ru.geekbrains.listofnotes.domain.Callback;
import ru.geekbrains.listofnotes.domain.Note;
import ru.geekbrains.listofnotes.domain.NoteFirestoreRepository;
import ru.geekbrains.listofnotes.domain.NoteRepository;
import ru.geekbrains.listofnotes.ui.mainscreen.list.ListOfNotesFragment;
import ru.geekbrains.listofnotes.ui.mainscreen.list.NoteAction;

public class MainFragment extends Fragment implements ListOfNotesFragment.OnNoteClicked, EditNoteHolder {

    private MainFragmentRouter mainFragmentRouter;
    private NoteRepository noteRepository;
    private Note deletedNote;
    private int indexDeletedNote;

    public MainFragmentRouter getRouter() {
        return mainFragmentRouter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noteRepository = NoteFirestoreRepository.SINGLE_INSTANCE;
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
        if (isNewNote) {
            noteRepository.addNote(note, new Callback<Note>() {
                @Override
                public void onSuccess(Note result) {
                    if (result == null) {
                        Toast.makeText(requireContext(), "No note", Toast.LENGTH_LONG).show();
                        return;
                    }
                    mainFragmentRouter.showListOfNotes(result, isNewNote ? NoteAction.ADD : NoteAction.UPDATE);
                }
            });
        } else {
            noteRepository.updateNote(note, new Callback<Note>() {
                @Override
                public void onSuccess(Note result) {
                    mainFragmentRouter.showListOfNotes(result, isNewNote ? NoteAction.ADD : NoteAction.UPDATE);
                }
            });
        }
    }

    @Override
    public void deleteNote(Note note) {
        noteRepository.deleteNote(note, new Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    mainFragmentRouter.showListOfNotes(note, NoteAction.DELETE);
                    deletedNote = note;
                }
            }
        });
    }

    @Override
    public void undoDelete() {
        if (deletedNote == null) {
            Toast.makeText(requireContext(), "No note to undo delete", Toast.LENGTH_LONG).show();
            return;
        }
        noteRepository.addNote(deletedNote, new Callback<Note>() {
            @Override
            public void onSuccess(Note result) {
                mainFragmentRouter.undoDelete(result);
                deletedNote = null;
            }
        });
    }
}