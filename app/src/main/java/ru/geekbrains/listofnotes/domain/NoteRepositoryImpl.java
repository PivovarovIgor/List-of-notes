package ru.geekbrains.listofnotes.domain;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class NoteRepositoryImpl implements NoteRepository {

    @Override
    public List<Note> getNotes() {

        List<Note> notes = new ArrayList<>();

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

        return notes;
    }
}
