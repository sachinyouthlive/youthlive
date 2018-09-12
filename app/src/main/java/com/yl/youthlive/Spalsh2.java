package com.yl.youthlive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.yl.youthlive.Activitys.UserInformation;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.socialPOJO.socialBean;

import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Spalsh2 extends AppCompatActivity {

    Button signup, signin;
    ImageView facebook, google;
    TextView privacy, terms;
    ProgressBar progress;
    SharePreferenceUtils sharePreferenceUtils;
    SharedPreferences fcmPref;
    SharedPreferences.Editor fcmEdit;
    CallbackManager mCallbackManager;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 12;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mCallbackManager = CallbackManager.Factory.create();
        // FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);

        // Manually checking internet connection


        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());
                                        Log.d("LoginJSONActivity", object.toString());
                                        try {
                                            JSONObject json = response.getJSONObject();
                                            final String name = json.getString("name");
                                            final String email = json.getString("email");
                                            final String id = json.getString("id");
                                            Log.d("FACEBOOKNAME", name + " Email > " + email);
                                            Log.d("FACEBOOKNAME", name + " Email > " + id);
                                            //   String profilePicUrl = json.getJSONObject("picture").getJSONObject("data").getString("url");
                                            String image = "https://graph.facebook.com/" + id + "/picture?type=large";


                                            socialLogin(email , id);


                                        } catch (Exception e) {

                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday,picture.type(large)");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(Spalsh2.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(Spalsh2.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //FirebaseApp.initializeApp(getApplicationContext());


        try {
            mAuth = FirebaseAuth.getInstance();
        }catch (Exception e)
        {
            e.printStackTrace();
        }




        signup = findViewById(R.id.create);
        signin = findViewById(R.id.log_first);
        facebook = findViewById(R.id.facebook_login);
        google = findViewById(R.id.ggleLogin);
        privacy = findViewById(R.id.privacy);
        terms = findViewById(R.id.terms);
        progress = findViewById(R.id.progress);


        fcmPref = getSharedPreferences("fcm", Context.MODE_PRIVATE);
        fcmEdit = fcmPref.edit();


        try {

            String tok = FirebaseInstanceId.getInstance().getToken();

            Log.d("token", tok);

            fcmEdit.putString("token", tok);

            fcmEdit.apply();

        } catch (Exception e) {

            new Thread() {
                @Override
                public void run() {

                    new MyFirebaseInstanceIDService().onTokenRefresh();

                }
            }.start();

            e.printStackTrace();
        }


        sharePreferenceUtils = SharePreferenceUtils.getInstance();


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Spalsh2.this, Terms.class);
                intent.putExtra("title", "Privacy Policy");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Spalsh2.this, TnC.class);
                intent.putExtra("title", "Terms & Conditions");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent alredyacco = new Intent(Spalsh2.this, Signin.class);
                startActivity(alredyacco);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Spalsh2.this, Register.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(Spalsh2.this, Arrays.asList("public_profile", "email", "user_birthday"));
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }

    public void socialLogin(final String email, final String pid) {
        progress.setVisibility(View.VISIBLE);

        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        SharedPreferences fcmPref = getSharedPreferences("fcm", Context.MODE_PRIVATE);

        String keey = fcmPref.getString("token", "");


        Call<socialBean> call = cr.socialSignIn(pid, email, keey);

        call.enqueue(new Callback<socialBean>() {
            @Override
            public void onResponse(Call<socialBean> call, Response<socialBean> response) {

                if (response.body().getData().getUserName().length() > 0) {

                    b.userId = response.body().getData().getUserId();
                    b.userName = response.body().getData().getUserName();

                    try {
                        b.userImage = response.body().getData().getUserImage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    sharePreferenceUtils.putString("type", "social");
                    sharePreferenceUtils.putString("user", email);
                    sharePreferenceUtils.putString("pass", pid);
                    if (response.body() != null) {
                        sharePreferenceUtils.putString("userType", response.body().getData().getType());
                    }
                    if (response.body() != null) {
                        sharePreferenceUtils.putString("yid", response.body().getData().getYouthLiveId());
                    }


                    //    Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Spalsh2.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                } else {

                    b.userId = response.body().getData().getUserId();
                    b.userName = response.body().getData().getUserName();

                    try {
                        b.userImage = response.body().getData().getUserImage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    sharePreferenceUtils.putString("type", "social");
                    sharePreferenceUtils.putString("user", email);
                    sharePreferenceUtils.putString("pass", pid);

                    Toast.makeText(Spalsh2.this, "Please update your info", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Spalsh2.this, UserInformation.class);
                    intent.putExtra("userId", response.body().getData().getUserId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                }


                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<socialBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("asdasdasd", "Google sign in failed", e);
                // ...
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("asdasd", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("asdasd", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String email = user.getEmail();
                            String pid = user.getUid();

                            socialLogin(email , pid);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("asdasd", "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

}
