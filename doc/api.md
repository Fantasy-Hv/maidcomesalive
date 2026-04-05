# Touhou Little Maid 扩展 API 文档

本文档详细说明 Touhou Little Maid 模组提供的所有扩展 API 接口、类和注解，以及如何使用它们来开发附属模组。

---

## 一、核心扩展接口

### 1.1 `@LittleMaidExtension` 注解

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension`

**用途**：标记一个类为女仆模组的扩展入口。所有实现 `ILittleMaid` 接口的类都必须有此注解。

**要求**：

- 类必须有 `@LittleMaidExtension` 注解
- 类必须有无参构造函数

**示例**：

```java
@LittleMaidExtension
public class MyModExtension implements ILittleMaid {
    // 实现扩展方法
}
```

---

### 1.2 `ILittleMaid` 接口

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid`

**用途**：女仆模组的主要扩展接口，提供 20+ 个默认方法供附属模组扩展。

**注册方式**：
模组初始化时，实现此接口的类会被自动发现（通过 `@LittleMaidExtension` 注解）。

**可用扩展方法**：

#### 物品与装备相关

| 方法                                 | 用途              | 调用时机 |
| ---------------------------------- | --------------- | ---- |
| `bindMaidBauble(BaubleManager)`    | 为物品绑定女仆饰品属性     | 初始化  |
| `addMaidBackpack(BackpackManager)` | 添加女仆背包类型        | 初始化  |
| `addChestType(ChestManager)`       | 添加箱子类型（隙间饰品识别用） | 初始化  |

#### 任务与 AI 相关

| 方法                                               | 用途                                      | 调用时机 |
| ------------------------------------------------ | --------------------------------------- | ---- |
| `addMaidTask(TaskManager)`                       | 添加女仆工作模式/任务                             | 初始化  |
| `registerTaskData(TaskDataRegister)`             | 注册任务数据（可添加到女仆上的自定义数据）                   | 初始化  |
| `addExtraMaidBrain(ExtraMaidBrainManager)`       | 添加额外 AI 数据（MemoryModuleType、SensorType） | 初始化  |
| `registerAIChatSerializer(SerializerRegister)`   | 注册 AI 聊天的序列化器                           | 初始化  |
| `registerAIFunctionCall(FunctionCallRegister)`   | 注册自定义 Function Call                     | 初始化  |
| `registerBroomControl(BroomControlManager)`      | 注册扫帚控制器                                 | 初始化  |
| `registerSpecialCropHandler(SpecialCropManager)` | 为作物模式添加特判                               | 初始化  |

#### 方块与结构相关

| 方法                                                | 用途        | 调用时机 |
| ------------------------------------------------- | --------- | ---- |
| `addMultiBlock(MultiBlockManager)`                | 添加多方块结构   | 初始化  |
| `addMaidMeal(MaidMealManager)`                    | 添加女仆饭类型   | 初始化  |
| `registerMaidEdibleBlock(MaidEdibleBlockManager)` | 注册女仆可食用方块 | 初始化  |

#### 聊天与提示相关

| 方法                                       | 用途          | 调用时机   |
| ---------------------------------------- | ----------- | ------ |
| `registerChatBubble(ChatBubbleRegister)` | 注册聊天气泡类型    | 初始化    |
| `addMaidTips(MaidTipsOverlay)`           | 添加女仆提示（客户端） | 客户端初始化 |

#### 渲染与动画相关（仅客户端）

| 方法                                                            | 用途                       | 调用时机   |
| ------------------------------------------------------------- | ------------------------ | ------ |
| `addAdditionMaidLayer(EntityMaidRenderer, Context)`           | 添加默认模型风格的 Layer 渲染       | 客户端初始化 |
| `addAdditionGeckoMaidLayer(GeckoEntityMaidRenderer, Context)` | 添加 GeckoLib 风格的 Layer 渲染 | 客户端初始化 |
| `addHardcodeAnimation(HardcodedAnimationManger)`              | 添加硬编码动画                  | 客户端初始化 |
| `registerMagicCastingAnimation(MagicCastingAnimationManager)` | 注册魔法咏唱动画提供器              | 客户端初始化 |

**完整示例**：

