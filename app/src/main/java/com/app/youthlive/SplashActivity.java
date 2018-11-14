package com.app.youthlive;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.youthlive.login2POJO.login2Bean;
import com.app.youthlive.socialPOJO.socialBean;
import com.github.javiersantos.piracychecker.PiracyChecker;
import com.github.javiersantos.piracychecker.callbacks.PiracyCheckerCallback;
import com.github.javiersantos.piracychecker.enums.Display;
import com.github.javiersantos.piracychecker.enums.InstallerID;
import com.github.javiersantos.piracychecker.enums.PiracyCheckerError;
import com.github.javiersantos.piracychecker.enums.PirateApp;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;


public class SplashActivity extends AppCompatActivity {
    ImageView bg;
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    ProgressBar progress;
    private String TAG = "splashAcctivity";
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
            Manifest.permission.CAMERA,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
    };

    private PiracyChecker checker;

    private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgyeBlAF+Tb5RcId3Y9sAnK7EoGEklDr24FByrgxwhQNsIOkhYfDH+KW4OGxqR47D+RjH3uHBgtKjD62qgvSsqiJR4KiHAq5gVZLZJ3nP0YDvnfWwyhg+t6FYnchGVGt2FbuNyw+XqPuZvoxUQmfB4qsIOQlbf9HI69uisnOZzuJ5b2VIVg3yIymF45jAm9+U5DdqP3vO7pHF4Y3yycOS6EIYs3VoZJ8JmJIOVHpFc//fxBaV4OKgKJij/28v5C94RRay55wHO0+ysW4fAQW52SxXX2vsQMGWYRmMzLs+N87PUixYJP96BbkUh4mkGFC1RMq8iUaeWLKIut74VhmCLQIDAQAB";

    // Generate 20 random bytes, and put them here.
    private static final byte[] SALT = new byte[] {
            -46, 65, 30, -128, -103, -57, 74, -64, 51, 88, -95,
            -45, 77, -117, -36, -113, -11, 32, -64, 89
    };

    //private LicenseCheckerCallback mLicenseCheckerCallback;
    //private LicenseChecker mChecker;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Library calls this when it's done.
        /*mLicenseCheckerCallback = new MyLicenseCheckerCallback();
        // Construct the LicenseChecker with a policy.
        mChecker = new LicenseChecker(
                this, new ServerManagedPolicy(this,
                new AESObfuscator(SALT, getPackageName(), deviceId)),
                BASE64_PUBLIC_KEY);
        */

        doCheck();

        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        edit = pref.edit();
        progress = findViewById(R.id.progress);

        bg = findViewById(R.id.splashbg);
        //bindLogo();

        //  Glide.with(this).load(R.drawable.splashyl).into(bg);


    }


    private void doCheck() {
        checker = new PiracyChecker(this)
                .enableGooglePlayLicensing(BASE64_PUBLIC_KEY)
                .enableInstallerId(InstallerID.GOOGLE_PLAY)
                .enableEmulatorCheck(true)
                .display(Display.ACTIVITY);
                /*.callback(new PiracyCheckerCallback() {
                    @Override
                    public void allow() {
                        if (hasPermissions(SplashActivity.this, PERMISSIONS)) {
                            startApp();
                        } else {
                            ActivityCompat.requestPermissions(SplashActivity.this, PERMISSIONS, REQUEST_CODE_ASK_PERMISSIONS);
                        }
                    }

                    @Override
                    public void doNotAllow(@NotNull PiracyCheckerError piracyCheckerError, @org.jetbrains.annotations.Nullable PirateApp pirateApp) {
                        Toast.makeText(SplashActivity.this, "Invalid Liscense", Toast.LENGTH_SHORT).show();
                    }
                });*/

        checker.start();
    }

