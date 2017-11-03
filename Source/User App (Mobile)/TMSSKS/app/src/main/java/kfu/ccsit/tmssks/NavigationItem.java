package kfu.ccsit.tmssks;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

public class NavigationItem extends TextView {

    private int mTintColor;
    private ColorStateList mDefaultColorList;
    private Drawable mIcon;

    public NavigationItem(Context context) {
        super(context);
        init(context, null);
    }

    public NavigationItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NavigationItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mIcon = null;
        String attention = null;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NavigationItem);
            for (int i = 0; i < a.getIndexCount(); i++) {
                switch (a.getIndex(i)) {
                    case R.styleable.NavigationItem_tintColor:
                        mTintColor = a.getColor(a.getIndex(i), 0);
                        break;
                    case R.styleable.NavigationItem_navIcon:
                        mIcon = ContextCompat.getDrawable(context,
                                a.getResourceId(a.getIndex(i), 0));
                        break;
                    case R.styleable.NavigationItem_attentionText:
                        attention = a.getString(a.getIndex(i));
                }
            }
            a.recycle();
        }

        if (Build.VERSION.SDK_INT > 17) {
            setCompoundDrawablesRelativeWithIntrinsicBounds(mIcon, null, null, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(mIcon, null, null, null);
        }

        mDefaultColorList = getTextColors();
        toggleState();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setTextAlignment(TEXT_ALIGNMENT_VIEW_START);
        }

        if (attention != null && !attention.isEmpty()) {
            String raw = getText().toString();
            SpannableString spannable = new SpannableString(raw + " " + attention);
            spannable.setSpan(new ForegroundColorSpan(Color.RED), raw.length() + 1,
                    raw.length() + 1 + attention.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            setText(spannable);
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        toggleState();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        toggleState();
    }

    private void toggleState() {
        if (mIcon != null) {
            if (isEnabled() && isSelected()) {
                mIcon.setColorFilter(mTintColor, PorterDuff.Mode.SRC_IN);
                setTextColor(mTintColor);
                setTypeface(Typeface.DEFAULT_BOLD);
            } else if (isEnabled()) {
                mIcon.setColorFilter(mDefaultColorList.getDefaultColor(), PorterDuff.Mode.SRC_IN);
                setTextColor(mDefaultColorList);
                setTypeface(Typeface.DEFAULT);
            } else {
                mIcon.setColorFilter(mDefaultColorList.getColorForState(new int[]{
                                -android.R.attr.state_enabled},
                        mDefaultColorList.getDefaultColor()), PorterDuff.Mode.SRC_IN);
                setTextColor(mDefaultColorList);
                setTypeface(Typeface.DEFAULT);
            }
        }
    }
}
