package com.landmarkremark;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.facebook.login.LoginManager;
import com.landmarkremark.databinding.ActivityMainBinding;
import com.landmarkremark.databinding.NavHeaderMainBinding;
import com.landmarkremark.models.User;
import com.landmarkremark.repository.UserRepo;
import com.landmarkremark.utils.Utils;
import com.landmarkremark.ui.allmarkers.FragmentAllMarkers;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private AppBarConfiguration mAppBarConfiguration;
    private User user;
    private MenuItem searchMenuItem;
    private UserRepo userRepo;
    private ActivityMainBinding activityMainBinding;
    private NavHeaderMainBinding navHeaderMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);
        userRepo = new UserRepo();
        initUiComponents();
        initUser(Utils.getLoggedInUserId());
    }

    private void initUser(String userId) {
        userRepo.findUser(userId, null, user -> {
            this.user = user;
            Utils.username = user.getName();
            navHeaderMainBinding.navEmail.setText(user.getEmail());
            navHeaderMainBinding.navName.setText(user.getName());
        });
    }

    private void initUiComponents() {
        setSupportActionBar(activityMainBinding.appbarMain.toolbar);

        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_myMarkers, R.id.nav_allMarkers_onMap, R.id.nav_allMarkers)
                .setDrawerLayout(activityMainBinding.drawerLayout)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(activityMainBinding.navView, navController);
        navHeaderMainBinding = NavHeaderMainBinding.bind(activityMainBinding.navView.getHeaderView(0));
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
        FragmentAllMarkers fragment_allMarkers = (FragmentAllMarkers) getForegroundFragment();
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
            Utils.clearPref();
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
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //Getters
    public NavController getNavController() {
        return navController;
    }

    public MenuItem getSearchMenuItem() {
        return searchMenuItem;
    }
}