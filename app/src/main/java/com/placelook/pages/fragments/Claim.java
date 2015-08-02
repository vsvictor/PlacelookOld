package com.placelook.pages.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.placelook.MainActivity;
import com.placelook.R;

import java.sql.Ref;

/**
 * Created by victor on 02.08.15.
 */
public class Claim extends BaseFragment {
    private OnClaim claim;
    private CheckBox cbFalseGoal;
    private CheckBox cbTryingToSeeOperator;
    private CheckBox cbStrangeBehavior;
    private CheckBox cbIllegalRequests;
    private EditText edClaimComment;
    private RelativeLayout rlSendClaim;
    private TextView tvClaimText;
    private TextView tvClaim;
    private TextView tvFalseGoal;
    private TextView tvTryingToSeeOperator;
    private TextView tvStrangeBehavior;
    private TextView tvIllegalRequests;
    private TextView tvSendClaim;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
    }
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        rView = inf.inflate(R.layout.claim, container, false);
        return rView;
    }
    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        tvClaimText = (TextView) rView.findViewById(R.id.tvClaimText);
        tvClaimText.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvClaim = (TextView) rView.findViewById(R.id.tvClaim);
        tvClaim.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvFalseGoal = (TextView) rView.findViewById(R.id.tvFalseGoal);
        tvFalseGoal.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvTryingToSeeOperator = (TextView) rView.findViewById(R.id.tvTryingToSeeOperator);
        tvTryingToSeeOperator.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvStrangeBehavior = (TextView) rView.findViewById(R.id.tvStrangeBehavior);
        tvStrangeBehavior.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
        tvIllegalRequests = (TextView) rView.findViewById(R.id.tvIllegalRequests);
        tvIllegalRequests.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));

        cbFalseGoal = (CheckBox) rView.findViewById(R.id.cbFalseGoal);
        cbTryingToSeeOperator = (CheckBox) rView.findViewById(R.id.cbTryingToSeeOperator);
        cbStrangeBehavior = (CheckBox) rView.findViewById(R.id.cbStrangeBehavior);
        cbIllegalRequests = (CheckBox) rView.findViewById(R.id.cbIllegalRequests);
        edClaimComment = (EditText) rView.findViewById(R.id.edClaimComment);
        rlSendClaim = (RelativeLayout) rView.findViewById(R.id.rlSendClaim);
        rlSendClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                claim.onClaim(cbFalseGoal.isChecked(),
                              cbTryingToSeeOperator.isChecked(),
                              cbStrangeBehavior.isChecked(),
                              cbIllegalRequests.isChecked(),
                              edClaimComment.getText().toString());
            }
        });
        tvSendClaim = (TextView) rView.findViewById(R.id.tvSendClaim);
        tvSendClaim.setTypeface(Typeface.createFromAsset(MainActivity.getMainActivity().getAssets(), "fonts/Roboto-Regular.ttf"));
    }
    public void setOnClaim(OnClaim listener){
        this.claim = listener;
    }
}
