package ru.geekbrains.listofnotes.ui.mainscreen.list;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
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

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

import ru.geekbrains.listofnotes.R;
import ru.geekbrains.listofnotes.domain.Note;
import ru.geekbrains.listofnotes.domain.NoteFirestoreRepository;
import ru.geekbrains.listofnotes.domain.NoteRepository;
import ru.geekbrains.listofnotes.ui.mainscreen.EditNoteHolder;
import ru.geekbrains.listofnotes.ui.mainscreen.MainFragmentRouter;

public class ListOfNotesFragment extends Fragment {

    private static final String TAG = "ListOfNotesFragment";
    private final int INSTANCE_ID = new Random().nextInt(100);
    private final NoteRepository repository = NoteFirestoreRepository.SINGLE_INSTANCE;
    private OnNoteClicked onNoteClicked;
    private EditNoteHolder editNoteHolder;
    private ListOfNotesAdapter notesAdapter;
    private Note selectedNote;
    private int scrollPosition;
    private RecyclerView listOfNotes;

    public ListOfNotesFragment() {
        writeLog("create instance");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        writeLog("onAttach");
        super.onAttach(context);

        if (getParentFragment() instanceof OnNoteClicked) {
            onNoteClicked = (OnNoteClicked) getParentFragment();
        }
        if (getParentFragment() instanceof EditNoteHolder) {
            editNoteHolder = (EditNoteHolder) getParentFragment();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        writeLog("onCreate");

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        notesAdapter = new ListOfNotesAdapter(this);
        notesAdapter.setOnNoteClickedListener(note -> onNoteClicked.onNoteClicked(note));
        notesAdapter.setOnNoteLongClickedListener(note -> selectedNote = note);

        repository.getNotes(result -> {
            notesAdapter.setData(result);
            notesAdapter.notifyDataSetChanged();
        });
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
            editNoteHolder.beginEditingNote(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        requireActivity().getMenuInflater().inflate(R.menu.menu_fragment_note, menu);
        menu.removeItem(R.id.option_menu_share);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        writeLog("onContextItemSelected");
        if (onMenuSelected(item)) {
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private boolean onMenuSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_menu_edit) {
            if (editNoteHolder != null) {
                editNoteHolder.beginEditingNote(selectedNote);
            }
            return true;
        } else if (item.getItemId() == R.id.option_menu_delete_note) {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
            dialogBuilder
                    .setTitle(R.string.title_dialog_delete)
                    .setMessage(String.format(getString(R.string.message_dialog_delete), selectedNote.getCaption()))
                    .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editNoteHolder.deleteNote(selectedNote);
                            EditNoteHolder tempEditNoteHolder = editNoteHolder;
                            Snackbar.make(((Activity) requireContext()).findViewById(R.id.coordinator),
                                    "The note has deleted",
                                    BaseTransientBottomBar.LENGTH_LONG)
                                    .setAction(R.string.cancel, v -> tempEditNoteHolder.undoDelete())
                                    .show();
                        }
                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> cancelActionToDeleteNote())
                    .setOnCancelListener(dialog -> cancelActionToDeleteNote())
                    .show();

            return true;
        }
        return false;
    }

    private void cancelActionToDeleteNote() {
        Toast.makeText(requireActivity().getApplicationContext(),
                String.format(getString(R.string.message_dialog_delete_canceled), selectedNote.getCaption()),
                Toast.LENGTH_LONG)
                .show();
        selectedNote = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        writeLog("onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        listOfNotes = view.findViewById(R.id.list_of_notes_container);

        listOfNotes.setLayoutManager(new LinearLayoutManager(requireContext()));

        listOfNotes.setAdapter(notesAdapter);

        getParentFragmentManager().setFragmentResultListener(MainFragmentRouter.KEY_RESULT, this, (requestKey, result) -> {
            if (result.containsKey(MainFragmentRouter.KEY_NOTE)) {
                Note note = result.getParcelable(MainFragmentRouter.KEY_NOTE);
                NoteAction noteAction = NoteAction.getActionByKey(result.getString(MainFragmentRouter.KEY_NOTE_ACTION));
                if (noteAction == NoteAction.ADD) {
                    scrollPosition = notesAdapter.addNote(note);
                } else if (noteAction == NoteAction.UPDATE) {
                    notesAdapter.updateNote(note);
                } else if (noteAction == NoteAction.DELETE) {
                    notesAdapter.delete(note);
                }
            }
        });

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

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (scrollPosition > 0) {
                listOfNotes.smoothScrollToPosition(scrollPosition);
                scrollPosition = 0;
            }
        }, 200L);
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
        editNoteHolder = null;
    }

    private void writeLog(String create_instance) {
        Log.i(TAG, create_instance + " id:" + INSTANCE_ID);
    }

    public void addNote(Note note) {
        notesAdapter.addNote(note);
    }

    public interface OnNoteClicked {
        void onNoteClicked(Note note);
    }
}
