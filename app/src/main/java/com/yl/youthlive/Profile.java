package com.yl.youthlive;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.youthlive.Activitys.FollowingActivity;
import com.yl.youthlive.Activitys.MessaageActivity;
import com.yl.youthlive.Activitys.MyVlog;
import com.yl.youthlive.Activitys.PersonalInfo;
import com.yl.youthlive.Activitys.RattingActivity;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.checkin.CheckinActivity;
import com.yl.youthlive.internetConnectivity.ConnectivityReceiver;
import com.yl.youthlive.loginResponsePOJO.loginResponseBean;
import com.yl.youthlive.updateProfilePOJO.updateProfileBean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.app.Activity.RESULT_OK;


public class Profile extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {
    public static final int GALLEY_REQUEST_CODE_CUSTOMER = 10;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private final int PICK_IMAGE_REQUEST1 = 1;
    private final int PICK_IMAGE_REQUEST2 = 2;
    TextView messagechate, followingAct, ratting_act, mycheckin, personal_info, vlogActivity, beggage, check, blocked, about, policy;
    ImageView choose_file;
    //CircleImageView profileimage;
    LinearLayout cover_image, profile_image;
    HashMap<String, String> user;
    String userID;
    Bitmap bitmap;
    TextView wallet;
    // SessionManager session;
    String shareProfile;
    String shareyouth, shareName;
    CircleImageView profileimage;
    ImageView profileimg;
    ProgressBar progress;
    ViewPager coverPager;
    CircleIndicator indicator;
    TextView name, youthId;
    private Uri realUri;

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