```java
@LittleMaidExtension
public class MyModExtension implements ILittleMaid {

    @Override
    public void addMaidTask(TaskManager manager) {
        manager.register(new MyCustomTask());
    }

    @Override
    public void addMaidBackpack(BackpackManager manager) {
        manager.registerBackpack(new MyCustomBackpack());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addHardcodeAnimation(HardcodedAnimationManger manager) {
        manager.addMaidAnimation(new MyCustomAnimation());
    }
}
```

---

## 二、动画 API

### 2.1 `ICustomAnimation<T>` 接口

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.animation.ICustomAnimation`

**用途**：自定义动画接口，用于添加硬编码动画（通过代码直接控制骨骼旋转）。

**类型参数**：`T extends LivingEntity` - 实体类型

**方法**：

| 方法                                                                         | 用途                  | 参数                  |
| -------------------------------------------------------------------------- | ------------------- | ------------------- |
| `setupRotations(entity, poseStack, ageInTicks, rotationYaw, partialTicks)` | 设置全局旋转（旧版模型）        | 实体、矩阵栈、tick、 yaw、插值 |
| `setupGeckoRotations(entity, poseStack, ...)`                              | 设置全局旋转（GeckoLib 模型） | 同上                  |
| `setRotationAngles(entity, models, limbSwing, ...)`                        | 设置骨骼旋转（旧版模型）        | 实体、骨骼 Map、行走参数等     |
| `setGeckoRotationAngles(entity, model, limbSwing, ...)`                    | 设置骨骼旋转（GeckoLib 模型） | 实体、模型、行走参数等         |

**使用示例**：

```java
public class SwordSwingAnimation implements ICustomAnimation<Mob> {
    @Override
    public void setGeckoRotationAngles(Mob entity, AnimatedGeoModel model,
                                       float limbSwing, float limbSwingAmount,
                                       float ageInTicks, float netHeadYaw, float headPitch) {
        // 获取手臂骨骼
        IBone rightArm = model.getBone("armRight").orElse(null);
        if (rightArm != null && entity.swinging) {
            // 计算挥舞角度
            float swingProgress = entity.getAttackAnim(1.0f);
            rightArm.setRotationX((float) (-Math.PI * swingProgress));
        }
    }
}

// 注册
@Override
public void addHardcodeAnimation(HardcodedAnimationManger manager) {
    manager.addMaidAnimation(new SwordSwingAnimation());
}
```

---

### 2.2 `IMagicCastingAnimationProvider` 接口

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.animation.IMagicCastingAnimationProvider`

**用途**：魔法咏唱动画提供器，用于为魔法类附属模组提供自定义咏唱动画。

**方法**：

| 方法                                                          | 返回类型                 | 用途                      |
| ----------------------------------------------------------- | -------------------- | ----------------------- |
| `getMagicCastingState(IMaid maid)`                          | `IMagicCastingState` | 判断女仆是否应该播放魔法动画          |
| `getAnimationBuilder(IMaid maid, IMagicCastingState state)` | `AnimationBuilder`   | 根据咏唱状态返回动画构建器           |
| `getPriority()`                                             | `int`                | 获取优先级（默认 100，数字越大优先级越高） |

**配合接口**：`IMagicCastingState`

**咏唱阶段枚举** (`CastingPhase`)：

- `NONE` - 不在咏唱中
- `INSTANT` - 瞬间施法
- `START` - 开始咏唱
- `CASTING` - 持续咏唱
- `END` - 咏唱结束

**使用示例**：

```java
public class MyMagicProvider implements IMagicCastingAnimationProvider {

    @Override
    public IMagicCastingState getMagicCastingState(IMaid maid) {
        if (maid.asEntity().isUsingItem() && 
            maid.asEntity().getUsedItemHand() == InteractionHand.MAIN_HAND) {
            ItemStack stack = maid.asEntity().getUseItem();
            if (stack.getItem() instanceof MagicWandItem) {
                return new MyMagicCastingState(maid);
            }
        }
        return null;
    }

    @Override
    public AnimationBuilder getAnimationBuilder(IMaid maid, IMagicCastingState state) {
        return switch (state.getCurrentPhase()) {
            case START -> new AnimationBuilder().addAnimation("magic_start");
            case CASTING -> new AnimationBuilder().addAnimation("magic_cast_loop");
            case END -> new AnimationBuilder().addAnimation("magic_end");
            case INSTANT -> new AnimationBuilder().addAnimation("magic_instant");
            default -> null;
        };
    }

    @Override
    public int getPriority() {
        return 150; // 高于默认值
    }
}

// 注册
@Override
public void registerMagicCastingAnimation(MagicCastingAnimationManager manager) {
    manager.registerProvider(new MyMagicProvider());
}
```

