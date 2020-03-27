package com.example.memorystresser;

import java.util.LinkedList;


public class EatUpJavaHeapTask implements Runnable {
    private LinkedList<byte[]> mBuffer = new LinkedList<byte[]>();
    public double mEatUpSize;//1JavaHeapEaterあたりのメモリ消費量
    public static int mAtomSize = 1024*4;//ループ1回あたりに取得するメモリ量


    public EatUpJavaHeapTask(LinkedList<byte[]> buffer, int memoryUsage) {
        if(buffer == null) {
            buffer = new LinkedList<byte[]>();
        }


//        double a = 0.1288;
//        double b = 38.121;
//        double c = 286.88;
//        double d = memoryUsage-c;
//        mEatUpSize = (b-Math.sqrt(b*b-4*a*d))*1024*1024/(2*a);

        //aとbを変えることで微調整が可能。
        //何度かアプリを動かし得られた値をプロットし、近似した一次関数を使用
        double a = 32.742;
        double b = 334.68;
        mEatUpSize = (double)(memoryUsage-b)/a*1024*1024;//方程式を解く
        mBuffer = buffer;
    }


    @Override
    public void run() {
        long eatup = 0;
        try {
            while(true) {
                //StressUtilityを実行し得られた値をeatupに追加
                //ここは無限ループだが、StressUtility.eatUpJavaHeapは指定した値を超えると何も行わない。
                eatup += StressUtility.eatUpJavaHeap(mBuffer, mEatUpSize, mAtomSize);
                Thread.sleep(500);
            }
         } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}