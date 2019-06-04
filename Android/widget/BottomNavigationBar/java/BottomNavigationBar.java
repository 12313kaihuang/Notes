package com.yu.hu.bottomnavigationbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 创建者：Vincent
 * 创建时间：2016/12/23
 * 优化，添加注释：HY  2019/6/4 10:11
 * 描述：  底部NavigationBar
 */
@SuppressWarnings("unused")
public class BottomNavigationBar extends LinearLayout implements View.OnClickListener {

    /**
     * items
     */
    private ArrayList<BottomItem> mBottomItems = new ArrayList<>();

    /**
     * 当前所选中的item的position
     */
    private int mSelectedPosition;

    /**
     * 选中 监听事件列表
     */
    private ArrayList<OnSelectedListener> mList;

    public BottomNavigationBar(Context context) {
        this(context, null);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //        parseAttrs(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomNavigationBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        //        parseAttrs(context, attrs);
        init();
    }

    //    private void parseAttrs(Context context, AttributeSet attrs) {
    //        if (attrs != null) {
    //            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomNavigationBar, 0, 0);
    //            mBackgroundColor = typedArray.getColor(R.styleable.BottomNavigationBar_bnbBackgroundColor,
    //                    BnbUtil.fetchContextColor(context, android.R.color.white));
    //
    //            typedArray.recycle();
    //        } else {
    //            mBackgroundColor = BnbUtil.fetchContextColor(context, android.R.color.white);
    //        }
    //    }

    private void init() {
        //横向布局
        setOrientation(HORIZONTAL);
        mList = new ArrayList<>();
    }

    /**
     * 添加item
     */
    public BottomNavigationBar addItem(BottomItem item) {
        mBottomItems.add(item);
        return this;
    }

    /**
     * 删除item
     */
    public BottomNavigationBar removeItem(BottomItem item) {
        mBottomItems.remove(item);
        return this;
    }

    /**
     * 真正的初始化
     * 在item添加完毕后调用
     */
    public void initialize() {
        if (mSelectedPosition > mBottomItems.size() - 1) {
            throw new IllegalArgumentException("Bottom Item Index overflow !!!");
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i = 0; i < mBottomItems.size(); i++) {
            BottomItem item = mBottomItems.get(i);

            //对应一个item的布局
            View btn = inflater.inflate(R.layout.layout_bottom_item, null);
            btn.setClickable(true);
            btn.setTag(i);
            btn.setOnClickListener(this);
            decorateBackground(btn, item);

            TextView tv_bottom = btn.findViewById(R.id.tv_bottom);
            decorateTextView(tv_bottom, item);

            ImageView ic_bottom = btn.findViewById(R.id.ic_bottom);
            decorateImageView(ic_bottom, item);

            //item均分宽度  width = 0dp  weight = 1
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                    ViewGroup.LayoutParams.MATCH_PARENT, 1);
            //加入item
            addView(btn, params);
        }

