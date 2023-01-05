package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.telegram.telegrambots.meta.api.objects.Message;

public class MethodsAuth {
    public static String checkPassword(String login, String passwrod) throws Exception {
        String res = "";
        String url = "https://sts.urfu.ru/adfs/OAuth2/authorize?resource=https%3A%2F%2Fistudent.urfu.ru&type=web_server&client_id=https%3A%2F%2Fistudent.urfu.ru&redirect_uri=https%3A%2F%2Fistudent.urfu.ru%3Fauth%26rp%3DL3Mvc2NoZWR1bGU%253D3ce998544cd42ddb4cffeaae05dbfff0&response_type=code&scope=";
        String bodyHTML = HttpUrlConnectionExample.sendUrfuRequest(url, login, passwrod);
//        System.out.println("bodyHTML" + bodyHTML);
        Document doc = Jsoup.parse(bodyHTML);
        Element elementLoginArea = doc.getElementById("loginArea");
        if (elementLoginArea == null) {
            res = "Вы успешно авторизовались!";
        } else {
            res = "Вы ввели неверные данные!";
        }
        System.out.println("logArea === " + elementLoginArea);
        System.out.println(res);
        return res;
    }
}
