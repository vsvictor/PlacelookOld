package com.placelook.pages.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.placelook.MainActivity;
import com.placelook.commands.Registration;
import com.placelook.utils.Convertor;
import com.placelook.R;

/**
 * Created by victor on 21.06.15.
 */
public class Header extends BaseFragment {
    private ImageView ivUser;
    private TextView tvLogo;
    private RelativeLayout rlLogo;
    private OnClick uListener;
    private OnClick press;
    private ImageView ivPrev;
    private RelativeLayout rlPress;

    @Override
    public void onCreate(Bundle saved){
        super.onCreate(saved);
    }
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
        rView = inf.inflate(R.layout.header, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved){
        super.onViewCreated(view, saved);
        ivUser = (ImageView) rView.findViewById(R.id.ivUser);
        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uListener.onClick();        }
        });
        tvLogo = (TextView) rView.findViewById(R.id.tvLogo);
        tvLogo.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
        rlLogo = (RelativeLayout) rView.findViewById(R.id.rlLogo);
        ivPrev = (ImageView) rView.findViewById(R.id.ivPrev);
        rlPress = (RelativeLayout) rView.findViewById(R.id.rlPress);
        rlPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                press.onClick();
            }
        });
    }
    public void setInvisibleAll(){
        ivPrev.setVisibility(View.INVISIBLE);
        ivUser.setVisibility(View.INVISIBLE);
    }
    public void setVisiblePrev(boolean visible){
        ivPrev.setVisibility(visible?View.VISIBLE:View.INVISIBLE);
    }
    public void setVisibleUser(boolean visible){
        ivUser.setVisibility(visible?View.VISIBLE:View.INVISIBLE);
    }

    public void setTextHeader(String text){
        tvLogo.setText(text);
    }
    public void setLeftHeader(int dp){
        rlLogo.setLeft((int) Convertor.convertDpToPixel(dp, MainActivity.getMainActivity()));
    }
    public void setOnUserClickListener(OnClick listener){
        this.uListener = listener;
    }
    public void setOnHeaderClickListener(OnClick listener){this.press = listener;}
}
