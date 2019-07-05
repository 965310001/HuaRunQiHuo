package com.genealogy.by.interfaces;


import com.genealogy.by.model.FamilyBean;

/**
 * 家庭成员选中回调
 */

public interface OnFamilyClickListener {
    void onFamilySelect(FamilyBean family);
}
