package ru.geekbrains.listofnotes.ui.mainscreen.details;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import ru.geekbrains.listofnotes.R;
import ru.geekbrains.listofnotes.domain.Note;
import ru.geekbrains.listofnotes.ui.mainscreen.EditNoteHolder;

public class NoteDetailsFragment extends Fragment {

    private static final String TAG = "NoteDetailsFragment";
    private static final String KEY_NOTE = "ARG_NOTE";
    private final int INSTANCE_ID = new Random().nextInt(100);
    private Note note;
    private EditNoteHolder editNoteHolder;

    public NoteDetailsFragment() {
        writeLog("create instance");
    }

    public static NoteDetailsFragment newInstance(Note note) {
        NoteDetailsFragment fragment = new NoteDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_NOTE, note);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        writeLog("onAttach");
        super.onAttach(context);

        if (getParentFragment() instanceof EditNoteHolder) {
            editNoteHolder = (EditNoteHolder) getParentFragment();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        writeLog("onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void initPopupMenu(View view) {
        TextView textView = view.findViewById(R.id.note_caption);
        textView.setOnClickListener(v -> {
            Activity activity = requireActivity();
            PopupMenu popupMenu = new PopupMenu(activity, v);
            activity.getMenuInflater().inflate(R.menu.menu_fragment_note, popupMenu.getMenu());
            setShareActionProvider(popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this::onMenuSelected);
            popupMenu.show();
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        writeLog("onCreateView");
        View view = inflater.inflate(R.layout.fragment_note_details, container, false);
        initPopupMenu(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        writeLog("onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            note = bundle.getParcelable(KEY_NOTE);

            TextView captionView = view.findViewById(R.id.note_caption);
            captionView.setText(note.getCaption());

            TextView descriptionView = view.findViewById(R.id.note_description);
            descriptionView.setText(note.getDescription());

            TextView createDateView = view.findViewById(R.id.create_date_note);
            SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
            Calendar createDate = note.getCreateDate();
            createDateView.setText(String.format("create: %s",
                    dateFormat.format(createDate.getTime())));
        }
    }

    @Override
    public void onStart() {
        writeLog("onStart");
        super.onStart();
    }

    @Override
    public void onPause() {
        writeLog("onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        writeLog("onResume");
        super.onResume();
    }

    @Override
    public void onStop() {
        writeLog("onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        writeLog("onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        writeLog("onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        writeLog("onDetach");
        super.onDetach();
        editNoteHolder = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        writeLog("onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_fragment_note, menu);
        setShareActionProvider(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setShareActionProvider(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.option_menu_share);
        if (menuItem == null) {
            return;
        }
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                String.format("Note: %s: %s", note.getCaption(), note.getDescription()));
        shareActionProvider.setShareIntent(shareIntent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        writeLog("onOptionsItemSelected");
        if (onMenuSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean onMenuSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_menu_edit) {
            if (editNoteHolder != null) {
                editNoteHolder.beginEditingNote(note);
            }
            return true;
        } else if (item.getItemId() == R.id.option_menu_delete_note) {
            Snackbar.make(((Activity) requireContext()).findViewById(R.id.coordinator),
                    "The note has deleted",
                    BaseTransientBottomBar.LENGTH_LONG)
                    .setAction(R.string.cancel, v -> Toast.makeText(requireContext(),
                            "Action delete has canceled",
                            Toast.LENGTH_LONG).show())
                    .show();
            return true;
        }
        return false;
    }

    private void writeLog(String create_instance) {
        Log.i(TAG, create_instance + " id:" + INSTANCE_ID + " "
                + ((note != null) ? note.getCaption() : ""));
    }
}
