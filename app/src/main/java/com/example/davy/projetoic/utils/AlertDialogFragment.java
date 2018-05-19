package com.example.davy.projetoic.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

public class AlertDialogFragment extends DialogFragment {
    String mMessege;

    public AlertDialogFragment() {

    }

    @SuppressLint("ValidFragment")
    public AlertDialogFragment(String mMessege) {
        this.mMessege = mMessege;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mMessege)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialogFragment.this.dismiss();
                    }
                });
        return builder.create();
    }
}