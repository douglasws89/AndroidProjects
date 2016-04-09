package com.example.cmps121.slugsocial;


public class SingleRow {
    SingleRow(){};

    SingleRow(String mes, String nick, String u_id, int img){
        message = mes;
        nickname = nick;
        user_id = u_id;
        image = img;
    }

    public String message;
    public String nickname;
    public String user_id;
    public int image;
}

