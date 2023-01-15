package org.example;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HttpUrlConnectionExample {

    private List<String> cookies;
    private static HttpURLConnection conn;

    private final String USER_AGENT = "Mozilla/5.0";

    public static String sendUrfuRequest(String url, String login, String password) throws Exception {
//        String url = "https://sts.urfu.ru/adfs/OAuth2/authorize?resource=https://istudent.urfu.ru&type=web_server&client_id=https://istudent.urfu.ru&redirect_uri=https://istudent.urfu.ru?auth&response_type=code&scope=";
//        String url = "https://istudent.urfu.ru/assets/vendor/changed/orphus.js?v=1667395886";
//        String url = "https://sts.urfu.ru/adfs/OAuth2/authorize?resource=https%3A%2F%2Fistudent.urfu.ru&type=web_server&client_id=https%3A%2F%2Fistudent.urfu.ru&redirect_uri=https%3A%2F%2Fistudent.urfu.ru%3Fauth%26rp%3DL3MvaHR0cC11cmZ1LXJ1LXJ1LXN0dWRlbnRzLXN0dWR5LWJycw%253D%253Dd1ca03c09b406b6d440b4bab78479bdb&response_type=code&scope=";
            // БРС 3 строка
        HttpUrlConnectionExample http = new HttpUrlConnectionExample();

        // make sure cookies is turn on
        CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

        // 1. Send a "GET" request, so that you can extract the form's data.
        String page = http.GetPageContent(url);
        System.out.println(http.getCookies());
        String postParams = http.getFormParams(page, login, password);

        System.out.println("1111111111111111111END " + postParams);

        // 2. Construct above post's content and then send a POST request for
        // authentication
        http.sendPost(url, postParams);
        System.out.println("22222222222222222222222END");

        // 3. success then gow.
        String result = http.GetPageContent(url);
        System.out.println("HTTP- " + conn.getHeaderFields());
        System.out.println("result === " + result);
        return " " + result;
    }

    public void sendPost(String url, String postParams) throws Exception {

//        HttpPost post = new HttpPost(url);
//
//        // add request parameter, form parameters
//        List<NameValuePair> urlParameters = new ArrayList<>();
//        urlParameters.add(new BasicNameValuePair("Host", ""));
//        urlParameters.add(new BasicNameValuePair("User-Agent", USER_AGENT));
//        urlParameters.add(new BasicNameValuePair("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
//        urlParameters.add(new BasicNameValuePair("Connection", "keep-alive"));
//        urlParameters.add(new BasicNameValuePair("UserName", "egorlantsov@mail.ru"));
//        urlParameters.add(new BasicNameValuePair("Password", "Egorka123@"));
//        urlParameters.add(new BasicNameValuePair("Kmsi", "true"));
//        urlParameters.add(new BasicNameValuePair("AuthMethod", "FormsAuthentication"));
//        urlParameters.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
//        urlParameters.add(new BasicNameValuePair("Content-Length", Integer.toString(postParams.length())));
//
//        post.setEntity(new UrlEncodedFormEntity(urlParameters));
//
//        DataOutputStream wr = new DataOutputStream(post.);
//        wr.writeBytes(postParams);
//        wr.flush();
//        wr.close();
//
//        try (CloseableHttpClient httpClient = HttpClients.createDefault(); CloseableHttpResponse response = httpClient.execute(post)) {
//
//            System.out.println(EntityUtils.toString(response.getEntity()));
//        }
// -----------------------------------------------------------------------------------------------
        URL obj = new URL(url);
        conn = (HttpURLConnection) obj.openConnection();

        // Acts like a browser
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Host", "");
        conn.setRequestProperty("User-Agent", USER_AGENT);
        System.out.println("asdasdsa");
        conn.setRequestProperty("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        System.out.println("1 === ");
        try {
            for (String cookie : this.cookies) {
                System.out.println("kukiii === ");
                conn.addRequestProperty("Set-Cookie", cookie.split(";", 1)[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("2 === ");
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));
//        conn.getOutputStream();
        System.out.println("ЗАШЕЛ ЛЛЛЛЛЛЛЛЛЛЛ===============================================");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        System.out.println("TestGetOut === " + conn.getOutputStream());

        // Send post request
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();
        System.out.println("TestGetOut === " + conn.getOutputStream());

        int responseCode = conn.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + postParams);
        System.out.println("Response Code : " + responseCode);
        System.out.println("Testtt Post " + conn.getHeaderFields());

        BufferedReader in =
                new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

    }

    public String GetPageContent(String url) throws Exception {

        URL obj = new URL(url);
        conn = (HttpURLConnection) obj.openConnection();

        // default is GET
        conn.setRequestMethod("GET");

        conn.setUseCaches(false);

        // act like a browser
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        if (cookies != null) {
            System.out.println("dasd");
            for (String cookie : this.cookies) {
                System.out.println("Куки ========= ");
                conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
            }
        }
        int responseCode = conn.getResponseCode();

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in =
                new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Get the response cookies
        setCookies(conn.getHeaderFields().get("Set-Cookie"));
        System.out.println("setCookies1 " + conn.getHeaderFields().get("Set-Cookie"));
        return response.toString();

    }

    public String getFormParams(String html, String username, String password)
            throws UnsupportedEncodingException {

        System.out.println("Extracting form's data...");

        Document doc = Jsoup.parse(html);

        // Google form id
        Element loginform = doc.getElementById("loginForm");
        Elements inputElements = loginform.getElementsByTag("input");
        List<String> paramList = new ArrayList<String>();
        for (Element inputElement : inputElements) {
            System.out.println(inputElement);
            String key = inputElement.attr("name");
//            System.out.println("key --- " + key);
            String value = inputElement.attr("value");
//            System.out.println("value --- " + value);

            if (key.equals("UserName"))
                value = username;
            else if (key.equals("Password"))
                value = password;
            paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
        }

        // build parameters list
        StringBuilder result = new StringBuilder();
        for (String param : paramList) {
            if (result.length() == 0) {
                result.append(param);
            } else {
                result.append("&" + param);
            }
        }
        System.out.println("result - getFormPar == " + result);
        return result.toString();
    }

    public List<String> getCookies() {
        return cookies;
    }

    public void setCookies(List<String> cookies) {
        if (cookies != null) {
            this.cookies = cookies;
        }
    }

}