package com.idnp.ExamenPlot;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import java.util.Date;
import java.util.Random;

public class Plot extends View {

    private int percentage;
    private int fontsize = 50;
    private int plotwidth;
    private int plotheigth;
    private int fontmargin;
    private int[] day_array;
    private float[] y_data;
    private Paint paint_axis;
    private Paint textPaint;
    private float major;
    private float minor;
    private int column_width;
    private Paint paint_data;

    public Plot(Context context) {
        super(context);
    }
    public Plot(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(attrs);
    }

    public Plot(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray ta = this.getContext().obtainStyledAttributes(attrs,
                R.styleable.Grafica);
       this.fontsize = ta.getInt(R.styleable.Grafica_fontsize, 0);

        this.day_array = new int[]{
                R.string.sunday,
                R.string.monday,
                R.string.thuesday,
                R.string.wednesday,
                R.string.thuesday,
                R.string.friday,
                R.string.saturday
        };
        Random random = new Random();
        this.y_data = new float[this.day_array.length];
        for (int i = 0; i< this.day_array.length; i++){
            this.y_data[i] = random.nextInt(100);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.plotheigth = this.getMeasuredHeight() - fontsize -10;
        this.plotwidth = this.getMeasuredWidth() -fontsize -10;
        this.fontmargin = this.fontsize*2;
        this.drawAxis(canvas);
        this.drawWeekOnAxisX(canvas);
        this.drawDataOnAxisY(canvas);
        this.drawDataOnPlot(canvas);
    }

    protected void drawAxis(Canvas canvas){
        paint_axis = new Paint();
        paint_axis.setStyle(Paint.Style.FILL);
        paint_axis.setStrokeWidth(5);
        paint_axis.setColor(Color.BLACK);

        canvas.drawLine(this.fontmargin, 0, this.fontmargin, this.plotheigth, paint_axis);
        canvas.drawLine(this.fontmargin, this.plotheigth, this.plotwidth, this.plotheigth, paint_axis);
    }

    protected void drawWeekOnAxisX(Canvas canvas){
        int day = new Date().getDay();
        column_width = this.plotwidth/this.day_array.length;

        textPaint = new Paint();
        textPaint.setTypeface(Typeface.DEFAULT);
        textPaint.setTextSize(this.fontsize);
        textPaint.setTextAlign(Paint.Align.CENTER);


        for (int i=0; i < this.day_array.length; i++){
            int position= i*column_width + this.fontmargin;
            int index = (day + i +1)%this.day_array.length;
            String label = "" + this.getContext().getString(this.day_array[index]);
            canvas.drawText(label, position, this.getMeasuredHeight(), textPaint);
        }
    }

    protected void drawDataOnAxisY(Canvas canvas){
        textPaint.setTextAlign(Paint.Align.RIGHT);
        major = Float.MIN_VALUE;
        minor = Float.MAX_VALUE;
        for (int  i =0; i < this.y_data.length; i++){
            if (y_data[i]> major)
                major = y_data[i];
            if (y_data[i]< minor)
                minor = y_data[i];
        }
        if (minor>0){
            minor = 0;
        }
        int range = (int) ( major - minor ) / this.y_data.length;
        int row_heigth = this.plotheigth / this.y_data.length;

        for (int i = 0; i <=y_data.length; i++){
            String label = (int)minor + (i*range)+ "";
            canvas.drawText(label, this.fontmargin-10, plotheigth - i*row_heigth + fontsize, textPaint);
        }
    }

    protected void drawDataOnPlot(Canvas canvas){
        paint_data = new Paint();
        paint_data.setColor(Color.RED);
        paint_data.setStyle(Paint.Style.FILL);
        paint_data.setStrokeWidth(5);
        float max_range = major - minor;
        int startX = fontmargin;
        int startY = (int)(plotheigth -  y_data[0]/max_range * plotheigth);
        for (int i = 0;i < y_data.length; i++){
            int new_y = (int)(plotheigth -  y_data[i]/max_range * plotheigth);
            int new_x= fontmargin + i*column_width;
            canvas.drawLine(startX, startY, new_x, new_y , paint_data);
            canvas.drawCircle(new_x, new_y, 10, paint_data);
            startX = new_x;
            startY = new_y;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 50;
        int height = 50;

        switch(widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                if(width > widthSize) {
                    width = widthSize;
                }
                break;
        }

        switch(heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                if (height > heightSize) {
                    height = heightSize;
                }
                break;
        }
        this.setMeasuredDimension(width, height);
    }
    public void setData(float[] data){
        this.y_data = data;
    }
}
