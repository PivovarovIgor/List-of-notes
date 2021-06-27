package ru.geekbrains.listofnotes.domain;

public interface Callback<T> {
    void onSuccess(T result);
}
