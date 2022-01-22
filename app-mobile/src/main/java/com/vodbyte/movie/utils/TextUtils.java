package com.vodbyte.movie.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

    public static SpannableString formatString(Context context, String text, int style) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new TextAppearanceSpan(context, style), 0, text.length(), 0);
        return spannableString;
    }

    public static String GetHtmlString(String htmlStr){

        //先将换行符保留，然后过滤标签
        Pattern p_enter = Pattern.compile("<br/>", Pattern.CASE_INSENSITIVE);
        Matcher m_enter = p_enter.matcher(htmlStr);
        htmlStr = m_enter.replaceAll("\n");

        //过滤html标签
        Pattern p_html = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        return  m_html.replaceAll("");
    }
}
