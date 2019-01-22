package com.pbelov.java.tg.geo_guess_bot;

import com.pbelov.java.tg.geo_guess_bot.Utils.StringUtils;
import com.pbelov.java.tg.geo_guess_bot.Utils.Utils;
import pro.zackpollard.telegrambot.api.chat.message.Message;
import pro.zackpollard.telegrambot.api.event.chat.message.TextMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.user.User;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ResultOfMethodCallIgnored")
class BaseEventsHelper {
    static final String GLOBAL = "global";
    static final String ME = "@pbelov";
    static final String RU_TAG = "ru";
    private static final String TAG = "BaseEventsHelper";
    static String chatName = null;
    static String messageText = null;
    static String senderUserName;
    static Message message;
    static User sender;
    static String argsString;
    static int hour, day;
    static Map<String, Map<String, Integer>> messagesMap = new HashMap<>();
    // if true - it will work, otherwise - not. obviously.

    static Map<String, Boolean> botStatus = new HashMap<String, Boolean>() {{
        put(GLOBAL, false);
    }};


    static void getBaseData(TextMessageReceivedEvent event, String type) {
        chatName = event.getChat().getName();
        message = event.getMessage();
        sender = message.getSender();
        senderUserName = sender.getUsername();
        String personName = sender.getFullName() + " (" + senderUserName + ")";
        messageText = event.getContent().getContent().trim();

        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        day = calendar.get(Calendar.DAY_OF_WEEK);

        Utils.println(TAG, "[" + type + "] " + chatName + ", " + StringUtils.getCurrentTimeStamp() + "; " + personName + ": " + messageText, chatName);
    }

    static boolean checkBotStatus() {
        // if debug mode, react only on owner
        if (!botStatus.get(GLOBAL) || (Main.DEBUG && !senderUserName.equals(ME))) {
            return false;
        } else {
            Utils.println(TAG, "debug mode = " + Main.DEBUG + ", bot status = " + botStatus.get(GLOBAL));
            return true;
        }
    }
}
