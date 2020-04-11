package com.acomlib.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.acomlib.util.DensityUtils;
import com.acomlib.util.ScreenUtils;
import com.acomlib.R;

import butterknife.ButterKnife;

/**
 * 基础弹出框
 * 底部弹出框
 * ADialog dialog = new ADialog(SettingPermissActivity.this,R.layout.dialog_setbackstage,true);
 * dialog.setAnimationsStyle(R.style.adialog_translate);
 * dialog.setGravity(Gravity.BOTTOM);
 * dialog.show();
 *
 * public void initView(){
 *   ButterKnife.bind(this);
 * }
 */
public class ADialog extends Dialog{

    private Context context;
    private SparseArray<View> views = new SparseArray<>();
    private int layoutId = 0;
    private int width = 0;
    private int height = 0;
    private int bgRadius = 0; //背景圆角
    private int bgColor = Color.WHITE; //背景颜色
    private boolean isMatchParent;

    public ADialog(@NonNull Context context) {
        this(context, R.layout.dialog_adialog_confirm);
    }

    public ADialog(@NonNull Context context, int layoutId) {
        this(context, layoutId, false);
    }

    public ADialog(@NonNull Context context, int layoutId, boolean isMatchParent) {
        super(context,R.style.basedialog);
        this.context = context;
        this.layoutId = layoutId;
        this.isMatchParent = isMatchParent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);
        ButterKnife.bind(this);
        init();
        initView();
    }

    /**
     * 重写加载自定义需求
     */
    public void initView(){}

    private void init() {
        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(getRoundRectDrawable(DensityUtils.dp2px(5), Color.TRANSPARENT));//getRoundRectDrawable(DensityUtils.dp2px(5), Color.WHITE)

        if(isMatchParent){
            width = WindowManager.LayoutParams.MATCH_PARENT;
        }else{
            width = (int)(ScreenUtils.getScreenWidth()*0.8);

        }
        height = WindowManager.LayoutParams.WRAP_CONTENT;
        setWidthHeight();
    }

    /**
     * 设置点击外部消失 默认消失
     * @param isTouchOutside
     * @return
     */
    public ADialog setTouchOutside(boolean isTouchOutside){
        setCanceledOnTouchOutside(isTouchOutside);
        return this;
    }
    //    >>>>>>>>>>>>>>>>>>>>>>>>>>>>设置动画>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public ADialog setAnimationsStyle(int style){
        getWindow().setWindowAnimations(style);
        return this;
    }

//    >>>>>>>>>>>>>>>>>>>>>>>>>>>>设置位置>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    /**
     * 设置位置
     * @param gravity
     * @param offX
     * @param offY
     */
    public ADialog setGravity(int gravity, int offX, int offY){
        setGravity(gravity);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.x = offX;
        layoutParams.y = offY;
        getWindow().setAttributes(layoutParams);
        return this;
    }
    public ADialog setGravity(int gravity){
        getWindow().setGravity(gravity);
        return this;
    }


    //    >>>>>>>>>>>>>>>>>>>>>>>>>>>>设置背景>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    private ADialog setbg(){
        getWindow().setBackgroundDrawable(getRoundRectDrawable(bgRadius, bgColor));
        return this;
    }

    /**
     * 设置背景颜色
     * @return
     */
    public ADialog setBgColor(int color){
        bgColor = color;
        return setbg();
    }

    public ADialog setBgColorRes(int colorRes){
        bgColor = context.getResources().getColor(colorRes);
        return setbg();
    }

    /**
     *  设置背景圆角
     * @param bgRadius
     */
    public ADialog setBgRadius(int bgRadius){
        this.bgRadius = DensityUtils.dp2px(bgRadius);
        return setbg();
    }

    /**
     *  设置背景圆角
     * @param bgRadius
     */
    public ADialog setBgRadiusPX(int bgRadius){
        this.bgRadius = bgRadius;
        return setbg();
    }



//    >>>>>>>>>>>>>>>>>>>>>>>>>>>>设置宽高>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    /**
     * 设置宽高
     */
    private ADialog setWidthHeight(){
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = width;
        lp.height = height;
        dialogWindow.setAttributes(lp);
        return this;
    }

    public ADialog setWidth(int width){
        this.width = DensityUtils.dp2px(width);
        return setWidthHeight();
    }

    public ADialog setWidthPX(int width){
        this.width = width;
        return setWidthHeight();
    }

    public ADialog setHeight(int height){
        this.height = DensityUtils.dp2px(height);
        return setWidthHeight();
    }
    public ADialog setHeightPX(int height){
        this.height = height;
        return setWidthHeight();
    }

    /**
     * 设置宽占屏幕的比例
     * @param widthRatio
     */
    public ADialog setWidthRatio(double widthRatio){
        width = (int)(ScreenUtils.getScreenWidth()*widthRatio);
        setWidthHeight();
        return this;
    }

    /**
     * 设置高占屏幕的比例
     * @param heightRatio
     */
    public ADialog setHeightRatio(double heightRatio){
        height = (int)(ScreenUtils.getScreenHeight()*heightRatio);
        setWidthHeight();
        return this;
    }

