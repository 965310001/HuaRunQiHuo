package com.genealogy.by.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.genealogy.by.R;
import com.genealogy.by.entity.SearchNearInBlood;
import com.genealogy.by.interfaces.OnFamilySelectListener;
import com.genealogy.by.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 家谱树自定义ViewGroup 父系
 */

public class FamilyTreeView6 extends ViewGroup {

    private static final int MAX_HEIGHT_DP = 590;//最大高度为590dp
    private static final int SPACE_WIDTH_DP = 20;//间距为20dp
    private static final int ITEM_WIDTH_DP = 50;//家庭成员View宽度50dp
    private static final int ITEM_HEIGHT_DP = 80;//家庭成员View高度80dp
    private static final float CALL_TEXT_SIZE_SP = 9f;//称呼文字大小9sp
    private static final float NAME_TEXT_SIZE_SP = 11f;//名称文字大小11sp
    private static final int LINE_WIDTH_DP = 2;//连线宽度2dp
    private static final int SCROLL_WIDTH = 2;//移动超过2dp，响应滑动，否则属于点击

    private OnFamilySelectListener mOnFamilySelectListener;

    private int mScreenWidth;//屏幕宽度PX
    private int mScreenHeight;//屏幕高度PX

    private int mItemWidthPX;//家庭成员View宽度PX
    private int mItemHeightPX;//家庭成员View高度PX
    private int mMaxWidthPX;//最大宽度PX
    private int mMaxHeightPX;//最大高度PX
    private int mSpacePX;//元素间距PX
    private int mLineWidthPX;//连线宽度PX

    private int mWidthMeasureSpec;
    private int mHeightMeasureSpec;

    private int mShowWidthPX;//在屏幕所占的宽度
    private int mShowHeightPX;//在屏幕所占的高度

    private SearchNearInBlood mFamilyMember;//我的

    private View mMineView;//我的View

    private List<View> mGenerationView;/*几世*/

    private int mGrandChildrenMaxWidth;//孙子女所占总长度

    private Paint mPaint;//连线样式
    private Path mPath;//路径

    private int mScrollWidth;//移动范围
    private int mCurrentX;//当前X轴偏移量
    private int mCurrentY;//当前Y轴偏移量
    private int mLastTouchX;//最后一次触摸的X坐标
    private int mLastTouchY;//最后一次触摸的Y坐标
    private int mLastInterceptX;
    private int mLastInterceptY;

    private int mCurrentLeft;//当前选中View的Left距离
    private int mCurrentTop;//当前选中View的Top距离
    private int mCurrentScrollX;//当前滚动位置
    private int mCurrentScrollY;//当前滚动位置

    public FamilyTreeView6(Context context) {
        this(context, null, 0);
    }

    public FamilyTreeView6(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FamilyTreeView6(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mScreenWidth = DisplayUtil.getScreenWidth();
        mScreenHeight = DisplayUtil.getScreenHeight();
        mScrollWidth = DisplayUtil.dip2px(SCROLL_WIDTH);
        mSpacePX = DisplayUtil.dip2px(SPACE_WIDTH_DP);
        mLineWidthPX = DisplayUtil.dip2px(LINE_WIDTH_DP);
        mItemWidthPX = DisplayUtil.dip2px(ITEM_WIDTH_DP);
        mItemHeightPX = DisplayUtil.dip2px(ITEM_HEIGHT_DP);
        mWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mItemWidthPX, MeasureSpec.EXACTLY);
        mHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mItemHeightPX, MeasureSpec.EXACTLY);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.reset();
        mPaint.setColor(0xFF888888);
        mPaint.setStrokeWidth(mLineWidthPX);
        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setPathEffect(new DashPathEffect(new float[]{mLineWidthPX, mLineWidthPX * 4}, 0));

