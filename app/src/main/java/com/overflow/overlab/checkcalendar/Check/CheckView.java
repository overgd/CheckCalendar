package com.overflow.overlab.checkcalendar.Check;

import android.content.Context;
import android.os.Build;
import android.widget.RelativeLayout;

import com.overflow.overlab.checkcalendar.R;

/**
 * Created by over on 10/18/2016.
 */

public class CheckView extends RelativeLayout {

    Context context;
    int STATUS;

    public CheckView(Context context) {
        super(context);
        this.context = context;

        setCheck();
    }

    public void setCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setBackground(context.getDrawable(R.drawable.ic_check_black_24dp));
        } else {
            setBackground(context.getResources().getDrawable(R.drawable.ic_check_black_24dp));
        }
    }

    public void clear() {
        setBackground(null);
    }
}
