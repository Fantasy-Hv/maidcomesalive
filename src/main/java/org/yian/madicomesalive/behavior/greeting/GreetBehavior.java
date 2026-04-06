package org.yian.madicomesalive.behavior.greeting;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.NotNull;
import org.yian.madicomesalive.MaidComesAlive;
import org.yian.madicomesalive.data.client.attachment.AnimateConstants;
import org.yian.madicomesalive.data.client.attachment.AnimationFlag;
import org.yian.madicomesalive.data.client.attachment.AttachmentRegister;
import org.yian.madicomesalive.memory.GreetMemory;
import org.yian.madicomesalive.memory.Memories;

import java.util.Optional;
import java.util.Set;

public class GreetBehavior<T extends EntityMaid> extends Behavior<T> {


    public GreetBehavior() {
        super(ImmutableMap.of(
                Memories.GREET_MEMORY.get(), MemoryStatus.VALUE_PRESENT
        ));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, T maid) {
//        MaidComesAlive.LOGGER.info("check greeting condition");
        if (!maid.isTame() || maid.getOwner() == null) {
            return false;
        }
        // 检查主人是否存在且在有效距离内
        GreetMemory memory = getMemory(maid);
        if (!memory.isMasterHere()){ //主人不在，刷新会话
            memory.setBehaviorTime(AnimateConstants.NOT_GREET_YET);
            return false;
        }
        if (memory.getBehaviorTime()==null)memory.setBehaviorTime(-1L);
        return memory.getBehaviorTime() == AnimateConstants.NOT_GREET_YET; // 未打招呼
    }

    @Override
    protected void start(ServerLevel level, T entity, long gameTime) {
        GreetMemory memory = getMemory(entity);
        memory.setBehaviorTime(System.currentTimeMillis());
        // 同步到客户端
        setCliFlags(entity,Set.of(AnimateConstants.GREETING_DIRTY));
        super.start(level, entity, gameTime);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, T maid, long gameTime) {
        GreetMemory memory = getMemory(maid);
        if(!checkDistance(memory))return false;// 不仅仅是离开了停止挥手，挥手超时了也要停止
        long cur = System.currentTimeMillis();
        return ( cur - memory.getBehaviorTime() < 5000); //没超时，继续
    }

    @Override
    protected void tick(@NotNull ServerLevel level, T maid, long gameTime) {
        //看向主人
        LivingEntity owner = maid.getOwner();
        GreetMemory memory = getMemory(maid);
        maid.getLookControl().setLookAt(owner);
        int times = memory.getJumpTimes();
        if (times==0)
            maid.getJumpControl().jump();
        memory.setJumpTimes((times+1)%10); // 10tick跳一下
//        if (maid.canBrainMoving())
//            BehaviorUtils.setWalkAndLookTargetMemories(maid,owner,0.4f,10);
        super.tick(level, maid, gameTime);
    }

    @Override
    protected void stop(ServerLevel level, T entity, long gameTime) {
        GreetMemory memory = getMemory(entity);
        //同步到客户端
        remCliFlags(entity,Set.of(AnimateConstants.GREETING_DIRTY));
        memory.setBehaviorTime(AnimateConstants.HAD_GREETED); // -2表示在此会话中打过招呼
        super.stop(level, entity, gameTime);
    }
    private boolean checkDistance(GreetMemory memory){
        return memory.isMasterHere();
    }

    private GreetMemory getMemory(EntityMaid maid){
        Optional<GreetMemory> here = maid.getBrain().getMemory(Memories.GREET_MEMORY.get());
        return  here.orElse(new GreetMemory());
    }

    private void setCliFlags(EntityMaid maid, Set<Integer> flags){
        AnimationFlag animationFlag = maid.getData(AttachmentRegister.ANIMATION_FLAGS);
        animationFlag.getFlags().addAll(flags);
        maid.setData(AttachmentRegister.ANIMATION_FLAGS.get(), animationFlag);
    }
    private void remCliFlags(EntityMaid maid, Set<Integer> flags){
        AnimationFlag animationFlag = maid.getData(AttachmentRegister.ANIMATION_FLAGS);
        animationFlag.getFlags().removeAll(flags);
        maid.setData(AttachmentRegister.ANIMATION_FLAGS.get(), animationFlag);
    }
}

