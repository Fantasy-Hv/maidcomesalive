package org.yian.madicomesalive.data.client.attachment;

public class AnimateConstants {
    //--------------客户端需要的状态--------------------
    public static  Integer GREETING_DIRTY = 3; //
    public static  int greeting_length = 2; // 两秒打招呼时间
    //---------------服务端需要的状态-------------------------------
    public static  int GREETING_INTERVAL = 750000;//，默认打招呼冷却时间
    //-----------------状态常量----------
    public static int HAD_GREETED = -2;
    public static int NOT_GREET_YET = -1;
}
