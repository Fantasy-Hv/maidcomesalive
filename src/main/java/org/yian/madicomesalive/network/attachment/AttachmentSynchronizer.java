package org.yian.madicomesalive.network.attachment;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import org.yian.madicomesalive.network.attachment.data.AnimationFlag;

import java.util.Set;

public class AttachmentSynchronizer {
    /**
     * 将女仆实体的附加数据同步给客户端，以此控制客户端动画的播放行为
     * @param maid
     * @param flags
     */
    public static void setCliFlags(EntityMaid maid, Set<Integer> flags){
        AnimationFlag animationFlag = maid.getData(AttachmentRegister.ANIMATION_FLAGS);
        animationFlag.getFlags().addAll(flags);
        maid.setData(AttachmentRegister.ANIMATION_FLAGS.get(), animationFlag);
    }
    public static void remCliFlags(EntityMaid maid, Set<Integer> flags){
        AnimationFlag animationFlag = maid.getData(AttachmentRegister.ANIMATION_FLAGS);
        animationFlag.getFlags().removeAll(flags);
        maid.setData(AttachmentRegister.ANIMATION_FLAGS.get(), animationFlag);
    }
}
