package org.yian.madicomesalive;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import org.yian.madicomesalive.behavior.Sensors;
import org.yian.madicomesalive.memory.Memories;
import org.yian.madicomesalive.data.client.attachment.AttachmentRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(MaidComesAlive.MODID)
public class MaidComesAlive {
    public static final String MODID = "maidcomesalive";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MaidComesAlive(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        // 注册实体附加数据
        NeoForge.EVENT_BUS.register(this);
        AttachmentRegister.ATTACHMENTS_REGISTRY.register(modEventBus);
        // 注册记忆
        Memories.MEMORY_MODULE_TYPES.register(modEventBus);
        // 注册传感器
        Sensors.SENSOR_TYPES.register(modEventBus);
        //注册模组配置列表
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
