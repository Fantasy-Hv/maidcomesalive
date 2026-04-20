package org.yian.madicomesalive.ai.memory;

import com.github.tartaricacid.touhoulittlemaid.molang.parser.ast.UnaryExpression;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GreetMemory {
    boolean masterHere;
    Map<Object,Integer> flags;
    Map<Object,Long> timestamps;// 记录一些动画播放时间戳
    int jumpTimes = 0;
    public GreetMemory(){
        flags = new HashMap<>();
        timestamps = new HashMap<>();
    }
    public boolean isMasterHere() {
        return masterHere;
    }

    public void setMasterHere(boolean masterHere) {
        this.masterHere = masterHere;
    }

    public Optional<Long> getTimestamp(Object key) {
        if (!timestamps.containsKey(key))
            return Optional.empty();
        return Optional.of(timestamps.get(key));
    }

    /**
     * 用于设置布尔状态
     * @param flag
     */
    public void setFlag(Object flag){
        flags.put(flag,null);
    }

    public void remFlag(Object flag){
        flags.remove(flag);
    }
    /**
     * 检查某个状态
     * @param flag
     * @return
     */
    public boolean checkFlag(Object flag){
        return flags.containsKey(flag);
    }

    public void setTimestamp(Object key, Long behaviorTime) {
        timestamps.put(key,behaviorTime);
    }

    /**
     * 某类标记可能有多个值
     * @param key
     * @param value
     */
    public void setFlag(String  key,int value){
        flags.put(key,value);
    }

    /**
     * 获取标记值
     * @param key
     * @return
     */
    public Optional<Integer> getFlag(Object key){
        if (!flags.containsKey(key))
            return Optional.empty();
        return Optional.of(flags.get(key));

    }
}
