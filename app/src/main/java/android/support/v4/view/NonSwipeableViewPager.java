package android.support.v4.view;

/**
 * Created by SANDIP JANA on 20-09-2018.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NonSwipeableViewPager extends ViewPager {

    private int mVelocity = 1;

    public NonSwipeableViewPager(Context context) {
        super(context);
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    void smoothScrollTo(int x, int y, int velocity) {
        //ignore passed velocity, use one defined here
        super.smoothScrollTo(x, y, mVelocity);
    }

}