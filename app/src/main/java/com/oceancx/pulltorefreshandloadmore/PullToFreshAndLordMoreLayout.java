package com.oceancx.pulltorefreshandloadmore;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;


/**
 * 实现RecyclerView的下拉刷新上拉加载更多
 * Created by oceancx on 15/11/12.
 */
public class PullToFreshAndLordMoreLayout extends FrameLayout {

    private int mTouchSlop;
    private ViewDragHelper mViewDragHelper;
    private RecyclerView mRecyclerView;
    private View header;
    private View footer;
    private View body;
    private RefreshAndLoadMoreListener mRLListener;

    public void setRefreshAndLoadMoreListener(RefreshAndLoadMoreListener mRLListener) {
        this.mRLListener = mRLListener;
    }

    public interface RefreshAndLoadMoreListener {
        public void onLoadMore();

        public void onRefresh();

        public void onLoadMoreComplete();

        public void onRefreshComplete();
    }


    public PullToFreshAndLordMoreLayout(Context context) {
        this(context, null);
    }

    public PullToFreshAndLordMoreLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToFreshAndLordMoreLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mViewDragHelper = ViewDragHelper.create(this, new DragHelperCallback());

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //    initData();
        header = findViewById(R.id.header);
        body = findViewById(R.id.body);
        footer = findViewById(R.id.footer);
        mRecyclerView = (RecyclerView) body.findViewById(R.id.ryc_views);

        DebugLog.e("header:" + header + " body:" + body + " footer:" + footer);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightSize = header.getMeasuredHeight() + footer.getMeasuredHeight() + getMeasuredHeight();
        int heightMode = MeasureSpec.EXACTLY;
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(heightSize, heightMode));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //onlyChild.layout(left, top, right, bottom);
        super.onLayout(changed, left, top, right, bottom);
        offsetTopAndBottom(-header.getMeasuredHeight());
    }


    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == getChildAt(0);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            DebugLog.e("top:" + top);
            if (top < -header.getMeasuredHeight()) {
                top = -header.getMeasuredHeight();
            } else if (top > footer.getMeasuredHeight()) {
                top = footer.getMeasuredHeight();
            }
            return top;
        }
    }

    /**
     * hack 黑科技
     * 如果RecyclerView滑动到顶部 , 那么再下拉,就可以进行下拉刷新
     * 如果RecyclerView滑动到底部 , 那么再上拉,就可以加载更多
     *
     * @param ev
     * @return
     */
    float initDownY, initDownX, lastDownX, lastDownY;
    boolean shouldIntercept = false;

    /**
     * 刚开始的时候 Intercept == false
     * RecyclerView可以进行滑动
     * 当intercept == true的时候 就中的ReyclerView的滚动
     *
     * @param ev
     * @return
     */
    boolean mRvReachTopWhenDown = false;
    boolean mRvReachBottomWhenDown = false;

    /**
     * 判断RcyclerView是否在顶点
     *
     * @return
     */
    private boolean isRvReachTop() {
        int checkPos = 0;

        View mFirstChild = mRecyclerView.getChildAt(0);
        int rvPos = mRecyclerView.getChildLayoutPosition(mFirstChild);
        // 当且仅当第一个可视节点==0 切其top==0的时候 此时rv到达顶部
        DebugLog.e("child top : " + mFirstChild.getTop());
        if (rvPos == checkPos && mFirstChild.getTop() == 0) {
            return true;
        }
        return false;
    }

    private boolean isRvReachBottom() {
        int checkPos = mRecyclerView.getAdapter().getItemCount() - 1;

        View mLastChild = mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1);
        int rvPos = mRecyclerView.getChildAdapterPosition(mLastChild);
        // 当且仅当第一个可视节点==0 切其top==0的时候 此时rv到达顶部
        DebugLog.e("rvPos:" + rvPos + " child bottom: " + mLastChild.getBottom() + "rv B: " + mRecyclerView.getBottom() + " meight:" + getMeasuredHeight() + " body heifghtg:" + body.getMeasuredHeight());
        if (rvPos == checkPos && mLastChild.getBottom() - mLastChild.getTop() == mLastChild.getMeasuredHeight()) {
            DebugLog.e("reach bootom");
            return true;
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        shouldIntercept = false;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                initDownY = ev.getY();
                initDownX = ev.getX();
                lastDownX = initDownX;
                lastDownY = initDownY;
                mViewDragHelper.processTouchEvent(ev);
                mRvReachTopWhenDown = isRvReachTop();
                mRvReachBottomWhenDown = isRvReachBottom();
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                float my = ev.getY();
                float mx = ev.getX();
                float dx = mx - initDownX;
                float dy = my - initDownY;
                /**
                 * 判断是否为下移,如果是下移,切在down的时候RvReachTop,那么就要开始Intercept
                 * 下移的条件是  dy > touchSlop && checkDegree >45
                 */
                DebugLog.e("check Degree :" + checkDegree(dx, dy) + " dy : " + dy + " mRcouTop:" + mRvReachTopWhenDown);
                if (!shouldIntercept && mRvReachTopWhenDown && dy > mTouchSlop && (checkDegree(dx, dy) > 45 || checkDegree(dx, dy) < -45)) {
                    DebugLog.e("reach touch true" + "  dd : " + checkDegree(dx, dy));
                    shouldIntercept = true;
                    mViewDragHelper.processTouchEvent(ev);
                } else if (!shouldIntercept && mRvReachBottomWhenDown && -dy > mTouchSlop && (checkDegree(dx, dy) > 45 || checkDegree(dx, dy) < -45)) {
                    shouldIntercept = true;
                    mViewDragHelper.processTouchEvent(ev);
                }
            }
            break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                shouldIntercept = false;
            }
            break;
        }
        return shouldIntercept && mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    private int checkDegree(float dx, float dy) {
        return (int) (Math.atan(dy / dx) * 180 / Math.PI);
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (mRvReachTopWhenDown) {
                    if (mRLListener != null) {
                        mRLListener.onRefresh();
                    }
                    if (mViewDragHelper.smoothSlideViewTo(getChildAt(0), 0, 0)) {
                        mRvReachTopWhenDown = false;
                        ViewCompat.postInvalidateOnAnimation(this);
                        mRLListener.onRefreshComplete();
                    }
                } else if (mRvReachBottomWhenDown) {
                    if (mRLListener != null) {
                        mRLListener.onLoadMore();
                    }
                    if (mViewDragHelper.smoothSlideViewTo(getChildAt(0), 0, 0)) {
                        mRvReachBottomWhenDown = false;
                        ViewCompat.postInvalidateOnAnimation(this);
                        mRLListener.onLoadMoreComplete();
                    }
                }
                return true;
        }
        return true;
    }
}