   /*public void ChooseDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.profile_coverdialog);
        cover_image = dialog.findViewById(R.id.cover_image);
        profile_image = dialog.findViewById(R.id.profile_image);
        cover_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                coverProfileUpdate();
            }

        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coverProfileUpdate();

            }
        });

        dialog.show();
    }*/

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);
        checkConnection();
        choose_file = view.findViewById(R.id.choose_file);
        messagechate = view.findViewById(R.id.messagechatee);
        mycheckin = view.findViewById(R.id.mycheckin);
        ratting_act = view.findViewById(R.id.ratting_act);

        wallet = (TextView) view.findViewById(R.id.wallet);

        name = (TextView) view.findViewById(R.id.name);
        youthId = (TextView) view.findViewById(R.id.youth_id);


        beggage = view.findViewById(R.id.beggage);

        about = view.findViewById(R.id.about);

        policy = view.findViewById(R.id.policy);

        check = view.findViewById(R.id.check);

        blocked = view.findViewById(R.id.blocked);


        coverPager = (ViewPager) view.findViewById(R.id.cover_pager);
        indicator = (CircleIndicator) view.findViewById(R.id.indicator);


        //profileimage = view.findViewById(R.id.profile_imagee);

        profileimage = (CircleImageView) view.findViewById(R.id.profile);
        profileimg = view.findViewById(R.id.ivBlurProfile);
        progress = (ProgressBar) view.findViewById(R.id.progress);

        // profile_imagee=view.findViewById(R.id.profile_image);
        vlogActivity = view.findViewById(R.id.vlogActivity);
        // session = new SessionManager(getActivity());
        //  user = session.getUserDetails();
        //userID = user.get(SessionManager.USER_ID);
        //  bean b = (bean) getApplicationContext();
        userID = SharePreferenceUtils.getInstance().getString("userId");
        //Toast.makeText(getActivity(), "" + SharePreferenceUtils.getInstance().getString("userId"), Toast.LENGTH_LONG).show();
        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonalInfo.class);
                bean b = (bean) getContext().getApplicationContext();

                intent.putExtra("userId", SharePreferenceUtils.getInstance().getString("userId"));
                intent.putExtra("ythlive", shareyouth);
                intent.putExtra("uname", shareName);
                intent.putExtra("uimage", shareProfile);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


            }
        });

        vlogActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyVlog.class);

                //bean b = (bean) getContext().getApplicationContext();

                intent.putExtra("userId", SharePreferenceUtils.getInstance().getString("userId"));
                intent.putExtra("ythlive", shareyouth);
                intent.putExtra("uname", shareName);
                intent.putExtra("uimage", shareProfile);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        personal_info = view.findViewById(R.id.personal_info);
        personal_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getActivity(), PersonalInfo.class);

                bean b = (bean) getContext().getApplicationContext();

                intent.putExtra("userId", SharePreferenceUtils.getInstance().getString("userId"));
                intent.putExtra("ythlive", shareyouth);
                intent.putExtra("uname", shareName);
                intent.putExtra("uimage", shareProfile);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


            }
        });
        mycheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CheckinActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
        ratting_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RattingActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });


        /*choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseDialog();
            }
        });*/


        followingAct = view.findViewById(R.id.followingAct);
        followingAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FollowingActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WalletNew.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        messagechate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MessaageActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });


        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Content.class);
                intent.putExtra("title", "About Us");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });


        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Terms.class);
                intent.putExtra("title", "Privacy Policy");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.profile_coverdialog);
                dialog.show();

                //   LinearLayout cover = (LinearLayout)dialog.findViewById(R.id.cover_image);
                LinearLayout profile = (LinearLayout) dialog.findViewById(R.id.profile_image);

            /*    cover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_IMAGE_REQUEST1);
                        dialog.dismiss();

                    }
                });*/

                profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_IMAGE_REQUEST2);
                        dialog.dismiss();

                    }
                });


            }
        });

        if (!SharePreferenceUtils.getInstance().getString("userId").isEmpty()) {
            loadData(SharePreferenceUtils.getInstance().getString("userId"));
        }


        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        // register connection status listener
        bean.getInstance().setConnectivityListener(this);
      /*  if (!SharePreferenceUtils.getInstance().getString("userId").isEmpty()) {
            loadData(SharePreferenceUtils.getInstance().getString("userId"));
        }
*/
    }


    /*public class CoverImage extends Fragment
    {

        String url;
        ImageView image;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.cober_image_layout , container , false);

            url = getArguments().getString("url");
            image = (ImageView)view.findViewById(R.id.image);

            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(url , image);


            return  view;
        }
    }*/

    ////////////////////internet connectivity check///////////////
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showalert(isConnected);
    }

    private void showalert(boolean isConnected) {
        if (isConnected) {

            // Toast.makeText(getActivity(), "Good! Connected to Internet", Toast.LENGTH_SHORT).show();
            //    message = "Good! Connected to Internet";
            //    color = Color.WHITE;
        } else {

            try {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getActivity());
                }
                builder.setTitle("NO INTERNET CONNECTION")
                        .setMessage("Please check your internet connection setting and click refresh")
                        .setPositiveButton(R.string.Refresh, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // Reload current fragment
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(Profile.this).attach(Profile.this).commit();
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
        }

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showalert(isConnected);

    }

    public void loadData(String userID) {

        progress.setVisibility(View.VISIBLE);

        final bean b = (bean) getContext().getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<loginResponseBean> call = cr.getProfile(userID);

        call.enqueue(new retrofit2.Callback<loginResponseBean>() {
            @Override
            public void onResponse(Call<loginResponseBean> call, retrofit2.Response<loginResponseBean> response) {


                //   if (Objects.equals(response.body().getStatus(), "1")) {

                    try {
                        CoverPager pageAdapter = new CoverPager(getChildFragmentManager(), response.body().getData().getCoverImage());
                        coverPager.setAdapter(pageAdapter);
                        indicator.setViewPager(coverPager);

                        b.userImage = response.body().getData().getUserImage();

                        ImageLoader loader = ImageLoader.getInstance();
                        loader.displayImage(response.body().getData().getUserImage(), profileimage);
                        // loader.displayImage(response.body().getData().getUserImage() , profileimg);

                        name.setText(response.body().getData().getUserName());
                        youthId.setText(Html.fromHtml("Youth Live ID: <b>" + response.body().getData().getYouthLiveId() + "</b>"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                //  } else {
                //       Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                //       Toast.makeText(getContext(),"error here", Toast.LENGTH_SHORT).show();
                //   }


                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<loginResponseBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
                Log.d("error", t.toString());
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST2 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();

            MultipartBody.Part body = null;

            String mCurrentPhotoPath = getPath(getContext(), selectedImageUri);

            File file = new File(mCurrentPhotoPath);

            RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);

            progress.setVisibility(View.VISIBLE);

            final bean b = (bean) getContext().getApplicationContext();

            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(b.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final AllAPIs cr = retrofit.create(AllAPIs.class);


            Call<updateProfileBean> call = cr.updateProfile(SharePreferenceUtils.getInstance().getString("userId"), body);

            call.enqueue(new retrofit2.Callback<updateProfileBean>() {
                @Override
                public void onResponse(Call<updateProfileBean> call, retrofit2.Response<updateProfileBean> response) {

                    b.userImage = response.body().getData().getUserImage();

                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    loadData(SharePreferenceUtils.getInstance().getString("userId"));


                    progress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<updateProfileBean> call, Throwable t) {
                    progress.setVisibility(View.GONE);
                }
            });


        } else if (requestCode == PICK_IMAGE_REQUEST1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();

            MultipartBody.Part body = null;

            String mCurrentPhotoPath = getPath(getContext(), selectedImageUri);

            File file = new File(mCurrentPhotoPath);

            RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);

            progress.setVisibility(View.VISIBLE);

            final bean b = (bean) getContext().getApplicationContext();

            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(b.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final AllAPIs cr = retrofit.create(AllAPIs.class);


            Call<loginResponseBean> call = cr.addCover(SharePreferenceUtils.getInstance().getString("userId"), "", body);

            call.enqueue(new retrofit2.Callback<loginResponseBean>() {
                @Override
                public void onResponse(Call<loginResponseBean> call, retrofit2.Response<loginResponseBean> response) {

                    b.userImage = response.body().getData().getUserImage();

                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    loadData(SharePreferenceUtils.getInstance().getString("userId"));


                    progress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<loginResponseBean> call, Throwable t) {
                    progress.setVisibility(View.GONE);
                }
            });


        }


    }

    public class CoverPager extends FragmentStatePagerAdapter {

        List<com.yl.youthlive.loginResponsePOJO.CoverImage> list = new ArrayList<>();

        public CoverPager(FragmentManager fm, List<com.yl.youthlive.loginResponsePOJO.CoverImage> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            com.yl.youthlive.CoverImage frag = new com.yl.youthlive.CoverImage();
            Bundle b = new Bundle();
            b.putString("url", list.get(position).getImage());
            frag.setArguments(b);
            return frag;
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

}
