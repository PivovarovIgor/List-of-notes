package ru.geekbrains.listofnotes.ui;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import ru.geekbrains.listofnotes.R;
import ru.geekbrains.listofnotes.ui.auth.AuthFragment;
import ru.geekbrains.listofnotes.ui.mainscreen.MainFragment;

public class MainRouter {

    private final FragmentManager fragmentManager;
    private final Context context;

    public MainRouter(FragmentManager fragmentManager, Context context) {
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    public void showInfo() {
        setFragment(new InfoFragment());
    }

    public void showSettings() {
        setFragment(new SettingsFragment());
    }

    private void setFragment(Fragment fragment) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.main_screen_container, fragment)
                .commit();
    }

    public void showNotes() {
        if (AuthFragment.getAuthorizeData(context, null)) {
            setFragment(new MainFragment());
        } else {
            setFragment(AuthFragment.newInstance());
        }
    }

    public boolean closeDetailFragment() {
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.main_screen_container);
        if (currentFragment instanceof MainFragment) {
            return ((MainFragment) currentFragment).getRouter().closeDetailFragment();
        }
        return false;
    }
}
