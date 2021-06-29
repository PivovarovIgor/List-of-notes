package ru.geekbrains.listofnotes.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Random;

import ru.geekbrains.listofnotes.R;
import ru.geekbrains.listofnotes.ui.auth.AuthData;
import ru.geekbrains.listofnotes.ui.auth.AuthFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private final int INSTANCE_ID = new Random().nextInt(100);
    private MainRouter mainRouter;

    public MainActivity() {
        writeLog("create instance");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer_main);

        mainRouter = new MainRouter(getSupportFragmentManager(), getApplicationContext());

        initToolBarAndDrawer();

        if (savedInstanceState != null) {
            writeLog("onCreate savedInstanceState != null");
        } else {
            writeLog("onCreate");
            mainRouter.showNotes();
        }

        getSupportFragmentManager().setFragmentResultListener(AuthFragment.AUTH_RESULT, this, (requestKey, result) -> {
            initHead();
            mainRouter.showNotes();
        });
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
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_main_screen) {
                mainRouter.showNotes();
            } else if (item.getItemId() == R.id.navigation_settings) {
                mainRouter.showSettings();
            } else if (item.getItemId() == R.id.navigation_about) {
                mainRouter.showInfo();
            } else {
                return false;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        initHead();
    }

    private void initHead() {

        NavigationView navigationView = findViewById(R.id.navigation_view);

        View headView = navigationView.getHeaderView(0);

        TextView userNameView = headView.findViewById(R.id.user_name);
        TextView userEmailView = headView.findViewById(R.id.user_email);
        MaterialButton unsignButton = headView.findViewById(R.id.unsign);

        unsignButton.setOnClickListener(v -> {
            AuthFragment.unSign(getApplicationContext());
            initHead();
            mainRouter.showNotes();
        });

        AuthData authData = AuthFragment.getAuthorizeData(getApplicationContext());
        if (authData != null) {
            userNameView.setText(authData.getName());
            userEmailView.setText(authData.getEmail());
            unsignButton.setVisibility(View.VISIBLE);
        } else {
            userNameView.setText(R.string.unauthorized);
            userEmailView.setText("");
            unsignButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        writeLog("onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        writeLog("onResume");
        super.onResume();
    }

    @Override
    protected void onResumeFragments() {
        writeLog("onResumeFragments");
        super.onResumeFragments();
    }

    @Override
    protected void onPause() {
        writeLog("onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        writeLog("onStop");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        writeLog("onRestart");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        writeLog("onDestroy");
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        writeLog("onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        writeLog("onCreateOptionsMenu");
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

    @Override
    public void onBackPressed() {

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        } else if (mainRouter.closeDetailFragment()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        writeLog("onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    private void writeLog(String create_instance) {
        Log.i(TAG, create_instance + " id:" + INSTANCE_ID);
    }
}