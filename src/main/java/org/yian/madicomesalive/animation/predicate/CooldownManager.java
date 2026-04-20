package org.yian.madicomesalive.animation.predicate;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import org.yian.madicomesalive.Config;
import org.yian.madicomesalive.MaidComesAliveClient;
import org.yian.madicomesalive.network.attachment.AttachmentRegister;

import java.util.HashMap;

public class CooldownManager {
    public static boolean checkAnimationCooldown(IMaid imaid,String animeName){
        HashMap<String, Long> cooldownTimestamp = imaid.asEntity().getData(AttachmentRegister.MAID_ANIMATION_TIMESTAMP.get());
        if (!cooldownTimestamp.containsKey(animeName)){ //第一次
            flushAnimationCooldown(imaid,animeName);
            return true;
        }
        if (System.currentTimeMillis() - cooldownTimestamp.getOrDefault(animeName,-1L) > Config.ANIMATION_CUTE_COOLDOWN.getAsInt()*1000){
            flushAnimationCooldown(imaid,animeName); // 冷却结束
            return true;
        }
        return false;
    }

    public static void flushAnimationCooldown(IMaid imaid,String animeName){
        MaidComesAliveClient.LOGGER.info("flush cooldown of {} ",animeName);
        imaid.asEntity().getData(AttachmentRegister.MAID_ANIMATION_TIMESTAMP.get()).put(animeName,System.currentTimeMillis());
    }
}
