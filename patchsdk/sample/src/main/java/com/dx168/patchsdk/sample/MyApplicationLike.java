package com.dx168.patchsdk.sample;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.dx168.patchsdk.IPatchManager;
import com.dx168.patchsdk.Listener;
import com.dx168.patchsdk.PatchManager;
import com.dx168.patchsdk.sample.tinker.SampleApplicationLike;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by jianjun.lin on 2016/10/31.
 */
@SuppressWarnings("unused")
@DefaultLifeCycle(application = "com.dx168.patchsdk.sample.MyApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class MyApplicationLike extends SampleApplicationLike {

    private static final String TAG = "MyApplicationLike";

    private OriginalApplication originalApplication;

    public MyApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
        originalApplication = new OriginalApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String appId = "20170215100259038-9119";
        String appSecret = "a57539a5d1a544d4b6ff7ec7ffa2bb4d";
        PatchManager.getInstance().init(getApplication(), "http://192.168.2.1:9011/", appId, appSecret, new IPatchManager() {
            @Override
            public void patch(Context context, String path) {
                TinkerInstaller.onReceiveUpgradePatch(context, path);
            }

            @Override
            public void cleanPatch(Context context) {
                TinkerInstaller.cleanPatch(context);
            }
        });
        PatchManager.getInstance().register(new Listener() {
            @Override
            public void onQuerySuccess(String response) {
                Log.i(TAG, "onQuerySuccess response=" + response);
            }

            @Override
            public void onQueryFailure(Throwable e) {
                Log.e(TAG, "onQueryFailure e=" + e);
            }

            @Override
            public void onDownloadSuccess(String path) {
                Log.i(TAG, "onDownloadSuccess path=" + path);
            }

            @Override
            public void onDownloadFailure(Throwable e) {
                Log.e(TAG, "onDownloadFailure e=" + e);
            }

            @Override
            public void onPatchSuccess() {
                Log.i(TAG, "onPatchSuccess");
            }

            @Override
            public void onPatchFailure(String error) {
                Log.e(TAG, "onPatchFailure=" + error);
            }

            @Override
            public void onLoadSuccess() {
                Log.i(TAG, "onLoadSuccess");
            }

            @Override
            public void onLoadFailure(String error) {
                Log.e(TAG, "onLoadFailure="+ error);
            }
        });
        PatchManager.getInstance().setTag("your tag");
        PatchManager.getInstance().queryAndPatch();
        originalApplication.onCreate();
    }

}
