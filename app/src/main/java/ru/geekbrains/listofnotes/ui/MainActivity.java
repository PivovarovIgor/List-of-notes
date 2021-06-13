package ru.geekbrains.listofnotes.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.geekbrains.listofnotes.R;
import ru.geekbrains.listofnotes.domain.Note;
import ru.geekbrains.listofnotes.ui.details.NoteDetailsFragment;
import ru.geekbrains.listofnotes.ui.list.ListOfNotesFragment;

public class MainActivity extends AppCompatActivity implements ListOfNotesFragment.OnNoteCliched {

    private static final String TAG_NOTE_FRAGMENT = NoteDetailsFragment.class.getName();
    private static final String KEY_CURRENT_NOTE = "current_note";
    private Note currentNote;
    private boolean isLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMainFragment();
        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        getSupportFragmentManager().removeOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                currentNote = null;
            }
        });

        if (savedInstanceState != null) {
            currentNote = savedInstanceState.getParcelable(KEY_CURRENT_NOTE);
            showNote();
        }
    }

    private void initMainFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, new ListOfNotesFragment())
                .commit();
    }

    @Override
    public void onNoteClicked(Note note) {

        this.currentNote = note;
        showNote();
    }

    private void showNote() {

        if (currentNote == null) {
            return;
        }

        getSupportFragmentManager()
                .popBackStack(TAG_NOTE_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        int idContainer = (isLandscape) ? R.id.notes_details_fragment : R.id.main_fragment;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(idContainer,
                        NoteDetailsFragment.newInstance(currentNote),
                        TAG_NOTE_FRAGMENT)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(TAG_NOTE_FRAGMENT)
                .commit();
    }

    @Override
    public void onBackPressed() {
        Log.i("MainActivity", "onBackPressed");
        if (currentNote != null) {
            Fragment fr = getSupportFragmentManager()
                    .findFragmentByTag(TAG_NOTE_FRAGMENT);
            if (fr != null) {
                currentNote = null;
            }
        }
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentNote != null) {
            outState.putParcelable(KEY_CURRENT_NOTE, currentNote);
        }
    }
}