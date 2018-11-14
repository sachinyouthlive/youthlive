package com.app.youthlive.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.youthlive.DataValidation;
import com.app.youthlive.NetworkUtility;
import com.app.youthlive.R;
import com.app.youthlive.bean;
import com.app.youthlive.forgotpasswordPOJO.ForgotPassword;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.youthlive.bean.getContext;


public class ForgotPasswordActivity extends AppCompatActivity {
    private String TAG = "FOrgotPassword";
    private EditText phoneno;
    private TextView submit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        phoneno = (EditText) findViewById(R.id.phone_number);
        submit = (TextView) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (DataValidation.isValidPhoneNumber(phoneno.getText().toString())) {
                    /// show error pupup
                    //  AppUtilits.displayMessage(ForgotPasswordActivity.this, getString(R.string.phone_no) + " " + getString(R.string.is_invalid));
                    Toast.makeText(ForgotPasswordActivity.this, getString(R.string.phone_no) + " " + getString(R.string.is_invalid), Toast.LENGTH_SHORT).show();
                } else {

                    callForgotPasswordAPI();

                }


            }
        });
    }

    public void callForgotPasswordAPI() {
        if (!NetworkUtility.isNetworkConnected(ForgotPasswordActivity.this)) {
            //    AppUtilits.displayMessage(ForgotPasswordActivity.this, getString(R.string.network_not_connected));
            Toast.makeText(ForgotPasswordActivity.this, getString(R.string.network_not_connected), Toast.LENGTH_SHORT).show();

        } else {
//////////////////////
            final bean b = (bean) getContext().getApplicationContext();

            Call<ForgotPassword> call = b.getRetrofit().UserForgotPassword(phoneno.getText().toString());

            Log.d("phoneno", phoneno.getText().toString());

            call.enqueue(new Callback<ForgotPassword>() {
                @Override
                public void onResponse(Call<ForgotPassword> call, Response<ForgotPassword> response) {

                    Log.d(TAG, "reponse : " + response.toString());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {

                            // response.body().getInformation().getOtp()
                            // start home activity
                            Intent intent = new Intent(ForgotPasswordActivity.this, OTP_varify.class);
                            intent.putExtra("userid", response.body().getInformation().getUserId());
                            intent.putExtra("otp", response.body().getInformation().getOtp());
                            intent.putExtra("phoneno", phoneno.getText().toString());

                            startActivity(intent);


                        } else {
                            //       AppUtilits.displayMessage(ForgotPasswordActivity.this, response.body().getMsg());
                            Toast.makeText(ForgotPasswordActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        //     AppUtilits.displayMessage(ForgotPasswordActivity.this, getString(R.string.failed_request));
                        Toast.makeText(ForgotPasswordActivity.this, getString(R.string.failed_request), Toast.LENGTH_SHORT).show();


                    }
                }

                @Override
                public void onFailure(Call<ForgotPassword> call, Throwable t) {

                    Log.e(TAG, " failure " + t.toString());
                    //  AppUtilits.displayMessage(ForgotPasswordActivity.this, getString(R.string.failed_request));
                    Toast.makeText(ForgotPasswordActivity.this, getString(R.string.failed_request), Toast.LENGTH_SHORT).show();

                }
            });


        }


    }

}
