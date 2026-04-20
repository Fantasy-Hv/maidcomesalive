package org.yian.madicomesalive.ai.sensor;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import org.jetbrains.annotations.NotNull;
import org.yian.madicomesalive.ai.memory.GreetMemory;
import org.yian.madicomesalive.ai.registry.Memories;

import java.util.Set;

/**
 * 打招呼状态机
 * A：主人不在
 * B:主人在
 * A->B:打一次招呼，设置打招乎位为true
 * B->A:设置打招呼位为false
 * 主人不在怎么判定？1.主人离开范围，就是不在，
 * 那么主人什么时候在？如果在范围内，且在视野内，就是在，
 * 如果主人在范围内而不在视野内，算不算在？
 * 如果不算
 *  仅仅是饶了一圈，丢了视野，还在范围内，那么招呼标志就刷新，再次看见就会打招呼，过于频繁
 * 如果算
 *  那么和女仆隔着障碍物，但是从范围外进入范围内那一刻就会从不在变为在，那么就会打招呼，但实际上并没有相遇。玩家就会错过酒狐的招呼
 *
 *  所以不能仅仅靠范围和视野来判断，
    考虑现实中的打招呼逻辑，如果在范围内且在视野，那肯定应该算，而在范围内不在视野内，不算在，为了避免过于频繁，应该给酒狐加一个冷却机制，
 */
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
            /**
             * 有一种情况，如果玩家在附近但是视野消失，这种情况不应该重置状态
             *
             * 如果玩家不在搜索范围内，设置为false
             * 如果玩家在搜索范围内且视野存在，应该设置为true
             */
            if (distance > SEARCH_RANGE ) {
            }
            else if(maid.hasLineOfSight(owner))
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
