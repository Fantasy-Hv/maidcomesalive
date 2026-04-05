package org.yian.madicomesalive.entity;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.AnimationState;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.Priority;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskIdle;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.ILoopType;
import org.yian.madicomesalive.utils.PredictUtils;

import java.util.*;

public class AnimationStates {
    public static List<AnimationState> animations = new ArrayList<>();
    private static final Random random = new Random();
    static {
        //当女仆休闲(并且高兴/好感度高等，可联动情绪萌动模组，_todo )时有概率播放卖萌动画
        AnimationState cuteAnimation = new AnimationState(
                "cute",
                ILoopType.EDefaultLoopTypes.LOOP,
                Priority.LOW,
                (iMaid, animationEvent) -> {
                    boolean checkMovement = PredictUtils.assertAll(
                            !iMaid.onHurt(),
                            !iMaid.isMaidInSittingPose(),
                            animationEvent.isMoving()
                    );
                    if (!checkMovement)return false;
                    IMaidTask task = iMaid.getTask();
                    boolean checkTask = PredictUtils.assertAll(
                            task instanceof TaskIdle
                    );
                    if (!checkTask)return false;
                    return random.nextBoolean();
                }
        );
        animations.add(cuteAnimation);
    }
}
