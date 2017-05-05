package com.example.tyrant.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.Calendar;

/**
 * Created by tyrant on 5/2/17.
 */

public class MyCalendar extends View {
    private Paint paint;
    private int verCount = 7;
    private int horCount = 7;

    private int offsetX;
    private int offsetY = 80;
    private int leftMargin = 50;
    private int rightMargin = 50;
    private int topMargin = 50;
    private int bottomMargin = 50;
    private int width = 0;
    private int height = 0;
    private float textSize = 60;
    private int calendarWidth = 800;
    private int calendarHeight = 800;
    private String calendarTitle = "Calendar";
    private Rect rect;
    private int delta;
    private int lastMonth;
    private int currMonth;
    private int today;
    private String[] weeks = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private int menuTextSize = 45;
    private int[] selection;
    private int itemWidth;
    private int selectionDate;
    private String[] dates;
    private SparseBooleanArray booleanArray;
    private Bitmap signBitmap;

    public MyCalendar(Context context) {
        super(context);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setTextSize(textSize);

        rect = new Rect();
        signBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.backpack);
    }

    public MyCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setTextSize(textSize);

        rect = new Rect();

        signBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.backpack);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyCalendar, 0, 0);
        calendarTitle = typedArray.getText(R.styleable.MyCalendar_calendarTitle).toString();
        calendarWidth = (int) typedArray.getDimension(R.styleable.MyCalendar_calendarWidth, calendarWidth);
        calendarHeight = (int) typedArray.getDimension(R.styleable.MyCalendar_calendarHeight, calendarHeight);
        textSize = typedArray.getDimension(R.styleable.MyCalendar_textSize, textSize);
        menuTextSize = (int) typedArray.getDimension(R.styleable.MyCalendar_menuTextSize, menuTextSize);