---

### 2.3 `IMagicCastingState` 接口

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.animation.IMagicCastingState`

**用途**：表示当前帧的魔法咏唱状态，所有数据由附属模组自行管理。

**方法**：

```java
CastingPhase getCurrentPhase();  // 获取当前咏唱阶段
boolean isCancelled();           // 咏唱是否被取消
void setCancelled(boolean cancelled); // 设置咏唱取消状态
```

**实现示例**：

```java
public class MyMagicCastingState implements IMagicCastingState {
    private final IMaid maid;
    private CastingPhase phase = CastingPhase.NONE;
    private boolean cancelled = false;
    private int tick = 0;

    public MyMagicCastingState(IMaid maid) {
        this.maid = maid;
        updatePhase();
    }

    private void updatePhase() {
        tick++;
        if (tick < 20) {
            phase = CastingPhase.START;
        } else if (tick < 100) {
            phase = CastingPhase.CASTING;
        } else {
            phase = CastingPhase.END;
        }

        // 检查咏唱是否被打断
        if (!maid.asEntity().isUsingItem()) {
            cancelled = true;
            phase = CastingPhase.NONE;
        }
    }

    @Override
    public CastingPhase getCurrentPhase() {
        return phase;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
```

---

### 2.4 `IModelRenderer` 接口

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.animation.IModelRenderer`

**用途**：模型渲染器接口，用于获取和操作骨骼（Bedrock 模型系统）。

**方法**：

| 方法                                  | 用途             |
| ----------------------------------- | -------------- |
| `getModelRenderer()`                | 获取 BedrockPart |
| `getRotateAngleX/Y/Z()`             | 获取旋转角度         |
| `setRotateAngleX/Y/Z(float)`        | 设置旋转角度         |
| `getInitRotateAngleX/Y/Z()`         | 获取初始旋转角度       |
| `getOffsetX/Y/Z()`                  | 获取偏移量          |
| `setOffsetX/Y/Z(float)`             | 设置偏移量          |
| `getRotationPointX/Y/Z()`           | 获取旋转点          |
| `isHidden()` / `setHidden(boolean)` | 显示/隐藏骨骼        |

---

### 2.5 `ICustomAnimation` 工具方法

```java
// 获取指定名称的骨骼
BedrockPart part = ICustomAnimation.getPartOrNull(models, "head");
if (part != null) {
    part.setRotateAngleX(0.5f);
}
```

---

## 三、实体与数据 API

### 3.1 `IMaid` 接口

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid`

**用途**：女仆实体接口，提供女仆属性和方法的访问。

**转换方法**：

```java
// 将任意 Mob 转换为 IMaid
IMaid maid = IMaid.convert(mob);
if (maid != null) {
    // 访问女仆属性
    String modelId = maid.getModelId();
    boolean isSitting = maid.isMaidInSittingPose();
    int favorability = maid.getFavorability();
}
```

**主要方法分类**：

#### 模型相关

| 方法                | 返回类型      | 用途           |
| ----------------- | --------- | ------------ |
| `getModelId()`    | `String`  | 获取模型 ID      |
| `isYsmModel()`    | `boolean` | 是否使用 YSM 渲染  |
| `getYsmModelId()` | `String`  | 获取 YSM 模型 ID |

#### 实体相关

| 方法               | 返回类型         | 用途                       |
| ---------------- | ------------ | ------------------------ |
| `asEntity()`     | `Mob`        | 获取原始实体                   |
| `asStrictMaid()` | `EntityMaid` | 转换为 EntityMaid（可能为 null） |

#### 任务与状态

| 方法                      | 返回类型        | 用途           |
| ----------------------- | ----------- | ------------ |
| `getTask()`             | `IMaidTask` | 获取当前任务       |
| `isSwingingArms()`      | `boolean`   | 手臂是否举起（攻击动画） |
| `isBegging()`           | `boolean`   | 是否在祈求        |
| `isMaidInSittingPose()` | `boolean`   | 是否坐姿         |

#### 物品相关

| 方法                           | 返回类型            | 用途       |
| ---------------------------- | --------------- | -------- |
| `getHandItemsForAnimation()` | `ItemStack[]`   | 获取手中物品缓存 |
| `getBackpackShowItem()`      | `ItemStack`     | 获取背部显示物品 |
| `getMaidBackpackType()`      | `IMaidBackpack` | 获取背包类型   |

#### 属性相关

| 方法                                | 返回类型      | 用途     |
| --------------------------------- | --------- | ------ |
| `getExperience()`                 | `int`     | 获取经验值  |
| `getFavorability()`               | `int`     | 获取好感度  |
| `hasBackpack()`                   | `boolean` | 是否有背包  |
| `hasHelmet()/hasChestPlate()/...` | `boolean` | 是否穿戴装备 |

---

### 3.2 `TaskDataKey<T>` 接口

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey`

**用途**：任务数据键，用于为女仆添加自定义数据。

**方法**：

```java
ResourceLocation getKey();                    // 数据键名
CompoundTag writeSaveData(T data);            // 保存到 NBT
T readSaveData(CompoundTag compound);         // 从 NBT 读取
CompoundTag writeSyncData(T data);            // 同步数据（默认同 save）
T readSyncData(CompoundTag compound);         // 读取同步数据
```

**使用示例**：

```java
// 定义数据键
public class MagicPowerKey implements TaskDataKey<Integer> {
    private static final ResourceLocation KEY = 
        ResourceLocation.fromNamespaceAndPath("mymod", "magic_power");

    @Override
    public ResourceLocation getKey() {
        return KEY;
    }

    @Override
    public CompoundTag writeSaveData(Integer data) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("power", data);
        return tag;
    }

    @Override
    public Integer readSaveData(CompoundTag compound) {
        return compound.getInt("power");
    }
}

// 注册
@Override
public void registerTaskData(TaskDataRegister register) {
    register.register(new MagicPowerKey());
}
```

---

## 四、任务系统 API

### 4.1 `IMaidTask` 接口

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask`

**用途**：女仆工作模式/任务接口，定义女仆的 AI 行为。

**必须实现的方法**：

| 方法                             | 返回类型                                   | 用途      |
| ------------------------------ | -------------------------------------- | ------- |
| `getUid()`                     | `ResourceLocation`                     | 任务唯一 ID |
| `getIcon()`                    | `ItemStack`                            | 任务图标    |
| `getAmbientSound(EntityMaid)`  | `SoundEvent`                           | 环境音效    |
| `createBrainTasks(EntityMaid)` | `List<Pair<Integer, BehaviorControl>>` | AI 任务列表 |

**可选覆盖的方法**：

| 方法                                    | 默认值     | 用途          |
| ------------------------------------- | ------- | ----------- |
| `createRideBrainTasks(EntityMaid)`    | 空列表     | 骑乘/待命时的 AI  |
| `isEnable(EntityMaid)`                | `true`  | 任务是否可用      |
| `isHidden(EntityMaid)`                | `false` | 是否在界面隐藏     |
| `enableLookAndRandomWalk(EntityMaid)` | `true`  | 是否允许张望和随机走动 |
| `enablePanic(EntityMaid)`             | `true`  | 是否允许受伤后逃跑   |
| `enableEating(EntityMaid)`            | `true`  | 是否允许吃饭      |
| `workPointTask(EntityMaid)`           | `false` | 是否需要工作点     |
| `canSitInJoy(EntityMaid, String)`     | `false` | 是否允许坐娱乐方块   |
| `searchRadius(EntityMaid)`            | 工作范围    | 实体搜索半径      |

**使用示例**：

```java
public class MinerTask implements IMaidTask {
    private static final ResourceLocation UID = 
        ResourceLocation.fromNamespaceAndPath("mymod", "miner");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Items.DIAMOND_PICKAXE);
    }

    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return SoundEvents.ANVIL_USE;
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        List<Pair<Integer, BehaviorControl<? super EntityMaid>>> tasks = new ArrayList<>();
        tasks.add(Pair.of(0, new MineBlockTask()));
        tasks.add(Pair.of(1, new PickupItemsTask()));
        return tasks;
    }

    @Override
    public boolean enableLookAndRandomWalk(EntityMaid maid) {
        return false; // 专心挖矿，不要乱走
    }
}

