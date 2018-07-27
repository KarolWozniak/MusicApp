package com.example.karol.musicapp;

/**
 * Created by Karol on 2018-03-12.
 */

public class Parser {

    private static final String link = "http://206.189.62.11:1997/api/converter?url=https://www.youtube.com/watch?v=";
    private static String mobileLink = "youtu.be/";
    private static String computerLink = "watch?v=";
    private String right_link = "";
    private String videoId = "";

    public Parser(String string) {
        this.right_link = link + search(string);
        this.videoId = string.replace(mobileLink ,"")
                             .replace(computerLink ,"")
                             .replace("https://","");
    }

    private String search(String string) {
        String new_one = "";
        String temp = "";
        boolean flag = false;
        for (char ch : string.toCharArray()) {
            if (temp.contains(computerLink) || temp.contains(mobileLink)) {
                flag = true;
            }
            if (flag) {
                new_one += ch;
            } else {
                temp += ch;
            }
        }
        return new_one;
    }

    public String getRight_link() {
        return right_link;
    }

    public String getImageLink() {
        return "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
    }

}
