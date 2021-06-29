package ru.geekbrains.listofnotes.ui.auth;

public class AuthData {
    private final String Name;
    private final String Email;

    public AuthData(String name, String email) {
        this.Name = name;
        this.Email = email;
    }

    public String getEmail() {
        return Email;
    }

    public String getName() {
        return Name;
    }
}
