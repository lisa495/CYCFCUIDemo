package test.com.cn.cycfcuidemo.UIView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import test.com.cn.cycfcuidemo.R;

public class HomeRingView extends View {

    private Paint ringPaint;//圆环画笔

    private Paint middleTextPaint;//文字画笔

    private Paint dislocationCirclePaint;//三个错位圆画笔

    private Paint smallCirclePaint;//四个小圆画笔

    private Paint topViewPaint;//顶部View的画笔 -圆角矩形

    private int centerViewX, centerViewY;//view的中心点

    private RectF ringRect;//渐变圆环的矩形

    private int ringRadius;//圆环半径

    private int smallCircleRadius;//四个小圆的半径

    private int radius;//三个错位圆的半径

    public HomeRingView(Context context) {
        super(context);
        init(context);
    }

    public HomeRingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HomeRingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setBackgroundColor(Color.WHITE);
        //初始化圆环画笔

        ringPaint = initPaint(getDimensPx(R.dimen.px_36_to_dp), false);
        middleTextPaint = initPaint(getDimensPx(R.dimen.px_2_to_dp), true);
        dislocationCirclePaint = initPaint(getDimensPx(R.dimen.px_2_to_dp), false);
        smallCirclePaint = initPaint(0, true);
        topViewPaint = initPaint(getDimensPx(R.dimen.px_2_to_dp), false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(width, heightMeasureSpec);
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //控件的宽和高
        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();
        //控件的中心点
        centerViewX = viewWidth / 2;
        centerViewY = viewHeight / 2;

        //计算小圆的半径
        smallCircleRadius = getDimensPx(R.dimen.px_48_to_dp);
        //计算三个错位圆的半径
        int value = Math.min(viewWidth, viewHeight);
        radius = (value / 2 - smallCircleRadius);
        //计算圆环的半径
        ringRadius = value / 2 - smallCircleRadius -getDimensPx(R.dimen.px_88_to_dp);


        //圆环的渐变
        ringRect = new RectF(centerViewX - ringRadius, centerViewY - ringRadius, centerViewX + ringRadius, centerViewY + ringRadius);
        int color[] = new int[]{Color.parseColor("#ea815c"), Color.parseColor("#e82418"), Color.parseColor("#ea815c")};
        SweepGradient linearGradient = new SweepGradient(centerViewX, centerViewY, color, null);
        ringPaint.setShader(linearGradient);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制渐变色的圆环
        canvas.rotate(-90, centerViewX, centerViewY);
        canvas.drawArc(ringRect, -10, 360, false, ringPaint);
        canvas.rotate(90, centerViewX, centerViewY);
        //绘制三个错位圆
        drawDislocationCircle(canvas);
        //绘制四个小圆
        drawSmallCircle(canvas);
        //绘制顶部矩形
        drawTopRectView(canvas);
        //绘制中间文字
        drawMiddleText(canvas);
    }

