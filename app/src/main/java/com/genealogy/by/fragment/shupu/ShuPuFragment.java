package com.genealogy.by.fragment.shupu;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.genealogy.by.R;
import com.genealogy.by.activity.PerfectingInformationActivity;
import com.genealogy.by.activity.PersonalHomePageActivity;
import com.genealogy.by.db.User;
import com.genealogy.by.entity.SearchNearInBlood;
import com.genealogy.by.interfaces.OnFamilySelectListener;
import com.genealogy.by.model.FamilyMember;
import com.genealogy.by.utils.SPHelper;
import com.genealogy.by.utils.my.BaseTResp2;
import com.genealogy.by.view.FamilyTreeView2;
import com.genealogy.by.view.FamilyTreeView3;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.mode.CacheMode;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.ToastUtil;

import static android.widget.PopupWindow.OnDismissListener;

public class ShuPuFragment extends Fragment {
    private Context mContext = getActivity();
    private PopupWindow popupWindow2;
    private PopupWindow popupWindowAdd, popupWindowEdit, popupWindow;
    private boolean mIsShowing = false;
    private static final String TAG = "ShuPuFragment";
    private List<User> users;
    private User user;
    private LinearLayout table;
    private View rootView;
    public boolean flg = true;

    private String param1;


    public static ShuPuFragment newInstance(String param1) {
        ShuPuFragment fragment = new ShuPuFragment();
        Bundle bundle = new Bundle();
        bundle.putString("param1", param1);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity();
        param1 = getArguments().getString("param1");
        Log.i(TAG, "onCreate: " + param1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        doit();
        rootView = inflater.inflate(R.layout.shupu_fuxi, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

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

        });
        details.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("userId", "");
            FastUtil.startActivity(mContext, PersonalHomePageActivity.class);
        });
        core.setOnClickListener(v -> {

        });
        add.setOnClickListener(v -> {
            ToastUtil.show("点击添加");
            popupWindow.dismiss();
            showPopupWindowAdd(v, user);
        });
        edit.setOnClickListener(v -> {
            ToastUtil.show("点击编辑");
            popupWindow.dismiss();
            showPopupWindowEdit(v);
        });
        popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setOnDismissListener(new poponDismissListener());
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
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
        // 设置按钮的点击事件
        RelativeLayout ll = contentView.findViewById(R.id.ll1);//背景图
        LinearLayout fuqin = contentView.findViewById(R.id.ll_fuqin);//父亲
        LinearLayout muqin = contentView.findViewById(R.id.ll_muqin);//母亲
        LinearLayout peiou = contentView.findViewById(R.id.ll_peiou);//配偶
        LinearLayout erzi = contentView.findViewById(R.id.ll_erzi);//儿子
        LinearLayout nver = contentView.findViewById(R.id.ll_nver);//女儿
        LinearLayout jiedi = contentView.findViewById(R.id.ll_jiedi);//姐弟
        ll.setOnClickListener(view1 -> popupWindowAdd.dismiss());
        fuqin.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("title", "父亲");
            bundle.putString("Userid", user.getUserid());
            bundle.putString("Gid", user.getGid());
            FastUtil.startActivity(mContext, PerfectingInformationActivity.class, bundle);
            popupWindowAdd.dismiss();
        });
        muqin.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("title", "母亲");
            bundle.putString("Userid", user.getUserid());
            bundle.putString("Gid", user.getGid());
            FastUtil.startActivity(mContext, PerfectingInformationActivity.class, bundle);
            popupWindowAdd.dismiss();
        });
        peiou.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("title", "配偶");
            bundle.putString("Userid", user.getUserid());
            bundle.putString("Gid", user.getGid());
            FastUtil.startActivity(mContext, PerfectingInformationActivity.class, bundle);
            popupWindowAdd.dismiss();
        });
        erzi.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("title", "儿子");
            bundle.putString("Userid", user.getUserid());
            bundle.putString("Gid", user.getGid());
            FastUtil.startActivity(mContext, PerfectingInformationActivity.class, bundle);
            popupWindowAdd.dismiss();
        });
        nver.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("title", "女儿");
            bundle.putString("Userid", user.getUserid());
            bundle.putString("Gid", user.getGid());
            FastUtil.startActivity(mContext, PerfectingInformationActivity.class, bundle);
            popupWindowAdd.dismiss();
        });
        jiedi.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("title", "兄弟姐妹");
            bundle.putString("Userid", user.getUserid());
            bundle.putString("Gid", user.getGid());
            FastUtil.startActivity(mContext, PerfectingInformationActivity.class, bundle);
            popupWindowAdd.dismiss();
        });
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

    private void showPopupWindowEdit(View view) {
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
            FastUtil.startActivity(mContext, PerfectingInformationActivity.class, bundle);
            popupWindowEdit.dismiss();
        });
        delete.setOnClickListener(v -> {
            WhetherDelete();
            popupWindowEdit.dismiss();
        });
        cancel.setOnClickListener(v -> popupWindowEdit.dismiss());
        popupWindowEdit = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindowEdit.setOnDismissListener(new popupDismissListener());
        popupWindowEdit.setBackgroundDrawable(new BitmapDrawable());
        popupWindowEdit.setOutsideTouchable(true);
        // 设置好参数之后再show
        popupWindowEdit.showAsDropDown(view);
    }

    private void WhetherDelete() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(mContext);
        normalDialog.setTitle("您确定要删除嘛？");
        normalDialog.setPositiveButton("确定",
                (dialog, which) -> {

                });
        normalDialog.setNegativeButton("取消",
                (dialog, which) -> {
                });
        // 显示
        normalDialog.show();
    }

    public void popup(View view, User user) {
        if (popupWindow2 == null) {
            initPopup(user);
        }
        if (!mIsShowing) {
            popupWindow2.showAtLocation(rootView.findViewById(R.id.line1), Gravity.BOTTOM, 0, 0);
            mIsShowing = true;
        }
    }

    private void initPopup(User user) {
        View pop = View.inflate(mContext, R.layout.keyboard, null);
        List<String> str = new ArrayList<>();
        EditText evinput = pop.findViewById(R.id.evinput);//背景图
        evinput.setGravity(Gravity.END);
        TextView cancel = pop.findViewById(R.id.tv_cancel);//
        TextView tv1 = pop.findViewById(R.id.tv1);
        LinearLayout numberkey = pop.findViewById(R.id.numberkey);
        TextView dismiss = pop.findViewById(R.id.dismiss);

        dismiss.setOnClickListener(view -> {
            if (flg) {
                numberkey.setVisibility(View.GONE);
                flg = false;
            } else {
                numberkey.setVisibility(View.VISIBLE);
                flg = true;
            }

        });
        tv1.setOnClickListener(view -> {
            str.add("1");
            evinput.setText(ListToString(str));
        });
        TextView tv2 = pop.findViewById(R.id.tv2);
        tv2.setOnClickListener(view -> {
            str.add("2");
            evinput.setText(ListToString(str));
        });
        TextView tv3 = pop.findViewById(R.id.tv3);
        tv3.setOnClickListener(view -> {
            str.add("3");
            evinput.setText(ListToString(str));
        });
        TextView tv4 = pop.findViewById(R.id.tv4);
        tv4.setOnClickListener(view -> {
            str.add("4");
            evinput.setText(ListToString(str));
        });
        TextView tv5 = pop.findViewById(R.id.tv5);
        tv5.setOnClickListener(view -> {
            str.add("5");
            evinput.setText(ListToString(str));
        });
        TextView tv6 = pop.findViewById(R.id.tv6);
        tv6.setOnClickListener(view -> {
            str.add("6");
            evinput.setText(ListToString(str));
        });
        TextView tv7 = pop.findViewById(R.id.tv7);
        tv7.setOnClickListener(view -> {
            str.add("7");
            evinput.setText(ListToString(str));
        });
        TextView tv8 = pop.findViewById(R.id.tv8);
        tv8.setOnClickListener(view -> {
            str.add("8");
            evinput.setText(ListToString(str));
        });
        TextView tv9 = pop.findViewById(R.id.tv9);
        tv9.setOnClickListener(view -> {
            str.add("9");
            evinput.setText(ListToString(str));
        });
        TextView tv0 = pop.findViewById(R.id.tv0);
        tv0.setOnClickListener(view -> {
            str.add("0");
            evinput.setText(ListToString(str));
        });
        TextView tv10 = pop.findViewById(R.id.tv10);
        tv10.setOnClickListener(view -> {
            str.add(".");
            evinput.setText(ListToString(str));
        });
        TextView shanchu = pop.findViewById(R.id.shanchu);
        shanchu.setOnClickListener(view -> {

            str.remove(str.size() - 1);
            evinput.setText(ListToString(str));
        });
        TextView queding = pop.findViewById(R.id.queding);
        queding.setOnClickListener(view -> {
            invitationDoit(ListToString(str), user.getUserid());
            popupWindow2.dismiss();
        });
        cancel.setOnClickListener(view -> popupWindow2.dismiss());
        TextView invitation = pop.findViewById(R.id.invitation);
        invitation.setOnClickListener(view -> invitationDoit(str.toString(), user.getUserid()));
        popupWindow2 = new PopupWindow(pop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow2.setTouchable(true);
        popupWindow2.setOutsideTouchable(false);
        popupWindow2.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mIsShowing = false;
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

    public void invitationDoit(String phone, String inviteesId) {
        phone = phone.replace(",", "");
        phone = phone.replace("[", "");
        phone = phone.replace("]", "");
        String userId = SPHelper.getStringSF(mContext, "UserId");
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("inviteesId", inviteesId);
        params.put("phone", phone.trim());
        JSONObject jsonObject = new JSONObject(params);
        final MediaType JSONS = MediaType.parse("application/json; charset=utf-8");
        ViseHttp.POST(ApiConstant.inviteUser)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setRequestBody(RequestBody.create(JSONS, jsonObject.toString()))
                .request(new ACallback<BaseTResp2<Object>>() {
                    @Override
                    public void onSuccess(BaseTResp2<Object> data) {
                        if (data.status == 200) {
                            showNormalDialog();
                            ToastUtil.show("邀请成功");
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

    public static String ListToString(List<String> list) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null || list.get(i) == "") {
                    continue;
                }
                sb.append(list.get(i));
            }
        }
        return sb.toString();
    }

    //遍历请求到的数据
    public void godata(SearchNearInBlood data) {
        user = new User();
        if (data.getSurname() != null) {
            user.setName(data.getSurname() + data.getName());
        } else {
            user.setName("");
        }
        user.setSex(data.getSex());
        if (data.getProfilePhoto() != null) {
            user.setProfilePhoto(data.getProfilePhoto());
        } else {
            user.setProfilePhoto("");
        }
        user.setUserid(data.getId() + "");
        user.setGid(data.getGId() + "");
        AddView(rootView, user);
        List<SearchNearInBlood> childrens = data.getChildrens();
        if (null != childrens && childrens.size() > 0) {
            for (int i = 0; i < childrens.size(); i++) {
                SearchNearInBlood children = childrens.get(i);
                if (children.getName() != null) {
                    user.setName(children.getName());
                } else {
                    user.setName("");
                }
                user.setSex(children.getSex());
                if (children.getProfilePhoto() != null) {
                    user.setProfilePhoto(children.getProfilePhoto());
                } else {
                    user.setProfilePhoto("");
                }
                AddView(rootView, user);
                ForChildrensGo(children.getChildrens());
                if (null != children.getSpouses()) {
                    if (children.getSpouses().size() > 0) {
                        SearchNearInBlood spouses = children.getSpouses().get(i);
                        if (spouses.getName() != null) {
                            user.setName(spouses.getName());
                        } else {
                            user.setName("");
                        }
                        user.setSex(spouses.getSex());
                        if (spouses.getProfilePhoto() != null) {
                            user.setProfilePhoto(spouses.getProfilePhoto());
                        } else {
                            user.setProfilePhoto("");
                        }
                        user.setGid(spouses.getGId() + "");
                        user.setUserid(spouses.getId() + "");
                        AddView(rootView, user);
                    }
                }
            }
        }
        List<SearchNearInBlood> spouses = data.getSpouses();
        if (null != spouses && spouses.size() != 0) {
            for (int i = 0; i < spouses.size(); i++) {
                SearchNearInBlood spouse = spouses.get(i);
                if (spouse.getName() != null) {
                    user.setName(spouse.getName());
                } else {
                    user.setName("");
                }
                user.setSex(spouse.getSex());
                if (spouse.getProfilePhoto() != null) {
                    user.setProfilePhoto(spouse.getProfilePhoto());
                } else {
                    user.setProfilePhoto("");
                }
                user.setGid(spouse.getGId() + "");
                user.setUserid(spouse.getId() + "");
                AddView(rootView, user);
            }
        }
    }

    public void ForChildrensGo(List<SearchNearInBlood> data) {
        if (null != data) {
            for (int i = 0; i < data.size(); i++) {
                user.setName(data.get(i).getName());
                user.setSex(data.get(i).getSex());
                user.setProfilePhoto(data.get(i).getProfilePhoto());
                AddView(rootView, user);
                if (data.get(i).getChildrens().size() > 0) {
//                    ForChildrensGo((List<Children>) data.get(i));
                    ForChildrensGo(data);
                }
                if (data.get(i).getSpouses().size() != 0) {
                    for (int f = 0; f < data.get(i).getSpouses().size(); f++) {
                        SearchNearInBlood spouses = data.get(i).getSpouses().get(f);
                        if (spouses.getName() != null) {
                            user.setName(spouses.getName());
                        } else {
                            user.setName("");
                        }
                        user.setSex(spouses.getSex());
                        if (spouses.getProfilePhoto() != null) {
                            user.setProfilePhoto(spouses.getProfilePhoto());
                        } else {
                            user.setProfilePhoto("");
                        }
                        AddView(rootView, user);
                    }
                }
            }
        }
    }

    //网络请求
    void doit() {
        HashMap<String, String> params = new HashMap<>();
        params.put("gId", SPHelper.getStringSF(mContext, "GId"));
        params.put("userId", SPHelper.getStringSF(mContext, "UserId"));
        JSONObject jsonObject = new JSONObject(params);
        final MediaType JSONS = MediaType.parse("application/json;charset=utf-8");
        ViseHttp.POST(ApiConstant.searchNearInBlood)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setRequestBody(RequestBody.create(JSONS, jsonObject.toString()))
                .request(new ACallback<BaseTResp2<SearchNearInBlood>>() {
                    @Override
                    public void onSuccess(BaseTResp2<SearchNearInBlood> data) {
                        if (data.status == 200) {
                            SPHelper.saveDeviceData(mContext, "SearchNearInBlood", data.data);
//                            godata(data.data);
                            initView();
                            convertData(data.data);

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
    }

    /*转换数据*/
    private void convertData(SearchNearInBlood data) {
        mFtvTree.setFamilyMember(data);
        mFtvTree.setOnFamilySelectListener(new OnFamilySelectListener() {
            @Override
            public void onFamilySelect(FamilyMember family) {
            }

            @Override
            public void onFamilySelect(SearchNearInBlood family) {
                Log.i(TAG, "onFamilySelect: " + family.toString());
            }
        });
    }

    private FamilyTreeView3 mFtvTree;
    private FamilyTreeView2 mFtvTree2;

    private void initView() {
        mFtvTree = rootView.findViewById(R.id.ftv_tree);
        mFtvTree2 = rootView.findViewById(R.id.ftv_tree2);
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void AddView(final View shuPuFragment, User user) {
        table = shuPuFragment.findViewById(R.id.shupu_table);
        View contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.item_user, null);
        LinearLayout lluser = contentView.findViewById(R.id.lluser);
        RelativeLayout rlName = contentView.findViewById(R.id.rl_name);
        lluser.setOnClickListener(view -> showPopupWindow(view, user));
        TextView imgs = contentView.findViewById(R.id.imgs);
        TextView name = contentView.findViewById(R.id.name);
        if (user.getSex() == 1) {
            setItemUser(rlName, imgs, name, R.mipmap.girl, R.color.C_D3606B);
        } else {
            setItemUser(rlName, imgs, name, R.mipmap.boy, R.color.user);
        }
        name.setText(user.getName());
        table.addView(contentView);
    }

    private void setItemUser(RelativeLayout rlName, TextView imgs, TextView name, int imageId, int colorId) {
        imgs.setBackgroundResource(imageId);
        name.setBackgroundResource(colorId);
        rlName.setBackgroundResource(colorId);
    }
}