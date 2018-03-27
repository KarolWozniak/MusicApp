package com.example.karol.musicapp;

/**
 * Created by Karol on 2018-03-12.
 */

public class Parser {
    private static final String link="https://youtubetoany.com/@api/json/mp3/";
    private String right_link="";

    public Parser(String string)
    {
        this.right_link=link+search(string);
    }

    private String search(String string)
    {
        String new_one="";
        String temp="";
        CharSequence cs="watch?v=";
        boolean flag=false;
        for (char ch: string.toCharArray()) {
            if(temp.contains(cs))
            {
                flag=true;
            }
            if(flag==true)
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
        return this.right_link;
    }
}
