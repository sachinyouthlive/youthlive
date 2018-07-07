package com.yl.youthlive.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.NetworkUtility;
import com.yl.youthlive.R;
import com.yl.youthlive.bean;
import com.yl.youthlive.forgotpasswordPOJO.ForgotPassword;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.yl.youthlive.bean.getContext;


public class OTP_varify extends AppCompatActivity {
    private String TAG = "otp_varify";
    private EditText opt_value;
    private TextView submit, resendotp;
    private String userid, otpvalue, phoneno;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opt_varification);

        opt_value = (EditText) findViewById(R.id.otp_value);
        submit = (TextView) findViewById(R.id.submit);
        resendotp = (TextView) findViewById(R.id.resend_otp);


        Intent intent = getIntent();

        if (intent != null && !intent.getExtras().getString("userid").equals(null)) {

            userid = intent.getExtras().getString("userid");
            otpvalue = intent.getExtras().getString("otp");
            phoneno = intent.getExtras().getString("phoneno");
        } else {

            userid = "";
            otpvalue = "";
            phoneno = "";
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(opt_value.getText().toString().trim())) {
                    /// show error pupup
                    //  AppUtilits.displayMessage(OTP_varify.this, getString(R.string.otp) + " " + getString(R.string.is_invalid));
                    Toast.makeText(OTP_varify.this, getString(R.string.otp) + " " + getString(R.string.is_invalid), Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "opt value " + opt_value.getText().toString().trim() + "  " + otpvalue);
                    if (opt_value.getText().toString().trim().equals(otpvalue)) {

                        /// create new passwor dscreen
                        Intent intent = new Intent(OTP_varify.this, New_Password.class);
                        intent.putExtra("userid", userid);
                        startActivity(intent);


                    } else {
                        //   AppUtilits.displayMessage(OTP_varify.this, getString(R.string.otp_not_match));
                        Toast.makeText(OTP_varify.this, getString(R.string.otp_not_match), Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });

        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callForgotPasswordAPI();

            }
        });


    }

    public void callForgotPasswordAPI() {
        if (!NetworkUtility.isNetworkConnected(OTP_varify.this)) {

            Toast.makeText(OTP_varify.this, getString(R.string.network_not_connected), Toast.LENGTH_SHORT).show();


        } else {
////////////////////////////

            final bean b = (bean) getContext().getApplicationContext();
            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(b.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            final AllAPIs cr = retrofit.create(AllAPIs.class);
            Call<ForgotPassword> call = cr.UserForgotPassword(phoneno);

            Log.d("phoneno", phoneno);

            call.enqueue(new Callback<ForgotPassword>() {
                @Override
                public void onResponse(Call<ForgotPassword> call, Response<ForgotPassword> response) {

                    Log.d(TAG, "reponse : " + response.toString());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {


                        } else {
                            //    AppUtilits.displayMessage(OTP_varify.this, response.body().getMsg());
                            Toast.makeText(OTP_varify.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //     AppUtilits.displayMessage(OTP_varify.this, getString(R.string.failed_request));
                        Toast.makeText(OTP_varify.this, getString(R.string.failed_request), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<ForgotPassword> call, Throwable t) {

                    Log.e(TAG, " failure " + t.toString());
                    //   AppUtilits.displayMessage(OTP_varify.this, getString(R.string.failed_request));
                    Toast.makeText(OTP_varify.this, getString(R.string.failed_request), Toast.LENGTH_SHORT).show();

                }
            });


        }


    }
}