package ru.geekbrains.listofnotes.domain;

import java.util.List;

public interface NoteRepository {

    static final int NO_NOTE = -1;

    List<Note> getNotes();

    int addNote(Note note);

    int updateNote(Note note);

    int deleteNote(Note note);
}
