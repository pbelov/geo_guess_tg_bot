package com.pbelov.java.tg.geo_guess_bot;

import com.pbelov.java.tg.geo_guess_bot.Utils.Utils;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

import java.util.Arrays;

class CommandsHelper extends BaseEventsHelper {
    private static final String TAG = "MessageHelper";

    private CommandsHelper() {
    }

    static String getCommandsData(CommandMessageReceivedEvent event) {
        // command params
        String[] args = event.getArgs();
        argsString = event.getArgsString();
        String command = event.getCommand();

        Utils.println(TAG, "handleCommand: args = " + Arrays.toString(args) + ", argsString = " + argsString + ", command = " + command);

        return command;
    }

    public static void handleAddCommand(CommandMessageReceivedEvent event) {

    }

    public static void handleNextCommand(CommandMessageReceivedEvent event) {

    }

    public static void hanndleSkipCommand(CommandMessageReceivedEvent event) {

    }
}
