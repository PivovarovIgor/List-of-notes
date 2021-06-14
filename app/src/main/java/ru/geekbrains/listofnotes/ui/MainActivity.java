package ru.geekbrains.listofnotes.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import ru.geekbrains.listofnotes.R;
import ru.geekbrains.listofnotes.domain.Note;
import ru.geekbrains.listofnotes.ui.details.NoteDetailsFragment;
import ru.geekbrains.listofnotes.ui.list.AboutFragment;
import ru.geekbrains.listofnotes.ui.list.ListOfNotesFragment;

public class MainActivity extends AppCompatActivity implements ListOfNotesFragment.OnNoteClicked {

    private static final String TAG_NOTE_FRAGMENT = NoteDetailsFragment.class.getName();
    private static final String KEY_CURRENT_NOTE = "current_note";
    private Note currentNote;
    private boolean isLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer_main);

        initToolBarAndDrawer();

        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
            currentNote = savedInstanceState.getParcelable(KEY_CURRENT_NOTE);
            showNote();
        } else {
            setFragment(new ListOfNotesFragment());
        }
    }

    private void initToolBarAndDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_main_screen) {
                    setFragment(new ListOfNotesFragment());
                } else if (item.getItemId() == R.id.navigation_settings) {
                    setFragment(new SettingsFragment());
                } else if (item.getItemId() == R.id.navigation_about) {
                    setFragment(new AboutFragment());
                } else {
                    return false;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appbar, menu);
        MenuItem actionSearch = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) actionSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, "Search: " + query, Toast.LENGTH_LONG).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equals("")) {
                    Toast.makeText(MainActivity.this, "New text search: " + newText, Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .popBackStack(TAG_NOTE_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, fragment)
                .commit();
    }

    @Override
    public void onNoteClicked(Note note) {

        this.currentNote = note;
        showNote();
    }

    private void showNote() {

        if (currentNote == null) {
            return;
        }

        getSupportFragmentManager()
                .popBackStack(TAG_NOTE_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        int idContainer = (isLandscape) ? R.id.notes_details_fragment : R.id.main_fragment;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(idContainer,
                        NoteDetailsFragment.newInstance(currentNote),
                        TAG_NOTE_FRAGMENT)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(TAG_NOTE_FRAGMENT)
                .commit();
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        if (currentNote != null) {
            Fragment fr = getSupportFragmentManager()
                    .findFragmentByTag(TAG_NOTE_FRAGMENT);
            if (fr != null) {
                currentNote = null;
            }
        }
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentNote != null) {
            outState.putParcelable(KEY_CURRENT_NOTE, currentNote);
        }
    }
}