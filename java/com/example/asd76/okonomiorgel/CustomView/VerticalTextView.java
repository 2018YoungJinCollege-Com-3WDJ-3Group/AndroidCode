package com.example.asd76.okonomiorgel.CustomView;

/**
 * Created by asd76 on 2018-04-24.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;

public class VerticalTextView extends android.support.v7.widget.AppCompatTextView {

    public VerticalTextView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas){
        TextPaint textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        textPaint.drawableState = getDrawableState();

        canvas.save();

        canvas.translate(getWidth(), 0);
        canvas.rotate(90); //원하는 방향 설정

        canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());

        getLayout().draw(canvas);
        canvas.restore();
    }
}
