package com.example.telegrambotgetcurrency;

import com.example.telegrambotgetcurrency.config.BotConfig;
import com.example.telegrambotgetcurrency.model.Quote;
import com.example.telegrambotgetcurrency.service.QuoteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

@Component
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Quote quote = new Quote();
        String curr = "";
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start" -> startCommandReceived(chatId, update.getMessage().getFrom().getUserName());
                case "/quote" -> {
                    try {
                        curr = QuoteService.getQuote(quote);
                    } catch (IOException e) {
                        sendMessage(chatId, "На сегодня нет цитаток");
                    }
                    sendMessage(chatId, curr);
                }
                case "/goRoll" -> {
                    switch (update.getMessage().getFrom().getUserName()) {
                        case ("NowaGen") -> curr = "Сегодня вечерком покрутим @dusshub @opeth_24?";
                        case ("opeth_24") -> curr = "Сегодня вечерком покрутим @dusshub @NowaGen?";
                        case ("dusshub") -> curr = "Сегодня вечерком покрутим @NowaGen @opeth_24?";
                    }
                    sendMessage(chatId,curr);
                }
            }
        }
    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = "Че, " + name + ", надо";
        sendMessage(chatId, answer);
    }


    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }
}