//    >>>>>>>>>>>>>>>>>>>>>>>>>>>>设置监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    /**
     * 设置监听
     * @param onClickListener
     * @param viewIds
     */
    public ADialog setOnClickListener(final DialogOnClickListener onClickListener, int... viewIds){
        final ADialog lDialog = this;
        for (int i = 0; i < viewIds.length; i++) {
            getView(viewIds[i]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClick(v, lDialog);
                }
            });
        }
        return this;
    }

    /**
     * 设置 关闭dialog的按钮
     * @param viewId
     * @return
     */
    public ADialog setCancelBtn(int viewId){
        getView(viewId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return this;
    }


//    >>>>>>>>>>>>>>>>>>>>>>>>>>>>设置常见属性>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = this.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * Will set the text of a TextView.
     *
     * @param viewId The view id.
     * @param value  The text to put in the text view.
     * @return The BaseViewHolder for chaining.
     */
    public ADialog setText(@IdRes int viewId, CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public ADialog setText(@IdRes int viewId, @StringRes int strId) {
        TextView view = getView(viewId);
        view.setText(strId);
        return this;
    }

    /**
     * Will set the image of an ImageView from a resource id.
     *
     * @param viewId     The view id.
     * @param imageResId The image resource id.
     * @return The BaseViewHolder for chaining.
     */
    public ADialog setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    /**
     * Will set background color of a view.
     *
     * @param viewId The view id.
     * @param color  A color, not a resource id.
     * @return The BaseViewHolder for chaining.
     */
    public ADialog setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * Will set background of a view.
     *
     * @param viewId        The view id.
     * @param backgroundRes A resource to use as a background.
     * @return The BaseViewHolder for chaining.
     */
    public ADialog setBackgroundRes(@IdRes int viewId, @DrawableRes int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    /**
     * Will set text color of a TextView.
     *
     * @param viewId    The view id.
     * @param textColor The text color (not a resource id).
     * @return The BaseViewHolder for chaining.
     */
    public ADialog setTextColor(@IdRes int viewId, @ColorInt int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }


    /**
     * Will set the image of an ImageView from a drawable.
     *
     * @param viewId   The view id.
     * @param drawable The image drawable.
     * @return The BaseViewHolder for chaining.
     */
    public ADialog setImageDrawable(@IdRes int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    /**
     * Add an action to set the image of an image view. Can be called multiple times.
     */
    public ADialog setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    /**
     * Add an action to set the alpha of a view. Can be called multiple times.
     * Alpha between 0-1.
     */
    public ADialog setAlpha(@IdRes int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    /**
     * Set a view visibility to VISIBLE (true) or GONE (false).
     *
     * @param viewId  The view id.
     * @param visible True for VISIBLE, false for GONE.
     * @return The BaseViewHolder for chaining.
     */
    public ADialog setGone(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * Set a view visibility to VISIBLE (true) or INVISIBLE (false).
     *
     * @param viewId  The view id.
     * @param visible True for VISIBLE, false for INVISIBLE.
     * @return The BaseViewHolder for chaining.
     */
    public ADialog setVisible(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        return this;
    }

    public static GradientDrawable getRoundRectDrawable(int radius, int color){
        //左上、右上、右下、左下的圆角半径
        float[] radiuss = {radius, radius, radius, radius, radius, radius, radius, radius};
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadii(radiuss);
        drawable.setColor(color!=0 ? color : Color.TRANSPARENT);
        return drawable;
    }

//    public static GradientDrawable getRoundRectDrawable(int radius, int color, boolean isFill, int strokeWidth){
//        //左上、右上、右下、左下的圆角半径
//        float[] radiuss = {radius, radius, radius, radius, radius, radius, radius, radius};
//        GradientDrawable drawable = new GradientDrawable();
//        drawable.setCornerRadii(radiuss);
//        drawable.setColor(isFill ? color : Color.TRANSPARENT);
//        drawable.setStroke(isFill ? 0 : strokeWidth, color);
//        return drawable;
//    }

    public interface DialogOnClickListener{
        void onClick(View v, ADialog lDialog);
    }
}