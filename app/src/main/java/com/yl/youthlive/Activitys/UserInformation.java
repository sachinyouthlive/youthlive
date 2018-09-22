package com.yl.youthlive.Activitys;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.yl.youthlive.HomeActivity;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.R;
import com.yl.youthlive.SharePreferenceUtils;
import com.yl.youthlive.bean;
import com.yl.youthlive.internetConnectivity.ConnectivityReceiver;
import com.yl.youthlive.login2POJO.login2Bean;
import com.yl.youthlive.loginResponsePOJO.loginResponseBean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UserInformation extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
    private static final int DATE_PICKER_ID = 112;
    private static final int RESULT_LOAD_IMG = 1;
    String UserInfo = "http://ec2-13-58-47-70.us-east-2.compute.amazonaws.com/softcode/api/update_user_info.php";
    EditText user_name, BirthDay, Biodata;
    String Username, Birthdate, BioData, gender, userid1 = "", Female, str, encodedImage = "", imgDecodableString, pImage = "";
    RadioButton radioMale, radioFemale, radioButton;
    RadioGroup rg;
    CircleImageView userimage;
    AlertDialog.Builder builder;
    Button signup_button;
    ImageView uploadpic;
    Uri imageuri, selectedImage;
    EditText edittext;
    Calendar myCalendar;
    ProgressBar progress;
    private int year, month, day;
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            BirthDay.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));
            Birthdate = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(day);
        }
    };

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    public static Boolean getFromPref(Context context, String key) {
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF,
                Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(key, false));
    }

    private static String getPath(final Context context, final Uri uri) {
        final boolean isKitKatOrAbove = true;

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        checkConnection();
        setContentView(R.layout.activity_user_information);
        SharedPreferences settings = getSharedPreferences("mypref", MODE_PRIVATE);
        userid1 = settings.getString("userid", "");
        builder = new AlertDialog.Builder(UserInformation.this);
        user_name = (EditText) findViewById(R.id.user_name);
        BirthDay = (EditText) findViewById(R.id.birthday);
        rg = (RadioGroup) findViewById(R.id.radioSex);
        Biodata = (EditText) findViewById(R.id.conform_passwor);
        signup_button = (Button) findViewById(R.id.signup_button);
        userimage = findViewById(R.id.userimage);
        uploadpic = findViewById(R.id.uploadpic);

        progress = (ProgressBar) findViewById(R.id.progress);

        uploadpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

////// picking birthday date//////////////
        myCalendar = Calendar.getInstance();

        edittext = (EditText) findViewById(R.id.birthday);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(UserInformation.this, R.style.DatePickerTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
////////////////

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get selected radio button from radioGroup
                int selectedId = rg.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);

                gender = radioButton.getText().toString();
                if (user_name.getText().length() < 1) {
                    Toast.makeText(UserInformation.this, "Please enter Username", Toast.LENGTH_LONG).show();
                } else {
                    updateDetails();
                }


            }
        });
    }

    public void updateDetails() {

        String name = user_name.getText().toString();

        if (name.length() > 0) {
            progress.setVisibility(View.VISIBLE);


            MultipartBody.Part body = null;

            try {

                String mCurrentPhotoPath = getPath(UserInformation.this, selectedImage);

                File file = new File(mCurrentPhotoPath);


                RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);

            } catch (Exception e) {
                e.printStackTrace();
            }


            final bean b = (bean) getApplicationContext();

            Call<loginResponseBean> call = b.getRetrofit().addUserData(body, name, gender, BirthDay.getText().toString(), Biodata.getText().toString(), getIntent().getStringExtra("userId"));

            call.enqueue(new Callback<loginResponseBean>() {
                @Override
                public void onResponse(Call<loginResponseBean> call, retrofit2.Response<loginResponseBean> response) {

                    if (Objects.equals(response.body().getStatus(), "1")) {

                        progress.setVisibility(View.VISIBLE);

                        SharedPreferences fcmPref = getSharedPreferences("fcm", Context.MODE_PRIVATE);

                        String keey = fcmPref.getString("token", "");


                        Call<login2Bean> call1 = b.getRetrofit().signIn(response.body().getData().getPhone(), response.body().getData().getPassword(), keey);


                        call1.enqueue(new Callback<login2Bean>() {
                            @Override
                            public void onResponse(Call<login2Bean> call, retrofit2.Response<login2Bean> response) {


                                if (Objects.equals(response.body().getStatus(), "1")) {
                                    Toast.makeText(UserInformation.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                    SharePreferenceUtils.getInstance().putString("userId", response.body().getData().getUserId());
                                    SharePreferenceUtils.getInstance().putString("userName", response.body().getData().getUserName());


                                    try {
                                        SharePreferenceUtils.getInstance().putString("userImage", response.body().getData().getUserImage());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    SharePreferenceUtils.getInstance().putString("type", "phone");
                                    SharePreferenceUtils.getInstance().putString("user", response.body().getData().getPhone());
                                    SharePreferenceUtils.getInstance().putString("pass", response.body().getData().getPassword());
                                    SharePreferenceUtils.getInstance().putString("userType", response.body().getData().getType());
                                    SharePreferenceUtils.getInstance().putString("yid", response.body().getData().getYouthLiveId());


                                    Intent Inbt = new Intent(UserInformation.this, HomeActivity.class);
                                    Inbt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(Inbt);

                                } else {
                                    Toast.makeText(UserInformation.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }


                                progress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(Call<login2Bean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                            }
                        });


                        /*Toast.makeText(UserInformation.this, "Profile Updated, Continue to login", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserInformation.this, Signin.class);
                        startActivity(intent);
                        finish();*/


                    } else {
                        Toast.makeText(UserInformation.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<loginResponseBean> call, Throwable t) {
                    progress.setVisibility(View.GONE);
                }
            });

        }


    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                return new DatePickerDialog(this, pickerListener, year, month, day);
        }
        return null;
    }

    private void SelectImage() {
        final CharSequence[] items = {"Take Photo from Camera",
                "Choose from Gallery",
                "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UserInformation.this);
        builder.setTitle("Add Photo!");
        builder.setIcon(R.drawable.cameraicon);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo from Camera")) {
                    Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    getpic.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
                    startActivityForResult(getpic, MY_PERMISSIONS_REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMG);
                } else if (items[item].equals("Cancel")) {

                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA && resultCode != 0 && data != null) {


            Bitmap photo = (Bitmap) data.getExtras().get("data");
            userimage.setImageBitmap(photo);


        }
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
                selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                final InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                Bitmap Image = BitmapFactory.decodeStream(imageStream);

                Image = getResizedBitmap(Image, 400);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Image.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                //  image_profile=(HeartImageView) findViewById(R.id.image_profile);
                // Set the Image in ImageView after decoding the String
                userimage.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
                userimage.setImageBitmap(Image);
               /* SharedPreferences sharedpreferences1 = profile.this.getSharedPreferences("MyPrefs", 1);
                SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                editor1.putString("loginid", String.valueOf(Image));
                editor1.commit();*/

            } else {
              /*  Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();*/
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(UserInformation.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (getFromPref(UserInformation.this, ALLOW_KEY)) {
                showSettingsAlert();
            } else if (ContextCompat.checkSelfPermission(UserInformation.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(UserInformation.this,
                        Manifest.permission.CAMERA)) {
                    showAlert();
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(UserInformation.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }
        } else {
        }
    }

    private void showSettingsAlert() {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(UserInformation.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");

        alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });
        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startInstalledAppDetailsActivity(UserInformation.this);
                    }
                });

        alertDialog.show();
    }

    private void showAlert() {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(UserInformation.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");

        alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // finish();
                    }
                });
        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(UserInformation.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                });
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        bean.getInstance().setConnectivityListener(this);


    }

    ///////////////////internet connectivity check///////////////
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showAlert(isConnected);
    }

    private void showAlert(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {

            // Toast.makeText(this, "Good! Connected to Internet", Toast.LENGTH_SHORT).show();
            //    message = "Good! Connected to Internet";
            //    color = Color.WHITE;
        } else {
            //    Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
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
            } catch (Exception e) {
                Log.d("TAG", "Show Dialog: " + e.getMessage());
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
    public void onNetworkConnectionChanged(boolean isConnected) {
        showAlert(isConnected);

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }

}
