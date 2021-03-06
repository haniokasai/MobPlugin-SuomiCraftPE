package suomicraftpe.mobplugin.utils;

import cn.nukkit.Server;
import cn.nukkit.utils.TextFormat;

import java.util.Random;

public class Utils {

    private static final Server SERVER = Server.getInstance();

    public static void logServerInfo (String text) {
        SERVER.getLogger().info(TextFormat.GOLD + "[MobPlugin] " + text);
    }

    private static final Random random = new Random(System.currentTimeMillis());

    public static int rand(int min, int max) {
        if (min == max) {
            return max;
        }
        return min + random.nextInt(max - min);
    }

    public static double rand(double min, double max){
        if(min == max){
            return max;
        }
        return min + Math.random() * (max-min);
    }

    public static boolean rand() {
        return random.nextBoolean();
    }

}
