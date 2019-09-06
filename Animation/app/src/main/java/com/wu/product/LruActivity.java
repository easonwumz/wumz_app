package com.wu.product;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * @Description: description
 * @Author: wumz
 * @CreateDate: 2019/8/20 11:17
 */
public class LruActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        long maxMemory = Runtime.getRuntime().maxMemory();
        Log.e("wu", "【LruActivity】maxMemory:" + maxMemory);
        int cacheSize = (int) (maxMemory / 8);
        Log.e("wu", "【LruActivity】cacheSize:" + cacheSize);
        LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(@NonNull String key, @NonNull Bitmap value) {
                return value.getByteCount();
            }
        };

    }
}
