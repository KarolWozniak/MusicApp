package com.example.karol.musicapp;

/**
 * Created by Karol on 2018-03-12.
 */

public class Parser {
    private static final String link = "http://207.154.200.78:1997/api/converter?url=https://www.youtube.com/watch?v=";
    private String right_link = "";

    public Parser(String string)
    {
        this.right_link=link+search(string);
    }

    private String search(String string) {
        String new_one="";
        String temp="";
        CharSequence computerLink="watch?v=";
        CharSequence mobileLink="youtu.be/";
        boolean flag = false;
        for (char ch: string.toCharArray()) {
            if(temp.contains(computerLink) || temp.contains(mobileLink))
            {
                flag = true;
            }
            if(flag)
            {
                new_one+=ch;
            }
            else
            {
                temp+=ch;
            }
        }
        return new_one;
    }

    public String getRight_link()
    {
        return right_link;
    }
}
