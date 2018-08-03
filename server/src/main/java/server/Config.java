package server;

import java.util.Random;

public class Config {
    private static boolean debug = false;

    public static void setDebug(boolean debug) {
        Config.debug = debug;
    }

    public static boolean isDebug() {
        return debug;
    }

    static String faces[] = {
            "(╯°□°）╯︵ ┻━┻",
            "¯\\_(ツ)_/¯",
            "( ͡° ͜ʖ ͡°)",
            "ಠ_ಠ",
            "༼ つ ◕_◕ ༽つ",
            "(ง'̀-'́)ง",
            "(ಥ﹏ಥ)",
            "(ノಠ益ಠ)ノ彡┻━┻",
            "♪~ ᕕ(ᐛ)ᕗ",
            "༼ つ ಥ_ಥ ༽つ",
            "~(˘▾˘~)",
            "\\ (•◡•) /",
            "(~˘▾˘)~",
            "ヾ(⌐■_■)ノ♪",
            "\\ (•◡•) /",
            "ಠ~ಠ"
    };

    public static String getRandomFace(){
        int i = new Random().nextInt(faces.length);
        return faces[i];
    }
}
