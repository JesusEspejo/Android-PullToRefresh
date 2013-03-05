package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ListView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * A {@link PullToRefreshListView} that expands to reveal content beneath it.
 */
public class RevealingListView extends PullToRefreshListView {

    static final String TAG = "RevealingListView";

    private ValueAnimator mAnimation;
    private ContentState mState;
    
    private enum ContentState {
        VISIBLE,
        GONE
    }
    
    public RevealingListView(Context context) {
        super(context);
    }

    public RevealingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RevealingListView(Context context, Mode mode) {
        super(context, mode);
        init();
    }

    public RevealingListView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
        init();
    }

    private void init() {
        setOnPullEventListener(mRevealEventListener);
        mState = ContentState.VISIBLE;
    }
    
    private OnPullEventListener<ListView> mRevealEventListener = new OnPullEventListener<ListView>() {
        @Override
        public void onPullEvent(PullToRefreshBase<ListView> listView, State state, Mode direction) {
            switch(state) {
                case RELEASE_TO_REFRESH:
                    hideList();
                    break;
                case RESET:
                    mHideListener.onListReset();
                    break;
            }
        }
    };

    /** Dismiss the list on header tap. */
    public void hideList() {
        mAnimation = ObjectAnimator.ofFloat(this, "y", getY(), getHeight());
        mAnimation.setDuration(700);
        mAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimation.start();
        mAnimation.addListener(mAnimationListener);
    }
    
    /** Show the list on button tap. */ 
    public void showList() {
        onReset();
        mAnimation.reverse();
    }
    
    private ValueAnimator.AnimatorListener mAnimationListener = new ValueAnimator.AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animation) {
            
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            switch(mState) {
                case VISIBLE:
                    mHideListener.onListHide();
                    mState = ContentState.GONE;
                    break;
                case GONE:
                    mHideListener.onListShow();
                    mState = ContentState.VISIBLE;
                    break;
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            
        }
    };
    
    public interface ListActionListener {
        public void onListHide();
        public void onListShow();
        public void onListReset();
    }
    
    private ListActionListener mHideListener;
    
    public void setOnListHideListener(ListActionListener listener) {
        mHideListener = listener;
    }
}
