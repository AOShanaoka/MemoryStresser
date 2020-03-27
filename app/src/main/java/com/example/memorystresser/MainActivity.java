package com.example.memorystresser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    TextView availmem;//使用可能メモリ量
    TextView eatenMem;//このアプリによる消費メモリ量

    //メモリ情報の取得に使用
    ActivityManager aManager;
    ActivityManager.MemoryInfo mInfo;

    boolean isRunning;//startボタン、stopボタンの二度押しによるエラー回避用


    //使用中のメモリ量を表示する
    final Handler handler = new Handler();
    final Runnable r = new Runnable() {
        @Override
        public void run() {
            getMemoryString();//使用可能なメモリ量を取得する
            getEatenMemory();//このアプリによって消費されるメモリの量を取得する
            handler.postDelayed(this, 100);//0.1秒ごとに繰り返し
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler.post(r);//UIスレッドに表示
        Button startButton = findViewById(R.id.start);
        Button stopButton = findViewById(R.id.stop);
        availmem = findViewById(R.id.availmem);
        eatenMem = findViewById(R.id.eatenmem);
        final EditText editText = findViewById(R.id.edittext);
        isRunning = false;
        aManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);//activitymanagerを取得

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().equals("") && !isRunning){//空欄でなく、かつ実効中でない場合
                    int i = parseInt(editText.getText().toString());//入力された数値を取得
                    MemoryStressTask task = new  MemoryStressTask(i);//入力された値を引数にとりMemoryStressTaskを生成
                    task.run();//実行
                    isRunning = true;//実行中へ変更
                }

            }
        });

        stopButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //実行中の場合
              if(isRunning){

                  //StopStressTaskを実行
                    StopStressTask task = new StopStressTask();
                    task.run();
                    isRunning = false;//実行中でない
               }

            }
        });
    }

    protected void onResume(){
        super.onResume();
    }

    protected void onPause(){
        super.onPause();
    }

    //使用可能なメモリ量を取得
    private void getMemoryString(){

        mInfo = new ActivityManager.MemoryInfo();
        aManager.getMemoryInfo(mInfo);//メモリ情報を取得
        String str = "memoryInfo.availMem[MB] = " + (int)(mInfo.availMem/1024/1024);//使用可能なメモリをMBに換算
        availmem.setText(str);//画面に表示
    }

    //このアプリによって消費されているメモリ量を取得
    // https://sites.google.com/site/camelsoft00/androidmemo-1/memorino-qu-de-fang-fa　を参考
        private void getEatenMemory(){
            List<ActivityManager.RunningServiceInfo> services = aManager.getRunningServices(100);//実行中のサービスを取得

            int totalPss = 0;


            //実行中サービスが存在する場合
            if (services != null) {
                int[] pids = new int[services.size()];//実行中サービスのプロセスIDを入れる配列

                for (int i = 0; i < services.size(); ++i) {
                    ActivityManager.RunningServiceInfo service = services.get(i);//i番目のサービス

                    //サービスのパッケージ名にこのアプリのパッケージ名を含む場合
                    if(service.service.getPackageName().contains("com.example.memorystresser")){
                        pids[i] = service.pid; //配列に該当サービスのプロセスIDを追加
                    }
                }

                android.os.Debug.MemoryInfo[] memInfos = aManager.getProcessMemoryInfo(pids);//各プロセスのメモリ情報を取得
                for (android.os.Debug.MemoryInfo info : memInfos) {
                    totalPss += info.getTotalPss();//各プロセスのtotalPssを加算
                }
                String str = "memory usage[MB] = " + (totalPss/1024);//MBに換算
                eatenMem.setText(str);//画面に表示。
            }
        }

}
