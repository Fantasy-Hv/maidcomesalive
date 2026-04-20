package org.yian.madicomesalive.ai.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.yian.madicomesalive.ai.memory.GreetMemory;

import java.util.Optional;
import java.util.function.Supplier;

import static org.yian.madicomesalive.MaidComesAlive.MODID;

public class Memories {
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES =
            DeferredRegister.create(Registries.MEMORY_MODULE_TYPE, MODID);
    // 打招呼记忆
    public static final Supplier<MemoryModuleType<GreetMemory>> GREET_MEMORY =
            MEMORY_MODULE_TYPES.register("greet_memory",
                    () -> new MemoryModuleType<>(Optional.empty())); // 初始值为空
}
