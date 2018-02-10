package com.example.ileafdemoapp.Utils;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

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
}
