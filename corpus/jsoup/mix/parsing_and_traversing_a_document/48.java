package com.asura.android_study.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.asura.android_study.R;
import com.asura.android_study.activity.base.BasePermissionActivity;
import com.asura.android_study.activity.behavior.CoordinatorLayoutActivity;
import com.asura.android_study.activity.behavior.UCBehaviorActivity;
import com.asura.android_study.activity.constranitlayout.ConstraintLayoutActivity;
import com.asura.android_study.activity.constranitlayout.ConstraintLayoutTransitionActivity;
import com.asura.android_study.activity.eventbus.SubscribeActivity;
import com.asura.android_study.activity.fragtofrag.Fragment2Activity;
import com.asura.android_study.activity.itemtype.ItemTypeActivity;
import com.asura.android_study.activity.threadpool.threadpool.ThreadPoolActivity;
import com.asura.android_study.activity.viewpager.ViewPagerActivity;
import com.asura.android_study.adapter.MailAppAdapter;
import com.asura.android_study.adapter.ScrollerAdapter;
import com.asura.android_study.receiver.NetWorkReceiver;
import com.asura.android_study.service.music.MessengerActivity;
import com.asura.android_study.service.music.MusicActivity;
import com.asura.android_study.utils.FontHelper;
import com.asura.android_study.view.CameraLiveWallpaper;
import com.asura.android_study.view.HorizontalListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BasePermissionActivity {

    @BindView(R.id.btn_open_qq)
    Button mBtnOpenQq;
    @BindView(R.id.btn_open_email)
    Button mBtnOpenEmail;
    @BindView(R.id.btn_open_email_app)
    Button mBtnOpenEmailApp;
    @BindView(R.id.btn_set_wallpaper)
    Button mBtnSetWallpaper;
    @BindView(R.id.btn_music_bind_service)
    Button mBtnMusicBindService;
    @BindView(R.id.btn_messenger_service)
    Button mBtnMessengerService;
    @BindView(R.id.tv_drawable)
    TextView mTvDrawable;
    @BindView(R.id.hlscrol)
    HorizontalListView mHlscrol;
    @BindView(R.id.btn_rxJava)
    Button mBtnRxJava;
    @BindView(R.id.btn_leafAnim)
    Button mBtnLeafAnim;
    @BindView(R.id.btn_coordinatorLayout)
    Button mBtnCoordinatorLayout;
    @BindView(R.id.btn_UCBehaviorActivity)
    Button mBtnUCBehaviorActivity;
    @BindView(R.id.btn_fragment_activity)
    Button mBtnFragmentActivity;
    @BindView(R.id.btn_item_type)
    Button mBtnItemType;
    @BindView(R.id.btn_viewpager)
    Button mBtnViewpager;
    @BindView(R.id.btn_bottom_nav)
    Button mBtnBottomNav;
    @BindView(R.id.btn_custom_data)
    Button mBtnCustomData;
    @BindView(R.id.font_content)
    LinearLayout mFontContent;
    @BindView(R.id.btn_constraintLayout)
    Button mBtnConstraintLayout;
    @BindView(R.id.btn_constraint_transition)
    Button mBtnConstraintTransition;
    @BindView(R.id.btn_threadPool1)
    Button mBtnThreadPool1;
    @BindView(R.id.btn_threadPool2)
    Button mBtnThreadPoo2;
    @BindView(R.id.btn_event_bus)
    Button mBtnEventBus;

    private NetWorkReceiver mNetWorkReceiver;
    private ScrollerAdapter mScrollerAdapter;

    @Override
    public int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        FontHelper.injectFont(mFontContent);
        mNetWorkReceiver = new NetWorkReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetWorkReceiver, intentFilter);


        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            strings.add("新开关" + i);
        }
        mScrollerAdapter = new ScrollerAdapter(MainActivity.this, strings);
        mHlscrol.setAdapter(mScrollerAdapter);
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        drawable.setBounds(new Rect(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()));
        mTvDrawable.setCompoundDrawables(drawable, null, null, null);

        checkSelfPermission();
    }

    @OnClick({R.id.btn_open_qq, R.id.btn_open_email, R.id.btn_open_email_app, R.id.btn_set_wallpaper,
            R.id.btn_music_bind_service, R.id.btn_messenger_service, R.id.btn_rxJava, R.id.btn_leafAnim,
            R.id.btn_coordinatorLayout, R.id.btn_UCBehaviorActivity, R.id.btn_fragment_activity,
            R.id.btn_item_type, R.id.btn_viewpager, R.id.btn_bottom_nav, R.id.btn_custom_data,
            R.id.btn_constraintLayout, R.id.btn_constraint_transition, R.id.btn_threadPool1,
            R.id.btn_threadPool2, R.id.btn_event_bus})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_open_qq:
                openQQ(MainActivity.this);
                break;
            case R.id.btn_open_email:
                openMailAppSend();
                break;
            case R.id.btn_open_email_app:
                openMailAppReceive();
                break;
            case R.id.btn_set_wallpaper:
                setTransparentWallpaper();
                startWallpaper();
                break;
            case R.id.btn_music_bind_service:
                startActivity(new Intent(MainActivity.this, MusicActivity.class));
                break;
            case R.id.btn_messenger_service:
                startActivity(new Intent(MainActivity.this, MessengerActivity.class));
                break;
            case R.id.btn_rxJava:
                mHlscrol.scrollTo(mHlscrol.getChildAt(0).getWidth() * 10);
                startActivity(new Intent(MainActivity.this, RxJavaActivity.class));
