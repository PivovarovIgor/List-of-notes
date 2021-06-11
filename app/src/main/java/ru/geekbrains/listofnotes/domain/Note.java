package ru.geekbrains.listofnotes.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class Note implements Parcelable {

    private final String caption;
    private final String description;
    private final Calendar createDate;

    public Note(String caption, String description, Calendar createDate) {
        this.caption = caption;
        this.description = description;
        this.createDate = (Calendar) createDate.clone();
    }

    protected Note(Parcel in) {
        caption = in.readString();
        description = in.readString();
        createDate = (Calendar) in.readSerializable();
    }

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

    public String getCaption() {
        return caption;
    }

    public String getDescription() {
        return description;
    }

    public Calendar getCreateDate() {
        return (Calendar) createDate.clone();
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
    }
}
