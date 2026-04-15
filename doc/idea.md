# 附属模组-maid-comes-alive企划

注意到目前女仆主动和主人玩耍的动作只有乞食和打雪球。本项目正是为填补这方面的空白而生，希望为狗修金带来一个更加有活人感的maid。

## 功能规划

### 待机动作

为女仆添加待机动作，在空闲、跟随主人、随机走动等行为状态中，女仆有概率表现一些动作。

### 聊天动作系统

结合项目目前已有的和主人聊天的功能，可以添加 function call 让酒狐在和主人聊天时附带某些动作/表情。

## 动作创意分类

### 情绪表达类动画

根据女仆的好感度、心情状态触发的自然反应。

**开心时**

happy_jump - 小幅度蹦跳，耳朵抖动

twirl - 原地转圈，裙摆飞扬

clap - 拍手欢呼

heart_sign - 比心手势

**害羞时（低好感度或特定互动）**

shy_cover_face - 双手捂脸

look_away - 低头侧身，手指绞在一起

blush_fidget - 不安地扭动身体

**困倦时**

rub_eyes - 揉眼睛

sleepy_wobble - 站不稳摇晃

**惊讶时**（需要提供新机制...）

surprised_jump - 被吓到跳起

gasp - 倒吸一口气，后退半步

### 环境互动类动画

根据周围环境自动触发的行为。

**天气相关**
（下雨撑伞？）

rain_dance - 下雨时开心地踩水坑

snow_play - 下雪时接雪花

wind_hold_hair - 大风时按住头发/帽子（？？？）

thunder_scared - 雷声时害怕地抱住自己

**时间相关**

morning_greet - 清晨向主人挥手问好

night_yawn - 夜晚打哈欠

**地形相关**

flower_smell - 经过花丛时停下闻花香

water_splash - 涉水时踢水花

leaf_kick - 走过草地时踢落叶

### 社交互动类动画

与玩家或其他实体的互动。

**对主人**

wave_greeting - 看到主人时挥手（已实现）

follow_close - 紧跟时的轻快步伐

present_gift - 递送物品时的礼仪动作

curtsy - 行礼/屈膝礼

**对其他女仆**

chat_gesture - 聊天时的手势

high_five - 击掌

group_dance - 多个女仆一起跳舞（需同步，貌似有点难实现）

**对生物**

pet_cat - 抚摸猫的动作

feed_animal - 喂食动物的动作

scare_creeper - 看到苦力怕时恐吓（女仆可以吓跑苦力怕）

### 工作间隙类动画

在任务执行中的小动作。

**空闲待机增强**

stretch_back - 伸懒腰（原模组已有）

fix_hair - 整理头发

adjust_clothes - 整理衣服

humming - 哼歌时轻微摇摆

daydream - 发呆，眼神飘忽

**工作完成后**

task_complete_cheer - 完成任务时的小庆祝

wipe_sweat - 擦汗


### 特殊情境类动画

触发条件更复杂的趣味动作。

**战斗相关**

victory_dance - 战斗胜利后跳舞

weapon_spin - 武器旋转耍帅

**受伤时**

bandage_self - 自我包扎

cry_for_help - 求助动作

**特殊物品**

eat_delicious - 吃到美食时的幸福表情

drink_tea - 优雅喝茶？？


### 节日/彩蛋类动画

birthday_celebrate - 生日庆祝（检测玩家生日）
new_year_bow - 新年鞠躬
。。。
## 实现方案
### 配置方案
需要给用户提供动画逻辑的开关
### 资源加载
女仆的动画是要女仆的模型一致，脱离模型的孤立动作会出现表现兼容性问题。目前项目采用将geckolib动画合并到模组原有geckolib模型动画中的方案，只是试验性地设计了大正女仆酒狐的动作并硬编码添加到酒狐的动作列表中，灵活性较差。

- 如何让女仆表现某一动作？ysm模型动作？geckolib模型动作？硬编码？
  - 目前geckolib模型动作已经在技术上验证可行。
  - ysm面临的问题是：女仆模组里只能通过ysm轮盘动画名称来播放ysm动画，但是轮盘动画名称又是由ysm模型自定义的，并且ysm闭源，难以获取模型数据。
  一个解决办法是提供运行时配置，用户自行配置ysm模型的动作列表，然后就可以使用女仆模组的ysm兼容接口来播放动画
### 动画播放时机判定
目前暂定使用IBranExtension接口添加新Behavior并配合animationState注册来实现
### function call 集成。