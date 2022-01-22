package com.vodbyte.freetv.common;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.vodbyte.freetv.app.Constant;
import com.vodbyte.freetv.R;
import com.vodbyte.freetv.contract.QrCodeActivity;
import com.vodbyte.freetv.contract.QrCodeEvent;
import com.vodbyte.freetv.mvp.view.activity.condition.ConditionActivity;
import com.vodbyte.freetv.mvp.view.activity.history.VideoHistoryActivity;
import com.vodbyte.freetv.mvp.view.activity.home.HomeActivity;
import com.vodbyte.freetv.mvp.view.activity.live.IjkTVLiveActivity;
import com.vodbyte.freetv.mvp.view.activity.search.SearchNewActivity;
import com.vodbyte.freetv.utils.AppTools;

import org.greenrobot.eventbus.EventBus;
import static com.vodbyte.freetv.app.Constant.ANIMATION_DURATION;
import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_IN_DURATION;
import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_IN_SCALE;
import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_OUT_DURATION;
import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_OUT_SCALE;

public class Ui {

    public static void configTopBar(Activity activity) {
        configTopBar(activity, "");
    }

    public static void configTopBar(Activity activity, String rightNote) {
        View top_bar_menu_root_home = activity.findViewById(R.id.top_bar_menu_root_home);
        View top_bar_menu_root_history = activity.findViewById(R.id.top_bar_menu_root_history);
        View top_bar_menu_root_search = activity.findViewById(R.id.top_bar_menu_root_search);
        View top_bar_menu_root_condition = activity.findViewById(R.id.top_bar_menu_root_condition);
        View top_bar_menu_root_tv_live = activity.findViewById(R.id.top_bar_menu_root_tv_live);
        ImageView top_bar_iv_home = activity.findViewById(R.id.top_bar_iv_home);
        ImageView top_bar_iv_history = activity.findViewById(R.id.top_bar_iv_history);
        ImageView top_bar_iv_search = activity.findViewById(R.id.top_bar_iv_search);
        ImageView top_bar_iv_condition = activity.findViewById(R.id.top_bar_iv_condition);
        ImageView top_bar_iv_tv_live = activity.findViewById(R.id.top_bar_iv_tv_live);
        TextView top_bar_tv_home = activity.findViewById(R.id.top_bar_tv_home);
        TextView top_bar_tv_history = activity.findViewById(R.id.top_bar_tv_history);
        TextView top_bar_tv_search = activity.findViewById(R.id.top_bar_tv_search);
        TextView top_bar_tv_condition = activity.findViewById(R.id.top_bar_tv_condition);
        TextView top_bar_tv_tv_live = activity.findViewById(R.id.top_bar_tv_tv_live);
        TextView top_bar_menu_right_note = activity.findViewById(R.id.top_bar_menu_right_note);

        TextView top_bar_menu_right_title = activity.findViewById(R.id.top_bar_menu_right_title);
        top_bar_menu_right_title.setText(String.format("%s %s", activity.getResources().getString(R.string.app_name),AppTools.getVerName()));

        if (!rightNote.isEmpty()) {
            top_bar_menu_right_title.setVisibility(View.GONE);
            top_bar_menu_right_note.setVisibility(View.VISIBLE);
            top_bar_menu_right_note.setText(rightNote);
        }

        top_bar_menu_root_tv_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, IjkTVLiveActivity.class);
                activity.startActivity(intent);
            }
        });

        top_bar_menu_root_history.setOnClickListener(v -> {
            Intent intent = new Intent(activity, VideoHistoryActivity.class);
            activity.startActivity(intent);
        });
        // 搜索
        top_bar_menu_root_search.setOnClickListener(v -> {
            Intent intent = new Intent(activity, SearchNewActivity.class);
            activity.startActivity(intent);
        });
        //筛选
