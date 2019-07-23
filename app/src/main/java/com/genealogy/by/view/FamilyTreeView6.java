package com.genealogy.by.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
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
 * 家谱树自定义ViewGroup 全部
 */

public class FamilyTreeView6 extends ViewGroup {

    private static final int MAX_HEIGHT_DP = 590;//最大高度为590dp
    private static final int SPACE_WIDTH_DP = 20;//间距为20dp
    private static final int ITEM_WIDTH_DP = 40;//家庭成员View宽度50dp
    private static final int ITEM_HEIGHT_DP = 90;//家庭成员View高度80dp
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

    private final int LINEDEFAULTCOLOR = Color.parseColor("#FFD7605A");
    private ImageView ivAvatar;
    private TextView tvName;
    private View familyView;
    private String url;

    private List<View> childes;
    private List<SearchNearInBlood> chideList;

    private View mView;
    private int count;

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
        mPaint.setColor(LINEDEFAULTCOLOR);
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

//        if (mFamilyMember.getUser().getSpouse() != null) {
//            widthDP[2] = ITEM_WIDTH_DP + SPACE_WIDTH_DP + ITEM_WIDTH_DP * 2;
//        }

        chideList = mFamilyMember.getUser().getChildrens();
        initWidthAndHeight(widthDP, chideList);

        mMaxWidthPX = mScreenWidth;
        for (int width : widthDP) {
            final int widthPX = DisplayUtil.dip2px(width);
            if (widthPX > mMaxWidthPX) {
                mMaxWidthPX = widthPX;
            }
        }

