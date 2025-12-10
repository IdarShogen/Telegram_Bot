package org.example;
import java.util.List;


public class Data {

    //Приветственый текст
    private final static String greeting =
            "Привет👋.\n" +
            "Я - ваш персональный ИИ-юрист.\n" +
            "Помогу с анализом документов, указать на скрытые риски, объяснить юридические детали простым языком.\n" +
            "Отправьте файл/скан-фото или опишите ситуацию - начнём разбираться.";


    private final static String BOT_API = "8499695905:AAELy3eaEDFm7caPjlOD5b-Ct08easQuV6U";
    private final static  String API_KEY = "6121262860423f102f3998777211e367";

    protected static String getBotApi() {
        return BOT_API;
    }
    protected static String getApiKey() {
        return API_KEY;
    }
    protected static String getGreeting() {
        return greeting;
    }







}