    private Paint initPaint(float strokeWidth, boolean isFill) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(isFill ? Paint.Style.FILL : Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.BUTT);
        return paint;
    }


    private int measureWidth(int widthMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int minimumWidth = getSuggestedMinimumWidth();
        int width = 0;
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                width = getResources().getDisplayMetrics().widthPixels - getPaddingLeft() - getPaddingRight();
                break;
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                width = Math.max(minimumWidth, widthSize);
                break;
        }
        return width;
    }

    private int measureHeight(int width, int heightMeasureSpec) {
        int minimumHeight = getSuggestedMinimumHeight();
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = 0;
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                height = width * 550 / 520;
                break;
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                height = Math.max(minimumHeight, heightSize);
                break;
        }
        return height;
    }

    //画三个错位圆
    private void drawDislocationCircle(Canvas canvas) {

        dislocationCirclePaint.setColor(Color.parseColor("#E82418"));
        canvas.drawCircle(centerViewX, centerViewY, radius, dislocationCirclePaint);

        dislocationCirclePaint.setColor(Color.parseColor("#ea7d95"));
        canvas.drawCircle(centerViewX - 10, centerViewY, radius - 3, dislocationCirclePaint);

        dislocationCirclePaint.setColor(Color.parseColor("#f4938c"));
        canvas.drawCircle(centerViewX + 13, centerViewY, radius - 3, dislocationCirclePaint);

    }

    //绘制小圆--包括含有字的以及没字的小圆
    private void drawSmallCircle(Canvas canvas) {
        List<String> list = new ArrayList<>();
        list.add("工资单");
        list.add("按揭房");
        list.add("保险单");
        list.add("公积金");
        int smallCircleColor[] = new int[]{Color.parseColor("#f2943f"), Color.parseColor("#f2523f"), Color.parseColor("#f23fce"), Color.parseColor("#3f9cf2")};
        float smallCircleOfTextDegree[] = new float[]{0.92f, 0.545f, 4.51f, 1.33f};//带有字的小圆
        float smallCircleDegree[] = new float[]{0.837f, 0.5148f, 3.0374f, 1.16f};//纯小圆
        if (list.size() != smallCircleColor.length) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            smallCirclePaint.setColor(smallCircleColor[i]);
            float textCircleX = calculateCoordinate(smallCircleOfTextDegree[i])[0];
            float textCircleY = calculateCoordinate(smallCircleOfTextDegree[i])[1];
            canvas.drawCircle(textCircleX, textCircleY, smallCircleRadius, smallCirclePaint);
            float smallX = calculateCoordinate(smallCircleDegree[i])[0];
            float smallY = calculateCoordinate(smallCircleDegree[i])[1];
            middleTextPaint.setColor(smallCircleColor[i]);
            middleTextPaint.setStyle(Paint.Style.FILL);
            int smallCircle=getDimensPx(R.dimen.px_15_to_dp);
            canvas.drawCircle(smallX, smallY, smallCircle, middleTextPaint);
            middleTextPaint.setColor(Color.WHITE);
            middleTextPaint.setTextSize(getDimensPx(R.dimen.px_26_to_dp));
            float[] textMeasure = measureText(list.get(i), middleTextPaint);
            canvas.drawText(list.get(i), textCircleX - (textMeasure[0] / 2), textCircleY + (textMeasure[1] / 2)-1.5f, middleTextPaint);
        }
    }

    //计算圆上任意一点的坐标
    private float[] calculateCoordinate(float degree) {
        double xValue = Math.cos(Math.PI / degree);
        double yValue = Math.sin(Math.PI / degree);
        float x = (float) (centerViewX + radius * xValue);
        float y = (float) (centerViewY + radius * yValue);
        return new float[]{x, y};
    }

    //绘制顶部的弧形矩形以及文字
    private void drawTopRectView(Canvas canvas) {
        //绘制小一圈的白色的圆角矩形当做背景，然后绘制
        RectF whiteRectF = new RectF(centerViewX - 100 + 1.5f, centerViewY - radius - 27 + 2, centerViewX + 100 - 1.5f, centerViewY - radius + 27 - 2);
        RectF rectF = new RectF(centerViewX - 100, centerViewY - radius - 27, centerViewX + 100, centerViewY - radius + 27);
        topViewPaint.setColor(Color.parseColor("#E82418"));
        topViewPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(rectF, 27, 27, topViewPaint);
        topViewPaint.setColor(Color.WHITE);
        topViewPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(whiteRectF, 27 - 2, 27 - 2, topViewPaint);
        middleTextPaint.setColor(Color.parseColor("#E82418"));
        String text = "均可申请";
        middleTextPaint.setTextSize(32);
        float[] textMeasure = measureText(text, middleTextPaint);
        canvas.drawText(text, centerViewX - (textMeasure[0] / 2), centerViewY - radius + textMeasure[1] / 2-1.5f, middleTextPaint);
    }

    //测量文字宽高
    private float[] measureText(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return new float[]{rect.width(), rect.height()};
    }

    private void drawMiddleText(Canvas canvas) {
        String text = "最高额度";
        String moneyFlag = "￥";
        middleTextPaint.setTextSize(getDimensPx(R.dimen.px_48_to_dp));
        canvas.drawText(moneyFlag, centerViewX - ringRadius + 40, centerViewY, middleTextPaint);
        float[] textMeasure = measureText(text, middleTextPaint);
        canvas.drawText(text, centerViewX - (textMeasure[0] / 2), (centerViewY - (textMeasure[1] / 2)), middleTextPaint);
        String bottomText = "20万";
        middleTextPaint.setTextSize(108);
        middleTextPaint.setFakeBoldText(true);
        float[] bottomTextMeasure = measureText(bottomText, middleTextPaint);
        canvas.drawText(bottomText, centerViewX - (bottomTextMeasure[0] / 2), centerViewY + (bottomTextMeasure[1]), middleTextPaint);

    }


    private int getDimensPx(int dimens) {
        return getResources().getDimensionPixelSize(dimens);
    }
}
