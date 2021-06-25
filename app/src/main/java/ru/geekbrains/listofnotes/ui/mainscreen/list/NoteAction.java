package ru.geekbrains.listofnotes.ui.mainscreen.list;

public enum NoteAction {

    ADD("ADD"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    private final String key;

    NoteAction(String key) {
        this.key = key;
    }

    public static NoteAction getActionByKey(String key) {
        for (NoteAction val : NoteAction.values()) {
            if (val.getKey().equals(key)) {
                return val;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }
}
