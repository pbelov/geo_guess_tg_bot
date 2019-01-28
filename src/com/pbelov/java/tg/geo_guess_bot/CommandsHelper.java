package com.pbelov.java.tg.geo_guess_bot;

import com.pbelov.java.tg.geo_guess_bot.Utils.FileUtils;
import com.pbelov.java.tg.geo_guess_bot.Utils.TgMsgUtil;
import com.pbelov.java.tg.geo_guess_bot.Utils.Utils;
import pro.zackpollard.telegrambot.api.chat.message.content.type.PhotoSize;
import pro.zackpollard.telegrambot.api.chat.message.send.InputFile;
import pro.zackpollard.telegrambot.api.chat.message.send.SendablePhotoMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.PhotoMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.TextMessageReceivedEvent;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class CommandsHelper extends BaseEventsHelper {
    private static final String TAG = "MessageHelper";
    private static final String GUESSES_SUBDIR = "guesses";
    private static HashMap<Long, State> mapStates = new HashMap<>();
    // last correct guess by user
    private static HashMap<Long, Integer> mapGuessID = new HashMap<>();

    private static HashMap<Long, AddState> mapAddStates = new HashMap<>();
    // general quest state per user
    private static HashMap<Long, GuessState> mapGuessStates = new HashMap<>();

    private static FilenameFilter filenameFilter = (dir, name) -> (name.endsWith(".jpg"));

    private CommandsHelper() {
    }

    private static State getCurrentState() {
        return mapStates.get(senderUserID);
    }

    private static AddState getCurrentAddState() {
        return mapAddStates.get(senderUserID);
    }

    private static GuessState getCurrentGuessState() {
        return mapGuessStates.get(senderUserID);
    }

    private static Integer getCurrentGuessID() {
        Integer t =  mapGuessID.get(senderUserID);
        if (t == null) {
            t = 0;
        } else {
            t++;
        }
        return t;
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
        if (getCurrentState() == null || getCurrentState() == State.IDLE) {
            addQuest(event);
        } else if (getCurrentState() == State.ADD) {
            TgMsgUtil.sendToChat(event, "Ты уже добавляешь угадайку");
        } else {
            TgMsgUtil.sendToChat(event, "Закончи текущую угадайку сначала");
        }
    }

    private static void addQuest(CommandMessageReceivedEvent event) {
        TgMsgUtil.sendToChat(event, "Пришли фотографию места для угадайки");
//        mapGuessID.put(senderUserID, getLatestQuestId());
        mapStates.put(senderUserID, State.ADD);
        mapAddStates.put(senderUserID, AddState.IMAGE);
    }

    public static void handleCancelCommand(CommandMessageReceivedEvent event) {
        if (getCurrentState() == State.ADD) {
            TgMsgUtil.sendToChat(event, "Ну ок");
            mapStates.put(senderUserID, State.IDLE);
            mapAddStates.put(senderUserID, AddState.IDLE);
        } else {
            TgMsgUtil.sendToChat(event, "Нечего отменять");
        }
    }

    public static void handleNextCommand(CommandMessageReceivedEvent event) {
        if (getCurrentState() == null || getCurrentState() == State.IDLE) {
            mapStates.put(senderUserID, State.GUESS);
            mapGuessStates.put(senderUserID, GuessState.Q);
            nextGuess(event);
        } else if (getCurrentState() == State.GUESS) {
            TgMsgUtil.sendToChat(event, "Отгадай текущее сначала");
        } else {
            TgMsgUtil.sendToChat(event, "Закончи добавление угадайки сначала");
        }

    }

    public static void handleSkipCommand(CommandMessageReceivedEvent event) {
        if (getCurrentState() == State.GUESS) {
//            mapStates.put(senderUserID, State.GUESS);
            TgMsgUtil.sendToChat(event, "Лооох!");
            skipGuess(event);
        } else if (getCurrentState() == State.ADD) {
            TgMsgUtil.sendToChat(event, "Закончи добавление угадайки сначала!");
        } else {
            TgMsgUtil.sendToChat(event, "Нечего скипать");
        }
    }

    public static void handleTopCommand(CommandMessageReceivedEvent event) {

    }

    public static void handleStatsCommand(CommandMessageReceivedEvent event) {

    }

    private static void skipGuess(CommandMessageReceivedEvent event) {
        mapStates.put(senderUserID, State.IDLE);
        mapGuessID.put(senderUserID, getCurrentGuessID());
        mapGuessStates.put(senderUserID, GuessState.Q);
        nextGuess(event);
    }

    private static void nextGuess(CommandMessageReceivedEvent event) {
        if (getCurrentGuessState() == GuessState.Q) {
            String nextGuessId = getNextQuestId();
            if (nextGuessId == null) {
                TgMsgUtil.sendToChat(event, "Ты всё отгадал, новых угадаек пока нет, попробуй позже");
                mapStates.put(senderUserID, State.IDLE);
                mapAddStates.put(senderUserID, AddState.IDLE);
            } else {
                File imagefile = new File(GUESSES_SUBDIR, nextGuessId + "_image.jpg");
                InputFile inputFile = new InputFile(imagefile);
                SendablePhotoMessage sendablePhotoMessage = SendablePhotoMessage.builder().photo(inputFile).build();
                event.getChat().sendMessage(sendablePhotoMessage);

                String qText = FileUtils.loadFileAsString(new File(GUESSES_SUBDIR, nextGuessId + "_q.txt"));
                TgMsgUtil.sendToChat(event, qText);
                mapGuessStates.put(senderUserID, GuessState.A);
            }
        } else {
            TgMsgUtil.sendToChat(event, "тут должен быть вопрос с картинкой");
        }

    }

    private static String getNextQuestId() {
        File guessesFile = new File(GUESSES_SUBDIR);
        if (!guessesFile.exists()) {
            guessesFile.mkdirs();
            guessesFile.mkdir();
        }
        Integer lastIndex = mapGuessID.get(senderUserID);
        if (lastIndex == null) {
            lastIndex = 0;
        } else {
            lastIndex++;
        }

        String guessesId[] = guessesFile.list(filenameFilter);
        if (guessesId.length == 0 || lastIndex >= guessesId.length) {
            return null;
        } else {
            return guessesId[lastIndex].replaceAll("_image.jpg", "");
        }
    }

    public static void addImage(PhotoMessageReceivedEvent event) {
        if (getCurrentState() == State.ADD) {
            if (getCurrentAddState() == AddState.IMAGE) {
                PhotoSize[] photoSize = event.getContent().getContent();
                File infile = new File(GUESSES_SUBDIR, getLatestQuestId() + "_image.jpg");
                photoSize[photoSize.length - 1].downloadFile(event.getMessage().getBotInstance(), infile);
                TgMsgUtil.sendToChat(event, "Теперь напиши сам вопрос: что нужно отгадать");
                mapAddStates.put(senderUserID, AddState.QUESTION);
            } else {
                TgMsgUtil.sendToChat(event, "Я жду картинку");
            }
        } else {
            //
        }
    }

    public static void addQuestion(TextMessageReceivedEvent event) {
        if (getCurrentAddState() == AddState.QUESTION) {
            File questionFile = new File(GUESSES_SUBDIR, (getLatestQuestId() - 1) + "_q.txt");
            FileUtils.writeStringToFile(messageText, questionFile);
            TgMsgUtil.sendToChat(event, "Отлично! Теперь ответ. Чем конкретнее, тем лучше, например название улицы и номер дома.");
            mapAddStates.put(senderUserID, AddState.ANSWER);
        } else {
            TgMsgUtil.sendToChat(event, "Я жду вопрос");
        }
    }

    public static void addAnswer(TextMessageReceivedEvent event) {
        if (getCurrentAddState() == AddState.ANSWER) {
            File questionFile = new File(GUESSES_SUBDIR, (getLatestQuestId() - 1) + "_a.txt");
            FileUtils.writeStringToFile(messageText, questionFile);
            TgMsgUtil.sendToChat(event, "Угадайка принята, спасибо!");
            mapAddStates.put(senderUserID, AddState.IDLE);
            mapStates.put(senderUserID, State.IDLE);
        } else {
            TgMsgUtil.sendToChat(event, "Я жду ответ");
        }
    }

    public static void handleMessage(TextMessageReceivedEvent event) {
        if (getCurrentState() == State.ADD) {
            if (getCurrentAddState() == AddState.IDLE) {
                TgMsgUtil.sendToChat(event, "Окей, присылай фотку");
                mapAddStates.put(senderUserID, AddState.QUESTION);
            } else if (getCurrentAddState() == AddState.QUESTION) {
                addQuestion(event);
            } else if (getCurrentAddState() == AddState.ANSWER) {
                addAnswer(event);
            }
        } else if (getCurrentState() == State.GUESS) {
            if (getCurrentGuessState() == GuessState.A) {
                String qText = FileUtils.loadFileAsString(new File(GUESSES_SUBDIR, getCurrentGuessID() + "_a.txt"));
                if (messageText.equalsIgnoreCase(qText.replaceAll(",", ""))) {
                    TgMsgUtil.sendToChat(event, "Верно!");
                    mapGuessID.put(senderUserID, getCurrentGuessID());
                    mapGuessStates.put(senderUserID, GuessState.IDLE);
                    mapStates.put(senderUserID, State.IDLE);
                } else {
                    TgMsgUtil.sendToChat(event, "Неверно!");
                }
            }
        }

        saveSate();
    }

    private static int getLatestQuestId() {
        return new File(GUESSES_SUBDIR).list(filenameFilter).length;
    }

//    private static String getQuestSuffix() {
//        String currentQuestID = mapGuessID.get(senderUserID);
//
//        if (currentQuestID == null) {
//            currentQuestID = GUESSES_SUBDIR + File.separator + new File(GUESSES_SUBDIR).list()[0];
//        } else {
//            currentQuestID = GUESSES_SUBDIR + File.separator + mapGuessID.get(senderUserID);
//        }
//
//        return currentQuestID.split("_")[0] + "_" + currentQuestID.split("_")[1];
//    }

    private static void saveSate() {
        FileUtils.saveSerializable(mapStates, "mapStates");
        FileUtils.saveSerializable(mapGuessID, "mapGuessID");
        FileUtils.saveSerializable(mapAddStates, "mapAddStates");
        FileUtils.saveSerializable(mapGuessStates, "mapGuessStates");
    }

    public static void restoreState() {
        mapStates = FileUtils.loadSerializable("mapStates");
        if (mapStates == null) {
            mapStates = new HashMap<>();
        }
        mapGuessID = FileUtils.loadSerializable("mapGuessID");
        if (mapGuessID == null) {
            mapGuessID = new HashMap<>();
        }
        mapAddStates = FileUtils.loadSerializable("mapAddStates");
        if (mapAddStates == null) {
            mapAddStates = new HashMap<>();
        }
        mapGuessStates = FileUtils.loadSerializable("mapGuessStates");
        if (mapGuessStates == null) {
            mapGuessStates = new HashMap<>();
        }

    }

    enum AddState {
        IDLE,
        IMAGE,
        QUESTION,
        ANSWER
    }

    enum GuessState {
        Q,
        A,
        IDLE,
    }

    enum State {
        ADD,
        GUESS,
        IDLE,
    }
}
