package com.example.ileafdemoapp.Utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

/**
 * Created by Clint on 10/2/18.
 */

public class Validation {

    public static boolean hasText(TextInputLayout textInputLayout) {
        String text = textInputLayout.getEditText().getText().toString().trim();
        textInputLayout.setError(null);

        if (text.length() == 0) {
//            if (textInputLayout.getId() == R.id.til_email) return false;
            textInputLayout.setError("This field cannot be empty");
            textInputLayout.requestFocus();
            return false;
        }
        return true;
    }

    public static String getString(Object param) {
        try {
            if (param instanceof TextInputLayout) {
                TextInputLayout textInputLayout = (TextInputLayout) param;
                if (textInputLayout.getEditText() != null) {
                    return (textInputLayout.getEditText().getText().toString().trim());
                }
            }
            if (param instanceof EditText) return ((EditText) param).getText().toString().trim();
            if (param instanceof TextView) return ((TextView) param).getText().toString().trim();
            if (param instanceof String) return ((String) param).trim();
            if (param instanceof Editable) return param.toString().trim();
            if (param instanceof CharSequence) return param.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static Boolean isNotEmpty(Object param) {
        try {
            String field = getString(param);
            return !(field.trim().isEmpty() || field.trim().equals("") || field.equals("null"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public static Uri getTempUri(Context context) {
        try {
            createfolder();

            //        mPhotoUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            //                new ContentValues());
            String tempPath =
                    Environment.getExternalStorageDirectory().getPath() + AppConst.APP_FOLDER_NAME + "temp.jpg";
            File file = new File(tempPath);
            Uri photoURI = null;
            //return Uri.fromFile(file);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                photoURI = FileProvider.getUriForFile(context,
                        context.getApplicationContext().getPackageName() + ".provider", file);
            } else {
                photoURI = Uri.fromFile(file);
            }
            return photoURI;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void createfolder() {

        String filepath = Environment.getExternalStorageDirectory().getPath();

        File file1 = new File(filepath, AppConst.APP_FOLDER_NAME);

        if (!file1.exists()) {
            file1.mkdirs();

            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
