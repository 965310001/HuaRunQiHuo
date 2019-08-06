package com.genealogy.by.fragment.shupu;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.genealogy.by.R;
import com.genealogy.by.activity.PerfectingInformationActivity;
import com.genealogy.by.activity.PersonalHomePageActivity;
import com.genealogy.by.activity.RelationshipChainActivity;
import com.genealogy.by.db.User;
import com.genealogy.by.entity.SearchNearInBlood;
import com.genealogy.by.interfaces.OnFamilySelectListener;
import com.genealogy.by.model.FamilyMember;
import com.genealogy.by.utils.SPHelper;
import com.genealogy.by.utils.my.BaseTResp2;
import com.genealogy.by.view.FamilyTreeView4;
import com.genealogy.by.view.FamilyTreeView6;
import com.genealogy.by.view.FamilyTreeView7;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.mode.CacheMode;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tech.com.commoncore.base.BaseApplication;
import tech.com.commoncore.base.BaseFragment;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.ToastUtil;

public class ShuPuFragment extends BaseFragment {

    private Context mContext;
    private PopupWindow popupWindow2, popupWindowAdd, popupWindowEdit, popupWindow;
    private boolean mIsShowing;
    private static final String TAG = "ShuPuFragment";
    private String param1;

    public static ShuPuFragment newInstance(String param1) {
        ShuPuFragment fragment = new ShuPuFragment();
        Bundle bundle = new Bundle();
        bundle.putString("param1", param1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SPHelper.getBooleanSF(getContext(), "isRefresh")) {
            doit();
            doit2();
            SPHelper.setBooleanSF(getContext(), "isRefresh", false);
        }
    }

    //父
    private void doit2() {
        showLoading();
        HashMap<String, String> params = new HashMap<>();
        params.put("gId", SPHelper.getStringSF(mContext, "GId"));
        params.put("userId", SPHelper.getStringSF(mContext, "UserId"));
        JSONObject jsonObject = new JSONObject(params);
        ViseHttp.POST(ApiConstant.setAsCenter)
                .setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setJson(jsonObject)
                .request(new ACallback<BaseTResp2<SearchNearInBlood>>() {
                    @Override
                    public void onSuccess(BaseTResp2<SearchNearInBlood> data) {
                        hideLoading();
                        if (data.isSuccess()) {
                            SPHelper.saveDeviceData(mContext, "SearchNearInBlood", data.data);
//                            initView();
                            convertData(data.data);
                            Log.e(TAG, "onSuccess: data = " + data.toString());
                        } else {
                            ToastUtil.show(data.msg);
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        hideLoading();
                        ToastUtil.show(errMsg);
                        Log.e(TAG, "errMsg: " + errMsg + ",errCode:  " + errCode);
                    }
                });
    }

    @Override
    public int getContentLayout() {
        return R.layout.shupu_fuxi;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mContext = getActivity();
        param1 = getArguments().getString("param1");
        switch (param1) {
            case "父系":
                doit2();
                break;

            case "近亲":
                doit();
                break;

            case "全部":
                doit2();
                break;
        }
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     */
    class popupDismissListener implements OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    private String middleId;/*中心Id*/

    private void showPopupWindow(View view, User user) {
        View contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.activity_popup, null);
        RelativeLayout ll = contentView.findViewById(R.id.ll1);//背景图
        TextView invitation = contentView.findViewById(R.id.invitation);
        TextView relationship = contentView.findViewById(R.id.relationship);
        TextView details = contentView.findViewById(R.id.details);
        TextView core = contentView.findViewById(R.id.core);
        TextView add = contentView.findViewById(R.id.add);
        TextView edit = contentView.findViewById(R.id.edit);
        ll.setOnClickListener(view1 -> popupWindow.dismiss());
        invitation.setOnClickListener(v -> {
            popup(v, user);
            popupWindow.dismiss();
        });
        relationship.setOnClickListener(v -> {
            //关系链条
            if (TextUtils.isEmpty(middleId)) {
                middleId = SPHelper.getStringSF(mContext, "UserId");
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("gId", user.getGid());
            params.put("userId", middleId);
            params.put("heId", user.getUserid());
            JSONObject jsonObject = new JSONObject(params);
            ViseHttp.POST(ApiConstant.getRelationshipChain)
                    .setHttpCache(true)
                    .cacheMode(CacheMode.FIRST_REMOTE)
                    .setJson(jsonObject)
                    .request(new ACallback<BaseTResp2<SearchNearInBlood>>() {
                        @Override
                        public void onSuccess(BaseTResp2<SearchNearInBlood> data) {
                            if (data.isSuccess()) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", data.data);
                                FastUtil.startActivity(mContext, RelationshipChainActivity.class, bundle);
                                popupWindow.dismiss();
                            } else {
                                Log.e(TAG, "onSuccess: data = " + data.msg);
                            }
                        }

                        @Override
                        public void onFail(int errCode, String errMsg) {
                            ToastUtil.show("onFail:errMsg=" + errMsg);
                            Log.e(TAG, "errMsg: " + errMsg + ",errCode:  " + errCode);
                        }
                    });
        });
        details.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("userId", user.getUserid());
            FastUtil.startActivity(mContext, PersonalHomePageActivity.class, bundle);
            popupWindow.dismiss();
        });

