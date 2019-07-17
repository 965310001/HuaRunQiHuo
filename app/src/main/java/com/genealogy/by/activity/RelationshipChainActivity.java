package com.genealogy.by.activity;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.R;
import com.genealogy.by.entity.SearchNearInBlood;
import com.genealogy.by.view.FamilyTreeView8;

import tech.com.commoncore.base.BaseTitleActivity;

public class RelationshipChainActivity extends BaseTitleActivity {

    private SearchNearInBlood data;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("查看关系链")
                .setOnLeftTextClickListener(view -> finish());
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_relationship_chain;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        data = (SearchNearInBlood) getIntent().getSerializableExtra("data");
        FamilyTreeView8 familyTreeView = findViewById(R.id.ftv_tree8);
        familyTreeView.setFamilyMember(data);


        familyTreeView.setOnTouchListener((v, event) -> {
            View view = (View) v;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: //手指按下
                    savedMatrix.set(matrix);
                    startPoint.set(event.getX(), event.getY());
                    mode = DRAG;
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event); //如果两点距离大于10 多点模式
                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        midPoint(middlePoint, event);
                        mode = ZOOM;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) { //拖动
                        matrix.set(savedMatrix);
                        matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y);
                    } else if (mode == ZOOM) { //缩放
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            matrix.set(savedMatrix);
                            float scale = newDist / oldDist;
                            matrix.postScale(scale, scale, middlePoint.x, middlePoint.y);
                        }
                        v.setScaleX(newDist);
                        v.setScaleY(newDist);
                    }
                    break;
            }
//            view.(matrix);
            return true;
        });
    }

    private static final int NONE = 0;     //初始状态
    private static final int DRAG = 1;     //拖动
    private static final int ZOOM = 2;     //缩放
    private int mode = NONE;               //当前事件
    private float oldDist;
    private PointF startPoint = new PointF();
    private PointF middlePoint = new PointF();
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    //两点距离
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    //两点中点
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

}
