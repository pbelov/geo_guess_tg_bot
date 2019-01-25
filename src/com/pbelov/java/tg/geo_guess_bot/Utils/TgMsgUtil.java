package com.pbelov.java.tg.geo_guess_bot.Utils;

import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.MessageReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.TextMessageReceivedEvent;

/**
 * Created by pbelov on 10/10/2016.
 */
public class TgMsgUtil {
    private TgMsgUtil() {}

    public static void replyInChat(MessageReceivedEvent event, String message) {
        SendableTextMessage sendableMessage = SendableTextMessage.builder()
                .message(message)
                .replyTo(event.getMessage())
                .build();
        event.getChat().sendMessage(sendableMessage);
    }

    public static void sendToChat(MessageReceivedEvent event, String message) {
        SendableTextMessage sendableMessage = SendableTextMessage.builder()
                .message(message)
                .build();
        event.getChat().sendMessage(sendableMessage);
    }
}
