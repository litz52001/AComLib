package com.acomlib.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间选择工具类 调用方法
 * DateSelectViewUtil.init(MainActivity.this)
 * .setOnDateSelectClick(DateSelectViewUtil.FORMAT_1,new DateSelectViewUtil.onDateSelectClick() {
 *
 * @Override public void onItemSelect(String date, View v) {
 * tv_data.setText(date);
 * }
 * }).show();
 */
public class DateSelectViewUtil {

    private static DateSelectViewUtil dateView;
    public static onDateSelectClick itemClick;
    private static TimePickerBuilder pvTime;
    private static String DATEFORMAT = "";

    public static final String FORMAT_1 = "yyyy年MM月dd日 hh时mm分";
    public static final String FORMAT_2 = "yyyy年MM月dd日";
    public static final String FORMAT_3 = "yyyy年MM月";
    public static final String FORMAT_4 = "yyyy年";
    public static final String FORMAT_5 = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_6 = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_7 = "yyyy-MM-dd";

    public static DateSelectViewUtil init(Context context) {
        if (dateView == null) dateView = new DateSelectViewUtil();

        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2017, 1, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2028, 1, 29);
        pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                if (TextUtils.isEmpty(DATEFORMAT)) {
                    SimpleDateFormat format = new SimpleDateFormat(FORMAT_7);
                    itemClick.onItemSelect(format.format(date), v);
                } else {
                    SimpleDateFormat format = new SimpleDateFormat(DATEFORMAT);
                    itemClick.onItemSelect(format.format(date), v);
                }
            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setContentTextSize(22)
                .setLineSpacingMultiplier(1.7f)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF138AEB)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒");

        return dateView;
    }

    /**
     * new boolean[]{true, true, true, false, false, false}
     *             对应"年", "月", "日", "时", "分", "秒"
     * @param type true为显示
     * @return
     */

    public DateSelectViewUtil setType(boolean[] type) {
        pvTime.setType(type);
        return dateView;
    }


    public void show() {
        pvTime.build().show();
    }

    /**
     * 默认返回 "yyyy-MM-dd"
     *
     * @param click
     * @return
     */
    public DateSelectViewUtil setOnDateSelectClick(onDateSelectClick click) {
        this.itemClick = click;
        return dateView;
    }

    /**
     * @param formatStr 传返回时间格式
     * @param click
     * @return
     */
    public DateSelectViewUtil setOnDateSelectClick(String formatStr, onDateSelectClick click) {
        this.itemClick = click;
        DATEFORMAT = formatStr;
        return dateView;
    }

    public interface onDateSelectClick {
        void onItemSelect(String date, View v);
    }
}
