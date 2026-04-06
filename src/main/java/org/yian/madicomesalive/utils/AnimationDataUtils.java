package org.yian.madicomesalive.utils;

import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.Animation;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.MolangParser;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.file.AnimationFile;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.resource.GeckoLibCache;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.json.JsonAnimationUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.server.ChainedJsonException;
import net.minecraft.util.GsonHelper;
import org.yian.madicomesalive.MaidComesAliveClient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
// copy from com.github.tartaricacid.touhoulittlemaid.client.resource.GeckoModelLoader
public class AnimationDataUtils {
    public static AnimationFile getAnimationFileObj(InputStream stream) {
        AnimationFile animationFile = new AnimationFile();
        MolangParser parser = GeckoLibCache.getInstance().parser;
        JsonObject jsonObject = GsonHelper.fromJson(CustomPackLoader.GSON, new InputStreamReader(stream, StandardCharsets.UTF_8), JsonObject.class);
        for (Map.Entry<String, JsonElement> entry : JsonAnimationUtils.getAnimations(jsonObject)) {
            String animationName = entry.getKey();
            Animation animation;
            try {
                animation = JsonAnimationUtils.deserializeJsonToAnimation(JsonAnimationUtils.getAnimation(jsonObject, animationName), parser);
                animationFile.putAnimation(animationName, animation);
            } catch (ChainedJsonException e) {
                MaidComesAliveClient.LOGGER.error("Failed to load animation {}: {}", animationName, e.getMessage());
            }
        }
        return animationFile;
    }
    public static AnimationFile mergeAnimationFile(AnimationFile target, AnimationFile source) {
        source.animations().forEach(target::putAnimation);
        return target;
    }
}
