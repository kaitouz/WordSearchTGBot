package com.tg.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {

            logger.info("Program is running");

            //Instantiate Telegram Bots API:
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            //Register this bot:
            telegramBotsApi.registerBot(new MyAmazingBot());

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

}
