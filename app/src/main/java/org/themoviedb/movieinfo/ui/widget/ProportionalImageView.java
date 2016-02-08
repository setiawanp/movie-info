package org.themoviedb.movieinfo.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.themoviedb.movieinfo.R;

public class ProportionalImageView extends ImageView {

    private static final float NO_ASPECT_RATIO = 0.0f;

    private float mAspectRatio;

    public ProportionalImageView(Context context) {
        super(context);
    }

    public ProportionalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(context, attrs);
    }

    public ProportionalImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthParam = getLayoutParams().width;
        int heightParam = getLayoutParams().height;

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mAspectRatio == NO_ASPECT_RATIO ||
                (widthParam == ViewGroup.LayoutParams.WRAP_CONTENT &&
                    heightParam == ViewGroup.LayoutParams.WRAP_CONTENT)) return;

        int width, height;
        if (heightParam == ViewGroup.LayoutParams.WRAP_CONTENT) {
            width = getMeasuredWidth();
            height = Math.round(mAspectRatio * (float) width);
        } else {
            height = getMeasuredHeight();
            width = Math.round(mAspectRatio * (float) height);
        }
        setMeasuredDimension(width, height);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ProportionalImageView);
        try {
            mAspectRatio = ta.getFloat(R.styleable.ProportionalImageView_aspectRatio, NO_ASPECT_RATIO);
        } finally {
            ta.recycle();
        }
    }
}
