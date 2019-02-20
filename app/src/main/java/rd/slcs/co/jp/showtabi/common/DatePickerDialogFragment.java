package rd.slcs.co.jp.showtabi.common;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Activity activity;

    Date date = null;

    public DatePickerDialogFragment(){
        super();
    }

    /**
     * 初期日付指定なしの場合のコンストラクタ
     * @param activity アクティビティ
     */
    public DatePickerDialogFragment (Activity activity) {
        super();
        this.activity = activity;
    }

    /**
     * 初期日付指定の場合のコンストラクタ
     * @param activity    アクティビティ
     * @param date        初期日付
     */
    public DatePickerDialogFragment (Activity activity, Date date) {
        super();
        this.activity = activity;
        this.date = date;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year;
        int month;
        int day;
        // 初期日付の指定がない場合
        if (date == null) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

        }
        // 初期日付の指定がある場合
        else {
            SimpleDateFormat fmtYear = new SimpleDateFormat("yyyy");
            SimpleDateFormat fmtMonth = new SimpleDateFormat("MM");
            SimpleDateFormat fmtDay = new SimpleDateFormat("dd");
            String sYear = fmtYear.format(date);
            String sMonth = fmtMonth.format(date);
            String sDay = fmtDay.format(date);

            year = Integer.parseInt(sYear);
            month = Integer.parseInt(sMonth) - 1;
            day = Integer.parseInt(sDay);

        }
        return new DatePickerDialog(getActivity(),
                (DatePickerDialog.OnDateSetListener)activity,  year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        //日付が選択されたときの処理
    }

}