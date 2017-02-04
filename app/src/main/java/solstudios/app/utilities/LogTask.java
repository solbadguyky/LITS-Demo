package solstudios.app.utilities;

import android.util.Log;

import java.util.ArrayList;


public class LogTask {
    public static final String TAG = "LITS";
    public static final String LOG_D = "Log.d";
    public static final String LOG_I = "Log.i";
    public static final String LOG_E = "Log.e";
    public static final String LOG_V = "Log.v";

    private boolean isEnableLog = true;

    public LogTask() {

    }

    /**
     * @param logString Log String data
     * @param logTAB    Log Screen Name
     * @param logType   LogType: LOG_D,LOG_I,LOG_E,LOG_V
     */
    public LogTask(String logString, String logTAB, String logType) {
        if (isEnableLog)
            switch (logType) {
                case LOG_D:
                    Log.d(TAG, logTAB + ": " + logString);
                    break;
                case LOG_I:
                    Log.i(TAG, logTAB + ": " + logString);
                    break;
                case LOG_E:
                    Log.e(TAG, logTAB + ": " + logString);
                    break;
                case LOG_V:
                    Log.v(TAG, logTAB + ": " + logString);
                    break;
            }
    }

    public LogTask(String logTAB, String logBlockName, String... logStrings) {
        if (isEnableLog) {
            Log.d(TAG, logTAB + "|" + logBlockName);
            Log.d(TAG, logBlockName + "----------Begin Block-Logs----------");
            for (String singleLog : logStrings) {
                Log.d(TAG, logBlockName + "-" + singleLog);
            }
            Log.d(TAG, logBlockName + "----------End Block-Logs----------");
        }
    }

    /**
     * Divides a string into chunks of a given character size.
     *
     * @param text      String text to be sliced
     * @param sliceSize int Number of characters
     * @return ArrayList<String> Chunks of strings
     */
    private ArrayList<String> splitString(String text, int sliceSize) {
        ArrayList<String> textList = new ArrayList<String>();
        String aux;
        int left = -1, right = 0;
        int charsLeft = text.length();
        while (charsLeft != 0) {
            left = right;
            if (charsLeft >= sliceSize) {
                right += sliceSize;
                charsLeft -= sliceSize;
            } else {
                right = text.length();
                aux = text.substring(left, right);
                charsLeft = 0;
            }
            aux = text.substring(left, right);
            textList.add(aux);
        }
        return textList;
    }

    /**
     * Divides a string into chunks.
     *
     * @param text String text to be sliced
     * @return ArrayList<String>
     */
    private ArrayList<String> splitString(String text) {
        return splitString(text, 80);
    }

    /**
     * Divides the string into chunks for displaying them into the Eclipse's
     * LogCat.
     *
     * @param text The text to be split and shown in LogCat
     * @param tag  The tag in which it will be shown.
     */
    public void splitAndLog(String tag, String text) {
        if (isEnableLog) {
            ArrayList<String> messageList = splitString(text);
            for (String message : messageList) {
                Log.d(tag, message);
            }
        }
    }
}
