package com.yl.youthlive;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.yl.youthlive.Activitys.UserInformation;
import com.yl.youthlive.DBHandler.SessionManager;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.loginResponsePOJO.loginResponseBean;
import com.yl.youthlive.socialPOJO.socialBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.yl.youthlive.R.id.Signup;

public class Register extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 12;
    private static final String BASEESIGNUP_URL = "http://ec2-13-58-47-70.us-east-2.compute.amazonaws.com/softcode/api/sign_up.php";
    private static final String TWITTER_KEY = "LBbbEwhJEotJqr3hfXlRHGtUk";
    private static final String TWITTER_SECRET = "RQL5V4FKdtqMLdWs6DkldiCoM7bkN4szL5s8oZKEHXXmHARWNR";
    private static final String BASEOTP_URL = "http://ec2-13-58-47-70.us-east-2.compute.amazonaws.com/softcode/api/sign_up.php";
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private static final String KEY_COUNTRY = "Contery";
    private static final String KEY_PASS = "passs";
    private static final String TAG = "ERROR";
    private static final String OTP = "otp";
    Button signup_button;
    int flag = 0;
    CallbackManager callbackManager;
    SessionManager session;
    RelativeLayout relative;
    ImageView facebook_login, googleLogin, twitter_login;
    ProgressBar progress;
    GoogleSignInClient mGoogleSignInClient;
    EditText Phone_no;
    CallbackManager mCallbackManager;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;
    String Phonenumber, Msg, opt_Phone, str, verificationCode, userId;
    AlertDialog.Builder builder;
    String jsonResponse;
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    private String fbUserID;
    //private TwitterAuthClient client;
    private int twiterUserID;
    private int RC_SIGN_IN = 22;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mCallbackManager = CallbackManager.Factory.create();
        // FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
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
                                                public void onResponse(Call<socialBean> call, retrofit2.Response<socialBean> response) {

                                                    if (response.body().getData().getUserName().length() > 0) {

                                                        edit.putString("type", "social");
                                                        edit.putString("user", email);
                                                        edit.putString("pass", id);
                                                        edit.putString("userType" , response.body().getData().getType());
                                                        edit.apply();

                                                        //  Toast.makeText(Register.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(Register.this, HomeActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);


                                                    } else {

                                                        edit.putString("type", "social");
                                                        edit.putString("user", email);
                                                        edit.putString("pass", id);
                                                        edit.commit();

                                                        Toast.makeText(Register.this, "Please update your info", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(Register.this, UserInformation.class);
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
                        Toast.makeText(Register.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(Register.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_register);

        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        edit = pref.edit();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        checkLocationPermission1();
        builder = new AlertDialog.Builder(Register.this);
        relative = findViewById(R.id.relative);
        Phone_no = findViewById(R.id.phone_no);
        signup_button = findViewById(Signup);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


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
                LoginManager.getInstance().logInWithReadPermissions(Register.this, Arrays.asList("public_profile", "email", "user_birthday"));
            }
        });

        twitter_login = (ImageView) findViewById(R.id.twitter);
        twitter_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        progress = (ProgressBar) findViewById(R.id.progress);

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //usersign();
                signUp();
            }
        });
    }


    public void signUp() {


        String phone = Phone_no.getText().toString();


        if (phone.length() > 0) {

            progress.setVisibility(View.VISIBLE);

            final bean b = (bean) getApplicationContext();

            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(b.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final AllAPIs cr = retrofit.create(AllAPIs.class);


            Call<loginResponseBean> call = cr.signUp(phone);

            call.enqueue(new Callback<loginResponseBean>() {
                @Override
                public void onResponse(Call<loginResponseBean> call, retrofit2.Response<loginResponseBean> response) {


                    if (Objects.equals(response.body().getStatus(), "1")) {

                        Intent intent = new Intent(Register.this, OTP.class);

                        intent.putExtra("code", response.body().getData().getVerificationCode());
                        intent.putExtra("userId", response.body().getData().getUserId());
                        intent.putExtra("phone", response.body().getData().getPhone());
                        intent.putExtra("country", response.body().getData().getCountryCode());


                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(Register.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }


                    // Toast.makeText(Register.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();

                    progress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<loginResponseBean> call, Throwable t) {
                    progress.setVisibility(View.GONE);
                    Log.d("register", t.toString());
                }
            });

        } else {
            Phone_no.setError("Invalid Phone");
        }


    }


    private void usersign() {
        showpDialog();
        Phonenumber = Phone_no.getText().toString().trim();
        if (Phonenumber.equals("")) {
            builder.setTitle("Somethimg went wrong..");
            builder.setMessage("Please Enter Valid Mobile Number");
            Displayalart("input_error");
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASEESIGNUP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("status");
                    if (!status.equals("0")) {
                        JSONObject user = jObj.getJSONObject("data");
                        Phonenumber = user.getString("phone");
                        verificationCode = user.getString("verificationCode");
                        str = jObj.getString("message");
                        userId = user.getString("userId");
                        SharedPreferences settings = getSharedPreferences("mypref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("userid", userId);
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), OTP.class);
                        intent.putExtra("verificationCode", verificationCode);
                        intent.putExtra("userId", userId);
                        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(Phonenumber, null, verificationCode, pi, null);
                        //Toast.makeText(getApplicationContext(), "Message Sent successfully!",Toast.LENGTH_LONG).show();

                        //OTP();
                    } else {
                        str = jObj.getString("message");
                        //  Toast.makeText(Register.this, str, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Register.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", Phonenumber);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void OTP() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASEOTP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    JSONObject data = obj.getJSONObject("data");
                    String code = data.getString("verificationCode");
                    // Toast.makeText(Register.this , code , Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*Intent intent=new Intent(Register.this,OTP.class);
                intent.putExtra("userId",userId);
                intent.putExtra("phone",Phonenumber);
                intent.putExtra("countryCode",Counter_code);
                startActivity(intent);*//**/
                StringRequest sr = new StringRequest(Request.Method.POST, "http://ec2-13-58-47-70.us-east-2.compute.amazonaws.com/softcode/api/sign_up.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("phone", Phonenumber);
                        return params;
                    }
                };
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Register.this, error.toString(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", Phonenumber);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void Displayalart(final String code) {
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (code.equals("input_error")) {
                    Phone_no.setText("");
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public boolean checkLocationPermission1() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.SEND_SMS)) {
                // Show an expanation to the user asynchronously -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                //  TODO: Prompt with explanation!
                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
                public void onResponse(Call<socialBean> call, retrofit2.Response<socialBean> response) {

                    if (response.body().getData().getUserName().length() > 0) {

                        edit.putString("type", "social");
                        edit.putString("user", account.getEmail());
                        edit.putString("pass", account.getId());
                        edit.putString("userType" , response.body().getData().getType());
                        edit.commit();

                        //  Toast.makeText(Register.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);


                    } else {

                        edit.putString("type", "social");
                        edit.putString("user", account.getEmail());
                        edit.putString("pass", account.getId());
                        edit.commit();

                        Toast.makeText(Register.this, "Please update your info", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register.this, UserInformation.class);
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
