package com.pbelov.java.tg.geo_guess_bot;

@SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored", "StatementWithEmptyBody"})
class MessageHelper extends BaseEventsHelper {
    static final String BOT_PREFIX = "бот, ";
    static final String CMD_AGR = "агрись на ";
    static final String CMD_CANCEL = "хватит";
    static final String CMD_MUTE = "выключи ";
    static final String CMD_RATES = "курс";
    static final String CMD_TRANSLATE = "переведи";
    //TODO: add others
    static final String HNTR = "хнтр";
    static final String BOT = "@pbelov_bot";
    private static final String TAG = "MessageHelper";
    private static final String FPS_SUBDIR = "fps";
    private static long fasID = -1;
    private static long muteID = -1;
    private static String userToFas = null;
    private static String userToMute = null;

    private MessageHelper() {
    }


}
