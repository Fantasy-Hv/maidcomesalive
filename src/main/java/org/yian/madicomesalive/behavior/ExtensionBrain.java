package org.yian.madicomesalive.behavior;

import com.github.tartaricacid.touhoulittlemaid.api.entity.ai.IExtraMaidBrain;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import org.yian.madicomesalive.MaidComesAlive;
import org.yian.madicomesalive.behavior.greeting.GreetBehavior;
import org.yian.madicomesalive.behavior.greeting.GreetSensor;
import org.yian.madicomesalive.memory.Memories;

import java.util.Collections;
import java.util.List;
public class ExtensionBrain implements IExtraMaidBrain {
    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> getWorkBehaviors() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> getIdleBehaviors() {
        // bugfix
        MaidComesAlive.LOGGER.info("register behavior!");
        return List.of(Pair.of(2,new GreetBehavior<>()));

    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> getCoreBehaviors() {
        return List.of(Pair.of(2,new GreetBehavior<>()));
    }

    @Override
    public List<MemoryModuleType<?>> getExtraMemoryTypes() {
        return List.of(Memories.GREET_MEMORY.get());
    }

    @Override
    public List<SensorType<? extends Sensor<? super EntityMaid>>> getExtraSensorTypes() {
        return List.of(Sensors.GREET_SENSOR.get());
    }
}
