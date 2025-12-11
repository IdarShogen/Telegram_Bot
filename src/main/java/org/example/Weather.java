package org.example;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



public class Weather {

    static TelegramBot bot = Main.bot;
    static List<String> cities = new ArrayList<>();


    //Main
    public static void run(Update update) {
        if(update.message() != null && update.message().text() != null) {
            long chatId = update.message().chat().id();
            String text = update.message().text();


        }

        else if(update.callbackQuery() != null ) {
            long chatId = update.callbackQuery().message().chat().id();
            String data= update.callbackQuery().data();


        }
        else {
            long chatId = update.message().chat().id();
            DefaultMethods.unknown(chatId);
        }
    }



    //Получение погоды из сайта
    public static String getWeather(String city) {
        String api = Data.getApiKey();

        try{
            String urlString =  "https://api.openweathermap.org/data/2.5/weather?q="
                    + java.net.URLEncoder.encode(city, "UTF-8")
                    + "&appid=" + api
                    + "&units=metric&lang=ru";

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int codeStatus = connection.getResponseCode();
            if(codeStatus != 200) {
                if(codeStatus == 401) return "Ошибка: неверный ключ API";
                else if(codeStatus == 404) return "Город не найден";
                else return "Ошибка запроса: HTTP " + codeStatus;
            }
            StringBuilder response = new StringBuilder();
            try(BufferedReader buf = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

                String line;
                while ((line = buf.readLine()) != null) {
                    response.append(line);
                }
            }

            String json = response.toString();

            JSONObject object = new JSONObject(json);
            double temp = object.getJSONObject("main").getDouble("temp");
            JSONArray weather = object.getJSONArray("weather");
            String description = weather.getJSONObject(0).getString("description");

            String result = "Погода в городе(селе) " + city + ":\n"
                    + "Температура: " + Math.round(temp) + "°C\n"
                    + "Состояние: " + description;


            return result;

        } catch (Exception e) {
            return "Ошибка формирования запроса: " + e.getMessage();
        }
    }


    //Создание inline-списка городов
    public static void newInlineKeyboardButton(long chatId, List<String> cities) {
        List<InlineKeyboardButton[]> rows = new ArrayList<>();
        InlineKeyboardButton[] add_remove = new InlineKeyboardButton[]{
                new InlineKeyboardButton("Добавить город").callbackData("add_city"),
                new InlineKeyboardButton("Удалить город").callbackData("remove_city")
        };
        for (int i = 0; i < cities.size(); i += 2) {
            if(i + 1 < cities.size()) {
                rows.add(new InlineKeyboardButton[]{
                        new InlineKeyboardButton(cities.get(i)).callbackData("weather_" + cities.get(i)),
                        new InlineKeyboardButton(cities.get(i + 1)).callbackData("weather_" + cities.get(i + 1))
                });
            } else {
                rows.add(new InlineKeyboardButton[]{
                        new InlineKeyboardButton(cities.get(i)).callbackData("weather_" + cities.get(i))
                });
            }
        }

        rows.add(add_remove);

        InlineKeyboardMarkup ikm = new InlineKeyboardMarkup(rows.toArray(new InlineKeyboardButton[0][]));
        Main.bot.execute(new SendMessage(chatId, "Список городов").replyMarkup(ikm));
    }


    //Вывод погоды города
    public static void showWeather(String city, long chatId) {
        Main.bot.execute(new SendMessage(chatId, Weather.getWeather(city)));
    }


    //Отображение списка городов
    public static void showCities(long chatId) {
        newInlineKeyboardButton(chatId, cities);
    }


    //Добавление нового города
    public static void addNewCity(Update update, long chatId) {
        bot.execute(new SendMessage(chatId, "Название города:\n"));
        String newCityName = update.message().text();
        cities.add(newCityName);
    }


    //Удаление города
    public static void removeCity(Update update, long chatId) {
        List<InlineKeyboardButton[]> list = new LinkedList<>();
        for (int i = 0; i < cities.size(); i++) {
            list.add(new InlineKeyboardButton[]{
                    new InlineKeyboardButton(cities.get(i)).callbackData("weather_" + cities.get(i))
            });
        }
        InlineKeyboardMarkup ikm = new InlineKeyboardMarkup(list.toArray(new InlineKeyboardButton[0][0]));
        bot.execute(new SendMessage(chatId, "Выберите город").replyMarkup(ikm));
        String data = update.callbackQuery().data();
        cities.remove(data.substring(8));
    }




}
