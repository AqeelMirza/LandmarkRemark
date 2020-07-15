package com.landmarkremark;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookActivity;
import com.facebook.FacebookButtonBase;
import com.facebook.login.LoginManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.landmarkremark.Models.MarkedNote;
import com.landmarkremark.Models.User;
import com.landmarkremark.Utils.AddCustomDialog;
import com.landmarkremark.Utils.CustomDialog;
import com.landmarkremark.Utils.SharedPref;
import com.landmarkremark.ui.AllMarkers.Fragment_AllMarkers;
import com.landmarkremark.ui.home.MapsFragment;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    User user;
    FloatingActionButton fab;
    public MenuItem search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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


        // Passing each menu ID as a set of Ids because each
        View headerView = navigationView.getHeaderView(0);
        final TextView navUsername = headerView.findViewById(R.id.nav_name);
        final TextView navEmail = headerView.findViewById(R.id.nav_email);

        //Firebase initialization
        firebaseDatabase = FirebaseDatabase.getInstance();
        //getting the reference of all users
        databaseReference = firebaseDatabase.getReference("users");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //  user = dataSnapshot.getValue(User.class);

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    user = userSnapshot.getValue(User.class);
                    Log.i("All Users", user.toString());
                    if (user.getId().equalsIgnoreCase(SharedPref.getloggedId())) {
                        navUsername.setText(user.getName());
                        navEmail.setText(user.getEmail());
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Calling add Notes Dialog
                addNotes();

            }
        });
    }

    public static NavController navController;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        final Fragment_AllMarkers fragment_allMarkers = new Fragment_AllMarkers();

        search = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        //hiding search option
        search.setVisible(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //searching for the item as text changes
                fragment_allMarkers.adpaterAllMarkers.getFilter().filter(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //searching for the item as text changes
                fragment_allMarkers.adpaterAllMarkers.getFilter().filter(query);
                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                fragment_allMarkers.adpaterAllMarkers.getFilter().filter("");
                //     fragment_allMarkers.adpaterAllMarkers.notifyDataSetChanged();
                return false;
            }
        });


        return true;
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
    public void addNotes() {

        CustomDialog customDialog = new CustomDialog(databaseReference, MainActivity.this);
        customDialog.addNotes(user);

    }

    //hiding FloatingAction Button
    public void hideFAB() {
        fab.setVisibility(View.GONE);
    }

    //showing FloatingAction Button
    public void showFAB() {
        fab.setVisibility(View.VISIBLE);
    }


}
