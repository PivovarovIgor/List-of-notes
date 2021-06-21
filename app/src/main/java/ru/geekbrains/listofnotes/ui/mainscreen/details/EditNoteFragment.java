package ru.geekbrains.listofnotes.ui.mainscreen.details;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import ru.geekbrains.listofnotes.R;
import ru.geekbrains.listofnotes.domain.Note;
import ru.geekbrains.listofnotes.ui.mainscreen.EditNote;

public class EditNoteFragment extends Fragment {

    private static final String KEY_NOTE = "ARG_NOTE";

    private EditText editCaption;
    private EditText editDescription;
    private DatePicker editCreateDate;
    private EditNote editNote;
    private Note note;

    public static EditNoteFragment newInstance(Note note) {
        EditNoteFragment editNoteFragment = new EditNoteFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_NOTE, note);
        editNoteFragment.setArguments(bundle);
        return editNoteFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editCaption = view.findViewById(R.id.edit_caption);
        editDescription = view.findViewById(R.id.edit_description);
        editCreateDate = view.findViewById(R.id.edit_create_date_note_calender);

        note = requireArguments().getParcelable(KEY_NOTE);

        editCaption.setText(note.getCaption());
        editDescription.setText(note.getDescription());
        Calendar createDate = note.getCreateDate();
        editCreateDate.updateDate(createDate.get(Calendar.YEAR),
                createDate.get(Calendar.MONTH),
                createDate.get(Calendar.DAY_OF_MONTH));

        if (getParentFragment() instanceof EditNote) {
            editNote = (EditNote) getParentFragment();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.applyEdit) {
            if (editNote != null) {

                Calendar newCreateDate = Calendar.getInstance();
                newCreateDate.set(Calendar.YEAR, editCreateDate.getYear());
                newCreateDate.set(Calendar.MONTH, editCreateDate.getMonth());
                newCreateDate.set(Calendar.DAY_OF_MONTH, editCreateDate.getDayOfMonth());

                note.setCaption(editCaption.getText().toString());
                note.setDescription(editDescription.getText().toString());
                note.setCreateDate(newCreateDate);

                Activity activity = requireActivity();
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                //Find the currently focused view, so we can grab the correct window token from it.
                View view = activity.getCurrentFocus();
                //If no view currently has focus, create a new one, just so we can grab a window token from it
                if (view == null) {
                    view = new View(activity);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                editNote.applyEditedNote(note);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}