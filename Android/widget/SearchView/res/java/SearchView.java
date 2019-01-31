package android.searchview;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 项目名：XunFeiTest
 * 包名：  com.android.xunfeitest
 * 文件名：SearchView
 * 创建者：HY
 * 创建时间：2019/1/31 14:26
 * 描述：  自定义控件SearchView
 */

public class SearchView extends RelativeLayout implements ISearcher {

    private TextWatcher textWatcher;
    private ISearcher.onImageButtonVoiceClickListener onImageButtonVoiceClickListener;
    private ISearcher.onImageButtonCancelClickListener onImageButtonCancelClickListener;
    private ISearcher.onTextViewSearchClickListener onTextViewSearchClickListener;


    EditText et_input;
    ImageButton ib_voice;
    ImageButton ib_cancle;
    TextView tv_search;
    ConstraintLayout rl_mapsearcher;
    View view;

    public SearchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        view = LayoutInflater.from(context).inflate(R.layout.layout_searchview, this);
        initTextWatcher();
        initView();
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(context).inflate(R.layout.layout_searchview, this);
        initTextWatcher();
        initView();
    }


    private void initView() {
        et_input = view.findViewById(R.id.et_input);
        ib_voice = view.findViewById(R.id.ib_voice);
        ib_cancle = view.findViewById(R.id.ib_cancle);
        tv_search = view.findViewById(R.id.tv_search);
        rl_mapsearcher = view.findViewById(R.id.rl_mapsearcher);

        et_input.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    rl_mapsearcher.setSelected(true);
                    et_input.setHint("");
                    if (et_input.getText().length() > 0) {
                        ib_cancle.setVisibility(View.VISIBLE);
                        tv_search.setVisibility(View.VISIBLE);
                        ib_voice.setVisibility(View.GONE);
                    } else {
                        et_input.setSelected(false);
                        ib_cancle.setVisibility(View.GONE);
                        tv_search.setVisibility(View.GONE);
                        ib_voice.setVisibility(View.VISIBLE);
                    }
                } else {
                    et_input.setHint("请输入关键字");
                }
            }
        });

        if (textWatcher != null) {
            et_input.addTextChangedListener(textWatcher);
        }

        ib_voice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onImageButtonVoiceClickListener != null) {
                    onImageButtonVoiceClickListener.onClick(et_input, ib_voice, SearchView.this);
                }
            }
        });

        ib_cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                et_input.setText("");
                et_input.clearFocus();
                if (onImageButtonCancelClickListener != null) {
                    onImageButtonCancelClickListener.onClick(et_input, ib_cancle, SearchView.this);
                }
            }
        });

        tv_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTextViewSearchClickListener != null) {
                    onTextViewSearchClickListener.onClick(et_input, tv_search, SearchView.this);
                }
            }
        });
    }

    /**
     * 语音按钮点击事件
     * @param listener listener
     */
    @Override
    public void setImageButtonVoiceClickListener(ISearcher.onImageButtonVoiceClickListener listener) {
        this.onImageButtonVoiceClickListener = listener;
    }

    /**
     * 清除图标点击事件
     * @param listener listener
     */
    @Override
    public void setImageButtonCancelClickListener(ISearcher.onImageButtonCancelClickListener listener) {
        this.onImageButtonCancelClickListener = listener;
    }

    /**
     * “搜索”点击事件
     * @param listener listener
     */
    @Override
    public void setonTextViewSearchClickListener(ISearcher.onTextViewSearchClickListener listener) {
        this.onTextViewSearchClickListener = listener;
    }

    /**
     * 获取EditText的内容
     * @return EditText的内容
     */
    @Override
    public String getSearchCondition() {
        return et_input.getText().toString().trim();
    }

    /**
     * 获取EditText
     * @return EditText
     */
    @Override
    public EditText getEt_input() {
        return et_input;
    }

    /**
     * 初始化textWatcher
     */
    private void initTextWatcher() {
        textWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if (length > 0 && et_input.isFocused()) {
                    ib_voice.setVisibility(View.GONE);
                    ib_cancle.setVisibility(View.VISIBLE);
                    tv_search.setVisibility(View.VISIBLE);
                } else {
                    ib_cancle.setVisibility(View.GONE);
                    tv_search.setVisibility(View.GONE);
                    ib_voice.setVisibility(View.VISIBLE);
                }
            }
        };
    }

}
