package ru.geekbrains.listofnotes.domain;

import java.util.List;

public interface NoteRepository {

    int NO_NOTE = -1;

    void getNotes(Callback<List<Note>> callback);

    void addNote(Note note, Callback<Note> callback);

    void updateNote(Note note, Callback<Note> callback);

    void deleteNote(Note note, Callback<Boolean> callback);

}
