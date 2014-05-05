package ar.com.glasit.rom.Adapters;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class KitchenPager extends ViewPager {

    public KitchenPager(Context context) {
        super(context);
    }

    public KitchenPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

}