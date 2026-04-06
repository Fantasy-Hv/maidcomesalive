package org.yian.madicomesalive.memory;

import net.minecraft.world.item.crafting.Ingredient;

public class GreetMemory {
    boolean masterHere;
    Long behaviorTime; // 动作发生的时间戳，或者特殊标记
    int jumpTimes = 0;
    public boolean isMasterHere() {
        return masterHere;
    }

    public void setMasterHere(boolean masterHere) {
        this.masterHere = masterHere;
    }

    public Long getBehaviorTime() {
        return behaviorTime;
    }

    public void setBehaviorTime(Long behaviorTime) {
        this.behaviorTime = behaviorTime;
    }

    public int getJumpTimes() {
        return jumpTimes;
    }

    public void setJumpTimes(int jumpTimes) {
        this.jumpTimes = jumpTimes;
    }
}