        mMaxHeightPX = Math.max(DisplayUtil.dip2px(MAX_HEIGHT_DP), mScreenHeight);
    }

    private void initWidthAndHeight(int[] widthDP, List<SearchNearInBlood> mMyChildren) {
        if (mMyChildren != null && mMyChildren.size() > 0) {
            widthDP[3] += (SPACE_WIDTH_DP + ITEM_WIDTH_DP) * mMyChildren.size();
            widthDP[4] = 0;
            int grandchildMaxWidthDP;
            for (int i = 0; i < mMyChildren.size(); i++) {
                final SearchNearInBlood child = mMyChildren.get(i);
                final List<SearchNearInBlood> grandChildrenList = child.getChildrens();
                grandchildMaxWidthDP = 0;
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
            Log.i(TAG, "initView: GenerationBean");
            int index = 0;
            for (SearchNearInBlood.GenerationBean bean : beans) {
                mGenerationView.add(createGeneration(String.format("%d", (++index)), bean));
            }
        }
        View view;
        /*儿子*/
        chideList = mFamilyMember.getUser().getChildrens();
        if (null != chideList && chideList.size() > 0) {
            for (SearchNearInBlood children : chideList) {
                children.setMineView(view = createFamilyView(children));
                mFamilyMember.getUser().addChildren(view);
                /*儿媳*/
                createFamilyView(children, children.getSpouses(), false);
                /*孙子*/
                createFamilyView(children, children.getChildrens(), true);
            }
        }

        /*妻子*/
        chideList = mFamilyMember.getUser().getSpouses();
        if (null != chideList && chideList.size() > 0) {
            for (SearchNearInBlood spouse : chideList) {
                spouse.setMineView(view = createFamilyView(spouse));
                mFamilyMember.getUser().addSpouses(view);
            }
        }
    }

    private void createFamilyView(SearchNearInBlood familyMember, List<SearchNearInBlood> child, boolean isChild) {
        if (null != familyMember && null != child && child.size() > 0) {
//            View view;
            for (SearchNearInBlood bean : child) {
                bean.setMineView(mView = createFamilyView(bean));
                if (isChild) {/*儿子*/
                    familyMember.addChildren(mView);
                    createFamilyView(bean, bean.getSpouses(), false);
                } else {/*儿媳*/
                    familyMember.addSpouses(mView);
                }
                createFamilyView(bean, bean.getChildrens(), true);
            }
        }
    }

    private View createGeneration(String title, SearchNearInBlood.GenerationBean bean) {
        familyView = LayoutInflater.from(getContext()).inflate(R.layout.item_generation, this, false);
        familyView.getLayoutParams().width = mItemWidthPX + mSpacePX + mLineWidthPX;
        familyView.getLayoutParams().height = mItemHeightPX;

        final TextView tvTitle = familyView.findViewById(R.id.tv_title);
        tvTitle.setTextSize(CALL_TEXT_SIZE_SP);
        tvTitle.setText(title);

        final TextView tvNum = familyView.findViewById(R.id.tv_num);
        tvNum.setTextSize(CALL_TEXT_SIZE_SP);
        tvNum.setText(String.format("男%d女%d", bean.getMen(), bean.getWomen()));

        this.addView(familyView);
        return familyView;
    }


    private View createFamilyView(SearchNearInBlood family) {
        familyView = LayoutInflater.from(getContext()).inflate(R.layout.item_family2, this, false);
        familyView.getLayoutParams().width = mItemWidthPX;
        familyView.getLayoutParams().height = mItemHeightPX;
        familyView.setTag(family);

        ivAvatar = familyView.findViewById(R.id.iv_avatar);

        tvName = familyView.findViewById(R.id.tv_name);
        tvName.setTextSize(NAME_TEXT_SIZE_SP);
        tvName.setText(family.getNickName());

        url = family.getProfilePhoto();
        if (!TextUtils.isEmpty(url)) {
            Glide.with(getContext())
                    .load(url)
                    .apply(new RequestOptions().error(R.mipmap.family_avatar))
                    .into(ivAvatar);
        } else {
            Glide.with(getContext()).load(R.mipmap.family_avatar).into(ivAvatar);
        }

        // TODO: 2019/7/18 设置男女背景颜色
        switch (family.getSex()) {
            case 0:/*男*/
                tvName.getRootView().setBackgroundColor(LINEDEFAULTCOLOR);
                break;
            case 1:
                tvName.getRootView().setBackgroundColor(Color.GRAY);
                break;
        }

        familyView.setOnClickListener(click);
        this.addView(familyView);
        return familyView;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mShowWidthPX = MeasureSpec.getSize(widthMeasureSpec);
        mShowHeightPX = MeasureSpec.getSize(heightMeasureSpec);

        count = getChildCount();
        for (int i = 0; i < count; i++) {
            mView = getChildAt(i);
            mView.measure(mWidthMeasureSpec, mHeightMeasureSpec);
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
            int mineTop = mItemHeightPX - mSpacePX;
            int mineLeft = (left + right - mItemWidthPX) / 2 - mShowWidthPX / 2 + mItemWidthPX - mSpacePX / 2;
            if (null != mGenerationView && mGenerationView.size() > 0) {
                int generationTop = mineTop;
                for (View view : mGenerationView) {
                    setChildViewFrame(view, mineLeft, generationTop, mItemWidthPX + mSpacePX + mLineWidthPX, mItemHeightPX);
                    generationTop += mItemHeightPX + mSpacePX * 2;
                }
            }
            mineLeft += mItemWidthPX + 2 * mSpacePX + mLineWidthPX;
            chideList = new ArrayList<>();
            chideList.add(mFamilyMember.getUser());
            childrenLayout(mineLeft, mineTop, chideList);
        }
    }


    void childrenLayout(int mineLeft, int mineTop, List<SearchNearInBlood> familyMember) {
        if (null != familyMember && familyMember.size() > 0) {
            /*排序*/
            chideList = new ArrayList<>();//儿子
            int chileLeft = mineLeft;
            for (SearchNearInBlood searchNearInBlood : familyMember) {
                setChildViewFrame(searchNearInBlood.getMineView(), chileLeft, mineTop, mItemWidthPX, mItemHeightPX);/*我*/
                /*妻子*/
                childes = searchNearInBlood.getSpouse();
                if (null != childes && childes.size() > 0) {
                    for (View view : childes) {
                        setChildViewFrame(view, chileLeft += (mItemWidthPX + mSpacePX), mineTop, mItemWidthPX, mItemHeightPX);
                    }
                }
                chileLeft += (mItemWidthPX + mSpacePX);
                for (SearchNearInBlood children : searchNearInBlood.getChildrens()) {
                    chideList.add(children);
                }
            }
            if (null != chideList && chideList.size() > 0) {
                childrenLayout(mineLeft, mineTop + mItemHeightPX + mSpacePX * 2, chideList);
            }
        }
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
            mView = mGenerationView.get(0);
            Paint mPaint = new Paint();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.reset();
            mPaint.setColor(LINEDEFAULTCOLOR);
            mPaint.setStrokeWidth(mLineWidthPX);
            mPaint.setStyle(Paint.Style.STROKE);

            PathEffect effects = new DashPathEffect(new float[]{mSpacePX, 6}, 1);
            mPaint.setPathEffect(effects);

            Rect rect = new Rect((int) mView.getX(), mView.getTop(),
                    (int) mView.getX() + DisplayUtil.dip2px(mSpacePX) + DisplayUtil.dip2px(mLineWidthPX),
                    (int) (mGenerationView.get(mGenerationView.size() - 1).getY() + mItemHeightPX));
            canvas.drawRect(rect, mPaint);
        }
    }

    private void drawSpouseLine(Canvas canvas) {
        childes = mFamilyMember.getUser().getSpouse();
        if (isListView(childes)) {
            int horizontalLineStartX, horizontalLineStopX, horizontalLineY;
            for (View view : childes) {
                horizontalLineStartX = (int) mMineView.getX() + mItemWidthPX;
                horizontalLineStopX = (int) view.getX();
                horizontalLineY = (int) view.getY() + mItemHeightPX / 2;
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

//    void drawSpouseLine(Canvas canvas, SearchNearInBlood searchNearInBlood) {
//        List<View> spouse = searchNearInBlood.getSpouse();
//        if (null != searchNearInBlood && isListView(spouse)) {
//            int size = spouse.size();
//            if (size >= 1) {/*一个配偶以上*/
//                mView = searchNearInBlood.getMineView();
//                int childLineStartX = (int) (mView.getX() + mItemWidthPX);
//                int childLineEndX = childLineStartX + mSpacePX;
//                int childLineY = (int) (mView.getY() + mItemHeightPX / 2);
//                mPath.reset();
//                mPath.moveTo(childLineStartX, childLineY);
//                mPath.lineTo(childLineEndX, childLineY);
//                canvas.drawPath(mPath, mPaint);
//
//                if (size >= 2) {/*两个配偶以上*/
//                    for (int i = 0; i < spouse.size(); i++) {
//                        mView = spouse.get(i);
//                        childLineStartX = (int) (mView.getX() + mItemWidthPX);
//                        childLineEndX = childLineStartX + mSpacePX;
//                        childLineY = (int) (mView.getY() + mItemHeightPX / 2);
//                        mPath.reset();
//                        mPath.moveTo(childLineStartX, childLineY);
//                        mPath.lineTo(childLineEndX, childLineY);
//                        canvas.drawPath(mPath, mPaint);
//                        if (i + 1 == size - 1) {
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//    }

    void drawChildrenLine(Canvas canvas, SearchNearInBlood searchNearInBloods) {
        if (null != searchNearInBloods) {
            int index = 0;
            List<SearchNearInBlood> childrens = searchNearInBloods.getChildrens();
            View mView;
            for (SearchNearInBlood searchNearInBlood : childrens) {
                mView = searchNearInBlood.getMineView();
                int childLineY = (int) mView.getY() - mSpacePX;
                final int childVerticalLineEndY = (int) mView.getY() + mItemWidthPX / 2;
                int childLineStartX = (int) mView.getX() + mItemWidthPX / 2;

                mPath.reset();
                mPath.moveTo(childLineStartX, childLineY);
                mPath.lineTo(childLineStartX, childVerticalLineEndY);
                canvas.drawPath(mPath, mPaint);

                drawChildrenLine(canvas, childrens.get(index));

                /*画配偶*/
                List<View> spouse = searchNearInBlood.getSpouse();
                if (null != spouse && spouse.size() > 0) {
                    int size = spouse.size();
                    childLineStartX = (int) (mView.getX() + mItemWidthPX);
                    int childLineEndX = childLineStartX + mSpacePX;
                    childLineY = (int) (mView.getY() + mItemHeightPX / 2);
                    mPath.reset();
                    mPath.moveTo(childLineStartX, childLineY);
                    mPath.lineTo(childLineEndX, childLineY);
                    canvas.drawPath(mPath, mPaint);

                    if (size >= 2) {
                        for (int i = 0; i < size; i++) {
                            mView = spouse.get(i);
                            childLineStartX = (int) (mView.getX() + mItemWidthPX);
                            childLineEndX = childLineStartX + mSpacePX;
                            childLineY = (int) (mView.getY() + mItemHeightPX / 2);
                            mPath.reset();
                            mPath.moveTo(childLineStartX, childLineY);
                            mPath.lineTo(childLineEndX, childLineY);
                            canvas.drawPath(mPath, mPaint);
                            if (i + 1 == size - 1) {
                                break;
                            }
                        }
                    }
                }
                index++;
            }

            count = childrens.size();
            final View startView, endChildView;
            int horizontalLineStopX, horizontalLineStartX, horizontalLineY;

            int myVerticalLineX, myVerticalLineStartY, myVerticalLinesStopY;
            if (count >= 1) {
                startView = childrens.get(0).getMineView();
                endChildView = childrens.get(count - 1).getMineView();
                horizontalLineStopX = (int) endChildView.getX() + mItemWidthPX / 2;
                horizontalLineStartX = (int) startView.getX() + mItemWidthPX / 2;
                horizontalLineY = (int) endChildView.getY() - mSpacePX;

                mPath.reset();
                mPath.moveTo(horizontalLineStartX, horizontalLineY);
                mPath.lineTo(horizontalLineStopX, horizontalLineY);
                canvas.drawPath(mPath, mPaint);

                mView = searchNearInBloods.getMineView();
                myVerticalLineX = (int) mView.getX() + mItemWidthPX / 2;
                myVerticalLineStartY = (int) mView.getY() + mItemHeightPX;
                myVerticalLinesStopY = myVerticalLineStartY + mSpacePX;
                mPath.reset();
                mPath.moveTo(myVerticalLineX, myVerticalLineStartY);
                mPath.lineTo(myVerticalLineX, myVerticalLinesStopY);
                canvas.drawPath(mPath, mPaint);

                horizontalLineStartX = (int) endChildView.getX() + mItemWidthPX / 2;
                horizontalLineStopX = (int) mView.getX() + mItemWidthPX / 2;
                mPath.reset();
                mPath.moveTo(horizontalLineStartX, horizontalLineY);
                mPath.lineTo(horizontalLineStopX, horizontalLineY);
                canvas.drawPath(mPath, mPaint);
            }
        }
    }

    private void drawChildrenLine(Canvas canvas) {
        List<SearchNearInBlood> searchNearInBloodList = new ArrayList<>();
        searchNearInBloodList.add(mFamilyMember.getUser());

        int myVerticalLineX, myVerticalLineStartY, myVerticalLinesStopY;

        int childLineY, childVerticalLineEndY, childLineStartX;

        for (SearchNearInBlood searchNearInBlood : searchNearInBloodList) {
            myVerticalLineX = (int) searchNearInBlood.getMineView().getX() + mItemWidthPX / 2;
            myVerticalLineStartY = (int) searchNearInBlood.getMineView().getY() + mItemHeightPX;
            myVerticalLinesStopY = myVerticalLineStartY + mSpacePX;
            mPath.reset();
            mPath.moveTo(myVerticalLineX, myVerticalLineStartY);
            mPath.lineTo(myVerticalLineX, myVerticalLinesStopY);
            canvas.drawPath(mPath, mPaint);

            /*画子女*/
            int index = 0;
            count = 0;
            for (View view : searchNearInBlood.getChildren()) {
                childLineY = (int) view.getY() - mSpacePX;
                childVerticalLineEndY = (int) view.getY() + mItemWidthPX / 2;
                childLineStartX = (int) view.getX() + mItemWidthPX / 2;

                mPath.reset();
                mPath.moveTo(childLineStartX, childLineY);
                mPath.lineTo(childLineStartX, childVerticalLineEndY);
                canvas.drawPath(mPath, mPaint);
                drawChildrenLine(canvas, searchNearInBlood.getChildrens().get(index));

                index++;
            }
            /*画连接儿子的线*/
            List<View> children = searchNearInBlood.getChildren();
            if (isListView(children)) {
                if (children.size() > 1) {
                    mPath.reset();
                    mPath.moveTo(children.get(0).getX() + mItemWidthPX / 2, children.get(0).getY() - mSpacePX);
                    mPath.lineTo(children.get(children.size() - 1).getX() + mItemWidthPX / 2, children.get(0).getY() - mSpacePX);
                    canvas.drawPath(mPath, mPaint);
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