package ru.geekbrains.listofnotes.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Objects;

public class Note implements Parcelable {

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
    private static int countId = 0;
    private final String id;
    private String caption;
    private String description;
    private Calendar createDate;

    public Note(String caption, String description, Calendar createDate) {
        this(String.valueOf(countId++), caption, description, createDate);
    }

    public Note(String id, String caption, String description, Calendar createDate) {
        this.caption = caption;
        this.description = description;
        if (createDate == null) {
            this.createDate = Calendar.getInstance();
        } else {
            this.createDate = (Calendar) createDate.clone();
        }
        this.id = id;
    }

    public Note() {
        this("", "", null);
    }

    protected Note(Parcel in) {
        caption = in.readString();
        description = in.readString();
        createDate = (Calendar) in.readSerializable();
        id = in.readString();
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getCreateDate() {
        return (Calendar) createDate.clone();
    }

    public void setCreateDate(Calendar createDate) {
        this.createDate = (Calendar) createDate.clone();
    }

    public boolean idNoteEquals(Note note) {
        if (note == null) {
            return false;
        }
        return this.id.equals(note.id);
    }

    public String getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);
        dest.writeString(description);
        dest.writeSerializable(createDate);
        dest.writeString(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(caption, note.caption) &&
                Objects.equals(description, note.description) &&
                Objects.equals(createDate, note.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caption, description, createDate);
    }
}
