package com.overflow.overlab.checkcalendar.Check;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;

/**
 * Created by over on 2017-03-24.
 */

public class CheckImageView extends AppCompatImageView {

    public int DATE;

    public CheckImageView(Context context) {
        super(context);
        DATE = 0;
    }

    public int getDATE() {
        return DATE;
    }

    public void setDATE(int DATE) {
        this.DATE = DATE;
    }
}