// 注册
@Override
public void addMaidTask(TaskManager manager) {
    manager.registerTask(new MinerTask());
}
```

---

### 4.2 子任务接口

| 接口                    | 用途       |
| --------------------- | -------- |
| `IAttackTask`         | 近战攻击任务标记 |
| `IRangedAttackTask`   | 远程攻击任务标记 |
| `IFarmTask`           | 农场任务标记   |
| `IFeedTask`           | 喂食任务标记   |
| `ISpecialCropHandler` | 特殊作物处理   |
| `IMaidMeal`           | 女仆饭类型    |

---

## 五、饰品与背包 API

### 5.1 `IMaidBauble` 接口

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble`

**用途**：女仆饰品接口，定义饰品的效果和行为。

**生命周期方法**：

| 方法                                            | 调用时机       |
| --------------------------------------------- | ---------- |
| `onTick(maid, baubleItem)`                    | 每 tick 执行  |
| `onInjured(maid, baubleItem, source, damage)` | 受伤时（可取消伤害） |
| `onDeath(maid, baubleItem, source)`           | 死亡时（可取消死亡） |
| `onPutOn(maid, baubleItem)`                   | 装备时        |
| `onTakeOff(maid, baubleItem)`                 | 卸下时        |

**触发方法**：

| 方法                                                                | 调用时机     |
| ----------------------------------------------------------------- | -------- |
| `onMeleeAttack(maid, baubleItem, target)`                         | 近战攻击时    |
| `onRangedAttack(maid, baubleItem, task)`                          | 远程攻击时    |
| `onMaidEat(maid, baubleItem, foodItem, mealType)`                 | 吃东西时     |
| `onFavorabilityLevelChange(maid, baubleItem, oldLevel, newLevel)` | 好感度等级变化时 |

