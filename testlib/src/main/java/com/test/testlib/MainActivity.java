package com.test.testlib;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.acomlib.util.MLog;
import com.acomlib.widget.CustomSpinner;
import com.acomlib.widget.DateSelectViewUtil;
import com.acomlib.widget.TipUtils;
import com.acomlib.widget.bean.CustomItem;
import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.manager.DownloadManager;
import com.squareup.leakcanary.RefWatcher;
import com.test.testlib.emptyview.EmptyViewController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements EmptyViewController.onEmptyViewRefreshClick {

    @BindView(R.id.logBtn)
    Button logBtn;

    @BindView(R.id.spinner)
    CustomSpinner spinner;
    Button a;

    EmptyViewController emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        MLog.init(true, true);
        emptyView = new EmptyViewController(this, findViewById(R.id.content));


        TextView text1 = findViewById(R.id.text1);
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateSelectViewUtil.init(MainActivity.this)
                        .setOnDateSelectClick(DateSelectViewUtil.FORMAT_1, new DateSelectViewUtil.onDateSelectClick() {
                            @Override
                            public void onItemSelect(String date, View v) {
                                text1.setText(date);
                            }
                        }).show();
            }
        });

        spinner.attachDataSource(getJmItems());

        spinner.setOnItemSelectedListener(new CustomSpinner.OnItemSelectedListenerSpinner() {
            @Override
            public void onItemSelected(CustomSpinner parent, View view, int i) {
                TipUtils.showToast(jmItems.get(i).getShowItemStr());
                MLog.e(parent.getSelectItem() + parent.getCustomItem().getValue());
            }
        });



    }

    private DownloadManager manager;
    private String url = "https://f29addac654be01c67d351d1b4282d53.dd.cdntips.com/imtt.dd.qq.com/16891/DC501F04BBAA458C9DC33008EFED5E7F.apk?mkey=5d6d132d73c4febb&f=0c2f&fsname=com.estrongs.android.pop_4.2.0.2.1_10027.apk&csr=1bbd&cip=115.196.216.78&proto=https";


    @OnClick({R.id.logBtn,R.id.updateBtn,R.id.kongbaiBtn,R.id.netBtn,R.id.normalBtn,R.id.loadBtn,R.id.falseBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.kongbaiBtn:
                emptyView.showEmptyView();
                break;
            case R.id.netBtn:
                emptyView.showNetErrorView();
                emptyView.setRefreshClick(this);
                break;
            case R.id.falseBtn:
                emptyView.showNetFalseView("错误数据异常");
                break;
            case R.id.normalBtn:

                break;
            case R.id.loadBtn:
//                emptyView.showEmptyView();
                break;
            case R.id.updateBtn:
                UpdateConfiguration configuration = new UpdateConfiguration()
                        //输出错误日志
                        .setEnableLog(true)
                        //设置自定义的下载
                        //.setHttpManager()
                        //下载完成自动跳动安装页面
//                .setJumpInstallPage(true)
                        //设置对话框背景图片 (图片规范参照demo中的示例图)
                        //.setDialogImage(R.drawable.ic_dialog)
                        //设置按钮的颜色
                        //.setDialogButtonColor(Color.parseColor("#E743DA"))
                        //设置对话框强制更新时进度条和文字的颜色
//                .setDialogProgressBarColor(Color.parseColor("#E743DA"))
                        //设置按钮的文字颜色
                        .setDialogButtonTextColor(Color.WHITE)
                        //设置是否显示通知栏进度
                        .setShowNotification(true)
                        //设置是否提示后台下载toast
                        .setShowBgdToast(true)
                        //设置强制更新
                        .setForcedUpgrade(false)
                        //设置对话框按钮的点击监听
//                        .setButtonClickListener(this)
                        //设置下载过程的监听
//                        .setOnDownloadListener(this)
                        ;
                manager = DownloadManager.getInstance(MainActivity.this);
                manager.setApkName("ESFileExplorer.apk")
                        .setApkUrl(url)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setShowNewerToast(true)
                        .setConfiguration(configuration)
                        .setApkVersionCode(2)
                        .setApkVersionName("2.1.8")
                        .setApkSize("20.4")
                        .setApkDescription(getString(R.string.dialog_msg))
//                .setApkMD5("DC501F04BBAA458C9DC33008EFED5E7F")
                        .download();

                break;
        }
    }

    List<CustomItem> jmItems = null;

    public List<CustomItem> getJmItems() {
        jmItems = new ArrayList<>();

        CustomItem item1 = new CustomItem();
        item1.setValue("对应值1");
        item1.setShowItemStr("选项1");

        CustomItem item2 = new CustomItem();
        item2.setValue("对应值2");
        item2.setShowItemStr("选项2");

        CustomItem item3 = new CustomItem();
        item3.setValue("对应值3");
        item3.setShowItemStr("选项3");

        CustomItem item4 = new CustomItem();
        item4.setValue("对应值4");
        item4.setShowItemStr("选项4");

        CustomItem item5 = new CustomItem();
        item5.setValue("对应值5");
        item5.setShowItemStr("选项5");

        jmItems.add(item1);
        jmItems.add(item2);
        jmItems.add(item3);
        jmItems.add(item4);
        jmItems.add(item5);

        return jmItems;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MyApp.getRefWatcher(this);//1
        refWatcher.watch(this);
    }

    @Override
    public void onEmptyViewRefresh() {
        TipUtils.showToast("刷新了");
    }
}
