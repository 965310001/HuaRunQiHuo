package com.genealogy.by.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
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
import com.genealogy.by.MyApplication;
import com.genealogy.by.R;
import com.genealogy.by.entity.SearchNearInBlood;
import com.genealogy.by.interfaces.OnFamilySelectListener;
import com.genealogy.by.utils.DisplayUtil;

import java.util.List;

import tech.com.commoncore.utils.SPHelper;

/**
 * 家谱树自定义ViewGroup
 */

public class FamilyTreeView4 extends ViewGroup {
    private static final String TAG = "FamilyTreeView4";

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

    private List<SearchNearInBlood> mMyChildren;//子女

    private View mMineView;//我的View

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

    private int mCurrentLeft = 0;//当前选中View的Left距离
    private int mCurrentTop = 0;//当前选中View的Top距离
    private int mCurrentScrollX = 0;//当前滚动位置
    private int mCurrentScrollY = 0;//当前滚动位置

    private String mUid;/*我的id*/

    public FamilyTreeView4(Context context) {
        this(context, null, 0);
    }

    public FamilyTreeView4(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FamilyTreeView4(Context context, AttributeSet attrs, int defStyleAttr) {
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
//        mMineView = null;
//        mSpouseView = null;
//        mFatherView = null;
//        mMotherView = null;
//        mPaternalGrandFatherView = null;
//        mPaternalGrandMotherView = null;
//        mMaternalGrandFatherView = null;
//        mMaternalGrandMotherView = null;
//        mFosterFatherView = null;
//        mFosterMotherView = null;
//        mFPGrandFatherView = null;
//        mFPGrandMotherView = null;
//        mFMGrandFatherView = null;
//        mFMGrandMotherView = null;

//        if (mBrothersView != null) {
//            mBrothersView.clear();
//        } else {
//            mBrothersView = new ArrayList<>();
//        }
//        if (mSpouseView != null) {
//            mSpouseView.clear();
//        } else {
//            mSpouseView = new ArrayList<>();
//        }
//        if (mChildrenView != null) {
//            mChildrenView.clear();
//        } else {
//            mChildrenView = new ArrayList<>();
//        }
//        if (mChildSpouseView != null) {
//            mChildSpouseView.clear();
//        } else {
//            mChildSpouseView = new ArrayList<>();
//        }
//        if (mGrandChildrenView != null) {
//            mGrandChildrenView.clear();
//        } else {
//            mGrandChildrenView = new ArrayList<>();
//        }
//        mMySpouse = null;
//        mMyFather = null;
//        mMyMother = null;
//        if (mMyBrothers != null) {
//            mMyBrothers.clear();
//            mMyBrothers = null;
//        }
        if (mMyChildren != null) {
            mMyChildren.clear();
            mMyChildren = null;
        }
    }

    private void initData(SearchNearInBlood familyMember) {
        this.mFamilyMember = familyMember;
        mUid = SPHelper.getStringSF(getContext(), "UserId");
    }

    final int[] widthDP = {
            830,//第一代最大宽度
//                720,//第二代最大宽度
            830,
//                ITEM_WIDTH_DP,//第三代最大宽度
//                ITEM_WIDTH_DP,//第四代最大宽度
//                ITEM_WIDTH_DP//第五代最大宽度
            830,
            830,
            830
    };

    private void initWidthAndHeight() {
//        final int[] widthDP = {
//                1000,//第一代最大宽度
////                720,//第二代最大宽度
//                10000,
////                ITEM_WIDTH_DP,//第三代最大宽度
////                ITEM_WIDTH_DP,//第四代最大宽度
////                ITEM_WIDTH_DP//第五代最大宽度
//                1000,
//                1000,
//                1000
//        };

//        if (mFamilyMember.getSpouse() != null) {
//            widthDP[2] = ITEM_WIDTH_DP + SPACE_WIDTH_DP + ITEM_WIDTH_DP * 2;
//        }
//
//        if (null != mFamilyMember.getChildrens() && mFamilyMember.getChildrens().size() > 0) {
//            widthDP[2] = ITEM_WIDTH_DP + (SPACE_WIDTH_DP + ITEM_WIDTH_DP) * mFamilyMember.getChildrens().size() * 2;
//        }

        initWidthAndHeight(mFamilyMember.getChildrens());

//        mMaxWidthPX = mScreenWidth;
//        for (int width : widthDP) {
//            final int widthPX = DisplayUtil.dip2px(width);
//            if (widthPX > mMaxWidthPX) {
//                mMaxWidthPX = widthPX;
//            }
//        }
//
//        mMaxHeightPX = Math.max(DisplayUtil.dip2px(MAX_HEIGHT_DP), mScreenHeight);
    }

    private void initWidthAndHeight(List<SearchNearInBlood> mMyChildren) {
        if (mMyChildren != null) {
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
                    } else if (grandchildCount == 2 && child.getSpouses() != null) {
                        grandchildMaxWidthDP = (ITEM_WIDTH_DP + SPACE_WIDTH_DP) * 5 / 2;
                    } else {
                        grandchildMaxWidthDP = (ITEM_WIDTH_DP + SPACE_WIDTH_DP) * grandchildCount;
                    }
                } else {
                    if (mMyChildren.size() > 1) {
                        if (child.getSpouses() != null) {
                            grandchildMaxWidthDP = (ITEM_WIDTH_DP + SPACE_WIDTH_DP) * 2;
                        } else {
                            grandchildMaxWidthDP = ITEM_WIDTH_DP + SPACE_WIDTH_DP;
                        }
                    } else {
                        grandchildMaxWidthDP = ITEM_WIDTH_DP + SPACE_WIDTH_DP;
                    }
                }

                widthDP[4] += grandchildMaxWidthDP;
            }
            widthDP[4] -= SPACE_WIDTH_DP;
            mGrandChildrenMaxWidth = DisplayUtil.dip2px(widthDP[4]);

