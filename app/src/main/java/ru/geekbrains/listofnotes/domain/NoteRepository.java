package ru.geekbrains.listofnotes.domain;

import java.util.List;

public interface NoteRepository {

    List<Note> getNotes();
}
