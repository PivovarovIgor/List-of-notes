package ru.geekbrains.listofnotes.ui.mainscreen;

import ru.geekbrains.listofnotes.domain.Note;

public interface EditNote {

    void beginEditingNote(Note note);

    void applyEditedNote(Note note);

    void abortEditingNote();
}