            Log.i(TAG, "initWidthAndHeight: " + mGrandChildrenMaxWidth);
        }

        mMaxWidthPX = mScreenWidth;
        for (int width : widthDP) {
            final int widthPX = DisplayUtil.dip2px(width);
            if (widthPX > mMaxWidthPX) {
                mMaxWidthPX = widthPX;
            }
        }

        mMaxHeightPX = Math.max(DisplayUtil.dip2px(MAX_HEIGHT_DP), mScreenHeight);
    }


    /*子*/
    void createFamilyView(SearchNearInBlood mFamilyMember, List<SearchNearInBlood> childes, boolean isChild) {
        if (null != mFamilyMember) {
            if (null != childes && childes.size() > 0) {
                View view;
                for (SearchNearInBlood children : childes) {
                    children.setMineView(view = createFamilyView(children));
                    if (isChild) {
                        mFamilyMember.addChildren(view);
                        createFamilyView(children, children.getSpouses(), false);
                    } else {/*是女生*/
                        mFamilyMember.addSpouses(view);
                    }
                    createFamilyView(children, children.getChildrens(), true);
                }
            }
        }
    }

//    void createFamilyView(SearchNearInBlood mFamilyMember, List<SearchNearInBlood> childes, boolean isChild) {
//        if (null != mFamilyMember) {
//            if (null != childes && childes.size() > 0) {
//                View view;
//                for (SearchNearInBlood children : childes) {
//                    children.setMineView(view = createFamilyView(children));
//                    if (isChild) {
//                        mFamilyMember.addChildren(view);
//                        createFamilyView(children, children.getSpouses(), false);
//                    } else {/*是女生*/
//                        mFamilyMember.addSpouses(view);
//                    }
//                    createFamilyView(children, children.getChildrens(), true);
//                }
//            }
//        }
//    }

    private void initView() {
        View view = createFamilyView(mFamilyMember);
        mFamilyMember.setMineView(view);

        mMineView = view;

        List<SearchNearInBlood> childes = mFamilyMember.getChildrens();
        if (null != childes && childes.size() > 0) {
            for (SearchNearInBlood children : childes) {
                children.setMineView(view = createFamilyView(children));
                mFamilyMember.addChildren(view);
                createFamilyView(children, children.getSpouses(), false);
                createFamilyView(children, children.getChildrens(), true);
            }
        }
        List<SearchNearInBlood> spouses = mFamilyMember.getSpouses();
        if (null != spouses && spouses.size() > 0) {
            for (SearchNearInBlood spouse : spouses) {
                spouse.setMineView(view = createFamilyView(spouse));
                mFamilyMember.addSpouses(view);
                createFamilyView(spouse, spouse.getChildrens(), true);
            }
        }
//        Log.i(TAG, "initView: " + mFamilyMember);
        /************************************** end *************************************/
    }

    private View createFamilyView(SearchNearInBlood family) {
//        Log.i(TAG, family.getNickName() + " " + family.getRelationship());

        final View familyView = LayoutInflater.from(getContext()).inflate(R.layout.item_family, this, false);
        familyView.getLayoutParams().width = mItemWidthPX;
        familyView.getLayoutParams().height = mItemHeightPX;
        familyView.setTag(family);

        final ImageView ivAvatar = familyView.findViewById(R.id.iv_avatar);
        ivAvatar.getLayoutParams().height = mItemWidthPX;

        final TextView tvCall = familyView.findViewById(R.id.tv_call);
//        tvCall.getLayoutParams().height = (mItemHeightPX - mItemWidthPX) / 2;
        tvCall.setTextSize(CALL_TEXT_SIZE_SP);
        tvCall.setText("(" + family.getRelationship() + ")");

        final TextView tvName = familyView.findViewById(R.id.tv_name);
//        tvName.getLayoutParams().height = (mItemHeightPX - mItemWidthPX) / 2;
        tvName.setTextSize(NAME_TEXT_SIZE_SP);

        if (family.getNickName() != null) {
            tvName.setText(family.getNickName());
        } else {
            tvName.setText("");
        }

        // TODO: 2019/7/9 设置头像
//        final String url = family.getMemberImg();
//        String url;
//        switch (family.getSex()) {
//            case 1:
//                url = "http://img01.store.sogou.com/app/a/10010016/fbd033945c886f4564cf066d9d8be67b";
//                break;
//            default:
//                url = "http://img4.imgtn.bdimg.com/it/u=1709400343,2447668899&fm=23&gp=0.jpg";
//                break;
//        }

        if (1 == family.getSex()) {
            tvName.setBackgroundResource(R.color.C_D3606B);
        } else {
            tvName.setBackgroundResource(R.color.user);
        }

        if (family.getId() == Integer.parseInt(mUid)) {
            tvName.setBackgroundResource(R.color.bg_color_deep);
        }

        Glide.with(MyApplication.getInstance())
                .load(mFamilyMember.getProfilePhoto())
                .apply(new RequestOptions().error(1 == family.getSex() ? R.mipmap.muqin : R.mipmap.fuqin))
                .into(ivAvatar);

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

    // TODO: 2019/7/10 子view摆放
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (mCurrentScrollX == 0 && mCurrentScrollY == 0) {
            scrollTo((left + right - mShowWidthPX) / 2, (top + bottom - mShowHeightPX) / 2);
        } else {
            scrollTo(mCurrentScrollX, mCurrentScrollY);
        }
        if (mMineView != null) {
            final int mineLeft;
            final int mineTop;
            if (mCurrentLeft == 0 && mCurrentTop == 0) {
                mineLeft = (left + right - mItemWidthPX) / 2;/*居中*/
                mineTop = (top + bottom - mItemHeightPX) / 2;/*居中*/
            } else {
                mineLeft = mCurrentLeft;
                mineTop = mCurrentTop;
            }

            setChildViewFrame(mMineView, mineLeft, mineTop, mItemWidthPX, mItemHeightPX);

            List<View> mSpouse = mFamilyMember.getSpouse();
            if (mSpouse != null) {
                // TODO: 2019/7/9 我配偶循环
                if (mSpouse != null && mSpouse.size() > 0) {
                    final int brotherCount = mSpouse.size();
                    for (int i = 0; i < brotherCount; i++) {
                        setChildViewFrame(mSpouse.get(i),
                                mineLeft + (i + 1) * (mItemWidthPX + mSpacePX),
                                mineTop,
                                mItemWidthPX, mItemHeightPX);
                    }
                }
            }
            /*我的儿子*/
            childrenLayout(mineLeft, mineTop, mFamilyMember.getChildrens(), mFamilyMember.getChildren());
        }
    }

    // TODO: 2019/7/10 子view 的摆放位置
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
//        super.onDraw(canvas);
//        drawSpouseLine(canvas);
////        drawParentLine(canvas);
////        drawBrothersLine(canvas);
//        drawChildrenLine(canvas);


        drawLine(canvas);
    }

    void drawLine(Canvas canvas) {
        drawSpouseLine(canvas, mMineView, mFamilyMember.getSpouse());/*画配偶*/
        drawChildrenLine(canvas, mMineView, mFamilyMember, mFamilyMember.getChildren());/*画子女*/
    }

    // TODO: 2019/7/10 这是我的配偶
    private void drawSpouseLine(Canvas canvas, View mineView, List<View> views) {
        if (views != null) {
            for (View view : views) {
                final int horizontalLineStartX = (int) mineView.getX() + mItemWidthPX / 2;
                final int horizontalLineStopX = (int) view.getX() + mItemWidthPX / 2;
                final int horizontalLineY = (int) view.getY() + mItemWidthPX / 2;
                mPath.reset();
                mPath.moveTo(horizontalLineStartX, horizontalLineY);
                mPath.lineTo(horizontalLineStopX, horizontalLineY);
                canvas.drawPath(mPath, mPaint);
            }
        }
    }

    // TODO: 2019/7/10 这是子女
    private void drawChildrenLine(Canvas canvas, View mMineView, SearchNearInBlood mFamilyMember,
                                  List<View> mMyChildren) {
        if (mMyChildren != null && mMyChildren.size() > 0) {
            final int myVerticalLineX = (int) mMineView.getX() + mItemWidthPX / 2;
            final int myVerticalLineStartY = (int) mMineView.getY() + mItemHeightPX;
            final int myVerticalLinesStopY = myVerticalLineStartY + mSpacePX;
            mPath.reset();
            mPath.moveTo(myVerticalLineX, myVerticalLineStartY);
            mPath.lineTo(myVerticalLineX, myVerticalLinesStopY);
            canvas.drawPath(mPath, mPaint);

            List<View> mChildrenView = mFamilyMember.getChildren();
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

                final List<SearchNearInBlood> childSpouse = mFamilyMember.getChildrens().get(i).getSpouses();


                if (childSpouse != null) {
                    drawSpouseLine(canvas, startChildView,
                            mFamilyMember.getChildrens().get(i).getSpouse());

//                    int grandSpouseCount = childSpouse.size();
//                    for (int j = 0; j < grandSpouseCount; j++) {
//                        //子女配偶View
//                        final View childSpouseView = mChildSpouseView.get(j);
//                        final int spouseLineEndX = (int) childSpouseView.getX() + mItemWidthPX / 2;
//                        mPath.reset();
//                        mPath.moveTo(childLineStartX, childVerticalLineEndY);
//                        mPath.lineTo(spouseLineEndX, childVerticalLineEndY);
//                        canvas.drawPath(mPath, mPaint);
//                    }

                    if (i < childrenViewCount - 1) {
                        final View endChildView = mChildrenView.get(i + 1);
                        final int horizontalLineStopX = (int) endChildView.getX() + mItemWidthPX / 2;
                        mPath.reset();
                        mPath.moveTo(childLineStartX, childLineY);
                        mPath.lineTo(horizontalLineStopX, childLineY);
                        canvas.drawPath(mPath, mPaint);
                    }

//                    final List<SearchNearInBlood> grandChildren = mFamilyMember.getChildrens().get(i).getChildrens();
//                    if (grandChildren != null) {
//                        final int grandChildrenCount = grandChildren.size();
////                        for (int j = 0; j < grandChildrenCount; j++) {
////                            final View startView = mGrandChildrenView.get(j + index);
////                            final int grandchildLineX = (int) startView.getX() + mItemWidthPX / 2;
////                            final int grandchildLineStartY = (int) startView.getY() - mSpacePX;
////                            final int garndchildLineEndY = (int) startView.getY();
////                            mPath.reset();
////                            mPath.moveTo(grandchildLineX, grandchildLineStartY);
////                            mPath.lineTo(grandchildLineX, garndchildLineEndY);
////                            canvas.drawPath(mPath, mPaint);
////
////                            if (j < grandChildrenCount - 1) {
////                                final View endView = mGrandChildrenView.get(j + 1 + index);
////                                final int hLineStopX = (int) endView.getX() + mItemWidthPX / 2;
////                                mPath.reset();
////                                mPath.moveTo(grandchildLineX, grandchildLineStartY);
////                                mPath.lineTo(hLineStopX, grandchildLineStartY);
////                                canvas.drawPath(mPath, mPaint);
////                            }
////                        }
////                        if (grandChildrenCount > 0) {
////                            final View grandChildView = mGrandChildrenView.get(index);
////                            final int vLineX = (int) startChildView.getX() + mItemWidthPX / 2;
////                            final int vLineStopY = (int) startChildView.getY() + mItemHeightPX;
////                            final int hLineY = (int) grandChildView.getY() - mSpacePX;
////                            mPath.reset();
////                            mPath.moveTo(vLineX, hLineY);
////                            mPath.lineTo(vLineX, vLineStopY);
////                            canvas.drawPath(mPath, mPaint);
////
////                            index += grandChildrenCount;
////                        }
//                    }
                }

                drawChildrenLine(canvas, startChildView, mFamilyMember.getChildrens().get(i), mFamilyMember.getChildrens().get(i).getChildren());

            }
        }
    }

