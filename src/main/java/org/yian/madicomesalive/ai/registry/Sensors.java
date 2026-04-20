package org.yian.madicomesalive.ai.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.yian.madicomesalive.MaidComesAlive;
import org.yian.madicomesalive.ai.sensor.GreetSensor;

import java.util.function.Supplier;

public class Sensors {
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES =
            DeferredRegister.create(Registries.SENSOR_TYPE, MaidComesAlive.MODID);
    public static final Supplier<SensorType<GreetSensor>> GREET_SENSOR =
            SENSOR_TYPES.register("greet_sensor", () -> new SensorType<>(GreetSensor::new));
}
