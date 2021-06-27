package ru.geekbrains.listofnotes.domain;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class NoteRepositoryImpl implements NoteRepository {

    public static NoteRepository SINGLE_INSTANCE = new NoteRepositoryImpl();

    private List<Note> notes;

    private NoteRepositoryImpl() {
        initListOfNotes();
    }

    private void initListOfNotes() {

        notes = new ArrayList<>();

        notes.add(new Note("Access code of door",
                "In house, when live my friend Bob.",
                new GregorianCalendar(2021, 1, 20)));

        notes.add(new Note("PIN-code of card",
                "9999",
                new GregorianCalendar(2021, 2, 15)));

        notes.add(new Note("Date began the course of geekbrains",
                "17 february 2021",
                new GregorianCalendar(2021, 2, 17)));

        notes.add(new Note("One",
                "17 february 2021",
                new GregorianCalendar(2021, 2, 17)));


        notes.add(new Note("Two",
                "17 february 2021",
                new GregorianCalendar(2021, 2, 17)));


        notes.add(new Note("Three",
                "17 february 2021",
                new GregorianCalendar(2021, 2, 17)));
    }


    @Override
    public void getNotes(Callback<List<Note>> callback) {
        callback.onSuccess(notes);
    }

    @Override
    public void addNote(Note note, Callback<Note> callback) {
        if (note == null) {
            callback.onSuccess(null);
        }
        int index = findNoteInCollection(note);
        if (index == NO_NOTE) {
            notes.add(note);
            index = notes.size() - 1;
        } else {
            notes.set(index, note);
        }
        callback.onSuccess(note);
    }

    @Override
    public void updateNote(Note note, Callback<Note> callback) {
        int index = findNoteInCollection(note);
        if (index != NO_NOTE) {
            notes.set(index, note);
        }
        callback.onSuccess(note);
    }

    @Override
    public void deleteNote(Note note, Callback<Boolean> callback) {
        int index = findNoteInCollection(note);
        if (index != NO_NOTE) {
            notes.remove(index);
            callback.onSuccess(true);
            return;
        }
        callback.onSuccess(false);
    }

    private int findNoteInCollection(Note note) {
        for (int i = 0; i < notes.size(); i++) {
            if (note.idNoteEquals(notes.get(i))) {
                return i;
            }
        }
        return NO_NOTE;
    }
}