        mPath = new Path();
        mPath.reset();
    }

    private void recycleAllView() {
        removeAllViews();
        mMineView = null;

        if (null != mGenerationView) {
            mGenerationView.clear();
        } else {
            mGenerationView = new ArrayList<>();
        }
    }

    private void initData(SearchNearInBlood familyMember) {
        this.mFamilyMember = familyMember;
//        mMyChildren = mFamilyMember.getChildren();
    }


    private void initWidthAndHeight() {
        final int[] widthDP = {
                830,//第一代最大宽度
                720,//第二代最大宽度
                ITEM_WIDTH_DP,//第三代最大宽度
                ITEM_WIDTH_DP,//第四代最大宽度
                ITEM_WIDTH_DP//第五代最大宽度
        };

        if (mFamilyMember.getUser().getSpouse() != null) {
            widthDP[2] = ITEM_WIDTH_DP + SPACE_WIDTH_DP + ITEM_WIDTH_DP * 2;
        }

        List<SearchNearInBlood> mMyChildren = mFamilyMember.getUser().getChildrens();
        initWidthAndHeight(widthDP, mMyChildren);

        mMaxWidthPX = mScreenWidth;
        for (int width : widthDP) {
            final int widthPX = DisplayUtil.dip2px(width);
            if (widthPX > mMaxWidthPX) {
                mMaxWidthPX = widthPX;
            }
        }

        mMaxHeightPX = Math.max(DisplayUtil.dip2px(MAX_HEIGHT_DP), mScreenHeight);

        Log.i(TAG, "initWidthAndHeight: " + mGrandChildrenMaxWidth);
    }

    private void initWidthAndHeight(int[] widthDP, List<SearchNearInBlood> mMyChildren) {
        if (mMyChildren != null && mMyChildren.size() > 0) {
            widthDP[3] += (SPACE_WIDTH_DP + ITEM_WIDTH_DP) * mMyChildren.size();
            widthDP[4] = 0;
            for (int i = 0; i < mMyChildren.size(); i++) {
                final SearchNearInBlood child = mMyChildren.get(i);
                final List<SearchNearInBlood> grandChildrenList = child.getChildrens();

                final int grandchildMaxWidthDP;
                if (grandChildrenList != null && grandChildrenList.size() > 0) {
                    final int grandchildCount = grandChildrenList.size();
                    if (grandchildCount == 1 && mMyChildren.size() == 1) {
                        grandchildMaxWidthDP = ITEM_WIDTH_DP + SPACE_WIDTH_DP;
                    } else if (grandchildCount == 2 && child.getSpouse() != null) {
                        grandchildMaxWidthDP = (ITEM_WIDTH_DP + SPACE_WIDTH_DP) * 5 / 2;
                    } else {
                        grandchildMaxWidthDP = (ITEM_WIDTH_DP + SPACE_WIDTH_DP) * grandchildCount;
                    }
                } else {
                    if (mMyChildren.size() > 1) {
                        if (child.getSpouse() != null) {
                            grandchildMaxWidthDP = (ITEM_WIDTH_DP + SPACE_WIDTH_DP) * 2;
                        } else {
                            grandchildMaxWidthDP = ITEM_WIDTH_DP + SPACE_WIDTH_DP;
                        }
                    } else {
                        grandchildMaxWidthDP = ITEM_WIDTH_DP + SPACE_WIDTH_DP;
                    }
                }
                widthDP[4] += grandchildMaxWidthDP;
//                initWidthAndHeight(widthDP, grandChildrenList);
            }
            widthDP[4] -= SPACE_WIDTH_DP;
            mGrandChildrenMaxWidth = DisplayUtil.dip2px(widthDP[4]);
        }
    }

    private void initView() {
        mMineView = createFamilyView(mFamilyMember.getUser());
        mFamilyMember.getUser().setMineView(mMineView);
        List<SearchNearInBlood.GenerationBean> beans = mFamilyMember.getGeneration();
        if (null != beans && beans.size() > 0) {
            int index = 0;
            for (SearchNearInBlood.GenerationBean bean : beans) {
                mGenerationView.add(createGeneration(String.format("%d", (++index)), bean));
            }
        }
        View view;
        /*儿子*/
        List<SearchNearInBlood> childes = mFamilyMember.getUser().getChildrens();
        if (null != childes && childes.size() > 0) {
            for (SearchNearInBlood children : childes) {
                children.setMineView(view = createFamilyView(children));
                mFamilyMember.getUser().addChildren(view);
                /*儿媳*/
                createFamilyView(children, children.getSpouses(), false);
                /*孙子*/
                createFamilyView(children, children.getChildrens(), true);
            }
        }

        /*妻子*/
        List<SearchNearInBlood> spouses = mFamilyMember.getUser().getSpouses();
        if (null != spouses && spouses.size() > 0) {
            for (SearchNearInBlood spouse : spouses) {
                spouse.setMineView(view = createFamilyView(spouse));
                mFamilyMember.getUser().addSpouses(view);
            }
        }
        Log.i(TAG, "initView: " + mFamilyMember);

//        if (mMySpouse != null) {
//            mSpouseView = createFamilyView(mMySpouse);
//        }
//        mChildrenView.clear();
//        if (mMyChildren != null) {
//            for (FamilyMember family : mMyChildren) {
//                mChildrenView.add(createFamilyView(family));
//                final FamilyMember childSpouse = family.getSpouse();
//                if (childSpouse != null) {
//                    mChildSpouseView.add(createFamilyView(childSpouse));
//                }
//
//                final List<FamilyMember> grandChildrens = family.getChildren();
//
//                if (grandChildrens != null && grandChildrens.size() > 0) {
//                    for (FamilyMember childFamily : grandChildrens) {
//                        mGrandChildrenView.add(createFamilyView(childFamily));
//                    }
//                }
//            }
//        }
    }

    private void createFamilyView(SearchNearInBlood familyMember, List<SearchNearInBlood> child, boolean isChild) {
        if (null != familyMember && null != child && child.size() > 0) {
            View view;
            for (SearchNearInBlood bean : child) {
                bean.setMineView(view = createFamilyView(bean));
                if (isChild) {/*儿子*/
                    familyMember.addChildren(view);
                    createFamilyView(bean, bean.getSpouses(), false);
                } else {/*儿媳*/
                    familyMember.addSpouses(view);
                }
                createFamilyView(bean, bean.getChildrens(), true);
            }
        }
    }

    private View createGeneration(String title, SearchNearInBlood.GenerationBean bean) {
        final View familyView = LayoutInflater.from(getContext()).inflate(R.layout.item_generation, this, false);
        familyView.getLayoutParams().width = mItemWidthPX;
        familyView.getLayoutParams().height = mItemHeightPX;

        final TextView tvTitle = familyView.findViewById(R.id.tv_title);
        tvTitle.getLayoutParams().height = (mItemHeightPX - mItemWidthPX) / 2;
        tvTitle.setTextSize(CALL_TEXT_SIZE_SP);
        tvTitle.setText(title);

        final TextView tvNum = familyView.findViewById(R.id.tv_num);
        tvNum.getLayoutParams().height = (mItemHeightPX - mItemWidthPX) / 2;
        tvNum.setTextSize(CALL_TEXT_SIZE_SP);
        tvNum.setText(String.format("男%d女%d", bean.getMen(), bean.getWomen()));

        this.addView(familyView);
        return familyView;
    }

    private View createFamilyView(SearchNearInBlood family) {
        Log.i(TAG, "createFamilyView: " + family.getNickName());
        final View familyView = LayoutInflater.from(getContext()).inflate(R.layout.item_family, this, false);
        familyView.getLayoutParams().width = mItemWidthPX;
        familyView.getLayoutParams().height = mItemWidthPX + mItemHeightPX;
        familyView.setTag(family);

        final ImageView ivAvatar = familyView.findViewById(R.id.iv_avatar);
//        ivAvatar.getLayoutParams().height = mItemWidthPX;

        final TextView tvCall = familyView.findViewById(R.id.tv_call);
//        tvCall.getLayoutParams().height = (mItemHeightPX - mItemWidthPX) / 2;
        tvCall.setTextSize(CALL_TEXT_SIZE_SP);
        tvCall.setText("(" + family.getNickName() + ")");

        final TextView tvName = familyView.findViewById(R.id.tv_name);
//        tvName.getLayoutParams().height = (mItemHeightPX - mItemWidthPX) / 2;
        tvName.setTextSize(NAME_TEXT_SIZE_SP);
        tvName.setText(family.getNickName());

        final String url = family.getProfilePhoto();
        if (!TextUtils.isEmpty(url)) {
            Glide.with(getContext())
                    .load(url)
                    .apply(new RequestOptions().error(R.mipmap.family_avatar))
                    .into(ivAvatar);
        }
//        if (family.isSelect()) {
//            ivAvatar.setBackgroundResource(R.drawable.shape_red_circle);
//        }

        familyView.setOnClickListener(click);

        this.addView(familyView);
        return familyView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mShowWidthPX = MeasureSpec.getSize(widthMeasureSpec);
        mShowHeightPX = MeasureSpec.getSize(heightMeasureSpec);

        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            childView.measure(mWidthMeasureSpec, mHeightMeasureSpec);
        }

        setMeasuredDimension(mMaxWidthPX, mMaxHeightPX);
    }

    private static final String TAG = "FamilyTreeView6";

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (mCurrentScrollX == 0 && mCurrentScrollY == 0) {
            scrollTo((left + right - mShowWidthPX) / 2, (top + bottom - mShowHeightPX) / 2);
        } else {
            scrollTo(mCurrentScrollX, mCurrentScrollY);
        }

        if (mMineView != null) {
            int mineLeft;
            int mineTop;
            if (mCurrentLeft == 0 && mCurrentTop == 0) {
                mineLeft = (left + right - mItemWidthPX) / 2;
                mineTop = (top + bottom - mItemHeightPX) / 2;
            } else {
                mineLeft = mCurrentLeft;
                mineTop = mCurrentTop;
            }

            // TODO: 2019/7/15  GenerationView
            if (null != mGenerationView && mGenerationView.size() > 0) {
                int index = 0;
                for (View view : mGenerationView) {
                    setChildViewFrame(view, mineLeft / 2, (++index) * mItemHeightPX, mItemWidthPX, mItemHeightPX);
                }
            }

//            mineLeft = mineLeft / 2 + 2 * mItemWidthPX - mItemWidthPX / 2;
//            setChildViewFrame(mMineView, mineLeft, mItemHeightPX, mItemWidthPX, mItemHeightPX);

            mineTop = mItemHeightPX;

            mineLeft = mineLeft / 4 * 3;
            setChildViewFrame(mMineView, mineLeft, mineTop, mItemWidthPX, mItemHeightPX);

            List<View> mSpouseView = mFamilyMember.getUser().getSpouse();
            if (null != mSpouseView && mSpouseView.size() > 0) {
                int index = 0;
                for (View view : mSpouseView) {
                    setChildViewFrame(view,
                            mineLeft + (++index) * (mSpacePX),
                            mineTop,
                            mItemWidthPX, mItemHeightPX);
                }
            }
            SearchNearInBlood user = mFamilyMember.getUser();

            List<View> mChildrenView = user.getChildren();
            Log.i(TAG, "onLayout: " + mChildrenView.size());

            List<SearchNearInBlood> mMyChildren = user.getChildrens();
            if (isListView(mChildrenView)) {
                final int childTop = mineTop + mItemHeightPX + mSpacePX * 2;
//                int childLeft = mineLeft + mItemWidthPX / 2 - mGrandChildrenMaxWidth / 2;

                int childLeft = mineLeft;

                final int grandChildrenTop = childTop + mItemHeightPX + mSpacePX * 2;
                int grandChildrenLeft = childLeft;
                final int childCount = mChildrenView.size();
                for (int i = 0; i < childCount; i++) {//儿子
                    final View myChildView = mChildrenView.get(i);
                    final SearchNearInBlood myChild = mMyChildren.get(i);
//                    final List<SearchNearInBlood> myChildSpouse = myChild.getSpouses();
                    final List<SearchNearInBlood> myGrandChildren = myChild.getChildrens();
                    final List<View> mGrandChildrenView = myChild.getChildren();

                    if (myGrandChildren != null && myGrandChildren.size() > 0) {
                        final int startGrandChildLeft = grandChildrenLeft;
                        int endGrandChildLeft = grandChildrenLeft;

                        final int myGrandChildrenCount = myGrandChildren.size();

                        int mineLeft1 = mineLeft;
                        for (int j = 0; j < myGrandChildrenCount; j++) {
                            final View grandChildView = mGrandChildrenView.get(j);
                            Log.i(TAG, "onLayout: " + myGrandChildren.get(j).getNickName());
                            setChildViewFrame(grandChildView, grandChildrenLeft, grandChildrenTop, mItemWidthPX, mItemHeightPX);
                            /*孙媳*/
                            List<View> spouse = myGrandChildren.get(j).getSpouse();
                            int index = 0;
                            if (isListView(spouse)) {
                                for (View view : spouse) {
                                    index++;
                                    setChildViewFrame(view,
                                            grandChildrenLeft + (index) * (mSpacePX + mItemWidthPX),
                                            grandChildrenTop, mItemWidthPX, mItemHeightPX);
                                }
                            }
                            grandChildrenLeft = grandChildrenLeft + spouse.size() * (mItemWidthPX + mSpacePX);

                            /*孙子 的儿子*/
                            mineLeft1 += childrenLayout(
                                    mineLeft1,
                                    grandChildrenTop,
                                    myGrandChildren.get(j),
                                    myGrandChildren.get(j).getChildrens(),
                                    myGrandChildren.get(j).getChildren());


                            endGrandChildLeft = grandChildrenLeft;
                            grandChildrenLeft += mItemWidthPX + mSpacePX;

                            childLeft = mineLeft + (myChild.getSpouses().size() - 1) * mSpacePX +
                                    myChild.getSpouses().size() * mItemWidthPX;
                        }

                        childLeft = (endGrandChildLeft - startGrandChildLeft) / 2 + startGrandChildLeft;
                    } else {
                        childLeft = grandChildrenLeft;
                        grandChildrenLeft += mSpacePX + mItemWidthPX;
                    }

                    childLeft = mineLeft;
                    setChildViewFrame(myChildView, childLeft, childTop, mItemWidthPX, mItemHeightPX);

                    childLeft = mineLeft + (i + 1) * mSpacePX;
                    grandChildrenLeft =
                            childrenSpouseLayout(childTop, mineLeft, grandChildrenLeft, myChild.getSpouses(),
                                    myChild.getSpouse());

                    childLeft = mineLeft + (myChild.getSpouses().size() - 1) * mSpacePX +
                            myChild.getSpouses().size() * mItemWidthPX;
                }
            }
        }
    }


    //话孙子
    private int childrenLayout(int mineLeft, int mineTop, SearchNearInBlood familyMember, List<SearchNearInBlood> childes, List<View> childrenView) {
        if (null != childes && childes.size() > 0 &&
                isListView(childrenView)) {
            final int childTop = mineTop + mItemHeightPX + mSpacePX * 2;
            int childLeft = mineLeft;
            final int grandChildrenTop = childTop + mItemHeightPX + mSpacePX * 2;
            int grandChildrenLeft = childLeft;
            final int childCount = childrenView.size();

            for (int i = 0; i < childCount; i++) {
                final View myChildView = childrenView.get(i);//每一个儿子
                if (null != myChildView) {
//                    final SearchNearInBlood myChild = childes.get(i);
//                    if (null != myChild) {
//                        final List<SearchNearInBlood> mChildes = myChild.getChildrens();
//                        if (mChildes != null && mChildes.size() > 0) {
//                            final int startGrandChildLeft = grandChildrenLeft;
//                            int endGrandChildLeft = grandChildrenLeft;
//
//                            List<View> chideViews = myChild.getChildren();
//                            SearchNearInBlood searchNearInBlood;
//                            for (int j = 0; j < chideViews.size(); j++) {
//                                final View grandChildView = chideViews.get(j);
//                                setChildViewFrame(grandChildView, grandChildrenLeft,
//                                        grandChildrenTop, mItemWidthPX, mItemHeightPX);
//                                endGrandChildLeft = grandChildrenLeft;
//                                grandChildrenLeft += mItemWidthPX + mSpacePX;
//                                searchNearInBlood = mChildes.get(j);
//                                /*孙子*/
////                                childrenLayout(
////                                        grandChildrenLeft,
////                                        grandChildrenTop, searchNearInBlood, searchNearInBlood.getChildrens(),
////                                        searchNearInBlood.getChildren());
//
//                                /*孙媳*/
////                                grandChildrenLeft = childrenSpouseLayout(grandChildrenTop,
////                                        grandChildrenLeft - (mItemWidthPX + mSpacePX),
//////                                        grandChildrenLeft,
////                                        grandChildrenLeft, searchNearInBlood.getSpouses(),
////                                        searchNearInBlood.getSpouse());
//
//                                grandChildrenLeft += mItemWidthPX + mSpacePX;
//                            }
//                            childLeft = (endGrandChildLeft - startGrandChildLeft) / 2 + startGrandChildLeft;
//                        } else {
//                            childLeft = grandChildrenLeft;
//                            grandChildrenLeft += mSpacePX + mItemWidthPX;
//                        }
//                    }

                    setChildViewFrame(myChildView,
                            grandChildrenLeft,
                            childTop, mItemWidthPX, mItemHeightPX);

                    List<View> spouse = childes.get(i).getSpouse();
                    if (isListView(spouse)) {
                        int index = 0;
                        for (View view : spouse) {
                            index++;
                            grandChildrenLeft += (index) * (mSpacePX + mItemWidthPX);
                            setChildViewFrame(view,
                                    grandChildrenLeft,
                                    childTop, mItemWidthPX, mItemHeightPX);
                        }
//                        grandChildrenLeft += (mItemWidthPX + mSpacePX);
                        childLeft += index * (mItemWidthPX + mSpacePX);
                    } else {
                        grandChildrenLeft += (mSpacePX + mItemWidthPX);
                        childLeft += (i + 1) * (mSpacePX);
                    }
                }
            }
            return grandChildrenLeft;
        }
        return 0;
    }

    private void childrenLayout(int mineLeft, int mineTop,
                                List<SearchNearInBlood> childes, List<View> childrenView) {
        if (null != childes && childes.size() > 0 &&
                childrenView != null && childrenView.size() > 0) {

            final int childTop = mineTop + mItemHeightPX + mSpacePX * 2;
            int childLeft = mineLeft + mItemWidthPX / 2 - mGrandChildrenMaxWidth / 2;
            final int grandChildrenTop = childTop + mItemHeightPX + mSpacePX * 2;
            int grandChildrenLeft = childLeft;
            final int childCount = childrenView.size();
            for (int i = 0; i < childCount; i++) {
                final View myChildView = childrenView.get(i);//每一个儿子
                if (null != myChildView) {
                    final SearchNearInBlood myChild = childes.get(i);
                    if (null != myChild) {
                        final List<SearchNearInBlood> mChildes = myChild.getChildrens();
                        if (mChildes != null && mChildes.size() > 0) {
                            final int startGrandChildLeft = grandChildrenLeft;
                            int endGrandChildLeft = grandChildrenLeft;

                            List<View> chideViews = myChild.getChildren();
                            SearchNearInBlood searchNearInBlood;
                            for (int j = 0; j < chideViews.size(); j++) {//孙子
                                final View grandChildView = chideViews.get(j);
                                setChildViewFrame(grandChildView, grandChildrenLeft,
                                        grandChildrenTop, mItemWidthPX, mItemHeightPX);
                                endGrandChildLeft = grandChildrenLeft;
                                grandChildrenLeft += mItemWidthPX + mSpacePX;
                                searchNearInBlood = mChildes.get(j);
                                /*孙子*/
                                childrenLayout(
                                        grandChildrenLeft,
                                        grandChildrenTop, searchNearInBlood.getChildrens(),
                                        searchNearInBlood.getChildren());

                                /*孙媳*/
                                grandChildrenLeft = childrenSpouseLayout(grandChildrenTop,
                                        grandChildrenLeft - (mItemWidthPX + mSpacePX),
//                                        grandChildrenLeft,
                                        grandChildrenLeft, searchNearInBlood.getSpouses(),
                                        searchNearInBlood.getSpouse());

                                grandChildrenLeft += mItemWidthPX + mSpacePX;
                            }
                            childLeft = (endGrandChildLeft - startGrandChildLeft) / 2 + startGrandChildLeft;
                        } else {
                            childLeft = grandChildrenLeft;
                            grandChildrenLeft += mSpacePX + mItemWidthPX;
                        }
                    }
                    setChildViewFrame(myChildView, childLeft, childTop, mItemWidthPX, mItemHeightPX);

                    // TODO: 2019/7/9 子配偶位置设置
                    grandChildrenLeft = childrenSpouseLayout(childTop, childLeft, grandChildrenLeft, myChild.getSpouses(),
                            myChild.getSpouse());
                }
            }
        }
    }

    private int childrenSpouseLayout(int childTop, int childLeft, int grandChildrenLeft,
                                     List<SearchNearInBlood> myChild, List<View> mChildSpouseView) {
        if (mChildSpouseView != null && mChildSpouseView.size() > 0) {
            for (int j = 0; j < mChildSpouseView.size(); j++) {
                Log.i(TAG, "childrenLayout: " + myChild.get(j)
                        .getNickName() + " " + myChild.get(j)
                        .getRelationship());

                final View spouseView = mChildSpouseView.get(j);
//                final int spouseLeft = childLeft + (j + 1) * (mItemWidthPX + mSpacePX);
                final int spouseLeft = childLeft + mSpacePX + mItemWidthPX;
                setChildViewFrame(spouseView, spouseLeft, childTop, mItemWidthPX, mItemHeightPX);
                grandChildrenLeft = Math.max(grandChildrenLeft, spouseLeft + mSpacePX + mItemWidthPX);

//                        chilrenLayout(mineLeft, mineTop, myChildSpouse.get(i1).getChildrens(),
//                                myChildSpouse.get(i1).getChildren());

            }
        }
        return grandChildrenLeft;
    }


    private void setChildViewFrame(View childView, int left, int top, int width, int height) {
        childView.layout(left, top, left + width, top + height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawGenerationLine(canvas);
        drawSpouseLine(canvas);
        drawChildrenLine(canvas);
    }

    private void drawGenerationLine(Canvas canvas) {
        if (isListView(mGenerationView)) {
            int index = 0;
            int horizontalLineStartX, horizontalLineStopX, verticalLinesStartY, verticalLinesStopY;
            for (View view : mGenerationView) {
                horizontalLineStartX = (int) view.getX();
                verticalLinesStartY = (int) view.getY();

                horizontalLineStopX = horizontalLineStartX + mItemWidthPX;
                verticalLinesStopY = verticalLinesStartY + mItemHeightPX;

                mPath.reset();
                index++;
                if (index == 1) {
                    mPath.moveTo(horizontalLineStartX, verticalLinesStartY);
                    mPath.lineTo(horizontalLineStopX, verticalLinesStartY);
                } else if (mGenerationView.size() == index) {
                    mPath.moveTo(horizontalLineStartX, verticalLinesStopY);
                    mPath.lineTo(horizontalLineStopX, verticalLinesStopY);
                }

                mPath.moveTo(horizontalLineStartX, verticalLinesStartY);
                mPath.lineTo(horizontalLineStartX, verticalLinesStopY);

                mPath.moveTo(horizontalLineStopX, verticalLinesStartY);
                mPath.lineTo(horizontalLineStopX, verticalLinesStopY);

                canvas.drawPath(mPath, mPaint);
            }
        }


    }

    private void drawSpouseLine(Canvas canvas) {
        List<View> mSpouseView = mFamilyMember.getUser().getSpouse();
        if (isListView(mSpouseView)) {
            for (View view : mSpouseView) {
                final int horizontalLineStartX = (int) mMineView.getX() + mItemWidthPX / 2;
                final int horizontalLineStopX = (int) view.getX() + mItemWidthPX / 2;
                final int horizontalLineY = (int) view.getY() + mItemWidthPX / 2;
                mPath.reset();
                mPath.moveTo(horizontalLineStartX, horizontalLineY);
                mPath.lineTo(horizontalLineStopX, horizontalLineY);
                canvas.drawPath(mPath, mPaint);
            }
        }
    }

    boolean isListView(List<View> views) {
        return null != views && views.size() > 0;
    }

    private void drawChildrenLine(Canvas canvas) {
        List<SearchNearInBlood> mMyChildren = mFamilyMember.getUser().getChildrens();
        if (mMyChildren != null && mMyChildren.size() > 0) {
            final int myVerticalLineX = (int) mMineView.getX() + mItemWidthPX / 2;
            final int myVerticalLineStartY = (int) mMineView.getY() + mItemHeightPX;
            final int myVerticalLinesStopY = myVerticalLineStartY + mSpacePX;
            mPath.reset();
            mPath.moveTo(myVerticalLineX, myVerticalLineStartY);
            mPath.lineTo(myVerticalLineX, myVerticalLinesStopY);
            canvas.drawPath(mPath, mPaint);

            int index = 0;
            int childSpouseIndex = 0;
            List<View> mChildrenView = mFamilyMember.getUser().getChildren();
            final int childrenViewCount = mChildrenView.size();
            for (int i = 0; i < childrenViewCount; i++) {
                final View startChildView = mChildrenView.get(i);
                final int childLineY = (int) startChildView.getY() - mSpacePX;
                final int childVerticalLineEndY = (int) startChildView.getY() + mItemWidthPX / 2;
                final int childLineStartX = (int) startChildView.getX() + mItemWidthPX / 2;
                mPath.reset();
                mPath.moveTo(childLineStartX, childLineY);
                mPath.lineTo(childLineStartX, childVerticalLineEndY);
                canvas.drawPath(mPath, mPaint);

//                final List<SearchNearInBlood> childSpouse = mMyChildren.get(i).getSpouses();
//                if (childSpouse != null) {
//                    mFamilyMember.getUser()
//                    final View childSpouseView = mChildSpouseView.get(childSpouseIndex);
//                    final int spouseLineEndX = (int) childSpouseView.getX() + mItemWidthPX / 2;
//                    mPath.reset();
//                    mPath.moveTo(childLineStartX, childVerticalLineEndY);
//                    mPath.lineTo(spouseLineEndX, childVerticalLineEndY);
//                    canvas.drawPath(mPath, mPaint);
//                    childSpouseIndex++;
//                }

//                if (i < childrenViewCount - 1) {
//                    final View endChildView = mChildrenView.get(i + 1);
//                    final int horizontalLineStopX = (int) endChildView.getX() + mItemWidthPX / 2;
//                    mPath.reset();
//                    mPath.moveTo(childLineStartX, childLineY);
//                    mPath.lineTo(horizontalLineStopX, childLineY);
//                    canvas.drawPath(mPath, mPaint);
//                }

                final List<SearchNearInBlood> grandChildren = mMyChildren.get(i).getChildrens();
                if (grandChildren != null) {
//                    final int grandChildrenCount = grandChildren.size();
//                    for (int j = 0; j < grandChildrenCount; j++) {
//                        List<View> mGrandChildrenView = grandChildren.get(j).getSpouse();
//                        final View startView = mGrandChildrenView.get(j + index);
//                        final int grandchildLineX = (int) startView.getX() + mItemWidthPX / 2;
//                        final int grandchildLineStartY = (int) startView.getY() - mSpacePX;
//                        final int garndchildLineEndY = (int) startView.getY();
//                        mPath.reset();
//                        mPath.moveTo(grandchildLineX, grandchildLineStartY);
//                        mPath.lineTo(grandchildLineX, garndchildLineEndY);
//                        canvas.drawPath(mPath, mPaint);
//
//                        if (j < grandChildrenCount - 1) {
//                            final View endView = mGrandChildrenView.get(j + 1 + index);
//                            final int hLineStopX = (int) endView.getX() + mItemWidthPX / 2;
//                            mPath.reset();
//                            mPath.moveTo(grandchildLineX, grandchildLineStartY);
//                            mPath.lineTo(hLineStopX, grandchildLineStartY);
//                            canvas.drawPath(mPath, mPaint);
//                        }
//                    }

//                    if (grandChildrenCount > 0) {
//                        final View grandChildView = mGrandChildrenView.get(index);
//                        final int vLineX = (int) startChildView.getX() + mItemWidthPX / 2;
//                        final int vLineStopY = (int) startChildView.getY() + mItemHeightPX;
//                        final int hLineY = (int) grandChildView.getY() - mSpacePX;
//                        mPath.reset();
//                        mPath.moveTo(vLineX, hLineY);
//                        mPath.lineTo(vLineX, vLineStopY);
//                        canvas.drawPath(mPath, mPaint);
//
//                        index += grandChildrenCount;
//                    }
                }
            }
        }
    }

    public void setFamilyMember(SearchNearInBlood familyMember) {
        recycleAllView();
        initData(familyMember);
        initWidthAndHeight();
        initView();
        invalidate();
    }

    public void setOnFamilySelectListener(OnFamilySelectListener onFamilySelectListener) {
        this.mOnFamilySelectListener = onFamilySelectListener;
    }

    private OnClickListener click = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnFamilySelectListener != null) {
                mCurrentLeft = v.getLeft();
                mCurrentTop = v.getTop();
                mCurrentScrollX = getScrollX();
                mCurrentScrollY = getScrollY();
                mOnFamilySelectListener.onFamilySelect((SearchNearInBlood) v.getTag());
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                mCurrentX = getScrollX();
//                mCurrentY = getScrollY();
//                mLastTouchX = (int) event.getX();
//                mLastTouchY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final int currentTouchX = (int) event.getX();
                final int currentTouchY = (int) event.getY();

                final int distanceX = currentTouchX - mLastTouchX;
                final int distanceY = currentTouchY - mLastTouchY;

                mCurrentX -= distanceX;
                mCurrentY -= distanceY;

//                if (mCurrentX < getLeft()) {
//                    mCurrentX = getLeft();
//                } else if (mCurrentX > getRight() - mShowWidthPX) {
//                    mCurrentX = getRight() - mShowWidthPX;
//                }
//
//                if (mCurrentY < getTop()) {
//                    mCurrentY = getTop();
//                } else if (mCurrentY > getBottom() - mShowHeightPX) {
//                    mCurrentY = getBottom() - mShowHeightPX;
//                }

                this.scrollTo(mCurrentX, mCurrentY);
                mLastTouchX = currentTouchX;
                mLastTouchY = currentTouchY;
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercerpt = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastInterceptX = (int) event.getX();
                mLastInterceptY = (int) event.getY();
                mCurrentX = getScrollX();
                mCurrentY = getScrollY();
                mLastTouchX = (int) event.getX();
                mLastTouchY = (int) event.getY();
                intercerpt = false;
                break;
            case MotionEvent.ACTION_MOVE:
                final int distanceX = Math.abs((int) event.getX() - mLastInterceptX);
                final int distanceY = Math.abs((int) event.getY() - mLastInterceptY);
                if (distanceX < mScrollWidth && distanceY < mScrollWidth) {
                    intercerpt = false;
                } else {
                    intercerpt = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercerpt = false;
                break;
        }
        return intercerpt;
    }
}