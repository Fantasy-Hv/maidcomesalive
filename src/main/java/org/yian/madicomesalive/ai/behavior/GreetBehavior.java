package org.yian.madicomesalive.ai.behavior;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import org.jetbrains.annotations.NotNull;
import org.yian.madicomesalive.MaidComesAlive;
import org.yian.madicomesalive.data.client.attachment.AnimateConstants;
import org.yian.madicomesalive.ai.memory.GreetMemory;
import org.yian.madicomesalive.ai.registry.Memories;
import org.yian.madicomesalive.data.server.FlagKeyConstants;
import org.yian.madicomesalive.network.attachment.AttachmentSynchronizer;

import java.util.Optional;
import java.util.Set;

import static org.yian.madicomesalive.utils.AiUtils.getMaidGreetMemory;

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
        GreetMemory memory = getMaidGreetMemory(maid);
        if (!memory.isMasterHere()){ //主人不在，刷新会话
            memory.setFlag(FlagKeyConstants.GREETING_SESSION_HISTORY,AnimateConstants.NOT_GREET_YET);
            return false;
        }
        //检查冷却
        Optional<Long> timestampOp = memory.getTimestamp(FlagKeyConstants.GREETING_TIMESTAMP);
        if (timestampOp.isEmpty()){
            memory.setTimestamp(FlagKeyConstants.GREETING_TIMESTAMP,-1L);
            timestampOp = Optional.of(-1L);
        }
        long curstamp = System.currentTimeMillis();
//        MaidComesAlive.LOGGER.info("last trigger time {},timeslap {}",timestampOp.get(),(curstamp-timestampOp.get())/1000);
        if (curstamp - timestampOp.get() < AnimateConstants.GREETING_INTERVAL)
            return false; // 仍在冷却内，不过于频繁打招呼。

        //检查当前会话打招呼历史
        long history =   memory.getFlag(FlagKeyConstants.GREETING_SESSION_HISTORY).orElseGet(()->{

            if (memory.checkFlag(FlagKeyConstants.OWNER_LOGIN)){
                MaidComesAlive.LOGGER.info("maid knew you login.");
                memory.remFlag(FlagKeyConstants.OWNER_LOGIN);
                return AnimateConstants.NOT_GREET_YET;
            }
            return  AnimateConstants.HAD_GREETED;
        });
        return history == AnimateConstants.NOT_GREET_YET;
    }

    @Override
    protected void start(ServerLevel level, T entity, long gameTime) {
        GreetMemory memory = getMaidGreetMemory(entity);
//        memory.setTimestamp(System.currentTimeMillis());
        MaidComesAlive.LOGGER.info("trigger greeting");
        memory.setTimestamp(FlagKeyConstants.GREETING_TIMESTAMP,System.currentTimeMillis());
        // 标记当前会话已经打过招呼
        memory.setFlag(FlagKeyConstants.GREETING_SESSION_HISTORY,AnimateConstants.HAD_GREETED);
        // 同步到客户端
        AttachmentSynchronizer.setCliFlags(entity,Set.of(AnimateConstants.GREETING_DIRTY));
        super.start(level, entity, gameTime);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, T maid, long gameTime) {
        GreetMemory memory = getMaidGreetMemory(maid);
        if(!checkDistance(memory))return false;// 不仅仅是离开了停止挥手，挥手超时了也要停止
        long cur = System.currentTimeMillis();
        Optional<Long> timestampOptional = memory.getTimestamp(FlagKeyConstants.GREETING_TIMESTAMP);
        if (timestampOptional.isEmpty())memory.setTimestamp(FlagKeyConstants.GREETING_TIMESTAMP,cur);
        long timestamp = timestampOptional.orElse(cur);
        return ( cur - timestamp < 4000); //没超时，继续
    }

    @Override
    protected void tick(@NotNull ServerLevel level, T maid, long gameTime) {
        //看向主人
        LivingEntity owner = maid.getOwner();
        maid.getLookControl().setLookAt(owner);
//        Optional<WalkTarget> walkTarget = maid.getBrain().getMemory(MemoryModuleType.WALK_TARGET);
        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
//        maid.getNavigation().stop();
//        if (maid.canBrainMoving())
//            BehaviorUtils.setWalkAndLookTargetMemories(maid,owner,0.4f,10);
        super.tick(level, maid, gameTime);
    }

    @Override
    protected void stop(ServerLevel level, T entity, long gameTime) {
        GreetMemory memory = getMaidGreetMemory(entity);
        //同步到客户端
        AttachmentSynchronizer.remCliFlags(entity,Set.of(AnimateConstants.GREETING_DIRTY));
        //添加冷却
        super.stop(level, entity, gameTime);
    }
    private boolean checkDistance(GreetMemory memory){
        return memory.isMasterHere();
    }


}

