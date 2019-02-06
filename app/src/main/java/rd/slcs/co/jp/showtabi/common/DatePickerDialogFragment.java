package rd.slcs.co.jp.showtabi.common;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

import rd.slcs.co.jp.showtabi.R;
import rd.slcs.co.jp.showtabi.activity.PlanAddActivity;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Activity activity;

    public DatePickerDialogFragment(){
        super();
    }

    public DatePickerDialogFragment (Activity activity) {
        super();
        this.activity = activity;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(),
                (DatePickerDialog.OnDateSetListener)activity,  year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        //日付が選択されたときの処理

    }

}