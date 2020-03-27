package com.example.memorystresser;

public class StopStressTask implements Runnable {

    @Override
    public void run() {
        try {
            String[] strs = StresserService.ServiceProcessName;//プロセス名を取得
            for (String id : strs)//各プロセスに対して実行
            StresserService.StopService(MyApplication.getInstance(),id);//サービスを終了する
            Thread.sleep(100);
            Thread.yield();

        }catch(Exception e){

        }




    }
}
