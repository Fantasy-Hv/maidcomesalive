package org.yian.madicomesalive.network.attachment.data;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashSet;
import java.util.Set;
// 存储状态,作为客户端控制动画的判据
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

}
