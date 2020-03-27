package com.example.memorystresser;

import java.lang.ref.WeakReference;
import android.content.Context;


//参考にしたアプリに搭載されており、今のアプリに合わせて調整もしたが、今回は未使用

public class DeadOrAliveTask  implements Runnable {

    private WeakReference<Context> mRef = null;
    int i;

    public DeadOrAliveTask(Context context, int memoryUsage) {
        mRef = new WeakReference<Context>(context);//弱参照を生成
        i = memoryUsage;//指定した消費メモリ量
    }

    public void run() {
        try {
            for (int i=0;i<31;i++) {
                task(StresserService.JavaHeapEater[i], StresserService.ServiceProcessName[i]);//各JavaHeapEaterに対して実行
                Thread.sleep(10);
                Thread.yield();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void task(Class clazz, String processName) {
        Context con = mRef.get();
        if(con==null) {
            return;
        }
        StresserService.startService(MyApplication.getInstance(), processName, i);//JavaHeapEaterを再起動

    }
}
