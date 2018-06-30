package com.yl.youthlive;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yl.youthlive.Activitys.UserInformation;
import com.yl.youthlive.DBHandler.SessionManager;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.internetConnectivity.ConnectivityReceiver;
import com.yl.youthlive.login2POJO.login2Bean;
import com.yl.youthlive.socialPOJO.socialBean;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Login extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener  {

    Button create, login;
    CallbackManager mCallbackManager;
    ImageView facebook_login, googleLogin, twitter_login;
    public static final String mypreference = "mypref";
    private ProgressDialog pDialog;
    String msg, loginid;
    SessionManager session;
    ProgressBar progress;
    String[] PERMISSIONS = {
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CAMERA
    };

    GoogleSignInClient mGoogleSignInClient;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private int RC_SIGN_IN = 22;

    SharedPreferences pref;
    SharedPreferences.Editor edit;

    SharedPreferences fcmPref;
    SharedPreferences.Editor fcmEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkConnection();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mCallbackManager = CallbackManager.Factory.create();
        // FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);

        // Manually checking internet connection


        Toast.makeText(this, "Login.java", Toast.LENGTH_SHORT).show();

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


                                            progress.setVisibility(View.VISIBLE);

                                            final bean b = (bean) getApplicationContext();

                                            final Retrofit retrofit = new Retrofit.Builder()
                                                    .baseUrl(b.BASE_URL)
                                                    .addConverterFactory(ScalarsConverterFactory.create())
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build();

                                            final AllAPIs cr = retrofit.create(AllAPIs.class);


                                            Call<socialBean> call = cr.socialSignIn(id, email);

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


                                                        edit.putString("type", "social");
                                                        edit.putString("user", email);
                                                        edit.putString("pass", id);
                                                        edit.apply();

                                                        Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(Login.this, HomeActivity.class);
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


                                                        edit.putString("type", "social");
                                                        edit.putString("user", email);
                                                        edit.putString("pass", id);
                                                        edit.apply();

                                                        Toast.makeText(Login.this, "Please update your info", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(Login.this, UserInformation.class);
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


                                            //new FacebookloginAsyncTask().execute(email);
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
                        Toast.makeText(Login.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(Login.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        fcmPref = getSharedPreferences("fcm" , Context.MODE_PRIVATE);
        fcmEdit = fcmPref.edit();


        try {

            String tok = FirebaseInstanceId.getInstance().getToken();

            Log.d("token", tok);

            fcmEdit.putString("token" , tok);

            fcmEdit.apply();

        } catch (Exception e) {

            new Thread() {
                @Override
                public void run() {
                    //If there are stories, add them to the table
                    //try {
                        // code runs in a thread
                        //runOnUiThread(new Runnable() {
                          //  @Override
                            //public void run() {
                                new MyFirebaseInstanceIDService().onTokenRefresh();
                            //}
                        //});
                    //} catch (final Exception ignored) {
                    //}
                }
            }.start();

            e.printStackTrace();
        }


        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        edit = pref.edit();


        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("asdasd", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("asdasd", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("Asdasd", "printHashKey()", e);
        }


        String type = pref.getString("type", "");
        String em = pref.getString("user", "");
        String pa = pref.getString("pass", "");


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        /*GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);*/

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(Login.this));


        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE_ASK_PERMISSIONS);
        }


        LoginManager.getInstance().logOut();
        try {
            getUserinfo();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        session = new SessionManager(getApplicationContext());
        googleLogin = findViewById(R.id.ggleLogin);
        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });
        facebook_login = (ImageView) findViewById(R.id.facebook_login);
        facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile", "email", "user_birthday"));
            }
        });

        twitter_login = (ImageView) findViewById(R.id.twitter);
        twitter_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* TwitterAuthClient authClient = new TwitterAuthClient();
                authClient.requestEmail(session, new com.twitter.sdk.android.core.Callback<String>() {
                    @Override
                    public void success(Result<String> result) {
                        // Do something with the result, which provides the email address
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Do something on failure
                    }
                });*/

            }
        });

        create = (Button) findViewById(R.id.create);
        login = (Button) findViewById(R.id.log_first);

        progress = (ProgressBar) findViewById(R.id.progress);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                Login.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent alredyacco = new Intent(Login.this, Signin.class);
                startActivity(alredyacco);
                Login.this.overridePendingTransition(R.anim.outt_anim, R.anim.out_anim);

            }
        });


        if (Objects.equals(type, "social")) {

            socialLogin(em, pa);

        } else if (Objects.equals(type, "phone")) {

            phoneLogin(em, pa);

        }


    }


    public void phoneLogin(final String phone, final String pass) {

        progress.setVisibility(View.VISIBLE);

        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<login2Bean> call = cr.signIn(phone, pass);

        call.enqueue(new Callback<login2Bean>() {
            @Override
            public void onResponse(Call<login2Bean> call, retrofit2.Response<login2Bean> response) {


                if (Objects.equals(response.body().getStatus(), "1")) {
                    Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    b.userId = response.body().getData().getUserId();
                    b.userName = response.body().getData().getUserName();

                    try {
                        b.userImage = response.body().getData().getUserImage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    edit.putString("type", "phone");
                    edit.putString("user", phone);
                    edit.putString("pass", pass);
                    edit.commit();

                    Intent Inbt = new Intent(Login.this, HomeActivity.class);
                    Inbt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(Inbt);

                } else {
                    Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }


                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<login2Bean> call, Throwable t) {
                progress.setVisibility(View.GONE);
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


        Call<socialBean> call = cr.socialSignIn(pid, email);

        call.enqueue(new Callback<socialBean>() {
            @Override
            public void onResponse(Call<socialBean> call, retrofit2.Response<socialBean> response) {

                if (response.body().getData().getUserName().length() > 0) {

                    b.userId = response.body().getData().getUserId();
                    b.userName = response.body().getData().getUserName();

                    try {
                        b.userImage = response.body().getData().getUserImage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    edit.putString("type", "social");
                    edit.putString("user", email);
                    edit.putString("pass", pid);
                    edit.commit();

                    Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, HomeActivity.class);
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


                    edit.putString("type", "social");
                    edit.putString("user", email);
                    edit.putString("pass", pid);
                    edit.commit();

                    Toast.makeText(Login.this, "Please update your info", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, UserInformation.class);
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


    public void Alredyaccount(View view) {
        Intent alredyacco = new Intent(Login.this, Signin.class);
        startActivity(alredyacco);

    }

    public void getUserinfo() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest nd = MessageDigest.getInstance("SHA");
            nd.update(signature.toByteArray());
            Log.d("KeyHash:-", Base64.encodeToString(nd.digest(), Base64.DEFAULT));
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
////////////////////internet connectivity check///////////////
    private void checkConnection() {
    boolean isConnected = ConnectivityReceiver.isConnected();
    showSnack(isConnected);
    }
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {

            // Toast.makeText(this, "Good! Connected to Internet", Toast.LENGTH_SHORT).show();
        //    message = "Good! Connected to Internet";
        //    color = Color.WHITE;
        } else {
            //  Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
            try {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle("NO INTERNET CONNECTION")
                        .setMessage("Please check your internet connection setting and click refresh")
                        .setPositiveButton(R.string.Refresh, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            catch(Exception e)
            {
                Log.d("TAG", "Show Dialog: "+e.getMessage());
            }
      //      message = "Sorry! Not connected to internet";
       //     color = Color.RED;
        }

       /* Snackbar snackbar = Snackbar
                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
        */
    }
    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        bean.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);

    }


    @SuppressLint("StaticFieldLeak")
    private class FacebookloginAsyncTask extends AsyncTask<String, Void, Void> {    //////today list asyntask
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            //String name=params[0];
            String email = params[0];
            // String image=params[0];
            String encoded = "null";

           /* URL imageURL = null;
            try {
                imageURL = new URL(image);
                Bitmap imagee = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                // Bitmap myBitmap = BitmapFactory.decodeStream(input);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                imagee.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            String url = "http://ec2-13-58-47-70.us-east-2.compute.amazonaws.com/softcode/api/socialsign_up.php";
            ArrayList<org.apache.http.NameValuePair> nameValuePairs = new ArrayList<org.apache.http.NameValuePair>();
            //  nameValuePairs.add(new org.apache.http.message.BasicNameValuePair("name", name));
            nameValuePairs.add(new org.apache.http.message.BasicNameValuePair("email", email));
            // nameValuePairs.add(new org.apache.http.message.BasicNameValuePair("image", encoded));

            ServiceHandler sh = new ServiceHandler();
            String userdata = sh.makeServiceCall(url,
                    ServiceHandler.POST, nameValuePairs);
            /*if (userdata != null)
                try {
                    JSONObject jsonObj = new JSONObject(userdata);
                    String strstatus=jsonObj.getString("status");
                    if (strstatus.equals("1")){
                        JSONObject obj2=jsonObj.getJSONObject("data");
                        msg=jsonObj.getString("message");
                        loginid=obj2.getString("userId");
                    }else{
                        msg=jsonObj.getString("message");
                    }
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                        }
                    });
                    e.printStackTrace();
                }
            else {
                Toast.makeText(getApplicationContext(), "No Data To Display", Toast.LENGTH_SHORT).show();
            }*/
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
          /*  SharedPreferences sharedpreferences = Login.this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("userId", loginid);
            editor.commit();
            String  userId = sharedpreferences.getString("userId", "");*/

            Intent intent = new Intent(Login.this, HomeActivity.class);
            startActivity(intent);

        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (
                    ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED
                    ) {
//

            } else {
                if (
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WAKE_LOCK)
                        ) {

                    Toast.makeText(getApplicationContext(), "Permissions are required for this app", Toast.LENGTH_SHORT).show();
                    finish();

                }
                //permission is denied (and never ask again is  checked)
                //shouldShowRequestPermissionRationale will return false
                else {
                    Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                            .show();
                    finish();
                    //                            //proceed with logic by disabling the related features or quit the app.
                }
            }
        }


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(new View(this).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            final GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.


            progress.setVisibility(View.VISIBLE);

            final bean b = (bean) getApplicationContext();

            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(b.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final AllAPIs cr = retrofit.create(AllAPIs.class);


            Call<socialBean> call = cr.socialSignIn(account.getId(), account.getEmail());

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


                        edit.putString("type", "social");
                        edit.putString("user", account.getEmail());
                        edit.putString("pass", account.getId());
                        edit.commit();

                        Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, HomeActivity.class);
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


                        edit.putString("type", "social");
                        edit.putString("user", account.getEmail());
                        edit.putString("pass", account.getId());
                        edit.commit();

                        Toast.makeText(Login.this, "Please update your info", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, UserInformation.class);
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


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("asdas", "signInResult:failed code=" + e.getStatusCode());

        }
    }

}
