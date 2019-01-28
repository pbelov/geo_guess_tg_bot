package com.pbelov.java.tg.geo_guess_bot;

import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.PhotoMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.TextMessageReceivedEvent;

@SuppressWarnings({"FieldCanBeLocal", "unchecked", "ResultOfMethodCallIgnored"})
public class MessageListener implements Listener {
    private final String TAG = "MessageListener";

    public MessageListener() {
        CommandsHelper.restoreState();
    }


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
        CommandsHelper.addImage(event);
    }

    private void handleCommand(CommandMessageReceivedEvent event) {
        final String command = CommandsHelper.getCommandsData(event);

        final String COMMAND_ADD = "add";
        final String COMMAND_CANCEL = "cancel";
        final String COMMAND_NEXT = "next";
        final String COMMAND_SKIP = "skip";
        final String COMMAND_TOP = "top";
        final String COMMAND_STATS = "stats";
        final String COMMAND_RESET = "reset";

        if (command.equals(COMMAND_ADD)) {
            CommandsHelper.handleAddCommand(event);
        } else if (command.equals(COMMAND_NEXT)) {
            CommandsHelper.handleNextCommand(event);
        } else if (command.equals(COMMAND_SKIP)) {
            CommandsHelper.handleSkipCommand(event);
        } else if (command.equals(COMMAND_CANCEL)) {
            CommandsHelper.handleCancelCommand(event);
        } else if (command.equals(COMMAND_TOP)) {
            CommandsHelper.handleTopCommand(event);
        } else if (command.equals(COMMAND_STATS)) {
            CommandsHelper.handleStatsCommand(event);
        } else if (command.equals(COMMAND_RESET)) {
            CommandsHelper.handleResetCommand(event);
        }
    }

    private void handleMessage(TextMessageReceivedEvent event) {
        CommandsHelper.handleMessage(event);
    }
}
