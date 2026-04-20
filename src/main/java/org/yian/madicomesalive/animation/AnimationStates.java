package org.yian.madicomesalive.animation;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.AnimationState;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.Priority;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskIdle;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.ILoopType;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.yian.madicomesalive.MaidComesAliveClient;
import org.yian.madicomesalive.animation.predicate.AnimatePredicates;
import org.yian.madicomesalive.animation.predicate.CooldownManager;
import org.yian.madicomesalive.utils.PredictUtils;

import java.util.*;
//在这里定义需要交给animationManager管理的AnimationState对象
public class AnimationStates {
    public static List<AnimationState> animations = new ArrayList<>();
    private static final Random random = new Random();
        //当女仆休闲(并且高兴/好感度高等，可联动情绪萌动模组，_todo )时有概率播放摇尾巴动画
    //这些逻辑判断应该放到behavoir中,客户端只需要读取attachment的状态就好了
    public static final  AnimationState cuteAnimation = new AnimationState(
                "cute",
                ILoopType.EDefaultLoopTypes.LOOP,
                Priority.LOWEST,
                (iMaid, animationEvent) -> {
                    //检查任务状态
//                    MaidComesAliveClient.LOGGER.info("checking for cute");
                    IMaidTask task = iMaid.getTask();
                    EntityMaid maid = iMaid.asStrictMaid();
                    if (!(task instanceof TaskIdle))return false;
                    // 检查运动状态
                    boolean check = PredictUtils.assertAll(
                            !iMaid.onHurt(),
                            !iMaid.isMaidInSittingPose(),
                            animationEvent.isMoving()

                    );
                    if (!check)return false;
                    //检查冷却 开了会无法播放动画，即使测试条件通过了也是如此
//                    if(!CooldownManager.checkAnimationCooldown(iMaid,"cute"))
//                        return false;
//                    CooldownManager.flushAnimationCooldown(iMaid,"cute");
                    if (maid != null) {
                        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
                    }
                    MaidComesAliveClient.LOGGER.info("trigger cute animate");
                    return true;
                });
    // 当狗修金回来时，女仆会招手
    public static final AnimationState greetAnimation = new AnimationState(
            "greet",
            ILoopType.EDefaultLoopTypes.LOOP,
            Priority.HIGHEST,
            ((iMaid, animationEvent) -> {
                // 检查状态
                if (iMaid.onHurt()||iMaid.onClimbable())return false;
                //检查狗修金是不是远归
               boolean res = AnimatePredicates.checkMasterComeBack(iMaid);
               return res;
            })
    );
    static {
        animations.add(cuteAnimation);
        animations.add(greetAnimation);
    }
}
