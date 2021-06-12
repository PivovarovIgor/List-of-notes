package ru.geekbrains.listofnotes.ui.details;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.geekbrains.listofnotes.R;
import ru.geekbrains.listofnotes.domain.Note;

public class NoteDetailsFragment extends Fragment {

    private static final String TAG_FOR_LOG = "NoteDetailsFragment";
    private static final String KEY_NOTE = "ARG_NOTE";

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Note note = bundle.getParcelable(KEY_NOTE);

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
    public void onPause() {
        super.onPause();
        writeLog("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        writeLog("onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        writeLog("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        writeLog("onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        writeLog("onDetach");
    }

    private void writeLog(String msg) {
        Log.i(TAG_FOR_LOG, msg);
    }
}
