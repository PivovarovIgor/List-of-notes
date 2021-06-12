package ru.geekbrains.listofnotes.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import ru.geekbrains.listofnotes.R;
import ru.geekbrains.listofnotes.domain.Note;
import ru.geekbrains.listofnotes.ui.details.NoteDetailsFragment;
import ru.geekbrains.listofnotes.ui.list.ListOfNotesFragment;

public class MainActivity extends AppCompatActivity implements ListOfNotesFragment.OnNoteCliched {

    private static final String KEY_CURRENT_NOTE = "current_note";
    private Note currentNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMainFragment();

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

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.notes_details_fragment, NoteDetailsFragment.newInstance(currentNote))
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment,
                            NoteDetailsFragment.newInstance(currentNote),
                            NoteDetailsFragment.class.getName())
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("MainActivity", "onBackPressed");
        if (currentNote != null) {
            Fragment fr = getSupportFragmentManager()
                    .findFragmentByTag(NoteDetailsFragment.class.getName());
            if (fr == null) {
                currentNote = null;
            }
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_CURRENT_NOTE, currentNote);
    }
}