//        float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 1, context.getResources().getDisplayMetrics());
        typedArray.recycle();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        if (calendarWidth > point.x) {
            calendarWidth = point.x;
        }
        if (calendarHeight > point.y) {
            calendarHeight = point.y;
        }
        if (calendarHeight > calendarWidth) {
            calendarHeight = calendarWidth;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wideMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int wideSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (wideMode == MeasureSpec.UNSPECIFIED) {
            width = wideSize;
        } else if (wideMode == MeasureSpec.AT_MOST) {
            width = wideSize;
        } else if (wideMode == MeasureSpec.EXACTLY) {
            width = wideSize;
        }

        if (heightMode == MeasureSpec.UNSPECIFIED) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        }

        leftMargin = (width - calendarWidth) / 2;
        topMargin = (height - calendarHeight) / 2;
        offsetX = calendarWidth;
        offsetY = calendarHeight / (verCount - 1);
        itemWidth = calendarWidth / horCount;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //写标题
        paint.setColor(Color.BLUE);
        paint.setTextSize(textSize);
        paint.getTextBounds(calendarTitle, 0, calendarTitle.length(), rect);
        int textLeftMargin = (width - rect.width()) >> 1;
        int textTopMargin  =  topMargin - offsetY;
        canvas.drawText(calendarTitle, textLeftMargin, textTopMargin, paint);

        //划线
        int startX;
        int startY;
        int endX;
        int endY = 0;

        for (int i = 0; i < verCount; i++) {
            startX = leftMargin;
            startY = topMargin + offsetY*i;
            endX = startX + offsetX;
            endY = startY;
            canvas.drawLine(startX, startY, endX, endY, paint);
        }

        int left = leftMargin;
        for (int i = 0; i < horCount + 1; i++) {
            canvas.drawLine(left, topMargin, left, endY, paint);
            left += itemWidth;
        }

        //写日期
        int itemCount = verCount * (horCount - 1);
        String example = "aa";
        paint.getTextBounds(example, 0, 1, rect);
        int oneWordWidth = rect.width();
        paint.getTextBounds(example, 0, 2, rect);
        int twoWordWidth = rect.width();
        int verCount = 0;
        int horCount = 0;
        int textWidth;
        dates = computeDate();
        for (int i = 1; i <= itemCount; i++) {
            int index = i - 1;
            if (booleanArray != null && booleanArray.get(index)) {
                paint.setColor(Color.GREEN);
                int ver = index / 7;
                int hor = index % 7;
                int lef = hor * itemWidth + leftMargin;
                int top = ver * offsetY   + topMargin;
                canvas.drawBitmap(signBitmap, null, new Rect(lef, top, lef + itemWidth, top + offsetY), paint);
            }

            textWidth = Integer.valueOf(dates[i]) < 10? oneWordWidth : twoWordWidth;
            if(i <= delta || i > currMonth + 1) {
                paint.setColor(Color.GRAY);
            } else if (i == today) {
                paint.setColor(Color.RED);
            } else {
                paint.setColor(Color.BLUE);
            }

            if (i <= 7) {
                int textLeft = leftMargin + i * itemWidth - (itemWidth >>1) - (textWidth >> 1);
                int textTop = topMargin + ((offsetY + rect.height()) >> 1);
                canvas.drawText(dates[i], textLeft, textTop, paint);
            } else {
                if (i % 7 == 1) {
                    verCount++;
                    horCount = 1;
                } else {
                    horCount++;
                }
                int textLeft = leftMargin + horCount * itemWidth - (itemWidth >> 1) - (textWidth>>1);
                int textTop = topMargin + ((offsetY + rect.height()) >> 1) + verCount * offsetY;
                canvas.drawText(dates[i], textLeft, textTop, paint);
            }
        }

        //写菜单
        paint.setTextSize(menuTextSize);
        paint.setColor(Color.BLUE);
        paint.getTextBounds("Aaa", 0, 3, rect);
        int menuLeftMargin = leftMargin + ((itemWidth - rect.width()) >> 1);
        int menuTopMargin  = topMargin  - (rect.height() >> 1);
        for (int i = 0; i < 7; i++) {
            canvas.drawText(weeks[i], menuLeftMargin, menuTopMargin, paint);
            menuLeftMargin += itemWidth;
        }

        //标注selection
        if (selection != null && selection.length == 4 && selectionDate < dates.length) {
            String date = dates[selectionDate];
            paint.setTextSize(textSize);
            paint.getTextBounds(date, 0, date.length(), rect);
            paint.setColor(Color.RED);
            canvas.drawRect(selection[0], selection[1], selection[2], selection[3], paint);
            paint.setColor(Color.YELLOW);
            canvas.drawText(date, selection[0] + ((itemWidth - rect.width()) >> 1) , selection[1] + ((offsetY + rect.height()) >> 1) - 4, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (x < leftMargin || x > (leftMargin + itemWidth * horCount) || y < (topMargin) || y > (topMargin + offsetY * (verCount + 1))) {
            return false;
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                selection = new int[4];
                int hor = ((x - leftMargin) / itemWidth);
                int ver = ((y - topMargin) / offsetY);
                selectionDate = hor + 1 + (ver * horCount);
                Log.e("TAG", "hor:"+(hor+1)+","+"ver:"+ver);
                selection[0] = hor * itemWidth + leftMargin;
                selection[1] = ver * offsetY + topMargin;
                selection[2] = selection[0] + itemWidth;
                selection[3] = selection[1] + offsetY;
                if (onDateSelectionListener != null && selectionDate < dates.length) {
                    onDateSelectionListener.onDateSelect(dates[selectionDate]);
                }
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    public String[] computeDate() {
        String[] dates = new String[43];
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        today = c.get(Calendar.DAY_OF_MONTH) + 1;
        lastMonth = computeDays(year, month);
        currMonth = computeDays(year, month + 1);

        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        delta = c.get(Calendar.DAY_OF_WEEK) - 1;
        for (int i = 1; i <= 42; i++) {
            dates[i] = String.valueOf(1);
        }
        for (int i = 1; i <= (42 - delta); i++) {
            dates[i+ delta] = String.valueOf(i);
        }
        for (int i = 1; i <= delta; i++) {
            dates[i] = String.valueOf(lastMonth + 1 - i);
        }
        for (int i = currMonth + 2; i <= 42; i++) {
            dates[i] = String.valueOf(i - currMonth -1);
        }
        return dates;
    }

    public int computeDays(int year, int month) {
        int days = 31;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 2:
                days = ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) ? 29 :28;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
        }
        return days;
    }

    interface OnDateSelectionListener {
        void onDateSelect(String date);
    }

    public OnDateSelectionListener onDateSelectionListener;

    public void setOnDateSelectionListener(OnDateSelectionListener onDateSelectionListener) {
        this.onDateSelectionListener = onDateSelectionListener;
    }

    public void setCheckMap(SparseBooleanArray checkMap) {
        this.booleanArray = checkMap;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (signBitmap != null) {
            signBitmap.recycle();
        }
        super.onDetachedFromWindow();
    }
}
