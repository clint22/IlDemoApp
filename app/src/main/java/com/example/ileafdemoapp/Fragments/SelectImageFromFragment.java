package com.example.ileafdemoapp.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.ileafdemoapp.R;
import com.example.ileafdemoapp.Utils.AppConst;
import com.example.ileafdemoapp.Utils.Validation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Clint on 11/2/18.
 */

public class SelectImageFromFragment extends DialogFragment {

    private Unbinder unbinder;

    @BindView(R.id.txtGallery)
    TextView txtGallery;

    @BindView(R.id.txtCamera)
    TextView txtCamera;

    @BindView(R.id.txtMsg)
    TextView txtMsg;

    private CallBack mCallback;

    private boolean cameraPermission = true;
    private boolean galleryPermission = true;

    public interface CallBack {
        void onItemSelected(int i);
    }

    public void setListener(CallBack mCallback) {
        this.mCallback = mCallback;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_image, container, false);
        unbinder = ButterKnife.bind(this, view);
        WindowManager.LayoutParams params = getDialog().getWindow()
                .getAttributes();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationBottom;
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        txtCamera.setEnabled(cameraPermission);
        txtGallery.setEnabled(galleryPermission);

        if (!cameraPermission || !galleryPermission) {

            String errorMsg = (!cameraPermission) ? "Camera permission not granted"
                    : "";
            errorMsg += (!galleryPermission) ? ", Gallery permission not granted"
                    : "";
            if (Validation.isNotEmpty(errorMsg)) {
                errorMsg += ", Go to application manager to change permissions";
                txtMsg.setText(errorMsg);
                txtMsg.setVisibility(View.VISIBLE);
            }
        }

        txtCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCallBack(AppConst.CAMERA_PICTURE);
            }
        });

        txtGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCallBack(AppConst.GALLERY_PICTURE);
            }
        });


    }


    private void setCallBack(int item) {
        if (mCallback != null)
            mCallback.onItemSelected(item);
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