//    private void drawParentLine(Canvas canvas) {
//        final int mineX = (int) mMineView.getX();
//        final int mineY = (int) mMineView.getY();
//
//        int fosterParentCenterX = mineX + mItemWidthPX / 2;
//        int parentCenterX = mineX + mItemWidthPX / 2;
//
//        final int horizontalLineY = mineY - mSpacePX;
//
////        if (haveEitherFosterParent() || haveEitherParent()) {
////            final int verticalLineX = mineX + mItemWidthPX / 2;
////            mPath.reset();
////            mPath.moveTo(verticalLineX, horizontalLineY);
////            mPath.lineTo(verticalLineX, mineY);
////            canvas.drawPath(mPath, mPaint);
////        }
////        if (haveEitherFosterParent() && haveEitherParent()) {
////            fosterParentCenterX = mineX - (mItemWidthPX + mSpacePX) * 2 + mItemWidthPX / 2;
////            parentCenterX = mineX + (mItemWidthPX + mSpacePX) * 2 + mItemWidthPX / 2;
////
////            mPath.reset();
////            mPath.moveTo(fosterParentCenterX, horizontalLineY);
////            mPath.lineTo(parentCenterX, horizontalLineY);
////            canvas.drawPath(mPath, mPaint);
////        }
////        if (haveEitherFosterParent()) {
////            int verticalLineEndY = horizontalLineY;
////            if (mFosterFatherView != null) {
////                verticalLineEndY = (int) mFosterFatherView.getY() + mItemWidthPX / 2;
////            } else if (mFosterMotherView != null) {
////                verticalLineEndY = (int) mFosterMotherView.getY() + mItemWidthPX / 2;
////            }
////            mPath.reset();
////            mPath.moveTo(fosterParentCenterX, horizontalLineY);
////            mPath.lineTo(fosterParentCenterX, verticalLineEndY);
////            canvas.drawPath(mPath, mPaint);
////        }
////        if (haveBothFosterParent()) {
////            final int lineStartX = (int) mFosterFatherView.getX() + mItemWidthPX / 2;
////            final int lineEndX = (int) mFosterMotherView.getX() + mItemWidthPX / 2;
////            final int lineY = (int) mFosterFatherView.getY() + mItemWidthPX / 2;
////            mPath.reset();
////            mPath.moveTo(lineStartX, lineY);
////            mPath.lineTo(lineEndX, lineY);
////            canvas.drawPath(mPath, mPaint);
////        }
////        if (haveEitherParent()) {
////            int verticalLineEndY = horizontalLineY;
////            if (mFatherView != null) {
////                verticalLineEndY = (int) mFatherView.getY() + mItemWidthPX / 2;
////            } else if (mMotherView != null) {
////                verticalLineEndY = (int) mMotherView.getY() + mItemWidthPX / 2;
////            }
////            mPath.reset();
////            mPath.moveTo(parentCenterX, horizontalLineY);
////            mPath.lineTo(parentCenterX, verticalLineEndY);
////            canvas.drawPath(mPath, mPaint);
////        }
////        if (haveBothParent()) {
////            final int lineStartX = (int) mFatherView.getX() + mItemWidthPX / 2;
////            final int lineEndX = (int) mMotherView.getX() + mItemWidthPX / 2;
////            final int lineY = (int) mFatherView.getY() + mItemWidthPX / 2;
////            mPath.reset();
////            mPath.moveTo(lineStartX, lineY);
////            mPath.lineTo(lineEndX, lineY);
////            canvas.drawPath(mPath, mPaint);
////        }
////        if (mFPGrandFatherView != null || mFPGrandMotherView != null) {
////            drawGrandParentLine(canvas, mFosterFatherView, mFPGrandFatherView, mFPGrandMotherView);
////        }
////        if (mFMGrandFatherView != null || mFMGrandMotherView != null) {
////            drawGrandParentLine(canvas, mFosterMotherView, mFMGrandFatherView, mFMGrandMotherView);
////        }
////        if (mPaternalGrandFatherView != null || mPaternalGrandMotherView != null) {
////            drawGrandParentLine(canvas, mFatherView, mPaternalGrandFatherView, mPaternalGrandMotherView);
////        }
////        if (mMaternalGrandFatherView != null || mMaternalGrandMotherView != null) {
////            drawGrandParentLine(canvas, mMotherView, mMaternalGrandFatherView, mMaternalGrandMotherView);
////        }
//    }
//    private void drawGrandParentLine(Canvas canvas, View parentView, View grandFatherView, View grandMotherView) {
//        final int verticalLineX = (int) parentView.getX() + mItemWidthPX / 2;
//        final int verticalLineStartY = (int) parentView.getY() + mItemWidthPX / 2;
//        int verticalLineEndY = verticalLineStartY;
//        if (grandFatherView != null) {
//            verticalLineEndY = (int) grandFatherView.getY() + mItemWidthPX / 2;
//        } else if (grandMotherView != null) {
//            verticalLineEndY = (int) grandMotherView.getY() + mItemWidthPX / 2;
//        }
//
//        mPath.reset();
//        mPath.moveTo(verticalLineX, verticalLineStartY);
//        mPath.lineTo(verticalLineX, verticalLineEndY);
//        canvas.drawPath(mPath, mPaint);
//
//        if (grandFatherView != null && grandMotherView != null) {
//            final int horizontalLineStartX = (int) grandFatherView.getX() + mItemWidthPX / 2;
//            final int horizontalLineEndX = (int) grandMotherView.getX() + mItemWidthPX / 2;
//            mPath.reset();
//            mPath.moveTo(horizontalLineStartX, verticalLineEndY);
//            mPath.lineTo(horizontalLineEndX, verticalLineEndY);
//            canvas.drawPath(mPath, mPaint);
//        }
//    }

