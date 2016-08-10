package com.example.exerciseapp.aty.activityrun;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * 设备管理消息接收类
 *
 * @author wangwei
 */
public class AdminReceiver extends DeviceAdminReceiver {

    /**
     * 打印日志信息
     *
     * @param msg 要打印信息
     */
    private void log(String msg) {
        Log.i("AdminReceiver", msg);
    }

    /**
     * 显示提示信息
     *
     * @param context
     * @param msg
     */
    private void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public DevicePolicyManager getManager(Context context) {
        log("调用了getManager方法");
        return super.getManager(context);
    }

    @Override
    public ComponentName getWho(Context context) {
        log("调用了getWho方法");
        return super.getWho(context);
    }

    /**
     * 禁用
     */
    @Override
    public void onDisabled(Context context, Intent intent) {
        log("调用了onDisabled方法");
        showToast(context, "禁用设备管理");
        super.onDisabled(context, intent);
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        log("调用了onDisableRequested方法");
        return super.onDisableRequested(context, intent);
    }

    /**
     * 激活
     */
    @Override
    public void onEnabled(Context context, Intent intent) {
        log("调用了onEnabled方法");
        showToast(context, "启动设备管理");
        super.onEnabled(context, intent);
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        log("调用了onPasswordChanged方法");
        super.onPasswordChanged(context, intent);
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        log("调用了onPasswordFailed方法");
        super.onPasswordFailed(context, intent);
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        log("调用了onPasswordSucceeded方法");
        super.onPasswordSucceeded(context, intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        log("调用了onReceive方法");
        super.onReceive(context, intent);
    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        log("调用了peekService方法");
        return super.peekService(myContext, service);
    }
}