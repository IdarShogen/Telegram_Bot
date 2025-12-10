package org.example;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendSticker;

import java.util.Scanner;


public class DefaultMethods {

    //Приветствие "Прступим"
    protected static void start(long chatId) {
        InlineKeyboardMarkup ikm = new InlineKeyboardMarkup(
                new InlineKeyboardButton("Приступим").callbackData("start")
        );
        Main.bot.execute(new SendMessage(chatId, Data.getGreeting()).replyMarkup(ikm));
    }

    //Запрос поддержки у админа
    protected static void help(long chatId) {
        Main.bot.execute(new SendMessage(chatId, "Опишите свою проблему."));
        System.out.println("Новый вопрос");
        Main.bot.execute(new SendMessage(chatId, "Админ скоро ответит😊"));
        Main.bot.execute(new SendMessage(chatId, answer()));
    }

    //Ответ админа
    public static String answer() {
        String input;
        StringBuilder answer = new StringBuilder();
        Scanner con = new Scanner(System.in);
        while(!(input = con.nextLine()).equals("send")) {
            answer.append(input).append(" ");
        }
        return answer.toString();
    }

    //Неизвестная команда
    protected static void unknown(long chatId) {
        Main.bot.execute(new SendMessage(chatId, "Неизвестная команда"));
        Main.bot.execute(new SendSticker(chatId, "CAACAgIAAxkBAAEPzW1pGx7RKMHxCuZVNIJHIiHXSuJrhAACmXEAArEloUg5PUGj44RXuzYE"));
    }

    //Показ спсика действий
    protected static void showMenu(long chatId) {
        InlineKeyboardMarkup ikm = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Помощь").callbackData("help")
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Меню").callbackData("menu"),
                        new InlineKeyboardButton("ChatGPT").callbackData("chatGPT"),
                        new InlineKeyboardButton("Баллы").callbackData("scores")
                },
                new InlineKeyboardButton[] {

                        new InlineKeyboardButton("Погода").callbackData("weather")
                }
        );

        Main.bot.execute(new SendMessage(chatId, "Список действий").replyMarkup(ikm));
    }

    //ссылка на ChatGPT
    protected static void chatGpt(long chatId) {
        Main.bot.execute(new SendMessage(chatId,"https://chatgpt.com/"));
        Main.bot.execute(new SendMessage(chatId, "Включи VPN!"));

    }

}