//                finish();
                break;
            case R.id.btn_leafAnim:
                mHlscrol.scrollTo(mHlscrol.getChildAt(0).getWidth() * 20);
                startActivity(new Intent(MainActivity.this, LeafLoadingActivity.class));
//                finish();
                break;
            case R.id.btn_coordinatorLayout:
                startActivity(new Intent(MainActivity.this, CoordinatorLayoutActivity.class));
                break;
            case R.id.btn_UCBehaviorActivity:
                startActivity(new Intent(MainActivity.this, UCBehaviorActivity.class));
                break;
            case R.id.btn_fragment_activity:
                startActivity(new Intent(MainActivity.this, Fragment2Activity.class));
                break;
            case R.id.btn_item_type:
                startActivity(new Intent(MainActivity.this, ItemTypeActivity.class));
                break;
            case R.id.btn_viewpager:
                startActivity(new Intent(MainActivity.this, ViewPagerActivity.class));
                break;
            case R.id.btn_bottom_nav:
                startActivity(new Intent(MainActivity.this, BottomNavActivity.class));
                break;
            case R.id.btn_custom_data:
                startActivity(new Intent(MainActivity.this, CustomDataActivity.class));
                break;
            case R.id.btn_constraintLayout:
                startActivity(new Intent(MainActivity.this, ConstraintLayoutActivity.class));
                break;
            case R.id.btn_constraint_transition:
                startActivity(new Intent(MainActivity.this, ConstraintLayoutTransitionActivity.class));
                break;
            case R.id.btn_threadPool1:
                startActivity(new Intent(MainActivity.this, com.asura.android_study.activity.threadpool.step1.MainActivity.class));
                break;
            case R.id.btn_threadPool2:
                startActivity(new Intent(MainActivity.this, ThreadPoolActivity.class));
                break;
            case R.id.btn_event_bus:
                startActivity(new Intent(MainActivity.this, SubscribeActivity.class));
                break;
            default:
        }
    }

    private void checkSelfPermission() {
        requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.CAMERA});
    }

    @Override
    public void onPermissionResult(boolean isAllow) {
        if (isAllow) {
            Toast.makeText(MainActivity.this, "权限ok", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "权限不ok", Toast.LENGTH_SHORT).show();
        }
    }

    private void startWallpaper() {
        final Intent pickWallpaper = new Intent(Intent.ACTION_SET_WALLPAPER);
        Intent chooser = Intent.createChooser(pickWallpaper, "选择壁纸");
        startActivity(chooser);
    }

    /**
     * 设置透明壁纸
     */
    private void setTransparentWallpaper() {
        startService(new Intent(MainActivity.this, CameraLiveWallpaper.class));
    }

    /**
     * 打开QQ
     *
     * @param context
     */
    public static void openQQ(Context context) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(new ComponentName("com.tencent.mobileqq",
                    "com.tencent.mobileqq.activity.SplashActivity"));
            //打开自带邮箱App
//            intent.setComponent(new ComponentName("com.android.email",
//                    "com.android.email.activity.EmailActivity"));
            if (!(context instanceof Activity)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "未安装QQ", Toast.LENGTH_SHORT).show();
        }
    }

    public void openMailAppSend() {
        Uri uri = Uri.parse("mailto:");
//        Uri uri = Uri.parse("http:");//选择浏览器App
        Intent intent = new Intent(Intent.ACTION_VIEW, null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(uri);
        startActivity(Intent.createChooser(intent, "请选择邮箱App"));
    }

    public void openMailAppReceive() {
        PackageManager pm = getPackageManager();
        Uri uri = Uri.parse("mailto:");
        Intent intent = new Intent(Intent.ACTION_VIEW, null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(uri);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        // 调用系统排序 ， 根据name排序
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));
//        final ArrayList<String> stringArrayList = new ArrayList<>();
        final ArrayList<Intent> intents = new ArrayList<>();
        for (ResolveInfo reInfo : resolveInfos) {
            String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
//            String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
//            Drawable icon = reInfo.loadIcon(pm); // 获得应用程序图标
            Intent appIntent = pm.getLaunchIntentForPackage(pkgName);
            intents.add(appIntent);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(new MailAppAdapter(this, resolveInfos), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = intents.get(which);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetWorkReceiver);
    }

}
