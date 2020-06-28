package com.example.zapimini.commons;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.zapimini.AddCreditActivity;
import com.example.zapimini.AddNonRecurringExpenseActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddItemDialog extends DialogFragment {
    String[] addItemOptions;
    Intent intent;

    public AddItemDialog(String[] addItemOptions) {
        this.addItemOptions = addItemOptions;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add item:")
                .setItems(this.addItemOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                intent = new Intent(getActivity(),
                                        AddNonRecurringExpenseActivity.class);
                                startActivity(intent);
                                //getActivity().finish();
                                break;
                            case 1:
                                intent = new Intent(getActivity(), AddCreditActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                                break;
                        }

                    }
                });
        return builder.create();
    }
}
