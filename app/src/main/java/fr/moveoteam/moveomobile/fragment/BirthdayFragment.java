package fr.moveoteam.moveomobile.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.Button;

/**
 * Classe permettant de créer une liste déroulant avec jour, mois et année dans une boite de dialogue
 * Created by Sylvain on 30/05/15.
 */
class BirthdayFragment extends DatePickerDialog {

    public BirthdayFragment(Context context, int theme, OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth) {
        super(context, theme, listener, year, monthOfYear, dayOfMonth);
    }

    @Override
    public Button getButton(int whichButton) {
        return super.getButton(whichButton);
    }


}
