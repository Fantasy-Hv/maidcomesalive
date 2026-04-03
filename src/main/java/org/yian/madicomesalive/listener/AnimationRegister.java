package org.yian.madicomesalive.listener;

import com.github.tartaricacid.touhoulittlemaid.client.resource.GeckoModelLoader;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.file.AnimationFile;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.yian.madicomesalive.MaidComesAlive;
import org.yian.madicomesalive.MaidComesAliveClient;
import org.yian.madicomesalive.utils.AnimationDataMapper;


import java.io.IOException;
import java.io.InputStream;
//DefaultGeckoAnimationEvent
public class AnimationRegister {
    public static ResourceLocation MAID_DEFAULT_ANIME_LOC = ResourceLocation.fromNamespaceAndPath(MaidComesAlive.MODID,
            "animations/winefox.animation.json"
    );

    public static void onGeckoAnimationEvent () {
            MaidComesAlive.LOGGER.info("捕获默认动画文件加载事件");
            try {
                // 从资源管理器读取
                InputStream animationJsonFileStream = Minecraft.getInstance()
                        .getResourceManager()
                        .getResource(MAID_DEFAULT_ANIME_LOC)
                        .orElseThrow(() -> new RuntimeException("找不到动画文件：" + MAID_DEFAULT_ANIME_LOC))
                        .open();
                AnimationFile file = AnimationDataMapper.getAnimationFile(animationJsonFileStream);
                MaidComesAlive.LOGGER.info("read animation: {}\n", file.animations());
                MaidComesAlive.LOGGER.info("originnal animations :{}",GeckoModelLoader.DEFAULT_MAID_ANIMATION_FILE.animations());
                animationJsonFileStream.close();
                animationJsonFileStream = Minecraft.getInstance()
                        .getResourceManager()
                        .getResource(MAID_DEFAULT_ANIME_LOC)
                        .orElseThrow(() -> new RuntimeException("找不到动画文件：" + MAID_DEFAULT_ANIME_LOC))
                        .open();
                GeckoModelLoader.mergeAnimationFile(animationJsonFileStream, GeckoModelLoader.DEFAULT_MAID_ANIMATION_FILE);
                animationJsonFileStream.close();
                MaidComesAlive.LOGGER.info("成功加载动画：" + MAID_DEFAULT_ANIME_LOC);

            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}

