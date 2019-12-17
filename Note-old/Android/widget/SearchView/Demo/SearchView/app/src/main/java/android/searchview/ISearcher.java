package android.searchview;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 项目名：XunFeiTest
 * 包名：  com.android.xunfeitest
 * 文件名：ISearcher
 * 创建者：HY
 * 创建时间：2019/1/31 14:25
 * 描述：  SearchView功能相关接口
 */

@SuppressWarnings("unused")
public interface ISearcher {

    /**
     * 语音按钮点击事件
     * @param listener 回调接口listener
     */
    void setImageButtonVoiceClickListener(onImageButtonVoiceClickListener listener);

    /**
     * 清除图标点击事件
     * @param listener 回调接口listener
     */
    void setImageButtonCancelClickListener(onImageButtonCancelClickListener listener);

    /**
     * “搜索”点击事件
     * @param listener 回调接口listener
     */
    void setonTextViewSearchClickListener(onTextViewSearchClickListener listener);

    /**
     * 获取EditText的内容
     * @return EditText的内容
     */
    String getSearchCondition();

    /**
     * 获取EditText
     * @return EditText
     */
    EditText getEt_input();

    /**
     * 语音按钮点击回调接口
     */
    interface onImageButtonVoiceClickListener {
        void onClick(EditText input, ImageView voice, View view);
    }

    /**
     * 清除按钮点击回调接口
     */
    interface onImageButtonCancelClickListener {
        void onClick(EditText input, ImageView cancle, View view);
    }

    /**
     * 点击搜多回调接口
     */
    interface onTextViewSearchClickListener {
        void onClick(EditText input, TextView search, View view);
    }

}

