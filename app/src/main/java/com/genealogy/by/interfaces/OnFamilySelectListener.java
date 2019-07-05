package com.genealogy.by.interfaces;


import com.genealogy.by.model.FamilyMember;

/**
 * 家庭成员选中回调
 */

public interface OnFamilySelectListener {
    void onFamilySelect(FamilyMember family);
}
