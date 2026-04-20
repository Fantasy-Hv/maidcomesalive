package org.yian.madicomesalive.animation.predicate;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import org.yian.madicomesalive.data.client.attachment.AnimateConstants;
import org.yian.madicomesalive.network.attachment.AttachmentRegister;

import java.util.Set;

public class AnimatePredicates {

    public static boolean checkMasterComeBack(IMaid iMaid){
        Set<Integer> flags = iMaid.asEntity().getData(AttachmentRegister.ANIMATION_FLAGS.get()).getFlags();
        return flags.contains(AnimateConstants.GREETING_DIRTY); // 之前不在身边而现在在身边,需要打招呼
    }
}
