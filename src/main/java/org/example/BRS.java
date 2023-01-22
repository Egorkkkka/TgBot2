package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

public class BRS {

    static String urlBrs = "https://sts.urfu.ru/adfs/OAuth2/authorize?resource=https%3A%2F%2Fistudent.urfu.ru&type=web_server&client_id=https%3A%2F%2Fistudent.urfu.ru&redirect_uri=https%3A%2F%2Fistudent.urfu.ru%3Fauth%26rp%3DL3MvaHR0cC11cmZ1LXJ1LXJ1LXN0dWRlbnRzLXN0dWR5LWJycw%253D%253Dd1ca03c09b406b6d440b4bab78479bdb&response_type=code&scope=";

    public static HashMap<String, String> parseBrs(String login, String password) throws Exception {
        HashMap<String, String> subject = new HashMap<>();

        String bodyHTML = HttpUrlConnectionExample.sendUrfuRequest(urlBrs, login, password);
        Document doc = Jsoup.parse(bodyHTML);
        Elements nameAndBrs = doc.getElementsByClass("js-service-rating-item");

        for (Element subj : nameAndBrs) {
            Element name = subj.getElementsByClass("js-service-rating-td td-0").get(0);
            Element rate = subj.getElementsByClass("js-service-rating-td td-1").get(0);
            Element nameRate = subj.getElementsByClass("js-service-rating-td td-2").get(0);
            subject.put(name.text(), rate.text() + " - " + nameRate.text());
        }

        return subject;
    }
}
