package ru.geekbrains.listofnotes.ui.auth;

import android.net.Uri;

public class AuthData {
    private final String Name;
    private final String Email;
    private final Uri uriPhoto;

    public AuthData(String name, String email, Uri uriPhoto) {
        this.Name = name;
        this.Email = email;
        this.uriPhoto = uriPhoto;
    }

    public String getEmail() {
        return Email;
    }

    public String getName() {
        return Name;
    }

    public Uri getUriPhoto() {
        return uriPhoto;
    }
}