        /*设为中心*/
        core.setOnClickListener(v -> {
            HashMap<String, String> params = new HashMap<>();
            params.put("gId", user.getGid());
            middleId = user.getUserid();
            params.put("userId", middleId);
            ViseHttp.POST(ApiConstant.searchNearInBlood)
                    .setHttpCache(true)
                    .cacheMode(CacheMode.FIRST_REMOTE)
                    .setJson(new JSONObject(params))
                    .request(new ACallback<BaseTResp2<SearchNearInBlood>>() {
                        @Override
                        public void onSuccess(BaseTResp2<SearchNearInBlood> data) {
                            if (data.isSuccess()) {
                                SPHelper.saveDeviceData(mContext, "SearchNearInBlood", data.data);
                                convertData(data.data);
                                popupWindow.dismiss();
                                Log.e(TAG, "onSuccess: data = " + data.toString());
                            } else {
                                Log.e(TAG, "onSuccess: data = " + data.msg);
                            }
                        }

                        @Override
                        public void onFail(int errCode, String errMsg) {
                            ToastUtil.show("onFail:errMsg=" + errMsg);
                            Log.e(TAG, "errMsg: " + errMsg + ",errCode:  " + errCode);
                        }
                    });
        });
        add.setOnClickListener(v -> {
            popupWindow.dismiss();
            showPopupWindowAdd(v, user);
        });
        edit.setOnClickListener(v -> {
            popupWindow.dismiss();
            showPopupWindowEdit(v, user);
        });
        popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setOnDismissListener(new poponDismissListener());
        ColorDrawable dw = new ColorDrawable(0x30000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setOnDismissListener(new popupDismissListener());
        backgroundAlpha(0.5f);
        // 设置好参数之后再show
        popupWindow.showAsDropDown(contentView);
    }

