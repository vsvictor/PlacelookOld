package com.placelook.controls;

/**
 * Created by victor on 11.08.15.
 */
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

public class ExRelativeLayout extends RelativeLayout {
    private OnKeyboarListener listener;

    public ExRelativeLayout(Context context) {
        super(context);
    }
    public ExRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ExRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public void setOnKeyboarListener(OnKeyboarListener listener) {
        this.listener = listener;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Activity activity = (Activity)getContext();
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        int screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
        int diff = (screenHeight - statusBarHeight) - height;
        if (listener != null) {
            listener.onSoftKeyboardShown(diff>128); // assume all soft keyboards are at least 128 pixels high
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    public void showKeyboard(){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }
    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }
}
