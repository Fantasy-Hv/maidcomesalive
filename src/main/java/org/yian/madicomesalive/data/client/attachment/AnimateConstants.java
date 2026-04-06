package org.yian.madicomesalive.data.client.attachment;

public class AnimateConstants {
    //--------------客户端需要的状态--------------------
    public static final Integer GREETING_DIRTY = 3; //
    public static final int greeting_length = 2; // 两秒打招呼时间
    //---------------服务端需要的状态-------------------------------
    public static final Integer CURRENT_MASTER_NEAR_FLAG = 1; // 狗修金现在在身边
    //-----------------状态常量----------
    public static long HAD_GREETED = -2L;
    public static long NOT_GREET_YET = -1L;
}