//    private void drawBrothersLine(Canvas canvas) {
////        if (mBrothersView != null && mBrothersView.size() > 0) {
////            final int brotherCount = mBrothersView.size();
////            final View brotherView = mBrothersView.get(brotherCount - 1);
////
////            final int horizontalLineStartX = (int) brotherView.getX() + mItemWidthPX / 2;
////            final int horizontalLineEndX = (int) mMineView.getX() + mItemWidthPX / 2;
////            final int horizontalLineY = (int) brotherView.getY() + mItemWidthPX / 2;
////            mPath.reset();
////            mPath.moveTo(horizontalLineStartX, horizontalLineY);
////            mPath.lineTo(horizontalLineEndX, horizontalLineY);
////            canvas.drawPath(mPath, mPaint);
////        }
//    }

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

//    //是否有父母其中一个
//    private boolean haveEitherParent() {
//        return mFatherView != null || mMotherView != null;
//    }
//
//    //是否父母都存在
//    private boolean haveBothParent() {
//        return mFatherView != null && mMotherView != null;
//    }
//
//    //是否有养父母其中一个
//    private boolean haveEitherFosterParent() {
//        return mFosterFatherView != null || mFosterMotherView != null;
//    }
//
//    //是否养父母都存在
//    private boolean haveBothFosterParent() {
//        return mFosterFatherView != null && mFosterMotherView != null;
//    }

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
        boolean intercept = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastInterceptX = (int) event.getX();
                mLastInterceptY = (int) event.getY();
                mCurrentX = getScrollX();
                mCurrentY = getScrollY();
                mLastTouchX = (int) event.getX();
                mLastTouchY = (int) event.getY();
                intercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                final int distanceX = Math.abs((int) event.getX() - mLastInterceptX);
                final int distanceY = Math.abs((int) event.getY() - mLastInterceptY);
                if (distanceX < mScrollWidth && distanceY < mScrollWidth) {
                    intercept = false;
                } else {
                    intercept = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        return intercept;
    }
}