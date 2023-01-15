package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class TestBot extends TelegramLongPollingBot {

    boolean waitPass = false;
    boolean waitNote = false;
    String LOGIN = "";
    String PASSWORD = "";
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
            try {
                handleMessage(update.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if (update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery());
        }
    }

    private void handleCallback(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String idChat = String.valueOf(message.getChatId());
        String[] param = callbackQuery.getData().split(":");
        String action = param[0];
        switch (action) {
            case "/exit":
            case "/start":
                List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                buttons.add(
                        Collections.singletonList(
                                InlineKeyboardButton.builder().text("Начать заново!").callbackData("/start").build()
                        )
                );
                try {
                    execute(
                            SendMessage.builder()
                                    .text("Привет!\n" +
                                            "Введи логин (почту) к личному кабинету УрФУ")
                                    .chatId(message.getChatId().toString())
                                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                                    .build()
                    );

                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                return;

            case "schedule":
                List<List<InlineKeyboardButton>> buttons2 = new ArrayList<>();
                buttons2.add(
                        Arrays.asList(
                                InlineKeyboardButton.builder().text("На день").callbackData("#").build(),
                                InlineKeyboardButton.builder().text("На неделю").callbackData("#").build(),
                                InlineKeyboardButton.builder().text("На месяц").callbackData("#").build()
                        )
                );

                try { // https://sts.urfu.ru/adfs/OAuth2/authorize?resource=https://istudent.urfu.ru&type=web_server&client_id=https://istudent.urfu.ru&redirect_uri=https://istudent.urfu.ru?auth&response_type=code&scope=
                    String url = "https://istudent.urfu.ru/s/schedule?auth-ok";
//                    String url = "https://sts.urfu.ru/adfs/OAuth2/authorize?resource=https%3A%2F%2Fistudent.urfu.ru&type=web_server&client_id=https%3A%2F%2Fistudent.urfu.ru&redirect_uri=https%3A%2F%2Fistudent.urfu.ru%3Fauth%26rp%3DL3Mvc2NoZWR1bGU%253D3ce998544cd42ddb4cffeaae05dbfff0&response_type=code&scope=";
                    execute(
                            SendMessage.builder()
                                    .text("На какой пероиод показать расписание занятий?")
                                    .chatId(message.getChatId().toString())
                                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons2).build())
                                    .build()
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return;

            case "items":
                String urlBrs = "https://sts.urfu.ru/adfs/OAuth2/authorize?resource=https%3A%2F%2Fistudent.urfu.ru&type=web_server&client_id=https%3A%2F%2Fistudent.urfu.ru&redirect_uri=https%3A%2F%2Fistudent.urfu.ru%3Fauth%26rp%3DL3MvaHR0cC11cmZ1LXJ1LXJ1LXN0dWRlbnRzLXN0dWR5LWJycw%253D%253Dd1ca03c09b406b6d440b4bab78479bdb&response_type=code&scope=";
                try {
                    String resBrs = HttpUrlConnectionExample.sendUrfuRequest(urlBrs, LOGIN, PASSWORD);
                    System.out.println(" RES BRS === " + resBrs);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                List<List<InlineKeyboardButton>> buttons3 = new ArrayList<>();
                buttons3.add(
                        Arrays.asList(
                                InlineKeyboardButton.builder().text("Выбрать предмет").callbackData("#").build(),
                                InlineKeyboardButton.builder().text("Все предметы").callbackData("#").build()
                        )
                );

                try {
                    execute(
                            SendMessage.builder()
                                    .text("Информация об успеваемости:")
                                    .chatId(message.getChatId().toString())
                                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons3).build())
                                    .build()
                    );
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                return;

            case "note":
                List<List<InlineKeyboardButton>> buttons4 = new ArrayList<>();
                buttons4.add(
                        Arrays.asList(
                                InlineKeyboardButton.builder().text("Посмотреть заметки").callbackData("showNotes").build(),
                                InlineKeyboardButton.builder().text("Написать заметку").callbackData("createNote").build()
                        )
                );

                try {
                    execute(
                            SendMessage.builder()
                                    .text("Заметки:")
                                    .chatId(message.getChatId().toString())
                                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons4).build())
                                    .build()
                    );
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                return;

            case "event":
                List<List<InlineKeyboardButton>> buttons5 = new ArrayList<>();
                buttons5.add(
                        Arrays.asList(
                                InlineKeyboardButton.builder().text("Да").callbackData("#").build(),
                                InlineKeyboardButton.builder().text("Нет").callbackData("#").build()
                        )
                );

                try {
                    execute(
                            SendMessage.builder()
                                    .text("Ты хочешь узнавать о всех мероприятих УрФУ ?:")
                                    .chatId(message.getChatId().toString())
                                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons5).build())
                                    .build()
                    );
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }

            case "showNotes":
                try {
                    execute(
                            SendMessage.builder()
                                    .text("Текст заметки: \n")
                                    .text(RedisStorage.readFile(idChat))
                                    .chatId(message.getChatId().toString())
                                    .build()
                    );
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                return;

            case "createNote":
                waitNote = true;
                try {
                    execute(
                            SendMessage.builder()
                                    .text("Введите текст заметки")
                                    .chatId(message.getChatId().toString())
                                    .build()
                    );
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                return;

        }

    }

    private void handleMessage(Message message) throws Exception {
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command = message
                        .getText()
                        .substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                switch (command) {
                    case "/start":
                    case "/exit":
                        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                        buttons.add(
                                Collections.singletonList(
                                        InlineKeyboardButton.builder().text("Начать заново").callbackData("/next").build()
                                )
                        );
                        try {
                            execute(
                                    SendMessage.builder()
                                            .text("Привет!\n" +
                                                    "Введи логин (почту) к личному кабинету УрФУ")
                                            .chatId(message.getChatId().toString())
                                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                                            .build()
                            );
                            return;
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }

                    case "/next":
                        List<List<InlineKeyboardButton>> buttons2 = new ArrayList<>();
                        buttons2.add(
                                Collections.singletonList(
                                        InlineKeyboardButton.builder().text("Узнать баллы в БРС").callbackData("items").build()
                                )

                        );
                        buttons2.add(
                                Collections.singletonList(
                                        InlineKeyboardButton.builder().text("Создать заметку в дневник").callbackData("note").build()
                                )
                        );
                        buttons2.add(
                                Collections.singletonList(
                                        InlineKeyboardButton.builder().text("Расписание").callbackData("schedule").build()
                                )
                        );
                        buttons2.add(
                                Collections.singletonList(
                                        InlineKeyboardButton.builder().text("Информация о мероприятиях").callbackData("event").build()
                                )
                        );


                        try {
                            execute(
                                    SendMessage.builder()
                                            .text("Ты успешно авторизовался")
                                            .chatId(message.getChatId().toString())
                                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons2).build())
                                            .build()
                            );
                            return;
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                }
            }

        }

        if (message.hasText()) {
            if(message.getText().contains("@") && message.getText().contains(".")) {
                waitPass = true;
                LOGIN = message.getText();
                try {
                    execute(
                            SendMessage.builder()
                                    .text("Введите пароль к личному кабинету")
                                    .chatId(message.getChatId().toString())
                                    .build()
                    );
                    return;
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }

            }
            if (waitNote) {
                RedisStorage.createFile(message.getChatId().toString(), message.getText());
                List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                buttons.add(
                        Collections.singletonList(
                                InlineKeyboardButton.builder().text("Посмотреть заметки").callbackData("showNotes").build()
                        )
                );
                buttons.add(
                        Collections.singletonList(
                                InlineKeyboardButton.builder().text("Назад").callbackData("/next").build()
                        )
                );

                try {
                    execute(
                            SendMessage.builder()
                                    .text("Заметка создана!")
                                    .chatId(message.getChatId().toString())
                                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                                    .build()
                    );
                    waitNote = false;
                    return;
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (message.hasText() && waitPass) {
            PASSWORD = message.getText();
            String status = MethodsAuth.checkPassword(LOGIN,PASSWORD);
            waitPass = false;
            if (status.equals("Вы успешно авторизовались!")) {
                List<List<InlineKeyboardButton>> buttons2 = new ArrayList<>();
                buttons2.add(
                        Collections.singletonList(
                                InlineKeyboardButton.builder().text("Узнать баллы в БРС").callbackData("items").build()
                        )

                );
                buttons2.add(
                        Collections.singletonList(
                                InlineKeyboardButton.builder().text("Создать заметку в дневник").callbackData("note").build()
                        )
                );
                buttons2.add(
                        Collections.singletonList(
                                InlineKeyboardButton.builder().text("Расписание").callbackData("schedule").build()
                        )
                );
                buttons2.add(
                        Collections.singletonList(
                                InlineKeyboardButton.builder().text("Информация о мероприятиях").callbackData("event").build()
                        )
                );
                buttons2.add(
                        Collections.singletonList(
                                InlineKeyboardButton.builder().text("Выйти из аккаунта").callbackData("/exit").build()
                        )
                );


                try {
                    execute(
                            SendMessage.builder()
                                    .text("Ты успешно авторизовался")
                                    .chatId(message.getChatId().toString())
                                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons2).build())
                                    .build()
                    );
                    return;
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    execute(
                            SendMessage.builder()
                                    .text("Вы ввели неверные данные!")
                                    .chatId(message.getChatId().toString())
                                    .build()
                    );
                    return;
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
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
}