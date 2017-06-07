package com.example.shagil.ninjalike;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.shagil.ninjalike.Activity.QuizActivity;
import com.example.shagil.ninjalike.Helper.DatabaseHelper;
import com.example.shagil.ninjalike.data.ScoreTable;

/**
 * Created by Siddiqui on 6/1/2017.
 */

public class AlertDialogFragment extends android.support.v4.app.DialogFragment {

    String skill;
    public AlertDialogFragment() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        skill=getArguments().getString("skill");
        final DatabaseHelper dbHelper=new DatabaseHelper(getActivity());
        return new AlertDialog.Builder(getActivity())
                .setTitle("All questions solved")
                .setMessage("Do you want to play again? \n This will reset your score.")
                .setPositiveButton("Show Score Card", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(getActivity(),ScoreTable.class);
                        intent.putExtra("skill",skill);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("Reset My Score", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.resetMyScore(skill);
                        Intent intent=new Intent(getActivity(),QuizActivity.class);
                        startActivity(intent);


                    }
                }).create();
    }
}
