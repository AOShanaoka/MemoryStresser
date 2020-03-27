package com.example.memorystresser;


public class MemoryStressTask implements Runnable {

    private int memoryUsage;

    public MemoryStressTask(int i){
        memoryUsage = i;//指定した消費メモリ量を取得
    }

    @Override
    public void run() {
        try {

            String[] strs = StresserService.ServiceProcessName;//プロセス名を取得
            for (String id: strs)//取得したプロセスそれぞれで実行

            // プロセス名、消費メモリ量を指定しサービスを立ち上げる
            StresserService.startService(MyApplication.getInstance(), id, memoryUsage);

            Thread.sleep(100);
            Thread.yield();

        }catch (Exception e){

        }
    }
}
