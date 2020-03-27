package com.example.memorystresser;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

public abstract class StresserService extends Service {
//    public static final String START_SERVICE = "start";
//    public static final String STOP_SERVICE = "stop";

    public static String ID_00 = "no00";
    public static String ID_01 = "no01";
    public static String ID_02 = "no02";
    public static String ID_03 = "no03";
    public static String ID_04 = "no04";
    public static String ID_05 = "no05";
    public static String ID_06 = "no06";
    public static String ID_07 = "no07";
    public static String ID_08 = "no08";
    public static String ID_09 = "no09";
    public static String ID_10 = "no10";
    public static String ID_11 = "no11";
    public static String ID_12 = "no12";
    public static String ID_13 = "no13";
    public static String ID_14 = "no14";
    public static String ID_15 = "no15";
    public static String ID_16 = "no16";
    public static String ID_17 = "no17";
    public static String ID_18 = "no18";
    public static String ID_19 = "no19";
    public static String ID_20 = "no20";
    public static String ID_21 = "no21";
    public static String ID_22 = "no22";
    public static String ID_23 = "no23";
    public static String ID_24 = "no24";
    public static String ID_25 = "no25";
    public static String ID_26 = "no26";
    public static String ID_27 = "no27";
    public static String ID_28 = "no28";
    public static String ID_29 = "no29";
    public static String ID_30 = "no30";
    public static String ID_31 = "no31";

    public static final String CHANNEL_ID = "ForegroundServiceChannel";


    public final static String[] ServiceProcessName = new String[]{
            ID_00,ID_01, ID_02, ID_03,
            ID_04,ID_05, ID_06, ID_07,
            ID_08,ID_09, ID_10, ID_11,
            ID_12,ID_13, ID_14, ID_15,
            ID_16,ID_17, ID_18, ID_19,
            ID_20,ID_21, ID_22, ID_23,
            ID_24,ID_25, ID_26, ID_27,
            ID_28,ID_29, ID_30, ID_31,
    };

    //実際にメモリを消費するクラス
    public final static Class[] JavaHeapEater = new Class[] {
            Stresser0.class, Stresser1.class, Stresser2.class,
            Stresser3.class, Stresser4.class, Stresser5.class,
            Stresser6.class, Stresser7.class, Stresser8.class,
            Stresser9.class, Stresser10.class, Stresser11.class,
            Stresser12.class, Stresser13.class, Stresser14.class,
            Stresser15.class, Stresser16.class, Stresser17.class,
            Stresser18.class, Stresser19.class, Stresser20.class,
            Stresser21.class, Stresser22.class, Stresser23.class,
            Stresser24.class, Stresser25.class, Stresser26.class,
            Stresser27.class, Stresser28.class, Stresser29.class,
            Stresser30.class, Stresser31.class,
    };

    private Thread mTh = null;
    public Object mHeap = null;
    public int memoryUsage;//入力値受け渡し用


    //idからJavaHeapEaterのclassを取得
    public static Class getClassFromID(String id) {
        int len = ServiceProcessName.length;


        for(int i=0;i<len;i++) {
            if(ServiceProcessName[i].equals(id)) {
                return JavaHeapEater[i];//idと合致したJavaHeapEaterを返す。
            }
        }
        return null;
    }

    public static Intent startService(Context context, String id, int memoryUsage){
        Class clazz = getClassFromID(id);//idからclassを取得

        //該当するclassをstartさせる
        Intent startIntent = new Intent(context, clazz);
        startIntent.putExtra("memoryUsage",memoryUsage);//最初に指定したメモリ消費量を受け渡す

        //Foreground Serviceを開始する
        //バージョンによる切り分け
        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(startIntent);
        } else {
            context.startService(startIntent);
        }
        return startIntent;
    }

    public static Intent StopService(Context context, String id){
        Class clazz = getClassFromID(id);//idからclassを取得
        //サービスを終了
        Intent intent = new Intent(context,clazz);
        context.stopService(intent);
        return intent;

    }


    @Override
    public void onCreate() {

        android.util.Log.v("kiyo","onCreate()"+getProperty());
        super.onCreate();

    }





    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        memoryUsage = intent.getIntExtra("memoryUsage", 1000);//指定したメモリ消費量を取得


        //mTh、mHeapが存在しない時
        if (mTh == null || mHeap == null){
           mTh = new MyStarter((EatUpJavaHeapTask)(mHeap = new EatUpJavaHeapTask(null, memoryUsage)));
//            mTh = new Thread((EatUpJavaHeapTask)(mHeap = new EatUpJavaHeapTask(null, memoryUsage)));
        }
        mTh.start();//EatUpJavaHeapTaskを実行する

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }


        //　通知の作成　https://androidwave.com/foreground-service-android-example/ を参考

        //notificationの作成
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        //バージョンによる切り分け
        if (Build.VERSION.SDK_INT >= 26) {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Foreground Service")
//                .setContentText(input)
                    .setSmallIcon(R.drawable.notification)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(1, notification);
            return START_STICKY;

        }else{
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Foreground Service")
//                .setContentText(input)
//                    .setSmallIcon(R.drawable.notification)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(1, notification);
            return START_STICKY;
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mTh !=null && mTh.isAlive()) {
            mTh.interrupt();//threadを中断
            mTh = null;
        }

    }

    /*
    * もとのコードから不要な部分を削除したもの。
    * 無駄が多いのですが、簡略化できず。
    * */

    public class MyStarter extends Thread {
        public MyStarter(Runnable runnable) {
            super(runnable);
        }

        @Override
        public void run() {
            try {
                super.run();
//                DeadOrAliveTask task = new DeadOrAliveTask(StresserService.this, memoryUsage);
                try {
                    while(mTh==Thread.currentThread()){
//                        task.run();
                        Thread.sleep(3000);
                    }
                }catch(InterruptedException e) {

                }
            } finally {

            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {return null;}

    public abstract String getProperty();


   // 通知に関するメソッド。 https://androidwave.com/foreground-service-android-example/ より。
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }







}
