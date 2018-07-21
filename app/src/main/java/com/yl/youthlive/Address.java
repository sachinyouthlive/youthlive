package com.yl.youthlive;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.yl.youthlive.Activitys.PersonalInfo;
import com.yl.youthlive.Activitys.PhoneUpdateActivity;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.updatePOJO.updateBean;
import com.yl.youthlive.updatephonePOJO.UpdatephonePOJO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by TBX on 11/23/2017.
 */

public class Address extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    FloatingActionButton editer;
    TextView username, youthId, phone, gender, birth, bio;
    PersonalInfo per;
    String userId;
    private GoogleApiClient mGoogleApiClient;
    private SharedPreferences pref;
    private SharedPreferences.Editor edit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getContext().getApplicationContext());
        View view = inflater.inflate(R.layout.address_layout, container, false);

        per = (PersonalInfo) getActivity();

        userId = getArguments().getString("userId");

        pref = getContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        edit = pref.edit();

        username = view.findViewById(R.id.username);
        youthId = view.findViewById(R.id.yid);
        phone = view.findViewById(R.id.phone);
        gender = view.findViewById(R.id.gender);
        birth = view.findViewById(R.id.birthday);
        bio = view.findViewById(R.id.bio);

        editer = view.findViewById(R.id.edit);

        Bundle b = getArguments();
        bean b1 = (bean) getContext().getApplicationContext();

        if (!Objects.equals(userId, b1.userId)) {
            editer.setVisibility(View.GONE);
            phone.setText("(Mobile Number Private)");
        } else {
            editer.setVisibility(View.VISIBLE);
            phone.setText(b.getString("phone"));
        }


        try {
            buildGoogleApiClient();
        } catch (Exception e) {
            e.printStackTrace();
        }


        username.setText(b.getString("user"));
        youthId.setText(b.getString("youth"));
        gender.setText(b.getString("gender"));

        String dob = b.getString("birth");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormatter.parse(dob);
        } catch (ParseException e) {
            e.printStackTrace();
        }

