package com.electrocoder.grader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.LocalSocketAddress;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import javax.xml.namespace.NamespaceContext;

public class PinInputView extends androidx.appcompat.widget.AppCompatEditText {

    float spaceBetweenInputs = 12;
    float numberOfInputs = 6;
    float charElevation = 4;

    Editable text;
    int textLength;
    float[] textWidths;
    Paint textPaint;
    Paint borderPaint;

    int[][] inputStates = new int[][] {
            new int[] {android.R.attr.state_selected},
            new int[] {android.R.attr.state_focused},
            new int[] {-android.R.attr.state_focused}
    };
    int[] boje = new int[3];
    ColorStateList borderColorStateList;

    private OnClickListener mClickListener;
    /////////////////////////////////////////////////////////////////////////////

    public PinInputView(Context context) {
        super(context);
    }

    public PinInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PinInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setBackgroundResource(0);

        float multi = context.getResources().getDisplayMetrics().density;
        spaceBetweenInputs *= multi;
        charElevation *= multi;

        numberOfInputs = attrs.getAttributeIntValue("http://schemas.android.com/apk/res-auto","maxLength", 6);

        textPaint = new Paint(getPaint());
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelection(getText().length());
                if(mClickListener != null) {
                    mClickListener.onClick(v);
                }
            }
        });

        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorSecondary, value, true);
        final int colorSelected = value.data;
        boje[0] = colorSelected;

        context.getTheme().resolveAttribute(R.attr.colorPrimaryDark, value, true);
        final int colorFocused = value.data;
        boje[1] = colorFocused;

        context.getTheme().resolveAttribute(R.attr.colorPrimaryDark, value, true);
        final int colorUnFocused = value.data;
        boje[2] = colorUnFocused;

        borderColorStateList = new ColorStateList(inputStates, boje);
        borderPaint = new Paint(getPaint());
        borderPaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mClickListener = l;
    }

    private int getColorForState(int... states) {
        return borderColorStateList.getColorForState(states, R.attr.colorControlHighlight);
    }

    /* next = is the current char the next character to be input? */
    private void updateColorForLines(boolean next) {
        if (isFocused()) {

            if (next) {
                borderPaint.setColor(
                        getColorForState(android.R.attr.state_selected));
            } else {
                borderPaint.setColor(
                        getColorForState(android.R.attr.state_focused));
            }
        } else {
            borderPaint.setColor(
                    getColorForState(-android.R.attr.state_focused));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int sirina = getWidth() - getPaddingEnd() - getPaddingStart();

        float pinSize = (sirina - (spaceBetweenInputs * (numberOfInputs - 1))) / numberOfInputs;

        int startX = 1;
        int bottom = getHeight() - getPaddingBottom();
        int top = getHeight() - getPaddingTop();
        int right = getPaddingEnd();

        Paint paint = getPaint();
        paint.setStyle(Paint.Style.STROKE);


        //Text Width
        text = getText();
        textLength = text.length();
        textWidths = new float[textLength];

        getPaint().getTextWidths(getText(), 0, textLength, textWidths);

        for(int i = 0; i < numberOfInputs; i++) {
            updateColorForLines(i == textLength);
            canvas.drawRoundRect(startX, 0, startX + pinSize, getMeasuredHeight(), 12f, 12f, borderPaint);

            if(textLength > i) {
                float sredinaBoxa = startX + pinSize / 2;

                canvas.drawText(text, i, i + 1, sredinaBoxa - textWidths[0] / 2, bottom - charElevation, textPaint);
            }

            // Za crtanje linije
            //canvas.drawLine(startX, bottom, startX + pinSize, bottom, getPaint());
            startX += pinSize + spaceBetweenInputs;
        }


    }
}
