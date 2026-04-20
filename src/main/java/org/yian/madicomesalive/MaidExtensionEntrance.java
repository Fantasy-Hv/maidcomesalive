package org.yian.madicomesalive;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.ExtraMaidBrainManager;
import org.yian.madicomesalive.ai.registry.ExtensionBrain;

@LittleMaidExtension
public class MaidExtensionEntrance implements ILittleMaid {
    @Override
    public void addExtraMaidBrain(ExtraMaidBrainManager manager) {
        manager.addExtraMaidBrain(new ExtensionBrain());
    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void registerMagicCastingAnimation(MagicCastingAnimationManager manager) {
//        // 注册摸头动画提供者
//        manager.register(new PatPatAnimationProvider());
//    }
}
