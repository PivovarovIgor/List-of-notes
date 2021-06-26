package ru.geekbrains.listofnotes.ui.mainscreen;

import ru.geekbrains.listofnotes.domain.Note;

public interface EditNoteHolder {

    void beginEditingNote(Note note);

    void applyEditedNote(Note note, boolean isNewNote);

    void deleteNote(Note note);

    void undoDelete();
}
