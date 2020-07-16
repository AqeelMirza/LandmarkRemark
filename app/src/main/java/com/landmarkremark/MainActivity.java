package com.landmarkremark;

import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.landmarkremark.models.MarkedNote;
import com.landmarkremark.models.User;
import com.landmarkremark.repository.UserRepo;
import com.landmarkremark.utils.CustomDialog;
import com.landmarkremark.utils.SharedPref;
import com.landmarkremark.ui.allmarkers.Fragment_AllMarkers;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private User user;
    private FloatingActionButton fab;
    private MenuItem searchMenuItem;
    private TextView navUsername, navEmail;
    private Location markedLocation;
    private UserRepo userRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userRepo = new UserRepo();
        initUiComponents();
        initUser(SharedPref.getLoggedInUserId());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNotesInDb(user);
            }
        });
    }

    private void initUser(String userId) {
        userRepo.findUser(userId, user -> {
            this.user = user;
            navEmail.setText(user.getEmail());
            navUsername.setText(user.getName());
        });
    }

    private void initUiComponents() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_myMarkers, R.id.nav_allMarkers)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View headerView = navigationView.getHeaderView(0);
        navUsername = headerView.findViewById(R.id.nav_name);
        navEmail = headerView.findViewById(R.id.nav_email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        initSearchComponents(menu);
        return true;
    }

    private void initSearchComponents(Menu menu) {
        searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        //hiding search option
        searchMenuItem.setVisible(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //searching for the item as text changes
                searchItem(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //searching for the item as text changes
                searchItem(query);
                return true;
            }
        });
    }

    private void searchItem(String query) {
        Fragment_AllMarkers fragment_allMarkers = (Fragment_AllMarkers) getForegroundFragment();
        fragment_allMarkers.searchItem(query);
    }

    public Fragment getForegroundFragment() {
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        Fragment currentFragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
        return navHostFragment == null ? null : currentFragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            //clearing the sharedPref
            SharedPref.clearPref();
            //logging out from facebook
            LoginManager.getInstance().logOut();
            //closing the Activity
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //adding notes at current location
    private void saveNotesInDb(final User user) {
        MarkedNote markedNote = fromLocation(getMarkedLocation());
        CustomDialog customDialog = new CustomDialog(MainActivity.this);
        customDialog.promptAndAcceptNote(user, markedNote, () -> userRepo.addNotesToDb(user, markedNote));
    }

    private MarkedNote fromLocation(Location location) {
        MarkedNote note = new MarkedNote();
        note.setAddress(SharedPref.convertLatLngToAddress(this, location.getLatitude(), location.getLongitude()));
        note.setLatitude(String.valueOf(location.getLatitude()));
        note.setLongitude(String.valueOf(location.getLongitude()));
        return note;
    }

    //hiding FloatingAction Button
    public void hideFab() {
        fab.setVisibility(View.GONE);
    }

    //showing FloatingAction Button
    public void showFab() {
        fab.setVisibility(View.VISIBLE);
    }

    //Getters
    public NavController getNavController() {
        return navController;
    }

    public MenuItem getSearchMenuItem() {
        return searchMenuItem;
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public Location getMarkedLocation() {
        return markedLocation;
    }

    public void setMarkedLocation(Location markedLocation) {
        this.markedLocation = markedLocation;
    }
}