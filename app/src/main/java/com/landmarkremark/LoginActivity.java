package com.landmarkremark;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.landmarkremark.databinding.ActivityLoginBinding;
import com.landmarkremark.models.User;
import com.landmarkremark.repository.UserRepo;
import com.landmarkremark.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private static final String EMAIL = "email";
    private static final String TAG = "LoginActivity";
    private static final String AUTH_TYPE = "rerequest";//Location Permission tag
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    //Facebook Callback
    private CallbackManager mCallbackManager;
    private ActivityLoginBinding activityLoginBinding;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = activityLoginBinding.getRoot();
        setContentView(view);

        FacebookLoginOnClick();

        //checking if the user is already loggedIn
        if (Utils.getIsLoggedIn()) {
            // check for permissions and navigating to MainActivity
            requestPermissionAndNavigate();
        }
    }

    private void FacebookLoginOnClick() {
        mCallbackManager = CallbackManager.Factory.create();

        // Set the initial permissions to request from the user while logging in
        activityLoginBinding.fbLoginBtn.setPermissions(Arrays.asList(EMAIL, "public_profile"));

        activityLoginBinding.fbLoginBtn.setAuthType(AUTH_TYPE);

        // Register a callback to respond to the user
        activityLoginBinding.fbLoginBtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                setResult(RESULT_OK);
                // Fetching Facebook details of the user
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v(TAG, response.toString());
                                // Application code
                                onLoginSuccess(object);
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                setResult(RESULT_CANCELED);
                finish();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(LoginActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                throw new RuntimeException("Unable to access Facebook", e);
            }
        });
    }

    //on Success result from Facebook
    private void onLoginSuccess(JSONObject object) {
        final User user;
        try {
            user = createUser(object);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create user from JSON", e);
        }
        initSharedPref(user);
        syncDb(user);
        requestPermissionAndNavigate();
    }

    //syncing with database to find user
    private void syncDb(User newUser) {
        UserRepo userRepo = new UserRepo();
        userRepo.findUser(newUser.getId(), newUser, user -> {
            //If user exists
            Log.i("LoginActivity", "User found");
        });
    }

    private void initSharedPref(User user) {
        // save value if LoggedIn successfully
        Utils.setIsLoggedIn(true);
        Utils.setLoggedId(user.getId());
    }

    private User createUser(JSONObject object) throws JSONException {
        User user = new User();
        user.setId(object.getString("id"));
        user.setEmail(object.getString("email"));
        user.setName(object.getString("name"));
        return user;
    }

    //navigating to MainActivity
    private void moveToMainActivity() {
        Intent in = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(in);
        finish();
    }

    //checking for Location permission
    private void requestPermissionAndNavigate() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //if permission is already granted, move to MainActivity
            moveToMainActivity();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                //Toast.makeText(LoginActivity.this, "Location Permission not granted.", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                // MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Toast.makeText(getApplicationContext(), R.string.granted, Toast.LENGTH_SHORT).show();
                    moveToMainActivity();
                } else {
                    // permission denied,
                    // functionality that depends on this permission may not work properly.
                    Toast.makeText(getApplicationContext(), R.string.denied, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
