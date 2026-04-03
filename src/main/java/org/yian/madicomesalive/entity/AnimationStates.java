package org.yian.madicomesalive.entity;

import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.AnimationState;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.ILoopType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnimationStates {
    public static List<AnimationState> animations = new ArrayList<>();
    private static Random random = new Random();
    static {
        //当女仆休闲时有概率播放卖萌动画
        AnimationState cuteAnimation = new AnimationState(
                "cute",
                ILoopType.EDefaultLoopTypes.LOOP,
                1,
                (iMaid, animationEvent) -> {
//                    boolean checkMovement = PredictUtils.assertAll(
//                            !iMaid.onHurt(),
//                            !iMaid.isSwingingArms(),
//                            !iMaid.isMaidInSittingPose(),
//                            !animationEvent.isMoving()
//                    );
//                    if (!checkMovement)return false;
//                    IMaidTask task = iMaid.getTask();
//                    boolean checkTask = PredictUtils.assertAll(
//                            task instanceof TaskIdle
//                    );
//                    if (!checkTask)return false;
//
//                    return random.nextBoolean();
                    return true;
                }
        );
        animations.add(cuteAnimation);
    }
}
