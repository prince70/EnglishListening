package com.feng.englishlistening;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.Bmob;


/**
 * Created by prince70 on 2019/3/12.
 */
public class MyApplication extends Application {
    private List<Activity> mList = new LinkedList<Activity>();
    public static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        Bmob.initialize(this, "2c5cd74a1244ca8618b54d42166f04f1");

    }


    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mList.clear();
            System.exit(0);
        }
    }

    public static Activity getCurrentActivity(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.currentActivity();
    }

    public Activity currentActivity() {
        if (mList.size() == 0) {
            return null;
        }
        return mList.get(mList.size() - 1);
    }

    public void add(Activity activity) {
        mList.add(activity);
    }

    public void remove(Activity activity) {
        mList.remove(activity);
    }

    public static void gotoLoginActivity(Activity activity) {
        MyApplication application = (MyApplication) activity.getApplication();
        application.goLoginActivity(activity);
    }

    public void goLoginActivity(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
        finishOtherActivity(LoginActivity.class);
    }

    private void finishOtherActivity(Class<? extends Activity> clazs) {
        Activity activity;
        for (int i = 0; i < mList.size(); i++) {
            activity = mList.get(i);
            if (activity != null) {
                if (i == mList.size() - 1 && activity.getClass() == clazs) {
                } else {
                    activity.finish();
                }
            }
        }
    }

    public static void addActivity(Activity activity) {
        MyApplication application = (MyApplication) activity.getApplication();
        application.add(activity);
    }

    public static void removeActivity(Activity activity) {
        MyApplication application = (MyApplication) activity.getApplication();
        application.remove(activity);
    }

    public static void exitApp(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        application.exit();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base); //解决差分包的问题

    }

    /**
     * 内存回收
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

}
