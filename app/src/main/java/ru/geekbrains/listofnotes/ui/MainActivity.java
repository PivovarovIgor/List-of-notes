package ru.geekbrains.listofnotes.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ru.geekbrains.listofnotes.R;
import ru.geekbrains.listofnotes.domain.Note;
import ru.geekbrains.listofnotes.ui.details.NoteDetailsActivity;
import ru.geekbrains.listofnotes.ui.details.NoteDetailsFragment;
import ru.geekbrains.listofnotes.ui.list.ListOfNotesFragment;

public class MainActivity extends AppCompatActivity implements ListOfNotesFragment.OnNoteCliched {

    private static final String KEY_CURRENT_NOTE = "current_note";
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            note = savedInstanceState.getParcelable(KEY_CURRENT_NOTE);
            showNote();
        }
    }

    @Override
    public void onNoteClicked(Note note) {

        this.note = note;
        showNote();
    }

    private void showNote() {

        if (note == null) {
            return;
        }

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.notes_details_fragment, NoteDetailsFragment.newInstance(note))
                    .commit();
        } else {
            Intent intent = new Intent(this, NoteDetailsActivity.class);
            intent.putExtra(NoteDetailsActivity.ARG_NOTE, note);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (note != null) {
            outState.putParcelable(KEY_CURRENT_NOTE, note);
        }
    }
}