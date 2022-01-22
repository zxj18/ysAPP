package com.vodbyte.movie.widget.component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.vodbyte.easy_rv_adapter.OnClickViewRvListener;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.RvSimpleAdapter;
import com.vodbyte.movie.R;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.bean.rv_cell.SourceCell;
import com.vodbyte.videocontroller.component.VodControlView;
import com.vodbyte.videoplayer.player.VideoView;
import com.vodbyte.videoplayer.util.PlayerUtils;

import java.util.ArrayList;
import java.util.List;

public class DefinitionControlView extends VodControlView {

    private TextView mDefinition;

    private RecyclerView mSwitchSourceView;
    private RecyclerView mSubSwitchSourceView;
    private RvSimpleAdapter mBaseAdapter;
    private RvSimpleAdapter mSubBaseAdapter;

    private PopupWindow mPopupWindow;
    private LinearLayout mPopLayout;

    private List<VodBean.VodPlayBean> mVodPlayBeanList;
    private OnUrlBeanSwitchListener mOnUrlBeanSwitchListener;

    public DefinitionControlView(@NonNull Context context) {
        super(context);
    }

    public DefinitionControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DefinitionControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {

        mPopupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_switch_source_pop, this, false);
        // 选集切换
        mSwitchSourceView = mPopLayout.findViewById(R.id.rv_switch_source_view);
        mSubSwitchSourceView = mPopLayout.findViewById(R.id.rv_sub_switch_source_view);

        mBaseAdapter = new RvSimpleAdapter();
        mSwitchSourceView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        mSwitchSourceView.setAdapter(mBaseAdapter);


        mPopupWindow.setContentView(mPopLayout);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xffffffff));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setClippingEnabled(false);
        mPopupWindow.setAnimationStyle(R.style.AnimationRightFade);


        mDefinition = findViewById(R.id.tv_definition);
        mDefinition.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("2222","22222");
                showRateMenu();
            }
        });

    }

    private void showRateMenu() {
        mPopLayout.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//        mPopupWindow.showAsDropDown(mDefinition, -((mPopLayout.getMeasuredWidth() - mDefinition.getMeasuredWidth()) / 2),
//                -(mPopLayout.getMeasuredHeight() + mDefinition.getMeasuredHeight() + PlayerUtils.dp2px(getContext(), 10)));

        mPopupWindow.showAtLocation(mDefinition, Gravity.RIGHT, 0, 0);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_definition_control_view;
    }

    @Override
    public void onVisibilityChanged(boolean isVisible, Animation anim) {
        super.onVisibilityChanged(isVisible, anim);
        if (!isVisible) {
            mPopupWindow.dismiss();
        }
    }

    @Override
    public void onPlayerStateChanged(int playerState) {
        super.onPlayerStateChanged(playerState);
        if (playerState == VideoView.PLAYER_FULL_SCREEN) {
            mDefinition.setVisibility(VISIBLE);
        } else {
            mDefinition.setVisibility(GONE);
            mPopupWindow.dismiss();
        }
    }

    public void setData(List<VodBean.VodPlayBean> vodPlayBeanList) {
        mVodPlayBeanList = vodPlayBeanList;

        if (vodPlayBeanList == null) return;

        List<BaseRvCell> baseRvCellList = new ArrayList<>();

        for (final VodBean.VodPlayBean vodPlayBean : vodPlayBeanList) {
            SourceCell sourceCell = new SourceCell(vodPlayBean);
            sourceCell.setListener(new OnClickViewRvListener() {
                @Override
                public void onClick(View view, int position) {

                }

                @Override
                public <C> void onClickItem(C data, int position) {
                    VodBean.VodPlayBean vodPlayBean2 = (VodBean.VodPlayBean)data;

                }

            });
            baseRvCellList.add(sourceCell);
        }

        mBaseAdapter.addAll(baseRvCellList);

    }

    private OnClickListener rateOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int index = (int) v.getTag();

//            ((TextView) mPopLayout.getChildAt(mCurIndex)).setTextColor(Color.BLACK);
//            ((TextView) mPopLayout.getChildAt(index)).setTextColor(ContextCompat.getColor(getContext(), R.color.dkplayer_theme_color));
//            mDefinition.setText(mRateStr.get(index));
//            switchDefinition(mRateStr.get(index));
//            mPopupWindow.dismiss();
//            mCurIndex = index;
        }
    };

    private void switchDefinition(String s) {
        mControlWrapper.hide();
        mControlWrapper.stopProgress();
//        String url = mVodPlayBeanList.get(s);
//        if (mOnUrlBeanSwitchListener != null)
//            mOnUrlBeanSwitchListener.onRateChange(url);
    }

    public interface OnUrlBeanSwitchListener {
        void onUrlBeanChange(VodBean.VodPlayBean.UrlBean urlBean);
    }

    public void setOnUrlBeanSwitchListener(OnUrlBeanSwitchListener onUrlBeanSwitchListener) {
        mOnUrlBeanSwitchListener = onUrlBeanSwitchListener;
    }
}
