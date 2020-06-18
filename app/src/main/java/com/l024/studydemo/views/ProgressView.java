package com.l024.studydemo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.l024.studydemo.R;

/**
 * @author wu_ming_zhi_bei
 * @date 2020/6/18 10:08
 * @Notes 圆形进度条
 */
public class ProgressView extends View {
    private Context mContext;
    //文字
    private String title;
    private String num;
    private String unit;
    //文字大小
    private float titleTextsize;
    private float numTextsize;
    private float unitTextsize;
    //文字颜色
    private int titleTextColor;
    private int numTextColor;
    private int unitTextColor;
    //背景圆的半径
    private float backCircleWidth;
    //外壳圆弧
    private float outerCircleWidth;
    //圆颜色
    private int backCircleColor;
    private int outerCircleColor;
    //终点圆宽度和颜色
    private float endCircleWidth;
    private int endCircleColor;

    private int width, height;
    private float currentPercent = 0.3f;
    private float edgeDistance;//背景圆与view边界的距离

    private Paint backCirclePaint,//画背景圆
            outerCirclePaint,//画进度圆弧
            endBigCirclePaint,//画终点实心大圆
            endSmallCirclePaint,//画终点实心小圆
            titlePaint,//画第一行文字
            numPaint,//画第二行文字
            unitPaint;//画第三行文字

