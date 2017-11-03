package kfu.ccsit.tmssks;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

public class ThemeUtils {

    /**
     * Tint drawable according to accent color (specified in the theme)
     *
     * @param context       App context
     * @param drawableResId Resource ID for the drawable to be tinted
     * @return The tinted drawable
     */
    public static Drawable tintByAccentColor(Context context, int drawableResId) {
        return tintByAccentColor(context, ContextCompat.getDrawable(context, drawableResId));
    }

    /**
     * Tint passed drawable according to accent color (specified in the theme)
     *
     * @param context  App context
     * @param drawable The drawable to be tinted
     * @return The tinted drawable
     */
    public static Drawable tintByAccentColor(Context context, Drawable drawable) {
        TypedValue typedColor = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, typedColor, true);
        drawable.setColorFilter(typedColor.data, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    /**
     * Tint drawable according to primary color (specified in the theme)
     *
     * @param context       App context
     * @param drawableResId Resource ID for the drawable to be tinted
     * @return The tinted drawable
     */
    public static Drawable tintByPrimaryColor(Context context, int drawableResId) {
        return tintByPrimaryColor(context, ContextCompat.getDrawable(context, drawableResId));
    }

    /**
     * Tint passed drawable according to primary color (specified in the theme)
     *
     * @param context  App context
     * @param drawable The drawable to be tinted
     * @return The tinted drawable
     */
    public static Drawable tintByPrimaryColor(Context context, Drawable drawable) {
        TypedValue typedColor = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedColor, true);
        drawable.setColorFilter(typedColor.data, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    /**
     * Get an attribute specified in the theme
     *
     * @param context App context
     * @param attrRes The attribute that is specified the theme
     * @return Attribute value from the current theme
     */
    public static int getThemeAttr(Context context, int attrRes) {
        TypedValue typedColor = new TypedValue();
        context.getTheme().resolveAttribute(attrRes, typedColor, true);
        return typedColor.data;
    }

    public static void updateDecorationViewVisibility(Context context, ImageView imageView,
                                                      boolean enabled) {
        int visibility = enabled ? View.VISIBLE : View.GONE;
        imageView.setVisibility(visibility);
    }
}
