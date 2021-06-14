package ru.geekbrains.listofnotes.ui.details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
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
import java.util.Date;

import ru.geekbrains.listofnotes.R;
import ru.geekbrains.listofnotes.domain.Note;

public class NoteDetailsFragment extends Fragment {

    private static final String TAG_FOR_LOG = "NoteDetailsFragment";
    private static final String KEY_NOTE = "ARG_NOTE";
    private Note note;

    public static NoteDetailsFragment newInstance(Note note) {
        NoteDetailsFragment fragment = new NoteDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_NOTE, note);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
        View view = inflater.inflate(R.layout.fragment_note_details, container, false);
        initPopupMenu(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            note = bundle.getParcelable(KEY_NOTE);

            TextView captionView = view.findViewById(R.id.note_caption);
            captionView.setText(note.getCaption());

            TextView descriptionView = view.findViewById(R.id.note_description);
            descriptionView.setText(note.getDescription());

            TextView createDateView = view.findViewById(R.id.create_date_note);
            SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.format_date));
            Date createDate = note.getCreateDate().getTime();
            createDateView.setText(String.format("create: %s",
                    dateFormat.format(createDate)));

            DatePicker dp = view.findViewById(R.id.create_date_note_calender);
            dp.updateDate(1900 + createDate.getYear(), createDate.getMonth(), createDate.getDay());
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
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
        if (onMenuSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean onMenuSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_menu_edit) {
            Toast.makeText(requireContext(), "Select option menu \"edit\"", Toast.LENGTH_LONG).show();
            return true;
        } else if (item.getItemId() == R.id.option_menu_delete_note) {
            Snackbar.make(((Activity)requireContext()).findViewById(R.id.coordinator),
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
}
