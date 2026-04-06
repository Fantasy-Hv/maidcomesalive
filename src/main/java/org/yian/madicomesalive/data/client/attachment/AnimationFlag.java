package org.yian.madicomesalive.data.client.attachment;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashSet;
import java.util.Set;
// 存储状态，并维护转移
public class AnimationFlag {
    private Set<Integer> flags;
    public AnimationFlag(){
        flags = new HashSet<>();
    }

    public Set<Integer> getFlags() {
        return flags;
    }

    public void setFlags(Set<Integer> flags) {
        this.flags = flags;
    }

    public static final StreamCodec<FriendlyByteBuf, AnimationFlag> STREAM_CODEC =
            StreamCodec.composite(
                    // 使用 ByteBufCodecs 的 set 编解码器
                    ByteBufCodecs.collection(
                            HashSet::new,
                            ByteBufCodecs.INT
                    ),
                    AnimationFlag::getFlags,           // 获取 set 的方法
                    (Set<Integer> set) -> {  // 从 set 重建对象
                        AnimationFlag data = new AnimationFlag();
                        data.setFlags(set);
                        return data;
                    }
            );

    /**
     *
     * @param cur_near 1表示主人在附近，0表示不在附近
     */
    public void updateGreetingState(boolean cur_near){
        if (cur_near){
            if (flags.contains(AnimateConstants.CURRENT_MASTER_NEAR_FLAG))return;
            else { // 由不在变为在，设置打招呼
                flags.add(AnimateConstants.CURRENT_MASTER_NEAR_FLAG);
                flags.add(AnimateConstants.GREETING_DIRTY);
            }
        }else { // 主人离开了，清除状态位
            flags.remove(AnimateConstants.CURRENT_MASTER_NEAR_FLAG);
            flags.remove(AnimateConstants.GREETING_DIRTY);
        }

    }
}
