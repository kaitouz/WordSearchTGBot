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

    HashMap<String,  Game>    GameList = new HashMap<String,  Game>();

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
                    GameList.get(ChatId).printResult();
                    table = "`    \uD83C\uDD36\uD83C\uDD30\uD83C\uDD3C\uD83C\uDD34 \uD83C\uDD42\uD83C\uDD43\uD83C\uDD30\uD83C\uDD41\uD83C\uDD43\uD83C\uDD34\uD83C\uDD33`" + "\n" + table;
                    message.setText(table);
                }
                else {
                    String a = "`Game is not running`" ;
                    message.setText(a);
                }
            } else {
                if (ChatText.compareTo("Quit Game") == 0) {
                    String b = "`\uD83D\uDC94 GAME OVER \uD83D\uDC94` \n `You cannot give` \n `up just yet...`";
                    GameList.remove(ChatId);
                    message.setText(b);
                } else {
                    int check = GameList.get(ChatId).CheckAnswer(ChatText);

                    if (check != 0) {
                        if(GameList.get(ChatId).WordAnswered.size() == 10) {
                            String a = "`\uD83C\uDFC6  Congratulation\\!!!\\ \uD83C\uDFC6` \n  `\uD83D\uDCAF You are amazing \uD83D\uDCAF`" ;
                            message.setText(a);
                            GameList.remove(ChatId);
                        } else {
                            String content = "`Correct`\n";
                            content = content + GameList.get(ChatId).CreateTable();
                            message.setText(content);
                        }
                    } else {
                        if(GameList.get(ChatId).GameHeart == 0) {
                            String b = "`\uD83D\uDC94 GAME OVER \uD83D\uDC94` \n `You cannot give` \n `up just yet...`";
                            message.setText(b);
                            GameList.remove(ChatId);
                        } else {
                            GameList.get(ChatId).GameHeart--;
                            String content = "`Incorrect`\n";
                            content = content + GameList.get(ChatId).CreateTable();
                            message.setText(content);
                        }
                    }
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