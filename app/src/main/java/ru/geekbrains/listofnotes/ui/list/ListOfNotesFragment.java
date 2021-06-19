package ru.geekbrains.listofnotes.ui.list;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

import ru.geekbrains.listofnotes.R;
import ru.geekbrains.listofnotes.domain.Note;
import ru.geekbrains.listofnotes.domain.NoteRepository;
import ru.geekbrains.listofnotes.domain.NoteRepositoryImpl;

public class ListOfNotesFragment extends Fragment {

    private static final String TAG = "ListOfNotesFragment";
    private final int INSTANCE_ID = new Random().nextInt(100);

    private NoteRepository noteRepository;
    private OnNoteClicked onNoteClicked;

    public ListOfNotesFragment() {
        writeLog("create instance");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        writeLog("onAttach");
        super.onAttach(context);

        if (context instanceof OnNoteClicked) {
            onNoteClicked = (OnNoteClicked) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        writeLog("onCreate");

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        noteRepository = new NoteRepositoryImpl();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        writeLog("onCreateView");

        return inflater.inflate(R.layout.fragment_list_of_notes, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        writeLog("onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_list_of_notes, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        writeLog("onOptionsItemSelected");
        if (item.getItemId() == R.id.option_menu_add_note) {
            Toast.makeText(requireContext(), "Selected option menu \"Add\"", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        writeLog("onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        RecyclerView listOfNotes = view.findViewById(R.id.list_of_notes_container);

        listOfNotes.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<Note> notes = noteRepository.getNotes();

        ListOfNotesAdapter notesAdapter = new ListOfNotesAdapter();
        notesAdapter.setData(notes);
        notesAdapter.setOnNoteClickedListener(note -> onNoteClicked.onNoteClicked(note));

        listOfNotes.setAdapter(notesAdapter);

        //notesAdapter.notifyDataSetChanged();
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
        onNoteClicked = null;
    }

    public interface OnNoteClicked {
        void onNoteClicked(Note note);
    }

    private void writeLog(String create_instance) {
        Log.i(TAG, create_instance + " id:" + INSTANCE_ID);
    }
}
