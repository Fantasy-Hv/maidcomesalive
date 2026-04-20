package org.yian.madicomesalive.event.subscriber;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yian.madicomesalive.MaidComesAlive;
import org.yian.madicomesalive.data.server.FlagKeyConstants;
import org.yian.madicomesalive.utils.AiUtils;

import java.util.List;

@EventBusSubscriber
public class PlayerLoginListener {
    private static final Logger log = LoggerFactory.getLogger(PlayerLoginListener.class);

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent loggedInEvent){
        Player player = loggedInEvent.getEntity();
        // 在某个范围内查找玩家拥有的所有女仆
        // 在某个范围内查找玩家拥有的所有女仆
        MaidComesAlive.LOGGER.info("player login");
        Level level = player.level();
        List<EntityMaid> maids = level.getEntitiesOfClass(EntityMaid.class, AABB.ofSize(player.getOnPos().getCenter(),40,40,40),
                maid -> maid.isOwnedBy(player) && maid.isAlive());
        maids.forEach(maid->{

            AiUtils.getMaidGreetMemory(maid).setFlag(FlagKeyConstants.OWNER_LOGIN);
        });
    }
}
