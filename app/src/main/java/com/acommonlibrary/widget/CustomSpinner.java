package com.acommonlibrary.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.acommonlibrary.util.EmptyUtils;
import com.acommonlibrary.widget.bean.CustomItem;
import com.acommonlibrary.R;

import java.util.List;

/**
 * 自定义Spinner
 */
public class CustomSpinner extends LinearLayout {

    private View view;
    private TextView tv_name;
    private RelativeLayout spinner_item;
    private ImageView ib;

    private List<CustomSpinnerItem> list;
    private List<String> listStr;
    //自定义适配器
    private MyAdapter mAdapter;
    //PopupWindow
    private PopupWindow pop;
    //是否显示PopupWindow，默认不显示
    private boolean isPopShow = true;
    private ListView listView;
    private LayoutInflater mInflater;
    private OnItemSelectedListenerSpinner onItemSelectedListener;
    private OnCusAdapter onCusAdapter;

    private int height;//popdialog 高
    private int postion = 0;//选中项

    public CustomSpinner(Context context) {
        super(context);
        initView(context);
    }

    public CustomSpinner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CustomSpinner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public static int setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        int ff = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        return ff;
    }

    private void initView(final Context context) {
        mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.custom_spinner, null);

        mAdapter = new MyAdapter();
        spinner_item = view.findViewById(R.id.spinner_item);
        tv_name = view.findViewById(R.id.et_name);
        ib = view.findViewById(R.id.spinner);
        spinner_item.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != list || listStr != null) {
                    if (pop == null) {
                        listView = new ListView(context);
                        listView.setCacheColorHint(0x00000000);
                        listView.setDividerHeight(0);
                        listView.setBackgroundColor(Color.rgb(255, 255, 255));
                        if (onCusAdapter != null) {
                            listView.setAdapter(onCusAdapter.getAdapter());
                        } else {
                            listView.setAdapter(mAdapter);
                        }

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                postion = i;
                                if (onCusAdapter != null) {
                                    onCusAdapter.getAdapter().notifyDataSetChanged();
                                    tv_name.setText(((CustomSpinnerItem) onCusAdapter.getAdapter().getItem(i)).getItemStr());
                                } else {
                                    mAdapter.notifyDataSetChanged();
                                    if (list != null) {
                                        tv_name.setText(list.get(i).getItemStr());
                                    } else if (listStr != null) {
                                        tv_name.setText(listStr.get(i));
                                    }
                                }

                                ib.setImageResource(R.mipmap.arrow_down_float);
                                pop.dismiss();
                                isPopShow = true;
                                CustomSpinner.this.view.setTag(getId());
                                if (onItemSelectedListener != null)
                                    onItemSelectedListener.onItemSelected(CustomSpinner.this, view, i);
                            }
                        });

                        if (height == 0) {
                            int hei = setListViewHeightBasedOnChildren(listView);
                            //这里设置下拉框的高度
                            if (hei >= 550) {
                                pop = new PopupWindow(listView, CustomSpinner.this.view.getWidth(), 550, true);
                            } else {
                                pop = new PopupWindow(listView, CustomSpinner.this.view.getWidth(), hei, true);
                            }
                        } else {
                            pop = new PopupWindow(listView, CustomSpinner.this.view.getWidth(), height, true);
                        }
                        pop.setBackgroundDrawable(new ColorDrawable(0x00000000));
                        pop.setFocusable(true);
                        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                isPopShow = true;
                                ib.setImageResource(R.mipmap.arrow_down_float);
                            }
                        });
                        ib.setImageResource(R.mipmap.arrow_down_float);
                        pop.showAsDropDown(view, 0, 0);
                        isPopShow = false;
                    } else {
                        if (isPopShow) {
                            ib.setImageResource(R.mipmap.arrow_up_float); //向上的箭头
                            pop.showAsDropDown(view, 0, 0);
                            isPopShow = false;
                        } else {
                            ib.setImageResource(R.mipmap.arrow_down_float); //向下的箭头
                            pop.dismiss();
                            isPopShow = true;
                        }
                    }
                }
            }
        });
        if (list == null && listStr == null) {
            tv_name.setText("");
        } else {
            if (list != null) {
                tv_name.setText(list.get(0).getItemStr());
            } else if (listStr != null) {
                tv_name.setText(listStr.get(0));
            }
        }

        addView(view);
    }

    public void attachDataSource(List itemList) {
        list = null;
        listStr = null;
        if (itemList == null || itemList.size() == 0) {
            tv_name.setText("未关联数据");
            return;
        }
        CustomSpinner.this.postion = 0;
        if (itemList.get(0) instanceof CustomSpinnerItem) {
            this.list = itemList;
            tv_name.setText(list.get(0).getItemStr());
        } else if (itemList.get(0).getClass().getName().equals("java.lang.String")) {
            this.listStr = itemList;
            tv_name.setText((String) itemList.get(0));
        }

    }

    public void setOnItemSelectedListener(OnItemSelectedListenerSpinner onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public void setCusAdapter(OnCusAdapter cusAdapter) {
        this.onCusAdapter = cusAdapter;
    }

    /**
     * 获取当前选中项文字信息
     *
     * @return
     */
    public String getSelectItem() {

        if (onCusAdapter != null) {
            return ((CustomSpinnerItem) onCusAdapter.getAdapter().getItem(CustomSpinner.this.postion)).getItemStr();
        } else {
            if (list != null) {
                return list.get(CustomSpinner.this.postion).getItemStr();
            } else if (listStr != null) {
                return listStr.get(CustomSpinner.this.postion);
            }
        }
        return "";
    }

    /**
     * 获取当前选中对象
     *
     * @return
     */
    public Object getSelectObject() {

        if (onCusAdapter != null) {
            return onCusAdapter.getAdapter().getItem(CustomSpinner.this.postion);
        } else {
            if (list != null) {
                return list.get(CustomSpinner.this.postion);
            } else if (listStr != null) {
                return listStr.get(CustomSpinner.this.postion);
            }
        }
        return null;
    }

    /**
     * 获取默认转换对象
     * @return
     */
    public CustomItem getCustomItem(){
        return (CustomItem)getSelectObject();
    }

    /**
     * 设置下拉框高度 默认550
     *
     * @param popHeight
     */
    public void setSpinnerHeight(int popHeight) {
        this.height = popHeight;
    }

    /**
     * 设置选中项
     *
     * @param index
     */
    public void setSelectedIndex(int index) {
        if (EmptyUtils.isNotEmpty(list)) {
            tv_name.setText(list.get(index).getItemStr());
        } else if (EmptyUtils.isNotEmpty(listStr)) {
            tv_name.setText(listStr.get(index));
        }
        if(EmptyUtils.isNotEmpty(onItemSelectedListener))
            onItemSelectedListener.onItemSelected(null, null, index);
    }

    @Override
    public void destroyDrawingCache() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
        }
        super.destroyDrawingCache();
    }

    /**
     * 自定义item类 必须 实现该类
     */
    public interface CustomSpinnerItem {
        String getItemStr();//显示的选择项
    }

    public interface OnItemSelectedListenerSpinner {
        void onItemSelected(CustomSpinner parent, View view, int i);
    }


    public interface OnCusAdapter {
        BaseAdapter getAdapter();
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            int count = 0;
            if (list != null) {
                count = list.size();
            } else if (listStr != null) {
                count = listStr.size();
            }
            return count;
        }

        @Override
        public Object getItem(int position) {
            Object object = null;
            if (list != null) {
                object = list.get(position);
            } else if (listStr != null) {
                object = listStr.get(position);
            }
            return object;

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
//            View view = mInflater.inflate(R.layout.item, null);
            View view = mInflater.inflate(R.layout.custom_spinner_item, null);
            final TextView item_name = view.findViewById(R.id.spinner_item_name);

            if (position == CustomSpinner.this.postion) {
                //选中条目的背景色
                view.setBackgroundColor(getResources().getColor(R.color.base_status_bar));
                item_name.setTextColor(getResources().getColor(R.color.white));
            }

            if (list != null) {
                item_name.setText(list.get(position).getItemStr());
            } else if (listStr != null) {
                item_name.setText(listStr.get(position));
            }

            //设置按钮的监听事件
            view.setTag(item_name);
            return view;
        }

    }

}