    private Handler handler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if(msg.what==1){
                currentPercent =  ((float)Integer.valueOf(msg.obj+"")/100);
                System.out.println("更新"+currentPercent);
                invalidate();
            }
            return false;
        }
    });

    private OnProgressListener onProgressListener;

    /**
     * 设置进度监听
     * @param onProgressListener
     */
    public void setOnProgressListener(OnProgressListener onProgressListener){
        this.onProgressListener = onProgressListener;
    }

    /**
     * 设置进度
     */
    public void setProgress(final float progress){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<=progress*100;i++){
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = i;
                    try {
                        Thread.sleep(20);
                        handler.sendMessage(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeMessages(1);
    }

    /**
     * 在java代码里new的时候会用到
     * @param context
     */
    public ProgressView(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * 在xml布局文件中使用时自动调用
     * @param context
     */
    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    /**
     * 不会自动调用，如果有默认style时，在第二个构造函数中调用
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化属性
     * @param context
     * @param attrs
     */
    private void init(Context context,AttributeSet attrs){
        this.mContext = context;
        if(attrs!=null){
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ProgressView);
            title = array.getString(R.styleable.ProgressView_title);
            num = array.getString(R.styleable.ProgressView_num);
            unit = array.getString(R.styleable.ProgressView_unit);
            titleTextsize = array.getDimension(R.styleable.ProgressView_titleTextsize,24);
            numTextsize = array.getDimension(R.styleable.ProgressView_numTextsize,48);
            unitTextsize = array.getDimension(R.styleable.ProgressView_unitTextsize,24);
            titleTextColor = array.getColor(R.styleable.ProgressView_titleTextColor, Color.parseColor("#656d78"));
            numTextColor = array.getColor(R.styleable.ProgressView_numTextColor, Color.parseColor("#4fc1e9"));
            unitTextColor = array.getColor(R.styleable.ProgressView_unitTextColor, Color.parseColor("#4fc1e9"));
            backCircleWidth = array.getDimension(R.styleable.ProgressView_backCircleWidth, 12);
            outerCircleWidth = array.getDimension(R.styleable.ProgressView_outerCircleWidth, 20);
            backCircleColor = array.getColor(R.styleable.ProgressView_backCircleColor, Color.parseColor("#e6e9ed"));
            outerCircleColor = array.getColor(R.styleable.ProgressView_outerCircleColor, Color.parseColor("#4fc1e9"));
            endCircleWidth = array.getDimension(R.styleable.ProgressView_endCircleWidth,24);
            endCircleColor = array.getColor(R.styleable.ProgressView_endCircleColor, Color.parseColor("#4fc1e9"));
            edgeDistance = array.getDimension(R.styleable.ProgressView_edgeDistance, 12);
            currentPercent = array.getFloat(R.styleable.ProgressView_currentPercent, 0);
            if(currentPercent>1||currentPercent<0){
                currentPercent = currentPercent>1?1:0;
            }
            //初始化画笔
            backCirclePaint = new Paint();
            backCirclePaint.setAntiAlias(true);
            backCirclePaint.setStrokeWidth(backCircleWidth);
            backCirclePaint.setColor(backCircleColor);
            backCirclePaint.setStyle(Paint.Style.STROKE);

            outerCirclePaint = new Paint();
            outerCirclePaint.setAntiAlias(true);
            outerCirclePaint.setStrokeWidth(outerCircleWidth);
            outerCirclePaint.setColor(outerCircleColor);
            outerCirclePaint.setStyle(Paint.Style.STROKE);

            endBigCirclePaint = new Paint();
            endBigCirclePaint.setAntiAlias(true);
            endBigCirclePaint.setStrokeWidth(endCircleWidth);
            endBigCirclePaint.setColor(endCircleColor);
            endBigCirclePaint.setStyle(Paint.Style.STROKE);

            endSmallCirclePaint = new Paint();
            endSmallCirclePaint.setAntiAlias(true);
            endSmallCirclePaint.setColor(Color.WHITE);
            endSmallCirclePaint.setStyle(Paint.Style.FILL);

            titlePaint = new Paint();
            //通过设置Flag来应用抗锯齿效果
            titlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
            titlePaint.setAntiAlias(true);
            //设置文字居中
            //titlePaint.setTextAlign(Paint.Align.CENTER);
            titlePaint.setColor(titleTextColor);
            titlePaint.setTextSize(titleTextsize);

            numPaint = new Paint();
            numPaint.setAntiAlias(true);
            numPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
            //设置文字居中
            //numPaint.setTextAlign(Paint.Align.CENTER);
            numPaint.setColor(numTextColor);
            numPaint.setTextSize(numTextsize);

            unitPaint = new Paint();
            unitPaint.setAntiAlias(true);
            unitPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
            //unitPaint.setTextAlign(Paint.Align.CENTER);
            unitPaint.setColor(unitTextColor);
            unitPaint.setTextSize(unitTextsize);
            //释放
            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ///获取总宽度,是包含padding值
         //处理WAP_CONTENT
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200,200);
        }else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(height, height);
        }else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(width, width);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //圆心
        int centerX = width / 2;
        int centerY = height / 2;
        //计算半径
        float radius = centerX - edgeDistance;
        //画背景圆
        drawBackCircle(canvas,centerX,centerY,radius);
        //绘制圆弧进度
        drawProgress(canvas,centerX,centerY);
        //绘制标题
        drawText(canvas);
    }

    /**
     * 绘制标题
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        Rect textRect = new Rect();
        //返回的则是当前文本所需要的最小宽度，也就是整个文本外切矩形的宽度
        titlePaint.getTextBounds(title, 0, title.length(), textRect);//25  50 175

        float h = height/ 4;

        //文字居中
        canvas.drawText(title, width / 2 - textRect.width() / 2, h + textRect.height() / 2, titlePaint);

        numPaint.getTextBounds(num, 0, num.length(), textRect);
        canvas.drawText(num, width / 2 - textRect.width() / 2, h*2 + textRect.height() / 2, numPaint);

        unitPaint.getTextBounds(unit, 0, unit.length(), textRect);
        canvas.drawText(unit, width / 2 - textRect.width() / 2, 3*h + textRect.height() / 2, unitPaint);
    }

    /**
     * 绘制背景圆
     * @param canvas
     * @param x
     * @param y
     * @param radius
     */
    private void drawBackCircle(Canvas canvas,int x,int y,float radius){
        canvas.drawCircle(x,y,radius,backCirclePaint);
    }
    /**
     * 绘制圆弧进度
     */
    private void drawProgress(Canvas canvas,int x,int y){
        //圆弧的范围
        RectF rectF = new RectF(edgeDistance, edgeDistance, width - edgeDistance, height - edgeDistance);
        //定义的圆弧的形状和大小的范围
        // 置圆弧是从哪个角度来顺时针绘画的
        //设置圆弧扫过的角度
        //设置我们的圆弧在绘画的时候，是否经过圆形 这里不需要
        //画笔
        canvas.drawArc(rectF, -90, 360 * currentPercent, false, outerCirclePaint);
        //绘制端圆
        //进度在0~100%的时候才会画终点小圆，可以自由改动
        if(currentPercent>0&&currentPercent<1){
            //绘制外层大圆
            canvas.drawCircle(x + rectF.width() / 2 * (float) Math.sin(360 * currentPercent * Math.PI / 180),
                    y - rectF.width() / 2 * (float) Math.cos(360 * currentPercent * Math.PI / 180), endCircleWidth / 2, endBigCirclePaint);
            //绘制内层圆点
            canvas.drawCircle(x + rectF.width() / 2 * (float) Math.sin(360 * currentPercent * Math.PI / 180),
                    y - rectF.width() / 2 * (float) Math.cos(360 * currentPercent * Math.PI / 180), endCircleWidth / 4, endSmallCirclePaint);
        }
    }

    public interface OnProgressListener{
        void progress(float currentPercent);
    };
}