**其他方法**：

```java
boolean syncClient(maid, baubleItem);  // 是否同步到客户端
```

**使用示例**：

```java
public class FireRingBauble implements IMaidBauble {

    @Override
    public void onTick(EntityMaid maid, ItemStack baubleItem) {
        // 周围生物着火
        List<LivingEntity> nearby = maid.level().getEntitiesOfClass(
            LivingEntity.class, 
            maid.getBoundingBox().inflate(3),
            e -> e != maid
        );
        for (LivingEntity entity : nearby) {
            entity.setSecondsOnFire(2);
        }
    }

    @Override
    public void onPutOn(EntityMaid maid, ItemStack baubleItem) {
        // 添加火焰抗性
        maid.getAttribute(Attributes.FIRE_RESISTANCE).addTransientModifier(
            new AttributeModifier("fire_ring", 1.0, AttributeModifier.Operation.ADD_VALUE)
        );
    }

    @Override
    public void onTakeOff(EntityMaid maid, ItemStack baubleItem) {
        // 移除火焰抗性
        maid.getAttribute(Attributes.FIRE_RESISTANCE).removeModifier("fire_ring");
    }
}

// 注册饰品
@Override
public void bindMaidBauble(BaubleManager manager) {
    manager.registerBauble(Items.BLAZE_ROD, new FireRingBauble());
}
```

---

### 5.2 `IMaidBackpack` 抽象类

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack`

**用途**：女仆背包类型，定义背包的行为和 GUI。

**必须实现的方法**：

| 方法                                  | 用途        |
| ----------------------------------- | --------- |
| `getId()`                           | 背包唯一 ID   |
| `getItem()`                         | 背包对应的物品   |
| `onPutOn(stack, player, maid)`      | 装备时回调     |
| `onTakeOff(stack, player, maid)`    | 卸下时回调     |
| `onSpawnTombstone(maid, tombstone)` | 女仆死亡生成墓碑时 |
| `getGuiProvider(entityId)`          | GUI 提供者   |
| `getAvailableMaxContainerIndex()`   | 可用容器最大索引  |

**客户端方法**：

```java
void offsetBackpackItem(PoseStack poseStack);  // 背部物品偏移
EntityModel<EntityMaid> getBackpackModel(modelSet);  // 背包模型
ResourceLocation getBackpackTexture();  // 背包材质
```

**可选覆盖方法**：

```java
ItemStack getTakeOffItemStack(stack, player, maid);  // 卸下时返回的物品
boolean hasBackpackData();  // 是否有背包数据
IBackpackData getBackpackData(maid);  // 获取背包数据
```

---

### 5.3 `IBackpackData` 接口

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.backpack.IBackpackData`

**用途**：背包数据存储接口。

**方法**：

```java
void load(CompoundTag tag);              // 从 NBT 加载
CompoundTag save();                      // 保存到 NBT
void syncToClient();                     // 同步到客户端
```

---

