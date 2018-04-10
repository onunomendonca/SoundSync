package com.mei.daam.soundsync;

public class Group {

    private String name;
    private String cenas;
    //LISTA de Músicas
    //LISTA Quem ta ligado


    public Group(String name, String cenas) {
        this.name = name;
        this.cenas = cenas;
    }


    public String getName() {
        return name;
    }

    public String getCenas() {
        return cenas;
    }
}
