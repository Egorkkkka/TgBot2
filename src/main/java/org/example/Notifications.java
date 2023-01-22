package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Notifications {
    static String url = "https://sts.urfu.ru/adfs/OAuth2/authorize?resource=https%3A%2F%2Fistudent.urfu.ru&type=web_server&client_id=https%3A%2F%2Fistudent.urfu.ru&redirect_uri=https%3A%2F%2Fistudent.urfu.ru%3Fauth%26rp%3DL25vdGlmaWNhdGlvbnM%253D8295d4f174526db51bd7f7fba74da451&response_type=code&scope=";

    public static ArrayList<String> parseEvents(String login, String password) throws Exception {
        String bodyHTML = HttpUrlConnectionExample.sendUrfuRequest(url, login, password);
        Document doc = Jsoup.parse(bodyHTML);
        Elements elementLoginArea = doc.getElementsByAttributeValue("data-type", "notification");
        ArrayList<String> eventsList = new ArrayList<>();
        int counter = 0;
        for (Element inputElement : elementLoginArea) {
            if (counter == 4) {
                return eventsList;
            }
            Element header = inputElement.getElementsByClass("tasks-header").get(0);
            inputElement.getElementsByTag("p");

            Element taskTime = inputElement.getElementsByClass("task-time").get(0);

            Element body = inputElement.getElementsByClass("survey-vacancy-info").get(0);

            Elements allP = body.getElementsByTag("p");
            StringBuilder bodyElement = new StringBuilder();

            for (Element p : allP) {
                bodyElement.append(p.text()).append("\n");
            }

            eventsList.add(header.text() + "\n---------------------------------\n" + taskTime.text() + "\n" + bodyElement);
            counter++;
        }
        return eventsList;
    }
}
