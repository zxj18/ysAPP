package com.vodbyte.movie.mvp.view.fragment.VodDetail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.vodbyte.android.widget.commentview.CommentView;
import com.vodbyte.android.widget.commentview.callback.OnCommentLoadMoreCallback;
import com.vodbyte.android.widget.commentview.callback.OnItemClickCallback;
import com.vodbyte.android.widget.commentview.callback.OnPullRefreshCallback;
import com.vodbyte.android.widget.commentview.callback.OnReplyLoadMoreCallback;
import com.vodbyte.android.widget.commentview.callback.OnScrollCallback;
import com.vodbyte.android.widget.commentview.defaults.DefaultCommentModel;
import com.vodbyte.android.widget.commentview.defaults.DefaultViewStyleConfigurator;
import com.vodbyte.movie.R;
import com.vodbyte.movie.app.LocalServer;
import com.vodbyte.movie.base.BaseLazyFragment;
import com.vodbyte.movie.base.BasePresenter;
import com.vodbyte.movie.mvp.model.vo.BookTabDirVO;
import com.vodbyte.movie.widget.ResizeFrameLayout;

import butterknife.BindView;

/**
 * 评论
 */
public class TabCommentFragment extends BaseLazyFragment {

    @BindView(R.id.user)
    EditText mUser;
    @BindView(R.id.editor)
    EditText mEditor;
    @BindView(R.id.button)
    Button mButton;
    @BindView(R.id.myCommentView)
    CommentView commentView;
    @BindView(R.id.resize_layout)
    ResizeFrameLayout mResizeLayout;

    private String mVodId;
    //本地测试数据
    private LocalServer localServer;
    private boolean isReply = false;
    private boolean isChildReply = false;
    private Gson gson;
    private int cp, rp;
    private long fid, pid;

    public void setData(BookTabDirVO bookTabDirVO,String bookId){

        lazyLoad();
    }

    @Override
    protected BasePresenter getInstance() {
        return null;
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_tab_comment;
    }

    @Override
    protected void initData(Bundle bundle) {
        gson = new Gson();
        localServer = new LocalServer(mBaseActivity, "api1");
    }

    @Override
    protected void initView(View view) {
        commentView.setViewStyleConfigurator(new DefaultViewStyleConfigurator(mBaseActivity));
        //设置空视图
        //commentView.setEmptyView(view);
        //设置错误视图
        //commentView.setErrorView(view);
        //获取callbackBuilder添加事件回调
        commentView.callbackBuilder()
                //下拉刷新回调
                .setOnPullRefreshCallback(new MyOnPullRefreshCallback())
                //上拉加载更多回调（加载更多评论数据）
                .setOnCommentLoadMoreCallback(new MyOnCommentLoadMoreCallback())
                //回复数据加载更多回调（加载更多回复）
                .setOnReplyLoadMoreCallback(new MyOnReplyLoadMoreCallback())
                //评论、回复Item的点击回调（点击事件回调）
                .setOnItemClickCallback(new MyOnItemClickCallback())
                //滚动事件回调
                .setOnScrollCallback(new MyOnScrollCallback())
                //设置完成后必须调用CallbackBuilder的buildCallback()方法，否则设置的回调无效
                .buildCallback();

        load(1, 1);
    }

