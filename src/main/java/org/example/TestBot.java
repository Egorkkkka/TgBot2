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
    boolean waitLogin = false;
    boolean waitNote = false;

    protected String LOGIN = "";
    protected String PASSWORD = "";
    HashMap<String, String> idChatLogPasMap = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            try {
                handleMessage(update.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if (update.hasCallbackQuery()) {
            try {
                handleCallback(update.getCallbackQuery());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleCallback(CallbackQuery callbackQuery) throws Exception {
        Message message = callbackQuery.getMessage();
        String idChat = String.valueOf(message.getChatId());
        String[] param = callbackQuery.getData().split(":");
        String action = param[0];
        switch (action) {
            case "/exit", "/start" -> {
                waitLogin = true;
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
            }
            case "schedule" -> {
                List<List<InlineKeyboardButton>> buttons2 = new ArrayList<>();
                buttons2.add(
                        List.of(
                                InlineKeyboardButton.builder().text("Назад").callbackData("/back").build(),
                                InlineKeyboardButton.builder().text("Загрузить").callbackData("").url("https://calendar.istudent.urfu.ru/get?id=98c3a3c5aaeef1e7e94e4b689cadcc8619c0a588").build()
                        )
                );
                try {
                    execute(
                            SendMessage.builder()
                                    .text("Расписание занятий")
                                    .chatId(message.getChatId().toString())
                                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons2).build())
                                    .build()
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            case "items" -> {
                try {
                    String logAndPas = getLogPasFromMap(message.getChatId().toString());
                    HashMap<String, String> subjects = BRS.parseBrs(logAndPas.split("#")[0], logAndPas.split("#")[1]);
                    List<List<InlineKeyboardButton>> buttons3 = new ArrayList<>();
                    buttons3.add(
                            Arrays.asList(
                                    InlineKeyboardButton.builder().text("Назад").callbackData("/back").build()
                            )
                    );
                    execute(
                            SendMessage.builder()
                                    .text("Информация об успеваемости:")
                                    .chatId(message.getChatId().toString())
                                    .build()
                    );
                    String allSubj = "";
                    for (Map.Entry<String, String> subject : subjects.entrySet()) {
                        allSubj += subject.getKey() + "\n" + subject.getValue() + "\n---------------------------------\n";
                    }
                    execute(
                            SendMessage.builder()
                                    .text(allSubj)
                                    .chatId(message.getChatId().toString())
                                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons3).build())
                                    .build()
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            case "note" -> {
                List<List<InlineKeyboardButton>> buttons4 = new ArrayList<>();
                buttons4.add(
                        Arrays.asList(
                                InlineKeyboardButton.builder().text("Посмотреть").callbackData("showNotes").build(),
                                InlineKeyboardButton.builder().text("Написать").callbackData("createNote").build(),
                                InlineKeyboardButton.builder().text("Назад").callbackData("/back").build()
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
            }
            case "event" -> {
                List<List<InlineKeyboardButton>> buttons5 = new ArrayList<>();
                buttons5.add(
                        List.of(
                                InlineKeyboardButton.builder().text("Назад").callbackData("/back").build()
                        )
                );
                ArrayList<String> list = Notifications.parseEvents(getLogPasFromMap(message.getChatId().toString()).split("#")[0],
                        getLogPasFromMap(message.getChatId().toString()).split("#")[1]);
                try {
                    execute(
                            SendMessage.builder()
                                    .text("Мероприятия: \n")
                                    .chatId(message.getChatId().toString())
                                    .build()
                    );
                    for (int i = 0; i <= 5; i++) {
                        if (i != 3) {
                            execute(
                                    SendMessage.builder()
                                            .text(list.get(i))
                                            .chatId(message.getChatId().toString())
                                            .build()
                            );
                        } else {
                            execute(
                                    SendMessage.builder()
                                            .text(list.get(i))
                                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons5).build())
                                            .chatId(message.getChatId().toString())
                                            .build()
                            );
                        }

                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            case "showNotes" -> {
                try {
                    execute(
                            SendMessage.builder()
                                    .text("Текст заметки: \n" + Storage.readFile(idChat))
                                    .chatId(message.getChatId().toString())
                                    .build()
                    );
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            case "createNote" -> {
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
            }
            case "/back" -> {
                List<List<InlineKeyboardButton>> buttonsMain = new ArrayList<>();
                buttonsMain.add(
                        Collections.singletonList(
                                InlineKeyboardButton.builder().text("Узнать баллы в БРС").callbackData("items").build()

                        )

                );
                buttonsMain.add(
                        Collections.singletonList(
                                InlineKeyboardButton.builder().text("Создать заметку в дневник").callbackData("note").build()
                        )
                );
                buttonsMain.add(
                        Collections.singletonList(
                                InlineKeyboardButton.builder().text("Расписание").callbackData("schedule").build()
                        )
                );
                buttonsMain.add(
                        Collections.singletonList(
                                InlineKeyboardButton.builder().text("Информация о мероприятиях").callbackData("event").build()
                        )
                );
                buttonsMain.add(
                        Collections.singletonList(
                                InlineKeyboardButton.builder().text("Выйти из аккаунта").callbackData("/exit").build()
                        )
                );
                try {
                    execute(
                            SendMessage.builder()
                                    .text("Меню: ")
                                    .chatId(message.getChatId().toString())
                                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttonsMain).build())
                                    .build()
                    );
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
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
                        try {
                            execute(
                                    SendMessage.builder()
                                            .text("Привет!\n" +
                                                    "Введи логин (почту) к личному кабинету УрФУ")
                                            .chatId(message.getChatId().toString())
                                            .build()
                            );
                            idChatLogPasMap.put(String.valueOf(message.getChatId()),"");
                            waitLogin = true;
                            return;
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                }
            }

        }

        if (message.hasText()) {
            if (waitLogin) {
                if (message.getText().contains("@") && message.getText().contains(".")) {
                    waitLogin = false;
                    waitPass = true;
                    LOGIN = message.getText();
                    for(Map.Entry<String, String> user : idChatLogPasMap.entrySet()) {
                        if (user.getKey().equals(message.getChatId().toString())) {
                            idChatLogPasMap.put(user.getKey(), LOGIN + "#");
                        }
                    }
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
                } else {
                    try {
                        execute(
                                SendMessage.builder()
                                        .text("Вы ввели неверный логин!")
                                        .chatId(message.getChatId().toString())
                                        .build()
                        );
                        return;
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (waitNote) {
                Storage.createFile(message.getChatId().toString(), message.getText());
                List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                buttons.add(
                        Collections.singletonList(
                                InlineKeyboardButton.builder().text("Посмотреть заметки").callbackData("showNotes").build()
                        )
                );
                buttons.add(
                        Collections.singletonList(
                                InlineKeyboardButton.builder().text("Назад").callbackData("/back").build()
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
            String status = "";
            PASSWORD = message.getText();
            for(Map.Entry<String, String> user : idChatLogPasMap.entrySet()) {
                if (user.getKey().equals(message.getChatId().toString())) {

                    String oldLogPas = idChatLogPasMap.get(user.getKey());
                    String newLogPas = oldLogPas + PASSWORD;
                    idChatLogPasMap.put(user.getKey(), newLogPas);
                    String loginFromMap = idChatLogPasMap.get(user.getKey()).split("#")[0]; //login#pass
                    String passwordFromMap = idChatLogPasMap.get(user.getKey()).split("#")[1]; //login#pass
                    status = MethodsAuth.checkPassword(loginFromMap, passwordFromMap);
                }
            }
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
                                    .text("Меню: ")
                                    .chatId(message.getChatId().toString())
                                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons2).build())
                                    .build()
                    );
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } else {
                List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                buttons.add(
                        Collections.singletonList(
                                InlineKeyboardButton.builder().text("Начать заново").callbackData("/start").build()
                        )
                );
                try {
                    execute(
                            SendMessage.builder()
                                    .text("Вы ввели неверные данные!")
                                    .chatId(message.getChatId().toString())
                                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                                    .build()
                    );
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

    public String getLogPasFromMap(String chatId) {
        String logAndPas = "";
        for(Map.Entry<String, String> user : idChatLogPasMap.entrySet()) {
            if (user.getKey().equals(chatId)) {
                logAndPas = idChatLogPasMap.get(chatId);
            }
        }
        return logAndPas;
    }
}