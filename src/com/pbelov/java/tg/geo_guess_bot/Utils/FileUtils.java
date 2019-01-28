package com.pbelov.java.tg.geo_guess_bot.Utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.nio.file.Paths;
import java.util.HashMap;

public class FileUtils {
    public static final File WORKING_DIR = new File(Paths.get("").toAbsolutePath().toString());
    private static final String TAG = "FileUtils";
    private static final Charset[] charsets = {Charset.forName("UTF-8"), Charset.forName("ISO-8859-1")};

    private FileUtils() {}

    public static void appendStringToFile(String string, File outputFile) {
        writeStringToFile(string, outputFile, true);
    }

    public static void writeStringToFile(String string, File outputFile) {
        writeStringToFile(string, outputFile, false);
    }

    private static void writeStringToFile(String string, File outputFile, boolean append) {
        BufferedWriter out = null;
        Exception exception = null;

        //try to write with each supported charset until we find one that works
        for (Charset charset : charsets) {
            try {
                CharsetEncoder encoder = charset.newEncoder();
                encoder.onMalformedInput(CodingErrorAction.REPORT);
                encoder.onUnmappableCharacter(CodingErrorAction.REPORT);
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile, append), encoder));
                out.append(string);
                return;
            } catch (Exception e) {
                exception = e;
            } finally {
                try {
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                    if (!append) {
                        System.out.println("File " + outputFile.getName() + " has written");
                    }
                } catch (IOException e) {
                    StringUtils.handleException(TAG, e);
                }
            }
        }

        //if no charsets worked, print exception and give up
        Utils.error(TAG, "fail: " + exception);
    }

    private static String readStringFromStream(InputStream is) {
        BufferedReader in = null;
        StringBuilder buf = new StringBuilder();

        //try to read with each supported charset until we find one that works
        Exception exception = null;
        for (Charset charset : charsets) {
            try {
                CharsetDecoder decoder = charset.newDecoder();
                decoder.onMalformedInput(CodingErrorAction.REPORT);
                decoder.onUnmappableCharacter(CodingErrorAction.REPORT);
                in = new BufferedReader(new InputStreamReader(is, decoder));

                String str;
                boolean isFirst = true;
                while ((str = in.readLine()) != null) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        buf.append('\n');
                    }

                    buf.append(str);
                }
                return buf.toString();

            } catch (Exception e) {
                exception = e;
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        Utils.error(TAG, "Error closing stream");
                    }
                }
            }

        }

        Utils.error(TAG, "Error opening or reading stream: " + exception);
        return null;
    }

    public static String loadFileAsString(File file) {
        Utils.error(TAG, "loadFileAsString: file name = " + file.getName());
        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (IOException e) {
            Utils.error(TAG, "Error opening or reading file " + file.getPath() + ": " + e);
        }

        return readStringFromStream(is);
    }

    public static void saveSerializable(Serializable prevDayMap, String name) {
        File file = new File(name);
        if (file.exists()) {
            file.delete();
        }

        ObjectOutputStream oos = null;
        FileOutputStream fout;
        try {
            fout = new FileOutputStream(name, true);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(prevDayMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static HashMap loadSerializable(String name) {
        HashMap readCase = null;

        if (new File(name).exists()) {
            ObjectInputStream objectinputstream = null;
            try {
                FileInputStream streamIn = new FileInputStream(name);
                objectinputstream = new ObjectInputStream(streamIn);
                readCase = (HashMap)objectinputstream.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (objectinputstream != null) {
                    try {
                        objectinputstream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return readCase;
    }
}
