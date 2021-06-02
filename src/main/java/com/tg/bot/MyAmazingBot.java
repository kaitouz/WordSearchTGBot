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

    //This method must always return Bot username
    @Override
    public String getBotUsername() {
        return "LeThienBot";
    }

    //This method must always return Bot Token (from @Botather)
    @Override
    public String getBotToken() {
        return "1849018194:AAH3mAkCsOFjSGIORzj14Jcc-WMCUptSu0I";
    }

    @Override
    public void onUpdateReceived(Update update) {

        // Check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {

            String ChatId = update.getMessage().getChatId().toString();             // Get ChatId
            String ChatText = update.getMessage().getText();                        // Get Text

            SendMessage message = new SendMessage();                                // Create a SendMessage object with mandatory fields

            message.setChatId(ChatId);                                              // Set ChatId to SendMessage
            message.enableMarkdownV2(true);


            // Game play (It will generate content of SendMessage):
            if(GameList.containsKey(ChatId) == false) {                             // Game of this user is not running
                System.out.println("qfwq");
                if(ChatText.compareTo("/startgame") == 0) {                         // User send command to start game
                    Game NewGame = new Game();
                    GameList.put(ChatId, NewGame);
                    String table = GameList.get(ChatId).CreateTable();
                    GameList.get(ChatId).printResult();
                    table = "`    \uD83C\uDD36\uD83C\uDD30\uD83C\uDD3C\uD83C\uDD34 \uD83C\uDD42\uD83C\uDD43\uD83C\uDD30\uD83C\uDD41\uD83C\uDD43\uD83C\uDD34\uD83C\uDD33`" + "\n" + table;
                    message.setText(table); // Set message to send
                }
                else {
                    String a = "`Game is not running`" ;
                    message.setText(a);
                }

            }
            else {                                                                   // Game of this user is running
                if (ChatText.compareTo("/startgame") == 0) {
                    String content = "`Game is still running`\n";
                    content = content + GameList.get(ChatId).CreateTable();
                    message.setText(content);
                }
                else if (ChatText.compareTo("/quitgame") == 0) {                          // User send command to quit game
                    String b = "`\uD83D\uDC94 GAME OVER \uD83D\uDC94` \n `You cannot give` \n `up just yet...`";
                    GameList.remove(ChatId);
                    message.setText(b);
                }
                else {                                                               // User send the answer

                    int check = GameList.get(ChatId).CheckAnswer(ChatText);// Check the answer
                    if (check != 0) {// If the answer is correct
                        if(GameList.get(ChatId).WordAnswered.size() == 10) {// If user has answered 10 answers
                            String a = "`\uD83C\uDFC6  Congratulation\\!!!\\ \uD83C\uDFC6` \n  `\uD83D\uDCAF You are amazing \uD83D\uDCAF`" ;
                            message.setText(a);
                            GameList.remove(ChatId);

                        } else {
                            String content = "`Correct`\n";
                            content = content + GameList.get(ChatId).CreateTable();
                            message.setText(content);
                        }
                    } // If the answer is incorrect
                    else {
                        if(GameList.get(ChatId).GameHeart == 1) {// If user got the wrong answer 3 times
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



            try {
                execute(message);  // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }
    }

}