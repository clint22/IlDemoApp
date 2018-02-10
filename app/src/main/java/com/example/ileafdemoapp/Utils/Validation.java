package com.example.ileafdemoapp.Utils;

import android.support.design.widget.TextInputLayout;

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
}