### 5.4 `ITriggerSlotChange` 接口

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.backpack.ITriggerSlotChange`

**用途**：饰品槽位变化触发器。

---

## 六、事件 API

### 6.1 客户端渲染事件

#### `RenderMaidEvent`

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.event.client.RenderMaidEvent`

**触发时机**：女仆渲染前

**可取消**：是

**方法**：

```java
IMaid getMaid();                       // 获取女仆
MaidModels.ModelData getModelData();   // 获取模型数据（可修改）
```

**使用示例**：

```java
@EventBusSubscriber(value = Dist.CLIENT)
public class RenderHandler {
    @SubscribeEvent
    public static void onRenderMaid(RenderMaidEvent event) {
        // 根据条件修改模型
        if (event.getMaid().getFavorability() > 100) {
            event.getModelData().setModel(loveModel);
            event.setCanceled(true);
        }
    }
}
```

---

### 6.2 动画加载事件

#### `DefaultGeckoAnimationEvent`

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.event.client.DefaultGeckoAnimationEvent`

**触发时机**：资源重载时，默认动画加载完成后

**方法**：

```java
AnimationFile getAnimationFile(AnimationType type);  // 获取动画文件
void addAnimation(AnimationType type, ResourceLocation file);  // 添加动画文件
```

**动画类型枚举**：

- `MAID` - 女仆主动画
- `TAC` - TACZ 枪械动画
- `CHAIR` - 坐垫动画
- `ISS` - 铁魔法动画
- `IM` - Immersive Melodies 动画

**使用示例**：

```java
@SubscribeEvent
public static void onAddAnimation(DefaultGeckoAnimationEvent event) {
    // 添加自定义动画文件
    event.addAnimation(DefaultGeckoAnimationEvent.AnimationType.MAID,
        ResourceLocation.fromNamespaceAndPath("mymod", "animation/maid.json"));
}
```

---

### 6.3 实体转换事件

#### `ConvertMaidEvent`

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.event.ConvertMaidEvent`

**触发时机**：`IMaid.convert(Mob)` 调用时

**用途**：允许其他模组的实体使用女仆渲染系统。

**方法**：

```java
Mob getEntity();           // 获取原始实体
void setMaid(IMaid maid);  // 设置女仆实现
IMaid getMaid();           // 获取女仆实现
```

**使用示例**：

```java
@SubscribeEvent
public static void onConvertMaid(ConvertMaidEvent event) {
    if (event.getEntity() instanceof MyCustomEntity entity) {
        event.setMaid(new IMaid() {
            @Override
            public Mob asEntity() {
                return entity;
            }

            @Override
            public String getModelId() {
                return "mymod:custom_model";
            }

            // 实现其他方法...
        });
    }
}
```

---

### 6.4 女仆 Tick 事件

#### `MaidTickEvent`

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent`

**触发时机**：`EntityMaid.tick()` 开始时

**可取消**：是（取消后不执行后续 Tick 逻辑）

**方法**：

```java
EntityMaid getMaid();
```

---

### 6.5 其他事件

| 事件                        | 触发时机     | 用途     |
| ------------------------- | -------- | ------ |
| `MaidAttackEvent`         | 女仆攻击时    | 修改攻击行为 |
| `MaidDamageEvent`         | 女仆受到伤害时  | 修改伤害计算 |
| `MaidDeathEvent`          | 女仆死亡时    | 处理死亡逻辑 |
| `MaidHurtEvent`           | 女仆受伤时    | 处理受伤逻辑 |
| `MaidPickupEvent`         | 女仆拾取物品时  | 处理拾取逻辑 |
| `MaidTamedEvent`          | 女仆被驯服时   | 处理驯服逻辑 |
| `MaidTaskEnableEvent`     | 任务启用/禁用时 | 监听任务状态 |
| `MaidBackpackChangeEvent` | 背包变化时    | 监听背包变化 |
| `MaidBaubleChangeEvent`   | 饰品变化时    | 监听饰品变化 |
| `InteractMaidEvent`       | 与女仆交互时   | 处理交互逻辑 |

---

## 七、GUI 事件 API

### 7.1 `MaidContainerGuiEvent`

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.event.client.MaidContainerGuiEvent`

**用途**：女仆 GUI 相关事件，有三个子事件：

#### `Init` 事件

```java
void addButton(String name, AbstractWidget button);  // 添加按钮
void removeButton(String name);                      // 移除按钮
```

#### `Render` 事件

