package com.handmark.pulltorefresh.library;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ListView;

/**
 * A {@link PullToRefreshListView} that expands to reveal content beneath it.
 */
public class RevealingListView extends PullToRefreshListView {

    static final String TAG = "RevealingListView";

    private ValueAnimator mAnimation;

    public RevealingListView(Context context) {
        super(context);
    }

    public RevealingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addPullEventListener();
    }

    public RevealingListView(Context context, Mode mode) {
        super(context, mode);
        addPullEventListener();
    }

    public RevealingListView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
        addPullEventListener();
    }

    private void addPullEventListener() {
        setOnPullEventListener(mRevealEventListener);
    }

    private OnPullEventListener<ListView> mRevealEventListener = new OnPullEventListener<ListView>() {

        @Override
        public void onPullEvent(PullToRefreshBase<ListView> listView, State state, Mode direction) {
            
            switch(state) {
                case RELEASE_TO_REFRESH:
                    hideList();
                    break;
            }
        }
    };

    /** Dismiss the list on header tap. */
    public void hideList() {
        mAnimation = ObjectAnimator.ofFloat(this, "y", getY(), getHeight() - 50f);
        mAnimation.setDuration(700);
        mAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimation.start();
    }

    /** Show the list on button tap. */ 
    public void showList() {
        mAnimation.reverse();
    }
}
