package org.yian.madicomesalive.animation.predicate;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import org.yian.madicomesalive.Config;
import org.yian.madicomesalive.data.client.attachment.AttachmentRegister;

import java.util.HashMap;

public class CooldownManager {
    public static boolean checkAnimationCooldown(IMaid imaid,String animeName){
        HashMap<String, Long> cooldownTimestamp = imaid.asEntity().getData(AttachmentRegister.MAID_ANIMATION_TIMESTAMP.get());
        return (System.currentTimeMillis() - cooldownTimestamp.getOrDefault(animeName,System.currentTimeMillis()-Config.ANIMATION_COOLDOWN.get())> Config.ANIMATION_COOLDOWN.getAsInt());
    }

    public static void flushAnimationCooldown(IMaid imaid,String animeName){
        imaid.asEntity().getData(AttachmentRegister.MAID_ANIMATION_TIMESTAMP.get()).put(animeName,System.currentTimeMillis());
    }
}
