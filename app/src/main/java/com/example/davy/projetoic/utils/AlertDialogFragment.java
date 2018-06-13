package com.example.davy.projetoic.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

public class AlertDialogFragment extends DialogFragment {
    String mMessege;
    Context mContext;
    public AlertDialogFragment() {

    }

    /**
     * Exibe uma menssagem para o usuario
     * @param mMessege menssagem a ser apresentada
     */
    @SuppressLint("ValidFragment")
    public AlertDialogFragment(String mMessege) {
        this.mMessege = mMessege;
    }

    @SuppressLint("ValidFragment")
    public AlertDialogFragment(String mMessege, Context context) {
        this.mMessege = mMessege;
        mContext = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder;
        if(mContext != null)
            builder = new AlertDialog.Builder(getActivity());
        else
            builder = new AlertDialog.Builder(mContext);
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