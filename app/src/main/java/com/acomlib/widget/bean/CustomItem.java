package com.acomlib.widget.bean;


import com.acomlib.widget.CustomSpinner;

public class CustomItem implements CustomSpinner.CustomSpinnerItem {
    public String value;//选中item对应的值
    public String showItemStr;//显示的item
    public String value2;//备用

    public String getValue() {
        return value;
    }

    /**
     * 选中对应的值
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    public String getShowItemStr() {
        return showItemStr;
    }

    /**
     * 显示用来选择的item
     * @param showItemStr
     */
    public void setShowItemStr(String showItemStr) {
        this.showItemStr = showItemStr;
    }

    @Override
    public String getItemStr() {
        return showItemStr;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    @Override
    public String toString() {
        return "CustomItem{" +
                "value='" + value + '\'' +
                ", showItemStr='" + showItemStr + '\'' +
                ", value2='" + value2 + '\'' +
                '}';
    }
}
