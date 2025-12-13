package org.example;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SetMyCommands;




public class Main {
    static TelegramBot bot;

    public Main(TelegramBot bot) {
        Main.bot = bot;
    }

    public static void handle(Update update) {
        if (update.message() != null && update.message().text() != null) {

            long chatId = update.message().chat().id();
            String text = update.message().text();

            if (UserStateService.getState(chatId) == UserState.AWAITING_CITY_NAME) {
                Weather.run(update);
                return;
            }

            switch (text) {
                case "/start": DefaultMethods.start(chatId); break;
                default: DefaultMethods.unknown(chatId);
            }

            return;
        }


        if(update.callbackQuery() != null && update.callbackQuery().data() != null) {
            long chatId = update.callbackQuery().message().chat().id();
            String data = update.callbackQuery().data();
            switch (data) {
                case "start", "menu" : DefaultMethods.showMenu(chatId); break;
                case "help" : DefaultMethods.help(chatId); break;
                case "chatGPT" : DefaultMethods.chatGpt(chatId); break;
                case "weather" : Weather.run(update); break;
                default: Weather.run(update);
            }

        }
    }


    public static void main(String[] args) {

        new Main(new TelegramBot(Data.getBotApi()));

        BotCommand[] commands = new BotCommand[] {
            new BotCommand("Start", "start")
        };
        bot.execute(new SetMyCommands(commands));

        bot.setUpdatesListener(updates -> {
            for(Update update : updates) {
                handle(update);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

    }
}