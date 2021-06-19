package ru.geekbrains.listofnotes.ui;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.geekbrains.listofnotes.R;
import ru.geekbrains.listofnotes.domain.Note;
import ru.geekbrains.listofnotes.ui.details.NoteDetailsFragment;
import ru.geekbrains.listofnotes.ui.list.ListOfNotesFragment;

public class MainRouter {

    private static final String TAG_LIST_OF_NOTES_FRAGMENT = "ListOfNotesFragment";
    private final FragmentManager fragmentManager;
    private final boolean isLandscape;

    public MainRouter(FragmentManager fragmentManager, boolean isLandscape) {
        this.fragmentManager = fragmentManager;
        this.isLandscape = isLandscape;
        setListFragment();
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

    public void showInfo() {
        setFragment(new InfoFragment());
    }

    public void showSettings() {
        setFragment(new SettingsFragment());
    }

    private void setListFragment() {
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

    public boolean closeDetailFragment() {
        Fragment fragment = fragmentManager
                .findFragmentById(R.id.notes_details_fragment);
        if (fragment instanceof NoteDetailsFragment) {
            if (isLandscape) {
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

    public void showNotes() {
        setFragment(new ListOfNotesFragment());
    }
}
