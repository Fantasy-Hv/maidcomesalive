package org.yian.madicomesalive.listener;

import com.github.tartaricacid.touhoulittlemaid.api.event.client.DefaultGeckoAnimationEvent;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.AnimationManager;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.AnimationState;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.file.AnimationFile;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.resource.GeckoLibCache;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import org.yian.madicomesalive.MaidComesAlive;
import org.yian.madicomesalive.MaidComesAliveClient;
import org.yian.madicomesalive.entity.AnimationStates;
import org.yian.madicomesalive.utils.AnimationDataUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

//DefaultGeckoAnimationEvent
//@EventBusSubscriber()
public class AnimationRegister extends SimplePreparableReloadListener<Void> {
    public static ResourceLocation MAID_DEFAULT_ANIME_LOC = ResourceLocation.fromNamespaceAndPath(MaidComesAlive.MODID,
            "animations/winefox.animation.json"
    );
    //DefaultGeckoAnimationEvent event
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onGeckoAnimationEvent (DefaultGeckoAnimationEvent event) {
            MaidComesAliveClient.LOGGER.error("捕获默认动画文件加载事件");
            MaidComesAliveClient.LOGGER.error("捕获默认动画文件加载事件");
            MaidComesAliveClient.LOGGER.error("捕获默认动画文件加载事件");
//            new Thread(
//                    ()->{
//
//                    }
//            ).start();
//        try {
//            Thread.sleep(4000);
            // 从资源管理器读取
//            InputStream animationJsonFileStream = Minecraft.getInstance()
//                    .getResourceManager()
//                    .getResource(MAID_DEFAULT_ANIME_LOC)
//                    .orElseThrow(() -> new RuntimeException("找不到动画文件：" + MAID_DEFAULT_ANIME_LOC))
//                    .open();
//            AnimationFile file = AnimationDataMapper.getAnimationFile(animationJsonFileStream);
//            MaidComesAliveClient.LOGGER.info("read animation: {}\n", file.animations());
//            MaidComesAliveClient.LOGGER.info("originnal animations :{}",GeckoModelLoader.DEFAULT_MAID_ANIMATION_FILE.animations());
//            animationJsonFileStream.close();
//            animationJsonFileStream = Minecraft.getInstance()
//                    .getResourceManager()
//                    .getResource(MAID_DEFAULT_ANIME_LOC)
//                    .orElseThrow(() -> new RuntimeException("找不到动画文件：" + MAID_DEFAULT_ANIME_LOC))
//                    .open();
//            GeckoModelLoader.mergeAnimationFile(animationJsonFileStream, GeckoModelLoader.DEFAULT_MAID_ANIMATION_FILE);
//            animationJsonFileStream.close();
            event.addAnimation(DefaultGeckoAnimationEvent.AnimationType.MAID,MAID_DEFAULT_ANIME_LOC);
            MaidComesAliveClient.LOGGER.error("成功加载动画：" + MAID_DEFAULT_ANIME_LOC);
//            registerAnimationStates();

    }

    public static void registerAnimationStates(){
        AnimationManager manager = AnimationManager.getInstance();
        if (manager!=null) {
            for(AnimationState state: AnimationStates.animations){
                manager.register(state);
                MaidComesAliveClient.LOGGER.info("add animate state {}",state.getAnimationName());
            }
            MaidComesAliveClient.LOGGER.info("additional animation states registered");
        }
    }
    public static void mergeAnimationResource(){
        new Thread(()->{
            try {
                Thread.sleep(10000);
                InputStream animationJsonFileStream = Minecraft.getInstance()
                    .getResourceManager()
                    .getResource(MAID_DEFAULT_ANIME_LOC)
                    .orElseThrow(() -> new RuntimeException("找不到动画文件：" + MAID_DEFAULT_ANIME_LOC))
                    .open();
                Map<ResourceLocation, AnimationFile> allAnimations =
                        GeckoLibCache.getInstance().getAnimations();
                if (allAnimations.isEmpty())
                    return;
                AnimationFile animationFile = AnimationDataUtils.getAnimationFile(animationJsonFileStream);
                for(ResourceLocation location: allAnimations.keySet()){
                    if (location.getPath().contains("wine")) {
                        AnimationDataUtils.mergeAnimationFile(allAnimations.get(location),animationFile);
                        MaidComesAlive.LOGGER.info("成功将动画合并入模型：" + location);
                    }

                }

            registerAnimationStates();
            animationJsonFileStream.close();
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @SubscribeEvent
    public static void onRegister(RegisterClientReloadListenersEvent event) {
        // 注册为资源重载监听器
        event.registerReloadListener(new AnimationRegister());
    }

    @Override
    protected Void prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        // prepare 阶段：在后台线程执行
        // 这里什么都不做，等待 apply 阶段
        return null;
    }

    @Override
    protected void apply(
            Void object,
            ResourceManager resourceManager,
            ProfilerFiller profiler
    ) {
        // apply 阶段：在主线程执行（关键！）
        // 此时女仆模组的 GeckoModelLoader.reload() 已经执行完毕
        // DEFAULT_MAID_ANIMATION_FILE 已经准备好

        mergeAnimationResource();
    }
}