    private void load(int code, int handlerId) {
        localServer.get(code, handler, handlerId);
    }

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    //commentView.loadFailed(true);//实际网络请求中如果加载失败调用此方法
                    commentView.loadComplete(gson.fromJson((String) msg.obj, DefaultCommentModel.class));
                    break;
                case 2:
                    //commentView.refreshFailed();//实际网络请求中如果加载失败调用此方法
                    commentView.refreshComplete(gson.fromJson((String) msg.obj, DefaultCommentModel.class));
                    break;
                case 3:
                    //commentView.loadFailed();//实际网络请求中如果加载失败调用此方法
                    commentView.loadMoreComplete(gson.fromJson((String) msg.obj, DefaultCommentModel.class));
                    break;
                case 4:
                    //commentView.loadMoreReplyFailed();//实际网络请求中如果加载失败调用此方法
                    commentView.loadMoreReplyComplete(gson.fromJson((String) msg.obj, DefaultCommentModel.class));
                    break;
            }

        }
    };

    /**
     * 下拉刷新回调类
     */
    class MyOnPullRefreshCallback implements OnPullRefreshCallback {

        @Override
        public void refreshing() {
            load(1, 2);
        }

        @Override
        public void complete() {
            //加载完成后的操作
        }

        @Override
        public void failure(String msg) {
            if (msg != null)
                Toast.makeText(mBaseActivity, msg, Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 上拉加载更多回调类
     */
    class MyOnCommentLoadMoreCallback implements OnCommentLoadMoreCallback {

        @Override
        public void loading(int currentPage, int willLoadPage, boolean isLoadedAllPages) {
            //因为测试数据写死了，所以这里的逻辑也是写死的
            if (!isLoadedAllPages) {
                if (willLoadPage == 2) {
                    load(2, 3);
                } else if (willLoadPage == 3) {
                    load(3, 3);
                }
            }
        }

        @Override
        public void complete() {
            //加载完成后的操作
        }

        @Override
        public void failure(String msg) {
            if (msg != null)
                Toast.makeText(mBaseActivity, msg, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 回复加载更多回调类
     */
    class MyOnReplyLoadMoreCallback implements OnReplyLoadMoreCallback<DefaultCommentModel.Comment.Reply> {

        @Override
        public void loading(DefaultCommentModel.Comment.Reply reply, int willLoadPage) {
            //因为测试数据写死了，所以这里的逻辑也是写死的
            //在默认回复数据模型中，kid作为父级索引
            //为了扩展性，把对应的具体模型传了出来，可根据具体需求具体使用
            if (reply.getKid() == 1593699394031L) {
                load(4, 4);
            } else {
                if (willLoadPage == 2) {
                    load(5, 4);
                } else if (willLoadPage == 3) {
                    load(6, 4);
                }
            }
        }

        @Override
        public void complete() {
            //加载完成后的操作
        }

        @Override
        public void failure(String msg) {
            if (msg != null)
                Toast.makeText(mBaseActivity, msg, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 点击事件回调
     */
    class MyOnItemClickCallback implements OnItemClickCallback<DefaultCommentModel.Comment, DefaultCommentModel.Comment.Reply> {

        @Override
        public void commentItemOnClick(int position, DefaultCommentModel.Comment comment, View view) {
            isReply = true;
            cp = position;
            isChildReply = false;
            fid = comment.getFid();
            mEditor.setHint("回复@" + comment.getPosterName() + ":");
            showInput(mEditor);
        }

        @Override
        public void replyItemOnClick(int c_position, int r_position, DefaultCommentModel.Comment.Reply reply, View view) {
            isReply = true;
            cp = c_position;
            rp = r_position;
            isChildReply = true;
            fid = reply.getKid();
            pid = reply.getId();
            mEditor.setHint("回复@" + reply.getReplierName() + ":");
            showInput(mEditor);
        }
    }

    class MyOnScrollCallback implements OnScrollCallback {

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            isReply = false;

//            if (editor.getText().toString().length()>0){
//                editor.setText("");
//            }
//            editor.setHint("发表你的评论吧~");
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {


        }
    }

    /**
     * 显示键盘
     *
     * @param et 输入焦点
     */
    public void showInput(final EditText et) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) mBaseActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }


    @Override
    protected void setListener() {
        mResizeLayout.setKeyboardListener(new ResizeFrameLayout.KeyboardListener() {
            @Override
            public void onKeyboardShown(int height) {
                Log.d("onKeyboardShown ",height+"");
            }

            @Override
            public void onKeyboardHidden(int height) {
                Log.d("onKeyboardHidden ",height+"");
            }
        });
    }

    @Override
    protected void lazyLoad() {
        if (!isVisible || !isPrepared || isLoaded){
            return;
        }
//        if (mBookTabDirVO != null){
//            isLoaded = true;
//            mTvUpdateTime.setText(mBookTabDirVO.getUpdateTime());
//        }
    }
}