```java
GuiGraphics getGraphics();  // 获取渲染器
int getMouseX/Y();          // 获取鼠标位置
float getPartialTicks();    // 获取插值
```

#### `Tooltip` 事件

```java
// 在 Render 事件基础上，用于渲染 Tooltip
```

**使用示例**：

```java
@EventBusSubscriber(value = Dist.CLIENT)
public class MaidGuiHandler {
    @SubscribeEvent
    public static void onGuiInit(MaidContainerGuiEvent.Init event) {
        event.addButton("my_button", new Button(
            event.getLeftPos() + 10, event.getTopPos() + 10,
            100, 20,
            Component.literal("My Button"),
            btn -> handleClick(event.getGui().getMaid())
        ));
    }

    @SubscribeEvent
    public static void onGuiRender(MaidContainerGuiEvent.Render event) {
        event.getGraphics().renderItem(
            new ItemStack(Items.DIAMOND),
            event.getLeftPos() + 50, event.getTopPos() + 50
        );
    }
}
```

---

## 八、其他 API

### 8.1 `IChairData` 接口

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.animation.IChairData`

**用途**：椅子数据接口。

---

### 8.2 `IMaidData` 接口

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.animation.IMaidData`

**用途**：女仆数据接口。

---

### 8.3 `IEntityData` 接口

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.animation.IEntityData`

**用途**：实体数据基础接口。

---

### 8.4 `IWorldData` 接口

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.animation.IWorldData`

**用途**：世界数据接口。

---

### 8.5 `IBroomControl` 接口

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.entity.IBroomControl`

**用途**：扫帚控制接口。

---

### 8.6 `IFishingType` 接口

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.entity.fishing.IFishingType`

**用途**：钓鱼类型接口。

---

### 8.7 `IMultiBlock` 接口

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.block.IMultiBlock`

**用途**：多方块结构接口。

---

### 8.8 `IMaidEdibleBlock` 接口

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.block.IMaidEdibleBlock`

**用途**：女仆可食用方块接口。

---

### 8.9 `ICustomSoundBuffer` 接口

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.client.sound.ICustomSoundBuffer`

**用途**：自定义声音缓冲区接口。

---

### 8.10 `MaidRenderState` 接口

**位置**：`com.github.tartaricacid.touhoulittlemaid.api.client.render.MaidRenderState`

**用途**：女仆渲染状态接口。

---

## 九、注册流程总结

### 9.1 服务端/通用注册

```java
@LittleMaidExtension
public class MyExtension implements ILittleMaid {
    @Override
    public void addMaidTask(TaskManager manager) {
        // 注册任务
    }

    @Override
    public void addMaidBackpack(BackpackManager manager) {
        // 注册背包
    }

    @Override
    public void bindMaidBauble(BaubleManager manager) {
        // 注册饰品
    }

    // ... 其他注册
}
```

### 9.2 客户端注册

```java
@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientRegistration {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // 客户端初始化
    }
}

@EventBusSubscriber(value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onRenderMaid(RenderMaidEvent event) {
        // 渲染事件
    }

    @SubscribeEvent
    public static void onAddAnimation(DefaultGeckoAnimationEvent event) {
        // 动画加载
    }
}
```

---

## 十、最佳实践

### 10.1 依赖配置

```gradle
dependencies {
    // 如果使用 JEI 等兼容
    compileOnly "mezz.jei:jei-${minecraft_version}-common-api:${jei_version}"
    compileOnly "mezz.jei:jei-${minecraft_version}-neoforge-api:${jei_version}"
}
```

### 10.2 注意事项

1. **`@OnlyIn(Dist.CLIENT)`**：所有客户端代码必须标注此注解
2. **无参构造函数**：`ILittleMaid` 实现类必须有无参构造函数
3. **线程安全**：动画和渲染在客户端主线程执行
4. **资源加载时机**：动画/模型资源在资源重载阶段加载
5. **网络同步**：自定义数据需要考虑服务端→客户端同步

### 10.3 调试技巧

```java
@Override
public Collection<? extends Function<EntityMaid, List<DebugTarget>>> getMaidDebugTargets() {
    return List.of(maid -> {
        List<DebugTarget> targets = new ArrayList<>();
        targets.add(new DebugTarget("Magic Power", String.valueOf(magicPower)));
        return targets;
    });
}
```

---

**文档版本**: 1.0  
**最后更新**: 2026-04-02  
**适用版本**: Touhou Little Maid 1.21 分支