    class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    private void showPopupWindowAdd(View view, User user) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.activity_popupadd, null);
        contentView.findViewById(R.id.ll1).setOnClickListener(view1 -> popupWindowAdd.dismiss());
        contentView.findViewById(R.id.ll_fuqin).setOnClickListener(v -> goPerfectingActivity(user, "父亲"));
        contentView.findViewById(R.id.ll_muqin).setOnClickListener(v -> goPerfectingActivity(user, "母亲"));
        contentView.findViewById(R.id.ll_peiou).setOnClickListener(v -> goPerfectingActivity(user, "配偶"));
        contentView.findViewById(R.id.ll_erzi).setOnClickListener(v -> goPerfectingActivity(user, "儿子"));
        contentView.findViewById(R.id.ll_nver).setOnClickListener(v -> goPerfectingActivity(user, "女儿"));
        contentView.findViewById(R.id.ll_jiedi).setOnClickListener(v -> goPerfectingActivity(user, "兄弟姐妹"));

        popupWindowAdd = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindowAdd.setOnDismissListener(new popupDismissListener());
        popupWindowAdd.setBackgroundDrawable(new BitmapDrawable());
        popupWindowAdd.setOutsideTouchable(true);
        backgroundAlpha(0.5f);
        // 设置好参数之后再show
        popupWindowAdd.showAsDropDown(view);
    }

    private void goPerfectingActivity(User user, String title) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("title", title);
        bundle.putString("mUserId", user.getUserid());
        bundle.putString("Gid", user.getGid());
        popupWindowAdd.dismiss();
        FastUtil.startActivity(mContext, PerfectingInformationActivity.class, bundle);
    }

    private void showPopupWindowEdit(View view, User user) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.activity_popupedit, null);
        // 设置按钮的点击事件
        RelativeLayout ll = contentView.findViewById(R.id.ll1);//背景图
        TextView edit = contentView.findViewById(R.id.edit);//编辑
        TextView delete = contentView.findViewById(R.id.delete);//删除
        TextView cancel = contentView.findViewById(R.id.cancel);//取消
        ll.setOnClickListener(view1 -> {
            if (popupWindowAdd != null) {
                popupWindowAdd.dismiss();
            }
        });
        edit.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("title", "编辑信息");
            bundle.putSerializable("user", user);
            FastUtil.startActivity(mContext, PerfectingInformationActivity.class, bundle);
            popupWindowEdit.dismiss();
        });
        delete.setOnClickListener(v -> {
            WhetherDelete(user);
            popupWindowEdit.dismiss();
        });
        cancel.setOnClickListener(v -> popupWindowEdit.dismiss());
        popupWindowEdit = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindowEdit.setOnDismissListener(new popupDismissListener());
