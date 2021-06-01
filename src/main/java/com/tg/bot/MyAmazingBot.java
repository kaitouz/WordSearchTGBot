package com.tg.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class MyAmazingBot extends TelegramLongPollingBot {

    HashMap<String, Game>    GameList = new HashMap<String, Game>();

    @Override
    public String getBotUsername() {
        return "LeThienBot";
    }

    @Override
    public String getBotToken() {
        return "1849018194:AAH3mAkCsOFjSGIORzj14Jcc-WMCUptSu0I";
    }

    @Override
    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {

            String ChatId = update.getMessage().getChatId().toString();
            String ChatText = update.getMessage().getText();

            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields

            message.setChatId(ChatId);
            message.enableMarkdownV2(true);

            if(GameList.containsKey(ChatId) == false) {
                if(ChatText.compareTo("Start Game") == 0) {
                    Game NewGame = new Game();
                    GameList.put(ChatId, NewGame);
                    String table = GameList.get(ChatId).CreateTable();
                    table = "Game started" + "\n" + table;
                    System.out.println(table);
                    message.setText(table);
                }
                else {
                    String a = "\u2665" + "ddqd";
                    message.setText(a);
                }
            } else {
                if (ChatText.compareTo("Quit Game") == 0) {
                    GameList.remove(ChatId);
                    message.setText("Game ended");
                } else {
                    int check = GameList.get(ChatId).CheckAnswer(ChatText);

                    System.out.println(check);
                    if (check != 0) {


                    } else GameList.get(ChatId).GameHeart--;

                    String content = "Game is running\n";
                    content = content + GameList.get(ChatId).CreateTable();
                    message.setText(content);

                }
            }

            // Call method to send the message
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }
    }

}