package org.yian.madicomesalive.utils;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.molang.parser.ast.UnaryExpression;
import net.minecraft.world.entity.ai.Brain;
import org.yian.madicomesalive.MaidComesAlive;
import org.yian.madicomesalive.ai.memory.GreetMemory;
import org.yian.madicomesalive.ai.registry.Memories;

import java.util.Optional;
//todo 想写一个统一可靠的获取记忆方法，(maid,memorytype);但是还想不到怎么实现比较好且高效，先为每个记忆单独写吧。
public class AiUtils {
    public static  GreetMemory getMaidGreetMemory(EntityMaid maid){
        Brain<EntityMaid> brain = maid.getBrain();
        Optional<GreetMemory> memoryOptional = brain.getMemory(Memories.GREET_MEMORY.get());
        if (memoryOptional.isEmpty()) {
            memoryOptional = Optional.of(new GreetMemory());
            brain.setMemory(Memories.GREET_MEMORY.get(),memoryOptional.get());
        }
        return  memoryOptional.orElse(new GreetMemory());
    }
}
