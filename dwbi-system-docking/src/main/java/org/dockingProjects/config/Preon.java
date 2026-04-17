package org.dockingProjects.config;

import lombok.Data;

@Data
public class Preon {

    private String name;
    private ThreadLocal<String> pwd = new ThreadLocal<>();

    public void setPwd(String s){
        this.pwd.set(s);
    }
    public String getPwd(){
        return  this.pwd.get();
    }
}
