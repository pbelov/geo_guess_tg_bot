package com.pbelov.java.tg.geo_guess_bot;


import pro.zackpollard.telegrambot.api.TelegramBot;

public class Main {
    public static boolean DEBUG = false;

    private static void println(String str) {
        System.out.println(str);
    }

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        //This returns a logged in TelegramBot instance or null if the API key was invalid.
        TelegramBot telegramBot = TelegramBot.login(Tokens.API_KEY);

        if (telegramBot != null) {
            println("Started bot");
        } else {
            println("Incorrect API_KEY. Shut down...");
            System.exit(-1);
        }

        //This registers the MessageListener Listener to this bot.
        telegramBot.getEventsManager().register(new MessageListener());
//        telegramBot.getEventsManager().register(new CommandListener(telegramBot));
        //This method starts the retrieval of updates.
        //The boolean it accepts is to specify whether to retrieve messages
        //which were sent before the bot was started but after the bot was last turned off.
        telegramBot.startUpdates(true);
        println("Started updates listener");

        //The following while(true) loop is simply for keeping the java application alive.
        //You can do this however you like, but none of the above methods are blocking and
        //so without this code the bot would simply boot then exit.
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
