package ru.geekbrains.listofnotes.ui;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import ru.geekbrains.listofnotes.R;
import ru.geekbrains.listofnotes.ui.mainscreen.MainFragment;

public class MainRouter {

    private final FragmentManager fragmentManager;

    public MainRouter(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
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
        setFragment(new MainFragment());
    }

    public boolean closeDetailFragment() {
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.main_screen_container);
        if (currentFragment instanceof MainFragment) {
            return ((MainFragment) currentFragment).getRouter().closeDetailFragment();
        }
        return false;
    }
}
