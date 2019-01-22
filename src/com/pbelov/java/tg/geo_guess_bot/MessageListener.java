package com.pbelov.java.tg.geo_guess_bot;

import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.PhotoMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.TextMessageReceivedEvent;

import java.io.IOException;

@SuppressWarnings({"FieldCanBeLocal", "unchecked", "ResultOfMethodCallIgnored"})
public class MessageListener implements Listener {
    private final String TAG = "MessageListener";

    @Override
    public void onCommandMessageReceived(CommandMessageReceivedEvent event) {
        BaseEventsHelper.getBaseData(event, "command");
        handleCommand(event);
    }

    @Override
    public void onTextMessageReceived(TextMessageReceivedEvent event) {
        BaseEventsHelper.getBaseData(event, "text message");
        handleMessage(event);
    }

    @Override
    public void onPhotoMessageReceived(PhotoMessageReceivedEvent event) {
        try {
            MessageHelper.analyzeImage(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleCommand(CommandMessageReceivedEvent event) {
        final String command = CommandsHelper.getCommandsData(event);

        final String COMMAND_ADD = "add";
        final String COMMAND_NEXT = "next";
        final String COMMAND_SKIP = "skip";

        if (command.equals(COMMAND_ADD)) {
            CommandsHelper.handleAddCommand(event);
        } else if (command.equals(COMMAND_NEXT)) {
            CommandsHelper.handleNextCommand(event);
        } else if (command.equals(COMMAND_SKIP)) {
            CommandsHelper.hanndleSkipCommand(event);
        }
    }

    private void handleMessage(TextMessageReceivedEvent event) {
        String messageText = MessageHelper.getMessagesData();

        if (!BaseEventsHelper.checkBotStatus()) {
            return;
        }


    }
}
