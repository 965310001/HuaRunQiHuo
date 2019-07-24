package com.genealogy.by.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.genealogy.by.R;

/**
 * 底部弹框
 */
public class BottomDialog extends DialogFragment {

    private View.OnClickListener mClick;
    private String[] mIds;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialog);
    }


    @Override
    public void onStart() {
        super.onStart();
        //设置 dialog 的宽高
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        //设置 dialog 的背景为 null
        getDialog().getWindow().setBackgroundDrawable(null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //去除标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; //底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        View view = createView(inflater, container);
        initClick(view);
        return view;
    }

    public void setOnClick(View.OnClickListener click) {
        this.mClick = click;
    }

    public void setGone(String... ids) {
        ids = new String[ids.length];
        this.mIds = ids;
        int index = 0;
        for (String id : ids) {
            mIds[index++] = id;
        }
    }

    private void initClick(View view) {
        if (null != mClick) {
            view.findViewById(R.id.t_translation).setOnClickListener(mClick);
            view.findViewById(R.id.tv_relational).setOnClickListener(mClick);
            view.findViewById(R.id.tv_delete).setOnClickListener(mClick);
        }
        view.findViewById(R.id.tv_cancel).setOnClickListener(v -> dismiss());
        try {
            if (null != mIds && mIds.length > 0) {
                view.findViewById(R.id.tv_delete).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected View createView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.dialog_bottom, container, false);
    }
}