// Get time from date
        SimpleDateFormat timeFormatter = new SimpleDateFormat("d MMM yyyy");
        String dobValue = timeFormatter.format(date);


        birth.setText(dobValue);
        String status = b.getString("status");
        if (status.isEmpty()) {
            bio.setText("Hi there, be my Youthlive friend!");
        } else {
            bio.setText(b.getString("status"));
        }

        String profileuserId = b.getString("userId");


        editer.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View view) {


                final String[] gend = {""};
                final String[] bday = {""};


                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.edit_user_details_options);
                dialog.show();

                final TextView phone = dialog.findViewById(R.id.phone);
                TextView password = dialog.findViewById(R.id.password);
                final TextView personal = dialog.findViewById(R.id.personal);

                final ProgressBar progress = dialog.findViewById(R.id.progress);

                final LinearLayout phoneHide = dialog.findViewById(R.id.phone_hide);
                final LinearLayout passwordHide = dialog.findViewById(R.id.password_hide);
                final LinearLayout personalHide = dialog.findViewById(R.id.personal_hide);


                final EditText phoneNumber = dialog.findViewById(R.id.phone_number);

                final EditText oldPassword = dialog.findViewById(R.id.old_password);
                final EditText newPassword = dialog.findViewById(R.id.new_password);
                final EditText confirmPassword = dialog.findViewById(R.id.confirm_new);

                final EditText username1 = dialog.findViewById(R.id.usename);

                username1.setText(username.getText().toString());

                final Spinner gender = dialog.findViewById(R.id.gender);
                Bundle b1 = getArguments();
                String s = b1.getString("gender");


                final TextView birth1 = dialog.findViewById(R.id.birth);


                String dob1 = birth.getText().toString();
                SimpleDateFormat dateFormatter1 = new SimpleDateFormat("d MMM yyyy");
                Date date1 = null;
                try {
                    date1 = dateFormatter1.parse(dob1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat timeFormatter2 = new SimpleDateFormat("yyyy-MM-dd");
                String dobValue1 = timeFormatter2.format(date1);
                birth1.setText(dobValue1);


                final EditText bio1 = dialog.findViewById(R.id.bio);


                bio1.setText(bio.getText().toString());


                Button update = dialog.findViewById(R.id.update);
                Button updatephone = dialog.findViewById(R.id.updatephone);

                List<String> genders = new ArrayList<>();

                final DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        int month = Integer.valueOf(datePicker.getMonth());
                        month = month + 1;
                        String m = String.valueOf(month);
                        String y = String.valueOf(datePicker.getYear());
                        String d = String.valueOf(datePicker.getDayOfMonth());

                        bday[0] = y + "-" + m + "-" + d;
                        birth1.setText(y + "-" + m + "-" + d);


                    }
                }, 2018, 1, 1);

                birth1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        datePickerDialog.show();

                    }
                });

                genders.add("Male");
                genders.add("Female");

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, genders);

                gender.setAdapter(adapter);

                gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        if (i == 0) {
                            gend[0] = "Male";
                        } else if (i == 1) {
                            gend[0] = "Female";
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                if (s.equals("Male")) {
                    gender.setSelection(0);
                } else {
                    gender.setSelection(1);
                }

                updatephone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final String pho = phoneNumber.getText().toString();
                        userId = getArguments().getString("userId");
                        if (!DataValidation.isValidPhoneNumber(pho)) {

//                            Intent intent=new Intent(getContext(), PhoneUpdateActivity.class);
//                            intent.putExtra("phoneno",pho);
//                            startActivity(intent);
//                            dialog.dismiss();

                            progress.setVisibility(View.VISIBLE);

                            final bean b = (bean) getContext().getApplicationContext();

                            final Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(b.BASE_URL)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            final AllAPIs cr = retrofit.create(AllAPIs.class);

                            Call<UpdatephonePOJO> call = cr.updatePhoneno(pho);

                            call.enqueue(new Callback<UpdatephonePOJO>() {
                                @Override
                                public void onResponse(Call<UpdatephonePOJO> call, Response<UpdatephonePOJO> response) {

                                    //      if (Objects.equals(response.body().getMessage(), "1")) {
                                    //         Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    //    if (mGoogleApiClient.isConnected()) {
                                    //       dialog.dismiss();
                                    //        signOut();
                                    //    }

                                    Log.d("sachin", "response");

                                    //   LoginManager.getInstance().logOut();

//                                    edit.remove("type");
//                                    edit.remove("user");
//                                    edit.remove("pass");
//                                    edit.apply();
//
//                                    Toast.makeText(getContext(), "Mobile Number Updated, Login with Updated Mobile Number", Toast.LENGTH_LONG).show();
//
//                                    Intent i = new Intent(getContext(), Login.class);
//                                    startActivity(i);
//                                    getActivity().finishAffinity();
//                                    dialog.dismiss();
                                    //     } else {
                                    //       Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    //     }
                                    String otp = response.body().getInformation().getOtp().toString();
                                    Intent intent = new Intent(getContext(), PhoneUpdateActivity.class);
                                    intent.putExtra("otp", otp);
                                    intent.putExtra("phoneno", pho);
                                    // Toast.makeText(getContext(), ""+otp, Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                    progress.setVisibility(View.GONE);
                                    dialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<UpdatephonePOJO> call, Throwable t) {

                                    Toast.makeText(getContext(), "failed to update", Toast.LENGTH_SHORT).show();
                                    progress.setVisibility(View.GONE);
                                }
                            });


                        } else {
                            Toast.makeText(getContext(), "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

                confirmPassword.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        final int DRAWABLE_LEFT = 0;
                        final int DRAWABLE_TOP = 1;
                        final int DRAWABLE_RIGHT = 2;
                        final int DRAWABLE_BOTTOM = 3;
                        if (event.getRawX() >= (confirmPassword.getRight() - confirmPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()) - 60) {

                            String old = oldPassword.getText().toString();
                            String newp = newPassword.getText().toString();
                            String conf = confirmPassword.getText().toString();

                            if (old.length() > 0) {

                                if (newp.length() > 0) {

                                    if (conf == newp) {


                                    } else {
                                        Toast.makeText(getContext(), "Password did not match", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(getContext(), "Invalid New Password", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getContext(), "Invalid Old Password", Toast.LENGTH_SHORT).show();
                            }


                            return true;
                        }
                        return false;
                    }
                });

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!username1.getText().toString().isEmpty()) {
                            progress.setVisibility(View.VISIBLE);

                            final bean b = (bean) getContext().getApplicationContext();

                            final Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(b.BASE_URL)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            final AllAPIs cr = retrofit.create(AllAPIs.class);

                            Call<updateBean> call = cr.updateUserData(username1.getText().toString(), gender.getSelectedItem().toString(), birth1.getText().toString(), bio1.getText().toString(), userId);

                            call.enqueue(new Callback<updateBean>() {
                                @Override
                                public void onResponse(Call<updateBean> call, Response<updateBean> response) {

                                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    //bio.setText(bio1.getText().toString());
                                    per.loadData();

                                    progress.setVisibility(View.GONE);
                                    dialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<updateBean> call, Throwable t) {
                                    progress.setVisibility(View.GONE);
                                    Log.d("asdasd", t.toString());
                                }
                            });

                        } else {
                            Toast.makeText(getContext(), "Please enter a valid Username", Toast.LENGTH_SHORT).show();
                        }
                    }

                });


                phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        phoneHide.setVisibility(View.VISIBLE);
                        passwordHide.setVisibility(View.GONE);
                        personalHide.setVisibility(View.GONE);

                    }
                });

                password.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        phoneHide.setVisibility(View.GONE);
                        passwordHide.setVisibility(View.VISIBLE);
                        personalHide.setVisibility(View.GONE);

                    }
                });
                personal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        phoneHide.setVisibility(View.GONE);
                        passwordHide.setVisibility(View.GONE);
                        personalHide.setVisibility(View.VISIBLE);

                    }
                });


            }
        });


        return view;
    }

    private synchronized void buildGoogleApiClient() {


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @SuppressLint("CommitPrefEdits")
                    @Override
                    public void onResult(@NonNull Status status) {


                        if (status.isSuccess()) {

                            edit.remove("type");
                            edit.remove("user");
                            edit.remove("pass");
                            edit.apply();
                            Intent i = new Intent(getContext(), Login.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            getActivity().finish();

                        }


                    }
                });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}