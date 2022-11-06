package org.example;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TestBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
//        if (update.hasMessage()) {
//            Message message = update.getMessage();
//            if (message.hasText()){
//                try {
//                    execute(
//                            SendMessage.builder()
//                                    .chatId(message.getChatId().toString())
//                                    .text("Testing - " + message.getText())
//                                    .build());
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        if (update.hasMessage()) {
            handleMessage(update.getMessage());
        }

        if (update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery());
        }
    }

    private void handleCallback(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String[] param = callbackQuery.getData().split(":");
        String action = param[0];
        switch (action) {
            case "/start":
                List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                buttons.add(
                        Arrays.asList(
                                InlineKeyboardButton.builder().text("Начать заново").callbackData("/start").build()
                        )
                );
                try {
                    execute(
                            SendMessage.builder()
                                    .text("Привет!\n" +
                                            "Введи логин (почту) к личному кабинету УрФУ")
                                    .chatId(message.getChatId().toString())
                                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                                    .build());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
        }
    }

    private void handleMessage(Message message) {
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command = message
                        .getText()
                        .substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                switch (command) {
                    case "/start":
                        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                        buttons.add(
                                Arrays.asList(
                                        InlineKeyboardButton.builder().text("Начать заново").callbackData("/start").build()
                                )
                        );
                        try {
                            execute(
                                    SendMessage.builder()
                                            .text("Привет!\n" +
                                                    "Введи логин (почту) к личному кабинету УрФУ")
                                            .chatId(message.getChatId().toString())
                                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                                            .build());
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                }
            }

        }
    }

    @Override
    public String getBotUsername() {
        return "@test_Urfu_bot";
    }

    @Override
    public String getBotToken() {
        return "5661011516:AAGka7AUxhB_YepFNTlmk4SKMOy5nsEQ8Ys";
    }


    public static void main(String[] args) throws TelegramApiException {
        TestBot bot = new TestBot();
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(bot);
    }
}