//        top_bar_menu_root_condition.setOnClickListener(v -> {
////            Intent intent = new Intent(activity, ConditionActivity.class);
////            activity.startActivity(intent);
//        });

        setMenuFocusAnimator(activity, top_bar_menu_root_history, new FocusAction() {
            @Override
            public void onFocus() {
                top_bar_iv_history.setImageResource(R.drawable.ic_history_focus);
                top_bar_tv_history.setTextColor(activity.getResources().getColor(R.color.colorTextFocus));
            }

            @Override
            public void onLoseFocus() {
                top_bar_iv_history.setImageResource(R.drawable.ic_history_normal);
                top_bar_tv_history.setTextColor(activity.getResources().getColor(R.color.colorTextNormal));
            }
        });
        setMenuFocusAnimator(activity, top_bar_menu_root_tv_live, new FocusAction() {
            @Override
            public void onFocus() {
                top_bar_iv_tv_live.setImageResource(R.drawable.ic_tv_live_focus);
                top_bar_tv_tv_live.setTextColor(activity.getResources().getColor(R.color.colorTextFocus));
            }

            @Override
            public void onLoseFocus() {
                top_bar_iv_tv_live.setImageResource(R.drawable.ic_tv_live_normal);
                top_bar_tv_tv_live.setTextColor(activity.getResources().getColor(R.color.colorTextNormal));
            }
        });
        setMenuFocusAnimator(activity, top_bar_menu_root_home, new FocusAction() {
            @Override
            public void onFocus() {
                top_bar_iv_home.setImageResource(R.drawable.ic_home_foces);
                top_bar_tv_home.setTextColor(activity.getResources().getColor(R.color.colorTextFocus));
            }

            @Override
            public void onLoseFocus() {
                top_bar_iv_home.setImageResource(R.drawable.ic_home_normal);
                top_bar_tv_home.setTextColor(activity.getResources().getColor(R.color.colorTextNormal));
            }
        });
        setMenuFocusAnimator(activity, top_bar_menu_root_search, new FocusAction() {
            @Override
            public void onFocus() {
                top_bar_iv_condition.setImageResource(R.drawable.ic_search_focus);
                top_bar_tv_condition.setTextColor(activity.getResources().getColor(R.color.colorTextFocus));
            }

            @Override
            public void onLoseFocus() {
                top_bar_iv_condition.setImageResource(R.drawable.ic_search_normal);
                top_bar_tv_condition.setTextColor(activity.getResources().getColor(R.color.colorTextNormal));
            }
        });
        // 筛选
        setMenuFocusAnimator(activity, top_bar_menu_root_condition, new FocusAction() {
            @Override
            public void onFocus() {
                top_bar_iv_search.setImageResource(R.drawable.ic_search_focus);
                top_bar_tv_search.setTextColor(activity.getResources().getColor(R.color.colorTextFocus));
            }

            @Override
            public void onLoseFocus() {
                top_bar_iv_search.setImageResource(R.drawable.ic_search_normal);
                top_bar_tv_search.setTextColor(activity.getResources().getColor(R.color.colorTextNormal));
            }
        });

        top_bar_menu_root_home.setOnClickListener(v -> {
            Intent intent = new Intent(activity, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
        });
    }

    public static void setMenuFocusAnimator(Activity activity, View view, FocusAction action) {
        view.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                view.setBackground(activity.getResources().getDrawable(R.drawable.bg_common_menu_focus));
                if (action != null) {
                    action.onFocus();
                }

                ValueAnimator animatorFirst = ValueAnimator.ofFloat(1.0f, ANIMATION_ZOOM_IN_SCALE).setDuration(ANIMATION_ZOOM_IN_DURATION);
                ValueAnimator animatorSecond = ValueAnimator.ofFloat(ANIMATION_ZOOM_IN_SCALE, ANIMATION_ZOOM_OUT_SCALE).setDuration(ANIMATION_ZOOM_OUT_DURATION);

                animatorFirst.addUpdateListener(animation -> {
                    if (view.isFocused()) {
                        view.setScaleX((float)animation.getAnimatedValue());
                        view.setScaleY((float)animation.getAnimatedValue());
                    } else {
                        animatorFirst.cancel();
                    }
                });
                animatorSecond.addUpdateListener(animation -> {
                    if (view.isFocused()) {
                        view.setScaleX((float)animation.getAnimatedValue());
                        view.setScaleY((float)animation.getAnimatedValue());
                    } else {
                        animatorFirst.cancel();
                    }
                });
                animatorFirst.start();
                animatorSecond.setStartDelay(ANIMATION_ZOOM_IN_DURATION);
                animatorSecond.start();
            } else {
                view.setBackground(activity.getResources().getDrawable(R.drawable.bg_common_menu_normal));
                if (action != null) {
                    action.onLoseFocus();
                }
                ValueAnimator animator = ValueAnimator.ofFloat(ANIMATION_ZOOM_OUT_SCALE, 1.0f).setDuration(ANIMATION_ZOOM_IN_DURATION);
                animator.addUpdateListener(animation -> {
                    view.setScaleX((float)animation.getAnimatedValue());
                    view.setScaleY((float)animation.getAnimatedValue());
                });
                animator.start();
            }
        });
    }

    public static void setViewFocusScaleAnimator(View view, FocusAction action) {
        view.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (action != null) {
                    action.onFocus();
                }

                ValueAnimator animator = ValueAnimator.ofFloat(1.0f, ANIMATION_ZOOM_OUT_SCALE).setDuration(ANIMATION_DURATION);

                animator.addUpdateListener(animation -> {
                    if (v.isFocused()) {
                        v.setScaleX((float) animation.getAnimatedValue());
                        v.setScaleY((float) animation.getAnimatedValue());
                    } else {
                        animator.cancel();
                    }
                });

                animator.setInterpolator(new OvershootInterpolator());
                animator.start();
            } else {
                if (action != null) {
                    action.onLoseFocus();
                }

                ValueAnimator animator = ValueAnimator.ofFloat(ANIMATION_ZOOM_OUT_SCALE, 1.0f).setDuration(ANIMATION_ZOOM_IN_DURATION);
                animator.addUpdateListener(animation -> {
                    v.setScaleX((float) animation.getAnimatedValue());
                    v.setScaleY((float) animation.getAnimatedValue());
                });
                animator.start();
            }
        });
    }

    public static void showNotice(Activity activity, String title, String content) {
        if (content.startsWith("e+")) {
            content = content.substring(2);
            Ui.showNoticeQrcode(activity, title, content);
            return;
        }
        if (content.startsWith("t+")) {
            content = content.substring(2);
            Ui.showNoticeText(activity, title, content);
            return;
        }
        Ui.showNoticeText(activity, title, content);
    }

    private static void showNoticeText(Activity activity, String title, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private static void showNoticeQrcode(Activity activity, String title, String url) {
        Intent intent = new Intent(activity, QrCodeActivity.class);
        activity.startActivity(intent);
        EventBus.getDefault().postSticky(new QrCodeEvent(url, title));
    }
}
