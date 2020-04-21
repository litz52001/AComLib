package com.acomlib.widget.emptyview;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acomlib.R;

/**
 * 空页面控制器  layoutid
 * emptyView = new EmptyViewController(this, findViewById(R.id.content));
 * showEmptyView()
 * showNetErrorView()
 * emptyView.setRefreshClick(this);
 * emptyView.restoreView();恢复原视图
 */
public class EmptyViewController {
    private Context mContext;
    private CommonEmptyHelper mHelper;
    /**
     * @param targetView 需要展示需要替换为empty界面的同级布局(比如listview)
     */
    public EmptyViewController(Activity context,View targetView) {
        if (targetView == null) {
            throw new IllegalArgumentException("You must return a right target view for empty view");
        }
        this.mContext = context;
        this.mHelper = new CommonEmptyHelper(targetView);
    }

    /**
     * 暂无数据
     */
    public void showEmptyView() {
        setRefreshClick(null);
        showEmptyView(mContext.getResources().getString(R.string.emptyview_none_data),R.mipmap.emptyview_nodata);
    }

    /**
     * 网络异常
     */
    public void showNetErrorView() {
        showEmptyView(mContext.getResources().getString(R.string.emptyview_net_error),R.mipmap.emptyview_netstatus);
    }

    /**
     * 请求失败 提示信息
     *
     */
    public void showNetFalseView(String falseMsg) {
        setRefreshClick(null);
        showEmptyView(falseMsg, R.mipmap.emptyview_error);
    }

    /**
     *  通用模板
     */
    public void showEmptyView(String subtitle,int imgId) {
        View emptyView = getDefaultEmptyView();
        if (emptyView instanceof DefaultEmptyView) {
            if (!TextUtils.isEmpty(subtitle)) {
                ((DefaultEmptyView) emptyView).setEmptyText(subtitle);
                ((DefaultEmptyView) emptyView).setEmptyIcon(imgId);
            }
        }

        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onRefreshClick != null) {
                    onRefreshClick.onEmptyViewRefresh();
                }
            }
        });

        showLayout(emptyView);
    }

    /**
     * 显示自定义的空页面
     */
    public void showLayout(View emptyLayout) {
        mHelper.showLayout(emptyLayout);
    }
    /**
     * 返回默认空数据layout
     */
    private View getDefaultEmptyView() {
        DefaultEmptyView emptyDefaultView = (DefaultEmptyView) LayoutInflater.from(mContext).inflate(R.layout.com_emptyview, null);
        emptyDefaultView.setEmptyText(R.string.emptyview_none_data);
        return emptyDefaultView;
    }
    /**
     * 恢复之前的视图
     */
    public void restoreView() {
        setRefreshClick(null);
        mHelper.restoreView();
    }

    private onEmptyViewRefreshClick onRefreshClick;
    public void setRefreshClick(onEmptyViewRefreshClick click){
        onRefreshClick = click;
    }

    public interface onEmptyViewRefreshClick{
        void onEmptyViewRefresh();
    }



    private class CommonEmptyHelper {
        private View targetView;
        private ViewGroup parentView;
        private int viewIndex;
        private ViewGroup.LayoutParams params;
        private View currentView;
        private CommonEmptyHelper(View view) {
            super();
            this.targetView = view;
        }
        private void init() {
            params = targetView.getLayoutParams();
            //找到被替换view的父视图
            if (targetView.getParent() != null) {
                parentView = (ViewGroup) targetView.getParent();
            } else {
                parentView = (ViewGroup) targetView.getRootView().findViewById(android.R.id.content);
            }
            //找到该view在父视图中的索引
            int count = parentView.getChildCount();
            for (int index = 0; index < count; index++) {
                if (targetView == parentView.getChildAt(index)) {
                    viewIndex = index;
                    break;
                }
            }
            currentView = targetView;
        }
        public View getCurrentLayout() {
            return currentView;
        }

        private void restoreView() {
            showLayout(targetView);
        }
        private void showLayout(View emptyView) {
            if (parentView == null) {
                init();
            }
            this.currentView = emptyView;
            //如果已经是那个view，那就不需要再进行替换操作了
            if (parentView.getChildAt(viewIndex) != emptyView) {
                ViewGroup parent = (ViewGroup) emptyView.getParent();
                if (parent != null) {
                    parent.removeView(emptyView);
                }
                parentView.removeViewAt(viewIndex);
                parentView.addView(emptyView, viewIndex, params);
            }
        }
        public View inflate(int layoutId) {
            return LayoutInflater.from(targetView.getContext()).inflate(layoutId, null);
        }
        public Context getContext() {
            return targetView.getContext();
        }
        public View getView() {
            return targetView;
        }
    }
}
