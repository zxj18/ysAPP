package com.vodbyte.movie.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vodbyte.movie.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;


public final class GapBottomNavigationView extends BottomNavigationView {
    private float centerRadius = 0f; //中间凹陷的半径
    private float cornerRadius = 12f; //拐角处的圆滑大小（越大越平滑）
    private float shadowLength = 6f; //阴影大小

    public GapBottomNavigationView(@NonNull Context context) {
        super(context);
    }

    public GapBottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GapBottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.TRANSPARENT);
        setBackground(gradientDrawable);


        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GapBottomNavigationView);
        centerRadius = ta.getDimension(R.styleable.GapBottomNavigationView_center_radius, 0f);
        shadowLength = ta.getDimension(R.styleable.GapBottomNavigationView_shadow_length, 6f);
        cornerRadius = ta.getDimension(R.styleable.GapBottomNavigationView_corner_radius, 12f);
        ta.recycle();

    }

    @SuppressLint({"DrawAllocation"})
    protected void onDraw(@NotNull Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        Path path = new Path();

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        //左边的半圆
        RectF rectL = new RectF(
                shadowLength,
                shadowLength,
                getHeight() + shadowLength,
                getHeight() - shadowLength
        );
        path.arcTo(rectL, 90, 180, false);

        path.lineTo(getWidth() / 2 - centerRadius - cornerRadius, shadowLength);

        //左边转角处
        path.quadTo(
                getWidth() / 2 - centerRadius,
                shadowLength,
                getWidth() / 2 - centerRadius,
                cornerRadius + shadowLength
        );

        //中间凹陷的半圆
        RectF rectCenter = new RectF(
                getWidth() / 2 - centerRadius,
                cornerRadius + shadowLength - centerRadius,
                getWidth() / 2 + centerRadius,
                cornerRadius + centerRadius + shadowLength
        );

        path.arcTo(rectCenter, 180, -180, false);

        //右边转角处
        path.quadTo(
                getWidth() / 2 + centerRadius,
                shadowLength,
                getWidth() / 2 + centerRadius + cornerRadius,
                shadowLength
        );
        path.lineTo((getWidth() - shadowLength - getHeight()  / 2), shadowLength);


        //右边的半圆
        RectF rectR = new RectF(
                getWidth() - shadowLength - getHeight() ,
                shadowLength,
                getWidth() - shadowLength,
                getHeight()  - shadowLength
        );
        path.arcTo(rectR, 270, 180, false);

        //最后的直线
        path.moveTo((getWidth() - shadowLength - getHeight()  / 2), getHeight()  - shadowLength);
        path.lineTo(getHeight()  / 2 + shadowLength, getHeight()  - shadowLength);
        path.close();
//
//        //关闭硬件加速才能有阴影效果
        setLayerType(LAYER_TYPE_HARDWARE,paint);


        //按背景色填充背景
        paint.setStyle(Style.FILL);
        if (VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorStateList backgroundTintLis = getBackgroundTintList();
            paint.setColor(backgroundTintLis != null ? backgroundTintLis.getDefaultColor() : Color.BLACK);
        }

        paint.setMaskFilter(null);
        paint.setAntiAlias(true);
        paint.setShadowLayer(shadowLength, (float)0, (float)0, Color.LTGRAY);
        canvas.drawPath(path, paint);

    }

}
