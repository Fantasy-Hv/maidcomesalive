package org.yian.madicomesalive.animation;

import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.AnimationManager;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.AnimationState;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.file.AnimationFile;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.resource.GeckoLibCache;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.yian.madicomesalive.MaidComesAlive;
import org.yian.madicomesalive.MaidComesAliveClient;
import org.yian.madicomesalive.utils.AnimationDataUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


public class AnimationRegister {
    public static ResourceLocation MAID_DEFAULT_ANIME_LOC = ResourceLocation.fromNamespaceAndPath(MaidComesAlive.MODID,
            "animations/winefox.animation.json"
    );


    public static void registerAnimationStates(){
        AnimationManager manager = AnimationManager.getInstance();
        if (manager==null) return;
        for(AnimationState state: AnimationStates.animations){
            manager.register(state);
            MaidComesAliveClient.LOGGER.info("add animate state {}",state.getAnimationName());
        }
        MaidComesAliveClient.LOGGER.info("additional animation states registered");

    }
    public static void mergeAnimationResource(){
        new Thread(()->{
            try {
                Thread.sleep(10000); // 等待女仆模组初始化动画资源（因为你是在异步线程中，主线程的我监听不到:(
                InputStream animationJsonFileStream = Minecraft.getInstance()
                    .getResourceManager()
                    .getResource(MAID_DEFAULT_ANIME_LOC)
                    .orElseThrow(() -> new RuntimeException("找不到动画文件：" + MAID_DEFAULT_ANIME_LOC))
                    .open();
                Map<ResourceLocation, AnimationFile> allAnimations =
                        GeckoLibCache.getInstance().getAnimations();
                if (allAnimations==null||allAnimations.isEmpty())return;
                AnimationFile animationFile = AnimationDataUtils.getAnimationFileObj(animationJsonFileStream);
                for(ResourceLocation location: allAnimations.keySet()){
                    if (location.getPath().contains("wine")) {
                        AnimationDataUtils.mergeAnimationFile(allAnimations.get(location),animationFile);
                        MaidComesAlive.LOGGER.info("成功将动画合并入模型：" + location);
                    }
                }
            registerAnimationStates();
            animationJsonFileStream.close();
            } catch (InterruptedException | IOException e) {
                MaidComesAliveClient.LOGGER.error("动画资源添加失败了！！");
                MaidComesAliveClient.LOGGER.error(e.getMessage());
            }
        }).start();
    }
}

