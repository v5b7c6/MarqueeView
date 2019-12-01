package com.v5b7c6.android.marqueeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.AnimRes;

import java.util.ArrayList;
import java.util.List;

public class MarqueeView extends ViewFlipper {

    private Context context;

    private int marqueeAnimDuration = 1000;
    private int textSize = 50;
    private int textColor = getResources().getColor(android.R.color.black);
    private boolean singleLine;
    private int gravity;

    @AnimRes
    private int inAnimResId;
    @AnimRes
    private int outAnimResId;

    private int position;
    private OnItemClickListener onItemClickListener;

    public MarqueeView(Context context) {
        super(context);
    }

    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MarqueeView, 0, 0);
        textSize = (int) typedArray.getDimension(R.styleable.MarqueeView_textSize, textSize);
        textSize = DisplayUtil.px2sp(context, textSize);
        textColor = typedArray.getColor(R.styleable.MarqueeView_textColor, textColor);
        singleLine = typedArray.getBoolean(R.styleable.MarqueeView_singleLine, singleLine);
        gravity = typedArray.getInt(R.styleable.MarqueeView_gravity, Gravity.LEFT | Gravity.CENTER_VERTICAL);
        marqueeAnimDuration = typedArray.getInteger(R.styleable.MarqueeView_marqueeAnimDuration, marqueeAnimDuration);
        inAnimResId = typedArray.getResourceId(R.styleable.MarqueeView_inAnimResId, R.anim.anim_bottom_in);
        outAnimResId = typedArray.getResourceId(R.styleable.MarqueeView_outAnimResId, R.anim.anim_top_out);
        typedArray.recycle();
    }

    /**
     * 根据宽度组合字符串列表，启动翻页公告
     *
     * @param messages     字符串列表
     */
    public void startWithFixedWidthList(final List<String> messages) {
        if (messages == null || messages.size() == 0) return;
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                int width = DisplayUtil.px2dip(context, getWidth());
                if (width == 0) {
                    return;
                }
                int limit = width / textSize;
                List<ItemBean> data = new ArrayList<>();
                for (int item = 0; item < messages.size(); item++) {
                    String message = messages.get(item);
                    int messageLength = message.length();
                    List<ItemBean> list = new ArrayList();
                    if (messageLength <= limit) {
                        list.add(new ItemBean(message, item));
                    } else {
                        int size = messageLength / limit + (messageLength % limit != 0 ? 1 : 0);
                        for (int i = 0; i < size; i++) {
                            int startIndex = i * limit;
                            int endIndex = ((i + 1) * limit >= messageLength ? messageLength : (i + 1) * limit);
                            list.add(new ItemBean(message.substring(startIndex, endIndex), item));
                        }
                    }
                    data.addAll(list);
                }
                postStart(data);
            }
        });
    }

    /**
     * 根据字符串列表，启动翻页公告
     *
     * @param messages     字符串列表
     */
    public void startWithList(List<String> messages) {
        if (messages == null || messages.size() == 0) return;
        List<ItemBean> data = new ArrayList<>();
        for (int item = 0; item < messages.size(); item++) {
            data.add(new ItemBean(messages.get(item), item));
        }
        postStart(data);
    }

    private void postStart(final List<ItemBean> itemBeanList) {
        post(new Runnable() {
            @Override
            public void run() {
                start(itemBeanList);
            }
        });
    }

    private boolean isAnimStart = false;

    private void start(final List<ItemBean> itemBeanList) {
        removeAllViews();
        clearAnimation();
        // 检测数据源
        if (itemBeanList == null || itemBeanList.isEmpty()) {
            throw new RuntimeException("The messages cannot be empty!");
        }
        position = 0;
        addView(createTextView(itemBeanList.get(position)));

        if (itemBeanList.size() >= 1) {
            setInAndOutAnimation();
            startFlipping();
        }

        if (getInAnimation() != null) {
            getInAnimation().setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if (isAnimStart) {
                        animation.cancel();
                    }
                    isAnimStart = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    position++;
                    if (position >= itemBeanList.size()) {
                        position = 0;
                    }
                    View view = createTextView(itemBeanList.get(position));
                    if (view.getParent() == null) {
                        addView(view);
                    }
                    isAnimStart = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    private TextView createTextView(ItemBean itemBean) {
        TextView textView = (TextView) getChildAt((getDisplayedChild() + 1) % 3);
        if (textView == null) {
            textView = new TextView(context);
            textView.setGravity(gravity);
            textView.setTextColor(textColor);
            textView.setTextSize(textSize);
            textView.setIncludeFontPadding(true);
            textView.setSingleLine(singleLine);
            if (singleLine) {
                textView.setMaxLines(1);
                textView.setEllipsize(TextUtils.TruncateAt.END);
            }
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getPosition(), (TextView) view);
                    }
                }
            });
        }
        textView.setTag(itemBean.getPosition());
        textView.setText(Html.fromHtml(itemBean.getContent()));
        return textView;
    }

    /**
     * 设置进入动画和离开动画
     *
     */
    private void setInAndOutAnimation() {
        Animation inAnim = AnimationUtils.loadAnimation(context, inAnimResId);
        inAnim.setDuration(marqueeAnimDuration);
        setInAnimation(inAnim);

        Animation outAnim = AnimationUtils.loadAnimation(context, outAnimResId);
        outAnim.setDuration(marqueeAnimDuration);
        setOutAnimation(outAnim);
    }

    public int getPosition() {
        return (int) getCurrentView().getTag();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, TextView textView);
    }
}
