package ru.geekbrains.listofnotes.domain;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class NoteFirestoreRepository implements NoteRepository{

    public static NoteRepository SINGLE_INSTANCE = new NoteFirestoreRepository();

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private static final String NOTES = "notes";
    private static final String CREATE_DATE = "create date";
    private static final String CAPTION = "caption";
    private static final String DESCRIPTION = "description";

    private NoteFirestoreRepository() {
    }

    @Override
    public void getNotes(Callback<List<Note>> callback) {

        firebaseFirestore.collection(NOTES)
                .orderBy(CREATE_DATE, Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            ArrayList<Note> result = new ArrayList<>();

                            for (QueryDocumentSnapshot document: task.getResult()) {
                                String caption = (String) document.get(CAPTION);
                                String description = (String) document.get(DESCRIPTION);
                                Date createDate = ((Timestamp) document.get(CREATE_DATE)).toDate();
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(createDate);
                                result.add(new Note(document.getId(), caption, description, calendar));
                            }

                            callback.onSuccess(result);

                        } else {
                            //task.getException();
                        }
                    }
                });
    }

    @Override
    public void addNote(Note note, Callback<Note> callback) {

        firebaseFirestore.collection(NOTES)
                .add(getDataToWrite(note))
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Note newNote = new Note(task.getResult().getId(),
                                    note.getCaption(),
                                    note.getDescription(),
                                    note.getCreateDate());
                            callback.onSuccess(note);
                        }
                    }
                });
    }

    @NotNull
    private HashMap<String, Object> getDataToWrite(Note note) {
        HashMap<String, Object> data = new HashMap<>();
        data.put(CAPTION, note.getCaption());
        data.put(DESCRIPTION, note.getDescription());
        data.put(CREATE_DATE, note.getCreateDate().getTime());
        return data;
    }

    @Override
    public void updateNote(Note note, Callback<Note> callback) {
        firebaseFirestore.collection(NOTES)
                .document(note.getId())
                .update(getDataToWrite(note))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess(note);
                        } else {
                            callback.onSuccess(null);
                        }
                    }
                });
    }

    @Override
    public void deleteNote(Note note, Callback<Boolean> callback) {

        firebaseFirestore.collection(NOTES)
                .document(note.getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onSuccess(task.isSuccessful());
                    }
                });
    }
}
