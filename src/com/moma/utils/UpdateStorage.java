package com.moma.utils;

import com.moma.FFMR.MMFR;

public class UpdateStorage {


    //更新mmfr
    public void run(){
        try {
            if(LocalInfo.s==null){
                try {
                    LocalInfo.s = new Storage(LocalInfo.NUMBERMMFR);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                LocalInfo.s.freeUnmanaged();
                for(int i =0;i<5;i++){
                    LocalInfo.s.push(new MMFR());
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
