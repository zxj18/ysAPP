package com.zhengsr.ecloadinglib.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.Nullable;

import com.zhengsr.ecloadinglib.R;
import com.zhengsr.ecloadinglib.utils.DimenUtils;
import com.zhengsr.ecloadinglib.utils.HandlerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengshaorui on 2018/4/7.
 */

public class EcJumpLoadingView extends View  {
    private static final String TAG = "EcLoadingView";
    //const
    private static final int SHAPEOFFSERT = 20;
    private static final int SHADERHEIGHT = 25;
    private static final float SCALE = 4.5f;
    private Rect mImageRect; ;
    private static final int[] BITMAP_RESID = new int[]{
            R.mipmap.ecloading_apple,R.mipmap.ecloading_hanbe,R.mipmap.ecloading_blue,
            R.mipmap.ecloading_oragin,R.mipmap.ecloading_li,R.mipmap.ecloading_li_oragin
    };
    //shape
    private int mMoveY = 0;
    private int mCurrentCount = 0;

    //paint
    private Paint mShapePaint;
    private Paint mShaderPaint;
    private int mWidth;
    private int mHeight;

    //normal
    private ValueAnimator mShapeAnim;
    //attrs
    private List<Bitmap> mBitmapList = new ArrayList<>();
    private int mAnimTime ;
    private RectF mShaderRectF;


    public EcJumpLoadingView(Context context) {
        this(context,null);
    }

    public EcJumpLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EcJumpLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EcJumpLoadingView);
        mAnimTime = ta.getInteger(R.styleable.EcJumpLoadingView_ec_jump_anim_time,500);


        int length = BITMAP_RESID.length;
        for (int i = 0; i < length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),BITMAP_RESID[i]);
            mBitmapList.add(bitmap);
        }
        ta.recycle();
        init();
    }

    private void init() {
        mShapePaint = new Paint();
        mShapePaint.setAntiAlias(true);
        mShapePaint.setColor(Color.RED);

        mShaderPaint = new Paint();
        mShaderPaint.setAntiAlias(true);
        mShaderPaint.setColor(Color.parseColor("#515151"));


    }

    public void setAnimTime(int time){
        mAnimTime = time;
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawImage(canvas);
        drawShader(canvas);
    }

    /**
     * 画不同的图片
     * @param canvas
     */
    private void drawImage(Canvas canvas) {


        canvas.save();
        //按比例缩放一下
        Bitmap bitmap = mBitmapList.get(mCurrentCount);
        int bwidth = (int) (mWidth * 1.0f / SCALE);
        int bheight = (int) (mHeight * 1.0f / SCALE);
        canvas.translate(mWidth/2 - bwidth / 2,mMoveY);
        mImageRect = new Rect(0,SHAPEOFFSERT, bwidth ,
                bheight + SHAPEOFFSERT);
        canvas.drawBitmap(bitmap,null, mImageRect,null);
        canvas.restore();
    }

    /**
     * 开始动画
     */
    public void startAnim(){
        if (mHeight == 0){
            throw new RuntimeException("start anim too early,which height is 0");
        }
        final Bitmap bitmap = mBitmapList.get(mCurrentCount);
        final int desHeight = (int) (mHeight - mImageRect.bottom - (mHeight - mShaderRectF.top));

        if (mShapeAnim == null) {
            mShapeAnim = ValueAnimator.ofFloat(mMoveY, desHeight);
            mShapeAnim.setDuration(mAnimTime);
            mShapeAnim.setInterpolator(new AccelerateInterpolator(1.2f));
            mShapeAnim.setRepeatMode(ValueAnimator.REVERSE);
            mShapeAnim.setRepeatCount(ValueAnimator.INFINITE);
        }
        mShapeAnim.cancel();
        mShapeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value  = (float) animation.getAnimatedValue();
               // Log.d(TAG, "zsr --> onAnimationUpdate: "+value);
                if (value == desHeight){
                    if (mCurrentCount < mBitmapList.size() - 1){
                        mCurrentCount ++;
                    }else{
                        mCurrentCount = 0;
                    }
                }
                mMoveY = (int) value;
                invalidate();
            }
        });

        mShapeAnim.start();
    }

    /**
     * 停止动画
     */
    public void stopAnim(){
        if (mShapeAnim != null){
            mShapeAnim.cancel();

        }
    }


    /**
     * 画阴影
     * @param canvas
     */
    private void drawShader(Canvas canvas){

        float defaultwidth = (float) (mWidth * 1.0f / (SCALE - 0.5));
        float defaultheight = mHeight * 1.0f / SCALE;
        float shaderWidth =  defaultwidth - defaultwidth * mMoveY / mHeight ;
        float shaderHeight =  SHADERHEIGHT - SHADERHEIGHT * mMoveY / mHeight ;
        mShaderRectF = new RectF(mWidth/2 - shaderWidth/2,mHeight - defaultheight / 2,
                mWidth/2 + shaderWidth/2,mHeight - defaultheight / 2 +shaderHeight);
        canvas.drawOval(mShaderRectF,mShaderPaint);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = DimenUtils.measureWidth(widthMeasureSpec);
        mHeight = DimenUtils.measureHeight(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }



    @Override
    protected void onWindowVisibilityChanged(final int visibility) {
        super.onWindowVisibilityChanged(visibility);
       // Log.d(TAG, "zsr --> onWindowVisibilityChanged: "+visibility);
        HandlerUtils.handlePost(500, new HandlerUtils.handlerListener() {
            @Override
            public void post() {
                if (visibility == View.VISIBLE){
                    startAnim();
                }else {
                    stopAnim();
                }
            }
        });
    }
}
