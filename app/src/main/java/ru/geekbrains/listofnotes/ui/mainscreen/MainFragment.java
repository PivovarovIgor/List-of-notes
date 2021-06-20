package ru.geekbrains.listofnotes.ui.mainscreen;


import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import ru.geekbrains.listofnotes.R;
import ru.geekbrains.listofnotes.domain.Note;
import ru.geekbrains.listofnotes.ui.mainscreen.list.ListOfNotesFragment;

public class MainFragment extends Fragment implements ListOfNotesFragment.OnNoteClicked {

    private MainFragmentRouter mainFragmentRouter;

    public MainFragmentRouter getRouter() {
        return mainFragmentRouter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainFragmentRouter = new MainFragmentRouter(getChildFragmentManager(),
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onNoteClicked(Note note) {
        mainFragmentRouter.showDetailNote(note);
    }
}