package com.ctyeung.ndkex1;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.NumberPicker;

public class NumberPickerX extends NumberPicker
{
    public NumberPickerX(Context context) {
        super(context);
    }

    public NumberPickerX(Context context, AttributeSet attrs) {
        super(context, attrs);
        processAttributes(attrs);
    }

    private void processAttributes(AttributeSet attrs) {
        this.setMinValue(attrs.getAttributeIntValue(null, "minValue", 0));
        this.setMaxValue(attrs.getAttributeIntValue(null, "maxValue", 100));
        this.setValue(attrs.getAttributeIntValue(null, "initial", 50));
    }

    public NumberPickerX(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(attrs);
    }
}