//        popupWindowEdit.setBackgroundDrawable(new BitmapDrawable());
//        ColorDrawable dw = new ColorDrawable(0x30000000);
        popupWindowEdit.setBackgroundDrawable(new ColorDrawable(0x30000000));
        popupWindowEdit.setOutsideTouchable(true);
        popupWindowEdit.setFocusable(true);

        View parent = ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
        popupWindowEdit.showAsDropDown(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void WhetherDelete(User user) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(mContext);
        normalDialog.setTitle("您确定要删除嘛？");
        normalDialog.setPositiveButton("确定",
                (dialog, which) -> ViseHttp.GET(ApiConstant.delUser)
                        .setHttpCache(true)
                        .cacheMode(CacheMode.FIRST_REMOTE)
                        .addParam("id", user.getUserid())
                        .request(new ACallback<BaseTResp2>() {
                            @Override
                            public void onSuccess(BaseTResp2 data) {
                                if (data.isSuccess()) {
                                    doit();
                                }
                                ToastUtil.show(data.msg);
                            }

                            @Override
                            public void onFail(int errCode, String errMsg) {
                                ToastUtil.show("onFail:errMsg=" + errMsg);
                                Log.e(TAG, "errMsg: " + errMsg + ",errCode:  " + errCode);
                            }
                        }));
        normalDialog.setNegativeButton("取消",
                (dialog, which) -> {
                });
        // 显示
        normalDialog.show();
    }

    private void popup(View view, User user) {
        initPopup(user);
        if (!mIsShowing) {
            popupWindow2.showAtLocation(mContentView.findViewById(R.id.line1), Gravity.BOTTOM, 0, 0);
            mIsShowing = true;
        }
    }

    private void initPopup(User user) {
        View pop = View.inflate(mContext, R.layout.keyboard, null);
        List<String> str = new ArrayList<>();
        EditText evinput = pop.findViewById(R.id.evinput);//背景图
        TextView cancel = pop.findViewById(R.id.tv_cancel);//
        TextView tv1 = pop.findViewById(R.id.tv1);
//        LinearLayout numberkey = pop.findViewById(R.id.numberkey);
        TextView dismiss = pop.findViewById(R.id.dismiss);

        TextView tvName = pop.findViewById(R.id.tv_name);
        tvName.setText(String.format("请输入\"%s\"的手机号码", user.getName()));
        dismiss.setOnClickListener(view -> {
            popupWindow2.dismiss();
            mIsShowing = false;
            evinput.setText("");
        });
        tv1.setOnClickListener(view -> {
            str.add("1");
            evinput.setText(ListToString(str));
            setSelection(evinput);
        });
        TextView tv2 = pop.findViewById(R.id.tv2);
        tv2.setOnClickListener(view -> {
            str.add("2");
            evinput.setText(ListToString(str));
            setSelection(evinput);
        });
        TextView tv3 = pop.findViewById(R.id.tv3);
        tv3.setOnClickListener(view -> {
            str.add("3");
            evinput.setText(ListToString(str));
            setSelection(evinput);
        });
        TextView tv4 = pop.findViewById(R.id.tv4);
        tv4.setOnClickListener(view -> {
            str.add("4");
            evinput.setText(ListToString(str));
            setSelection(evinput);
        });
        TextView tv5 = pop.findViewById(R.id.tv5);
        tv5.setOnClickListener(view -> {
            str.add("5");
            evinput.setText(ListToString(str));
            setSelection(evinput);
        });
        TextView tv6 = pop.findViewById(R.id.tv6);
        tv6.setOnClickListener(view -> {
            str.add("6");
            evinput.setText(ListToString(str));
            setSelection(evinput);
        });
        TextView tv7 = pop.findViewById(R.id.tv7);
        tv7.setOnClickListener(view -> {
            str.add("7");
            evinput.setText(ListToString(str));
            setSelection(evinput);
        });
        TextView tv8 = pop.findViewById(R.id.tv8);
        tv8.setOnClickListener(view -> {
            str.add("8");
            evinput.setText(ListToString(str));
            setSelection(evinput);
        });
        TextView tv9 = pop.findViewById(R.id.tv9);
        tv9.setOnClickListener(view -> {
            str.add("9");
            evinput.setText(ListToString(str));
            setSelection(evinput);
        });
        TextView tv0 = pop.findViewById(R.id.tv0);
        tv0.setOnClickListener(view -> {
            str.add("0");
            evinput.setText(ListToString(str));
            setSelection(evinput);
        });
        TextView tv10 = pop.findViewById(R.id.tv10);
        tv10.setOnClickListener(view -> {
            str.add(".");
            evinput.setText(ListToString(str));
            setSelection(evinput);
        });
        TextView shanchu = pop.findViewById(R.id.shanchu);
        shanchu.setOnClickListener(view -> {
            if (str.size() > 0) {
                str.remove(str.size() - 1);
                evinput.setText(ListToString(str));
                setSelection(evinput);
            }
        });
        TextView queding = pop.findViewById(R.id.queding);
        queding.setOnClickListener(view -> {
            invitationDoit(ListToString(str), user.getUserid(), user.getRelationship());
//            popupWindow2.dismiss();
            mIsShowing = false;
            evinput.setText("");
        });
        cancel.setOnClickListener(view -> {
            popupWindow2.dismiss();
            mIsShowing = false;
            evinput.setText("");
        });
        TextView invitation = pop.findViewById(R.id.invitation);
        invitation.setOnClickListener(view -> invitationDoit(str.toString(), user.getUserid(), user.getRelationship()));
        popupWindow2 = new PopupWindow(pop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow2.setTouchable(true);
        popupWindow2.setOutsideTouchable(false);
        popupWindow2.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mIsShowing = false;
    }

    private void setSelection(EditText evinput) {
        evinput.setSelection(evinput.getText().toString().trim().length());
    }

    private void showNormalDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(mContext);
        normalDialog.setTitle("发起成功");
        normalDialog.setMessage("对方注册或登录APP即可看到您的邀请函。");
        normalDialog.setPositiveButton("分享下载链接到微信",
                (dialog, which) -> {

                });
        normalDialog.setNegativeButton("取消",
                (dialog, which) -> dialog.dismiss());
        // 显示
        normalDialog.show();
    }

    private void invitationDoit(String phone, String inviteesId, String relation) {
        phone = phone.replace(",", "")
                .replace("[", "")
                .replace("]", "")
                .replace(" ", "");
        String userId = SPHelper.getStringSF(mContext, "UserId");
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("inviteesId", inviteesId);
        params.put("phone", phone.trim());
        params.put("type", "0");
        params.put("relation", relation);
        ViseHttp.POST(ApiConstant.inviteUser)
                .setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setJson(new JSONObject(params))
                .request(new ACallback<BaseTResp2<Object>>() {
                    @Override
                    public void onSuccess(BaseTResp2<Object> data) {
                        if (data.isSuccess()) {
                            showNormalDialog();
                            ToastUtil.show("邀请成功");
                            popupWindow2.dismiss();
                        } else {
                            Log.e(TAG, "onSuccess: msg = " + data.msg + ",status=" + data.status);
                            ToastUtil.show("提示 ：" + data.msg);
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        Log.e("", "errMsg: " + errMsg + "errCode:  " + errCode);
                    }
                });
    }

    private static String ListToString(List<String> list) {
        if (list != null && list.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                if (TextUtils.isEmpty(list.get(i))) {
                    continue;
                }
                sb.append(list.get(i));
            }
            return sb.toString();
        }
        return "";
    }

    //网络请求
    private void doit() {
        showLoading();
        HashMap<String, String> params = new HashMap<>();
        params.put("gId", SPHelper.getStringSF(mContext, "GId"));
        params.put("userId", SPHelper.getStringSF(mContext, "UserId"));
        ViseHttp.POST(ApiConstant.searchNearInBlood)
                .setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setJson(new JSONObject(params))
                .request(new ACallback<BaseTResp2<SearchNearInBlood>>() {
                    @Override
                    public void onSuccess(BaseTResp2<SearchNearInBlood> data) {
                        hideLoading();
                        if (data.isSuccess()) {
                            SPHelper.saveDeviceData(mContext, "SearchNearInBlood", data.data);
                            convertData(data.data);
                        } else {
                            ToastUtil.show(data.msg);
                            Log.e(TAG, "onSuccess: data = " + data.msg);
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        hideLoading();
                        ToastUtil.show(errMsg);
                        Log.e(TAG, "errMsg: " + errMsg + ",errCode:  " + errCode);
                    }
                });
    }

    /*转换数据*/
    private void convertData(SearchNearInBlood data) {
        FamilyTreeView4 mFtvTree = mContentView.findViewById(R.id.ftv_tree);//近亲
        FamilyTreeView6 mFtvTree1 = mContentView.findViewById(R.id.ftv_tree1);//全部
        FamilyTreeView7 mFtvTree2 = mContentView.findViewById(R.id.ftv_tree2);//父系

        OnFamilySelectListener onFamilySelectListener = new OnFamilySelectListener() {
            @Override
            public void onFamilySelect(FamilyMember family) {
            }

            @Override
            public void onFamilySelect(SearchNearInBlood family) {
                User user = new User(family.getId() + "",
                        SPHelper.getStringSF(BaseApplication.getInstance(), "GId"));
                user.setSex(family.getSex());
                user.setSurname(family.getSurname());
                user.setName(family.getName());
                user.setBirthArea(family.getBirthArea());
                user.setBirthPlace(family.getBirthPlace());
                user.setEmail(family.getEmail());
                user.setWord(family.getWord());
                user.setUsedName(family.getUsedName());
                user.setIsCelebrity(family.getIsCelebrity());
                user.setNumber(family.getNumber());
                user.setNoun(family.getNoun());
                user.setPhone(family.getPhone());
                user.setRanking(family.getRanking());
                user.setRelationship(family.getRelationship());
                showPopupWindow(family.getMineView(), user);
            }
        };

        switch (param1) {
            case "父系":
                mFtvTree.setVisibility(View.GONE);
                mFtvTree1.setVisibility(View.GONE);

                mFtvTree2.setVisibility(View.VISIBLE);
                mFtvTree2.setFamilyMember(data);
                mFtvTree2.setOnFamilySelectListener(onFamilySelectListener);
                break;

            case "近亲":
                mFtvTree1.setVisibility(View.GONE);
                mFtvTree2.setVisibility(View.GONE);

                mFtvTree.setVisibility(View.VISIBLE);
                mFtvTree.setFamilyMember(data);
                mFtvTree.setOnFamilySelectListener(onFamilySelectListener);

//                mFtvTree.setOnFamilySelectListener(new OnFamilySelectListener() {
//                    @Override
//                    public void onFamilySelect(FamilyMember family) {
//                    }
//
//                    @Override
//                    public void onFamilySelect(SearchNearInBlood family) {
//                        User user = new User(family.getId() + "",
//                                SPHelper.getStringSF(BaseApplication.getInstance(), "GId"));
//                        user.setSex(family.getSex());
//                        user.setSurname(family.getSurname());
//                        user.setName(family.getName());
//                        user.setBirthArea(family.getBirthArea());
//                        user.setBirthPlace(family.getBirthPlace());
//                        user.setEmail(family.getEmail());
//                        user.setWord(family.getWord());
//                        user.setUsedName(family.getUsedName());
//                        user.setIsCelebrity(family.getIsCelebrity());
//                        user.setNumber(family.getNumber());
//                        user.setNoun(family.getNoun());
//                        user.setPhone(family.getPhone());
//                        user.setRanking(family.getRanking());
//                        user.setRelationship(family.getRelationship());
//                        showPopupWindow(family.getMineView(), user);
//                    }
//                });
                break;

            case "全部":
                mFtvTree.setVisibility(View.GONE);
                mFtvTree2.setVisibility(View.GONE);

                mFtvTree1.setVisibility(View.VISIBLE);
                mFtvTree1.setFamilyMember(data);
                mFtvTree1.setOnFamilySelectListener(onFamilySelectListener);
//                mFtvTree1.setOnFamilySelectListener(new OnFamilySelectListener() {
//                    @Override
//                    public void onFamilySelect(FamilyMember family) {
//                    }
//
//                    @Override
//                    public void onFamilySelect(SearchNearInBlood family) {
//                        User user = new User(family.getId() + "",
//                                SPHelper.getStringSF(BaseApplication.getInstance(), "GId"));
//                        user.setSex(family.getSex());
//                        user.setSurname(family.getSurname());
//                        user.setName(family.getName());
//                        user.setBirthArea(family.getBirthArea());
//                        user.setBirthPlace(family.getBirthPlace());
//                        user.setEmail(family.getEmail());
//                        user.setWord(family.getWord());
//                        user.setUsedName(family.getUsedName());
//                        user.setIsCelebrity(family.getIsCelebrity());
//                        user.setNumber(family.getNumber());
//                        user.setNoun(family.getNoun());
//                        user.setPhone(family.getPhone());
//                        user.setRanking(family.getRanking());
//                        user.setRelationship(family.getRelationship());
//                        showPopupWindow(family.getMineView(), user);
//                    }
//                });
                break;
        }
    }


    public void hidePopupWindow() {
        if (null != popupWindow2 && popupWindow2.isShowing()) {
            popupWindow2.dismiss();
            return;
        }
        if (null != popupWindowAdd && popupWindowAdd.isShowing()) {
            popupWindow2.dismiss();
            return;
        }
        if (null != popupWindowEdit && popupWindowEdit.isShowing()) {
            popupWindow2.dismiss();
            return;
        }
        if (null != popupWindow && popupWindow.isShowing()) {
            popupWindow2.dismiss();
            return;
        }
    }
}