        getChildAt(mSelectedPosition).setSelected(true);
    }

    @Override
    public void onClick(View view) {
        int index = (int) view.getTag();
        if (mSelectedPosition != index) {
            selectItem(index);
        }
    }

    /**
     * 选中某个item
     *
     * @param index 下标
     */
    private void selectItem(int index) {
        getChildAt(mSelectedPosition).setSelected(false);
        getChildAt(index).setSelected(true);

        //回调selected事件
        for (OnSelectedListener listener : mList) {
            listener.OnSelected(mSelectedPosition, index);
        }

        mSelectedPosition = index;
    }

    /**
     * 设置背景
     */
    private void decorateBackground(View v, BottomItem item) {
        if (item.getActiveBgResID() == 0 || item.getInactiveBgResID() == 0) {
            Log.d("decorateBackground", "decorateBackground: aaaaaaaaaaaaaaaa");
            return;
        }

        StateListDrawable stateListDrawable = new StateListDrawable();

        //应用程序是否在前台，当有通知栏被拉下来或者一个对话框弹出的时候应用程序就不在前台了
        stateListDrawable.addState(new int[]{-android.R.attr.state_window_focused},
                ContextCompat.getDrawable(getContext(), item.getInactiveBgResID()));

        //用户选择了当前View
        stateListDrawable.addState(new int[]{android.R.attr.state_selected},
                ContextCompat.getDrawable(getContext(), item.getActiveBgResID()));

        //android.R.attr.state_pressed 按下
        if (item.isPressEffect()) {
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed},
                    ContextCompat.getDrawable(getContext(), item.getActiveBgResID()));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackground(stateListDrawable);
        } else {
            v.setBackgroundDrawable(stateListDrawable);
        }
    }

    /**
     * 装饰文本 TextView
     */
    private void decorateTextView(TextView tv, BottomItem item) {
        tv.setText(item.getText());

        /**
         * Sample:
         * Normal(-state_pressed, -state_selected) ---> Color.GRAY
         * state_pressed   ---> Color.BLACK
         * state_selected  ---> Color.GREEN
         * */
        //        int[][] states = new int[][]{
        //                new int[]{-android.R.attr.state_pressed, -android.R.attr.state_selected},
        //                new int[]{android.R.attr.state_pressed},
        //                new int[]{android.R.attr.state_selected}
        //        };
        //        int[] colors = new int[]{ Color.GRAY, Color.BLACK, Color.GREEN };

        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_selected},
                new int[]{android.R.attr.state_selected}
        };
        int[] colors = new int[]{
                //默认文本颜色
                (item.getInactiveTextColor() == 0) ? ContextCompat.getColor(getContext(), R.color.dark_grey) : item.getInactiveTextColor(),
                //选中时文本颜色
                (item.getActiveTextColor() == 0) ? BnbUtil.fetchContextColor(getContext(), R.attr.colorPrimary) : item.getActiveTextColor()};

        ColorStateList colorStateList = new ColorStateList(states, colors);
        tv.setTextColor(colorStateList);

        //设置字体大小
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, (item.getTextSize() == 0) ? 12 : item.getTextSize());
    }

    /**
     * 装饰图片icon
     */
    private void decorateImageView(ImageView iv, BottomItem item) {
        if (item.getMode() == 0) {// Tint Mode
            Drawable drawable;
            if (item.getIconDrawable() != null) {
                drawable = item.getIconDrawable();
            } else if (item.getIconResID() != 0) {
                drawable = ContextCompat.getDrawable(getContext(), item.getIconResID());
            } else {
                return;
            }

            int[][] states = new int[][]{
                    new int[]{-android.R.attr.state_selected},
                    new int[]{android.R.attr.state_selected}
            };
            int[] colors = new int[]{
                    (item.getInactiveIconColor() == 0) ? ContextCompat.getColor(getContext(), R.color.dark_grey) : item.getInactiveIconColor(),
                    (item.getActiveIconColor() == 0) ? BnbUtil.fetchContextColor(getContext(), R.attr.colorPrimary) : item.getActiveIconColor()};
            StateListDrawable stateListDrawable = getStateListDrawable(drawable, states);
            Drawable drawable1 = getStateDrawable(stateListDrawable, colors, states);
            iv.setImageDrawable(drawable1);
        } else { // Drawable Mode
            Drawable activeDrawable, inactiveDrawable;
            if (item.getActiveIconDrawable() != null) {
                activeDrawable = item.getActiveIconDrawable();
            } else if (item.getActiveIconResID() != 0) {
                activeDrawable = ContextCompat.getDrawable(getContext(), item.getActiveIconResID());
            } else {
                return;
            }

            if (item.getInactiveIconDrawable() != null) {
                inactiveDrawable = item.getInactiveIconDrawable();
            } else if (item.getActiveIconResID() != 0) {
                inactiveDrawable = ContextCompat.getDrawable(getContext(), item.getInactiveIconResID());
            } else {
                return;
            }

            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{-android.R.attr.state_selected}, inactiveDrawable);
            stateListDrawable.addState(new int[]{android.R.attr.state_selected}, activeDrawable);
            iv.setImageDrawable(stateListDrawable);
        }
    }

    private StateListDrawable getStateListDrawable(Drawable drawable, int[][] states) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        for (int[] state : states) {
            stateListDrawable.addState(state, drawable);
        }
        return stateListDrawable;
    }

    private Drawable getStateDrawable(Drawable drawable, int[] colors, int[][] states) {
        ColorStateList colorList = new ColorStateList(states, colors);

        Drawable.ConstantState state = drawable.getConstantState();
        drawable = DrawableCompat.wrap(state == null ? drawable : state.newDrawable()).mutate();
        DrawableCompat.setTintList(drawable, colorList);
        return drawable;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.mSelectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public void addOnSelectedListener(OnSelectedListener listener) {
        //添加一个item的点击事件
        mList.add(listener);
    }

    public interface OnSelectedListener {
        void OnSelected(int oldPosition, int newPosition);
    }
}

