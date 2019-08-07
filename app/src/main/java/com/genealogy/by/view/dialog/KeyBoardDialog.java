package com.genealogy.by.view.dialog;

import android.support.v4.app.FragmentManager;

import com.genealogy.by.R;

import tech.com.commoncore.dialog.BaseBottomDialog;

public class KeyBoardDialog extends BaseBottomDialog {

    public static KeyBoardDialog create(FragmentManager manager) {
        KeyBoardDialog dialog = new KeyBoardDialog();
        dialog.setFragmentManager(manager);
        return dialog;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.keyboard;
    }
}
