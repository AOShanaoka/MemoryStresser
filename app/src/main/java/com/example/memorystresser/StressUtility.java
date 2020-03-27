package com.example.memorystresser;


import java.util.LinkedList;


public class StressUtility {

    public static long eatUpJavaHeap(LinkedList<byte[]> list, double eatUpSize, int atomSize) {
        long ret = 0;
        try {
            while(list.size()*atomSize < eatUpSize) {//list要素数×atomSize < eatUpSizeの間
                list.add(new byte[atomSize]);//atomSizeバイトlistに書き込む
                ret += atomSize;//atomSizeずつ追加する
            }
        } catch(Throwable t) {
            t.printStackTrace();
        }

        return ret;//retを返す
    }

}
