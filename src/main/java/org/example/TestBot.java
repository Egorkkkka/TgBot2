package org.example;

import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TestBot extends DefaultAbsSender {

    protected TestBot(DefaultBotOptions options) {
        super(options);
    }

    @Override
    public String getBotToken() {
        return "5661011516:AAGka7AUxhB_YepFNTlmk4SKMOy5nsEQ8Ys";
    }

    public static void main(String[] args) throws TelegramApiException {
        TestBot bot = new TestBot(new DefaultBotOptions());
        bot.execute(SendMessage.builder().chatId("374927123123548").text("Hello from Java").build());
        bot.execute(SendMessage.builder().chatId("374927123123548").text("Hello from Java").build());
    }
}