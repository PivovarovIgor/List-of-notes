package ru.geekbrains.listofnotes.ui.details;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ru.geekbrains.listofnotes.R;
import ru.geekbrains.listofnotes.domain.Note;

public class NoteDetailsActivity extends AppCompatActivity {

    public static final String ARG_NOTE = "ARG_NOTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        if (savedInstanceState == null) {

            Note note = getIntent().getParcelableExtra(ARG_NOTE);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.note_detail_container, NoteDetailsFragment.newInstance(note))
                    .commit();
        }
    }
}