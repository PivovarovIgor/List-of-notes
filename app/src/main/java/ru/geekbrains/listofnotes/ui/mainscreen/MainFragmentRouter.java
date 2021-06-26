package ru.geekbrains.listofnotes.ui.mainscreen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.geekbrains.listofnotes.R;
import ru.geekbrains.listofnotes.domain.Note;
import ru.geekbrains.listofnotes.ui.mainscreen.details.EditNoteFragment;
import ru.geekbrains.listofnotes.ui.mainscreen.details.NoteDetailsFragment;
import ru.geekbrains.listofnotes.ui.mainscreen.list.ListOfNotesFragment;
import ru.geekbrains.listofnotes.ui.mainscreen.list.NoteAction;

public class MainFragmentRouter {

    public static final String KEY_RESULT = "KEY_RESULT";
    public static final String KEY_NOTE = "KEY_NOTE";
    public static final String KEY_NOTE_ACTION = "KEY_NOTE_ACTION";
    private final FragmentManager fragmentManager;
    private final boolean isLandscape;

    public MainFragmentRouter(FragmentManager fragmentManager, boolean isLandscape) {
        this.fragmentManager = fragmentManager;
        this.isLandscape = isLandscape;
        configureListFragment();
    }

    public void showDetailNote(Note note) {
        if (note == null) {
            setFragment(new ListOfNotesFragment());
        } else {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.notes_details_fragment,
                            NoteDetailsFragment.newInstance(note))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
    }

    private void configureListFragment() {
        Fragment frToRemove = fragmentManager.findFragmentById(getContainerViewIdOfList(!isLandscape));
        Fragment frToReplace = fragmentManager.findFragmentById(getContainerViewIdOfList(isLandscape));
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (frToRemove instanceof ListOfNotesFragment) {
            ft.remove(frToRemove);
        }
        if (isLandscape || frToReplace == null) {
            ft.add(getContainerViewIdOfList(isLandscape), new ListOfNotesFragment());
        }
        if (!ft.isEmpty()) {
            ft.commit();
        }
    }

    public void showListOfNotes(Note note, NoteAction noteAction) {
        ListOfNotesFragment listOfNotesFragment = new ListOfNotesFragment();
        if (note != null) {
            Bundle bundleResult = new Bundle();
            bundleResult.putParcelable(KEY_NOTE, note);
            bundleResult.putString(KEY_NOTE_ACTION, noteAction.getKey());
            fragmentManager.setFragmentResult(KEY_RESULT, bundleResult);
        }
        setFragment(listOfNotesFragment);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction ft = fragmentManager
                .beginTransaction()
                .replace(getContainerViewIdOfList(isLandscape), fragment);
        if (isLandscape) {
            Fragment frToRemove = fragmentManager.findFragmentById(getContainerViewIdOfList(false));
            if (frToRemove != null) {
                ft.remove(frToRemove);
            }
        }
        ft.commit();
    }

    private int getContainerViewIdOfList(boolean isLand) {
        return isLand ? R.id.list_of_notes_fragment : R.id.notes_details_fragment;
    }

    public void undoDelete(Note note, int index) {
        Fragment fragment = fragmentManager.findFragmentById(getContainerViewIdOfList(isLandscape));
        if (fragment instanceof ListOfNotesFragment) {
            ((ListOfNotesFragment) fragment).undoDeleteNote(note, index);
        }
    }

    public boolean closeDetailFragment() {
        Fragment fragment = fragmentManager
                .findFragmentById(R.id.notes_details_fragment);
        if (!(fragment instanceof ListOfNotesFragment)) {
            if (isLandscape && fragment != null) {
                fragmentManager
                        .beginTransaction()
                        .remove(fragment)
                        .commit();
            } else {
                setFragment(new ListOfNotesFragment());
            }
            return true;
        }
        return false;
    }

    public void beginEditingNote(Note note) {
        setFragment(EditNoteFragment.newInstance(note));
    }
}
