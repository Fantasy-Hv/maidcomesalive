package org.yian.madicomesalive.data.client.attachment;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.yian.madicomesalive.MaidComesAlive;

import java.util.HashMap;
import java.util.function.Supplier;
//本模组用到的实体状态
public class AttachmentRegister {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS_REGISTRY =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MaidComesAlive.MODID);
    // 女仆每个动画上次播放开始的时间戳。
    public static final Supplier<AttachmentType<HashMap<String ,Long>>> MAID_ANIMATION_TIMESTAMP = ATTACHMENTS_REGISTRY.register(
            "animation_timestamp", () -> AttachmentType.builder(()->new HashMap<String ,Long>()).build()
    );
    public static final Supplier<AttachmentType<AnimationFlag>> ANIMATION_FLAGS = ATTACHMENTS_REGISTRY.register("animation_flag",
            ()-> AttachmentType.builder(AnimationFlag::new).sync(AnimationFlag.STREAM_CODEC)
                    .build());
}
