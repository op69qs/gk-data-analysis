package org.dockingProjects.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        Preon p = new Preon();
        String s = null;
        StringBuffer ss ;
        StringBuilder sss;
        s.equals("");
        int a = s.hashCode();
        HashMap h = new HashMap();
        ConcurrentHashMap c = new ConcurrentHashMap();
        Hashtable t = new Hashtable();



        new Thread(new Runnable() {
            @Override
            public void run() {
                p.setName("刘备");
                p.setPwd("刘备");
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程1====="+p.getName()+"======="+p.getPwd());
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                p.setName("曹操");
                p.setPwd("曹操");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程2====="+p.getName()+"======="+p.getPwd());
            }
        }).start();



    }
}
