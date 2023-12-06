package com.gin.mobilefp_englishquizlet;

public class RequestHelper {
    public static final int MULTIPLE_CHOICE_RESULT = 0x001;

    public static class CommandFromResult{
        public static final int COMMAND_UNKNOWN = -1;
        public static final int COMMAND_RESTART_TEST = 0x0001;
        public static final int COMMAND_NEW_TEST = 0x0002;
    }
}
