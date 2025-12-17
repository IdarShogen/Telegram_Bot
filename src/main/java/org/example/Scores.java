package org.example;

import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Scores {
    public static String getScoresHtml(String surname, String recordBook) {
        String urlString = "https://usp.kbsu.ru/getinfo.php";
        try {

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");

            String postData = "c_fam=" + surname + "&tabn=" + recordBook;
            byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);

            try(OutputStream out = connection.getOutputStream()){
                out.write(postDataBytes);
            }

            int responseCode = connection.getResponseCode();
            if(responseCode != 200) {
                return "Ошибка: HTTP " + responseCode;
            }

            StringBuilder response = new StringBuilder();
            try(BufferedReader buf = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while((line = buf.readLine()) != null) {
                    response.append(line);
                }
            }

            return response.toString();
        }
        catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
        }
        return null;
    }

    public static String parseScores(String html) {
        Document doc = (Document) Jsoup.parse(html);
        Element table = doc.select("table").first();

        Elements rows = table.select("tbody > tr");


        if (rows.isEmpty()) return "Ошибка: таблица пуста";

        StringBuilder result = new StringBuilder();

        for(Element row : rows) {
            Elements cells = row.select("td");

            if(cells.size() < 8) continue;

            String number = cells.get(0).text();
            String subject = cells.get(1).text();
            String totalScore = cells.get(6).text();

            if(subject.trim().isEmpty()) continue;

            result.append(number).append(". ")
                    .append(subject).append("\n")
                    .append("   Баллы: ").append(totalScore).append("\n\n");
        }

        return result.toString();
    }


    public static String getScores() {
        String html = getScoresHtml(Data.getSurname(), Data.getRecordBook());

        if(html.startsWith("Ошибка")) return html;

        return parseScores(html);
    }

}
