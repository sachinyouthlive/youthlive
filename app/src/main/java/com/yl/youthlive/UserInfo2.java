package com.yl.youthlive;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yl.youthlive.Activitys.UserInformation;

import org.webrtc.CallSessionFileRotatingLogSink;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfo2 extends AppCompatActivity {

    CircleImageView profile;
    ImageButton upload;
    EditText name , status;
    TextView dob;
    RadioGroup gender;
    RadioButton male , female;
    Button submit;
    ProgressBar progress;
    Uri selectedImage;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    private static final int RESULT_LOAD_IMG = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info2);


        profile = findViewById(R.id.view16);
        upload = findViewById(R.id.imageButton16);
        name = findViewById(R.id.editText2);
        status = findViewById(R.id.editText3);
        dob = findViewById(R.id.textView63);
        gender = findViewById(R.id.radioGroup);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        submit = findViewById(R.id.button18);
        progress = findViewById(R.id.progress);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectImage();

            }
        });

    }


    private void SelectImage() {

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/CaviarDreamsBold.ttf");

        final CharSequence[] items = {
                typeface(typeface , "Take Photo from Camera"),
                typeface(typeface , "Choose from Gallery"),
                typeface(typeface , "Cancel")
        };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UserInfo2.this);
        //builder.setTitle("Add Photo!");



        builder.setTitle(typeface(typeface , "Add Photo!"));
        builder.setIcon(R.drawable.ic_picture);
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

    private SpannableString typeface(Typeface typeface, CharSequence chars) {
        if (chars == null) {
            return null;
        }
        SpannableString s = new SpannableString(chars);
        s.setSpan(new TypefaceSpan(typeface), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }

    public class TypefaceSpan extends MetricAffectingSpan {

        private final Typeface typeface;

        public TypefaceSpan(Typeface typeface) {
            this.typeface = typeface;
        }

        @Override
        public void updateDrawState(TextPaint tp) {
            tp.setTypeface(typeface);
            tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }

        @Override
        public void updateMeasureState(TextPaint p) {
            p.setTypeface(typeface);
            p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
    }

}
