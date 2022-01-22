package com.vodbyte.android.widget.commentview.model;

import androidx.annotation.Keep;

import java.util.List;

/**
 * <p>自定义评论模型抽象类</p>
 * 自定义评论模型必须继承自此抽象类
 *
 * @author Jidcoo
 */
@Keep
public abstract class CommentEnable extends PagerEnable {

    /**
     * 必须实现该方法并确保返回值非NULL
     *
     * @return
     */
    public abstract <T extends ReplyEnable> List<T> getReplies();
}
