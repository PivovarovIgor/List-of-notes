package ru.geekbrains.listofnotes.domain;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class NoteRepositoryImpl implements NoteRepository {

    public static NoteRepositoryImpl SINGLE_INSTANCE = new NoteRepositoryImpl();

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
    public List<Note> getNotes() {
        return notes;
    }

    @Override
    public int addNote(Note note) {
        if (note == null) {
            return NO_NOTE;
        }
        int index = findNoteInCollection(note);
        if (index == NO_NOTE) {
            notes.add(note);
            index = notes.size() - 1;
        } else {
            notes.set(index, note);
        }
        return index;
    }

    @Override
    public int updateNote(Note note) {
        int index = findNoteInCollection(note);
        if (index != NO_NOTE) {
            notes.set(index, note);
        }
        return index;
    }

    @Override
    public int deleteNote(Note note) {
        int index = findNoteInCollection(note);
        if (index != NO_NOTE) {
            notes.remove(index);
        }
        return index;
    }

    @Override
    public int undoDeleteNote(Note note, int indexRecover) {
        int index = findNoteInCollection(note);
        if (index != NO_NOTE) {
            return index;
        }
        if (indexRecover > notes.size() - 1) {
            notes.add(note);
            indexRecover = notes.size() - 1;
        } else {
            notes.add(indexRecover, note);
        }
        return indexRecover;
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