/*

    private class MyLicenseCheckerCallback implements LicenseCheckerCallback {
        public void allow(int policyReason) {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            // Should allow user access.


            if (hasPermissions(SplashActivity.this, PERMISSIONS)) {
                startApp();
            } else {
                ActivityCompat.requestPermissions(SplashActivity.this, PERMISSIONS, REQUEST_CODE_ASK_PERMISSIONS);
            }


            //displayResult(getString(R.string.allow));
        }

        public void dontAllow(int policyReason) {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            //displayResult(getString(R.string.dont_allow));
            // Should not allow access. In most cases, the app should assume
            // the user has access unless it encounters this. If it does,
            // the app should inform the user of their unlicensed ways
            // and then either shut down the app or limit the user to a
            // restricted set of features.
            // In this example, we show a dialog that takes the user to a deep
            // link returned by the license checker.
            // If the reason for the lack of license is that the service is
            // unavailable or there is another problem, we display a
            // retry button on the dialog and a different message.
            //displayDialog(policyReason == Policy.RETRY);

            Toast.makeText(SplashActivity.this, "Invalid Liscense", Toast.LENGTH_SHORT).show();
        }

        public void applicationError(int errorCode) {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            // This is a polite way of saying the developer made a mistake
            // while setting up or calling the license checker library.
            // Please examine the error code and fix the error.
            //String result = String.format(getString(R.string.application_error), errorCode);
            //displayResult(result);
        }
    }

*/

    /*protected Dialog onCreateDialog(int id) {
        final boolean bRetry = id == 1;
        return new AlertDialog.Builder(this)
                .setTitle(R.string.unlicensed_dialog_title)
                .setMessage(bRetry ? R.string.unlicensed_dialog_retry_body : R.string.unlicensed_dialog_body)
                .setPositiveButton(bRetry ? R.string.retry_button : R.string.restore_access_button,
                        new DialogInterface.OnClickListener() {
                            boolean mRetry = bRetry;

                            public void onClick(DialogInterface dialog, int which) {
                                if (mRetry) {
                                    doCheck();
                                } else {
                                    mChecker.followLastLicensingUrl(SplashActivity.this);
                                }
                            }
                        })
                .setNegativeButton(R.string.quit_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create();
    }*/


    public void startApp() {

        String type = pref.getString("type", "");
        String em = pref.getString("user", "");
        String pa = pref.getString("pass", "");

        if (type.length() > 0 && em.length() > 0 && pa.length() > 0) {
            if (Objects.equals(type, "social")) {

                socialLogin(em, pa);

            } else if (Objects.equals(type, "phone")) {

                phoneLogin(em, pa);

            }

        } else {

            Timer t = new Timer();

            t.schedule(new TimerTask() {
                @Override
                public void run() {

                    Intent intent = new Intent(SplashActivity.this, Spalsh2.class);
                    startActivity(intent);
                    finish();

                }
            }, 2000);

        }

    }

    public void phoneLogin(final String phone, final String pass) {

        // progress.setVisibility(View.VISIBLE);

        final bean b = (bean) getApplicationContext();


        SharedPreferences fcmPref = getSharedPreferences("fcm", Context.MODE_PRIVATE);

        String keey = fcmPref.getString("token", "");

        Call<login2Bean> call = b.getRetrofit().signIn(phone, pass, keey);

        call.enqueue(new Callback<login2Bean>() {
            @Override
            public void onResponse(Call<login2Bean> call, retrofit2.Response<login2Bean> response) {


                if (Objects.equals(response.body().getStatus(), "1")) {

                    if (response.body().getData().getUserName().length() > 0) {
                        Toast.makeText(SplashActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        SharePreferenceUtils.getInstance().putString("userId", response.body().getData().getUserId());
                        SharePreferenceUtils.getInstance().putString("userName", response.body().getData().getUserName());


                        try {
                            SharePreferenceUtils.getInstance().putString("userImage", response.body().getData().getUserImage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        SharePreferenceUtils.getInstance().putString("type", "phone");
                        SharePreferenceUtils.getInstance().putString("user", phone);
                        SharePreferenceUtils.getInstance().putString("pass", pass);
                        SharePreferenceUtils.getInstance().putString("userType", response.body().getData().getType());
                        SharePreferenceUtils.getInstance().putString("yid", response.body().getData().getYouthLiveId());


                        Intent Inbt = new Intent(SplashActivity.this, HomeActivity.class);
                        Inbt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(Inbt);
                        SplashActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                    } else {
                        SharePreferenceUtils.getInstance().putString("userId", response.body().getData().getUserId());
                        SharePreferenceUtils.getInstance().putString("userName", response.body().getData().getUserName());


                        try {
                            SharePreferenceUtils.getInstance().putString("userImage", response.body().getData().getUserImage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        SharePreferenceUtils.getInstance().putString("type", "phone");
                        SharePreferenceUtils.getInstance().putString("user", phone);
                        SharePreferenceUtils.getInstance().putString("pass", pass);

                        Toast.makeText(SplashActivity.this, "Please update your info", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SplashActivity.this, UserInfo2.class);
                        intent.putExtra("userId", response.body().getData().getUserId());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);


                    }


                } else {
                    //  Toast.makeText(SplashActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SplashActivity.this, Spalsh2.class);
                    startActivity(intent);
                    finish();


                }


                //   progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<login2Bean> call, Throwable t) {
                //  progress.setVisibility(View.GONE);
                Intent intent = new Intent(SplashActivity.this, Spalsh2.class);
                startActivity(intent);
                finish();
            }
        });


    }


    public void socialLogin(final String email, final String pid) {
        //  progress.setVisibility(View.VISIBLE);

        final bean b = (bean) getApplicationContext();

        SharedPreferences fcmPref = getSharedPreferences("fcm", Context.MODE_PRIVATE);

        String keey = fcmPref.getString("token", "");

        Call<socialBean> call = b.getRetrofit().socialSignIn(pid, email, keey);

        call.enqueue(new Callback<socialBean>() {
            @Override
            public void onResponse(Call<socialBean> call, retrofit2.Response<socialBean> response) {

                if (response.body().getData().getUserName().length() > 0) {

                    SharePreferenceUtils.getInstance().putString("userId", response.body().getData().getUserId());
                    SharePreferenceUtils.getInstance().putString("userName", response.body().getData().getUserName());


                    try {
                        SharePreferenceUtils.getInstance().putString("userImage", response.body().getData().getUserImage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    SharePreferenceUtils.getInstance().putString("type", "social");
                    SharePreferenceUtils.getInstance().putString("user", email);
                    SharePreferenceUtils.getInstance().putString("pass", pid);
                    SharePreferenceUtils.getInstance().putString("userType", response.body().getData().getType());
                    SharePreferenceUtils.getInstance().putString("yid", response.body().getData().getYouthLiveId());


                    Toast.makeText(SplashActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    SplashActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


                } else {

                    SharePreferenceUtils.getInstance().putString("userId", response.body().getData().getUserId());
                    SharePreferenceUtils.getInstance().putString("userName", response.body().getData().getUserName());


                    try {
                        SharePreferenceUtils.getInstance().putString("userImage", response.body().getData().getUserImage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    SharePreferenceUtils.getInstance().putString("type", "social");
                    SharePreferenceUtils.getInstance().putString("user", email);
                    SharePreferenceUtils.getInstance().putString("pass", pid);


                    Toast.makeText(SplashActivity.this, "Please update your info", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SplashActivity.this, UserInfo2.class);
                    intent.putExtra("userId", response.body().getData().getUserId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                }


                //      progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<socialBean> call, Throwable t) {
                //   progress.setVisibility(View.GONE);
                Intent intent = new Intent(SplashActivity.this, Spalsh2.class);
                startActivity(intent);
                finish();

            }
        });


    }

    private void bindLogo() {
        // Start animating the image
        final ImageView splash = findViewById(R.id.splashbg);
        final AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
        animation1.setDuration(700);
        final AlphaAnimation animation2 = new AlphaAnimation(1.0f, 0.2f);
        animation2.setDuration(700);
        //animation1 AnimationListener
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation2 when animation1 ends (continue)
                splash.startAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
            }
        });

        //animation2 AnimationListener
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation1 when animation2 ends (repeat)
                splash.startAnimation(animation1);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
            }
        });

        splash.startAnimation(animation1);
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
                            ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.MODIFY_AUDIO_SETTINGS) == PackageManager.PERMISSION_GRANTED
                    ) {
                startApp();

            } else {
                if (
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WAKE_LOCK) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.MODIFY_AUDIO_SETTINGS)
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
    protected void onDestroy() {
        super.onDestroy();
        checker.destroy();
    }


}
