package org.yian.madicomesalive.behavior.greeting;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import org.jetbrains.annotations.NotNull;
import org.yian.madicomesalive.MaidComesAlive;
import org.yian.madicomesalive.memory.GreetMemory;
import org.yian.madicomesalive.memory.Memories;

import java.util.Set;

public class GreetSensor extends Sensor<EntityMaid> {
    // 设置更大的检测范围
    private static final double SEARCH_RANGE = 20.0;

    public GreetSensor() {
        // 每 20 tick (1秒) 检测一次，节省性能
        super(20);
    }

    @Override
    protected void doTick(@NotNull ServerLevel serverLevel, EntityMaid maid) {
        boolean master_here = false;

        LivingEntity owner = maid.getOwner();
        // 检查主人是否在同一维度且存活
        if (owner != null && maid.isTame() && owner.level() == serverLevel && owner.isAlive()) {
            double distance = maid.distanceTo(owner);
            // 如果在范围内且可见（可选：加上视线检查）
            if (distance < SEARCH_RANGE && maid.hasLineOfSight(owner))
                master_here = true;
        }
        GreetMemory memory = maid.getBrain().getMemory(Memories.GREET_MEMORY.get()).orElse(new GreetMemory());
        memory.setMasterHere(master_here);
        maid.getBrain().setMemory(Memories.GREET_MEMORY.get(), memory);
    }

    // 这个传感器不依赖其他记忆，它是源头
    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {

        return ImmutableSet.of();
    }
}
