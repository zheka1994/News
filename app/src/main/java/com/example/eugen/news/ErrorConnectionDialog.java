package com.example.eugen.news;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;


public class ErrorConnectionDialog extends DialogFragment {
    private OkClickListener okClickListener;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Connection Error")
                .setMessage("You don't have internet!")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        okClickListener.Okclick();
                    }
                });
        return builder.create();
    }
    public interface OkClickListener{
        void Okclick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        okClickListener = (OkClickListener)context;
    }
}
