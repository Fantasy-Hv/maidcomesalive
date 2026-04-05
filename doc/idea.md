# 附属模组-maid-comes-alive企划

注意到目前的女仆基本上只会发出语音，或者执行任务，而没有与主人的互动。
本项目正是为填补这方面的空白而生，希望为狗修金带来一个更加有活人感的maid.

## 功能

1. 待机动作和语音
   为女仆添加待机动作，在 空闲、跟随主人、随机走动等行为状态中，女仆有概率表现一些动作，初步的想法是播放ysm动画的动作。
2. 添加互动动作，结合项目目前已有的和主人聊天的功能，可以添加一个functino call让主人呼叫酒狐做某一个动作。
   🎭 动作创意分类
   1️⃣ 情绪表达类动画
   根据女仆的好感度、心情状态触发的自然反应：
   开心时：
   happy_jump - 小幅度蹦跳，耳朵抖动
   twirl - 原地转圈，裙摆飞扬
   clap - 拍手欢呼
   heart_sign - 比心手势
   害羞时（低好感度或特定互动）：
   shy_cover_face - 双手捂脸
   look_away - 低头侧身，手指绞在一起
   blush_fidget - 不安地扭动身体
   困倦时：
   yawn_stretch - 打哈欠伸懒腰
   rub_eyes - 揉眼睛
   sleepy_wobble - 站不稳摇晃
   惊讶时：
   surprised_jump - 被吓到跳起
   gasp - 倒吸一口气，后退半步
   2️⃣ 环境互动类动画
   根据周围环境自动触发的行为：
   天气相关：
   rain_dance - 下雨时开心地踩水坑
   snow_play - 下雪时接雪花
   wind_hold_hair - 大风时按住头发/帽子
   thunder_scared - 雷声时害怕地抱住自己
   时间相关：
   morning_greet - 清晨向主人挥手问好
   sunset_watch - 黄昏时眺望远方
   night_yawn - 夜晚打哈欠
   地形相关：
   flower_smell - 经过花丛时停下闻花香
   water_splash - 涉水时踢水花
   leaf_kick - 走过草地时踢落叶
   3️⃣ 社交互动类动画
   与玩家或其他实体的互动：
   对主人：
   wave_greeting - 看到主人时挥手
   run_to_master - 跑向主人（距离远时）
   follow_close - 紧跟时的轻快步伐
   present_gift - 递送物品时的礼仪动作
   curtsy - 行礼/屈膝礼
   对其他女仆：
   chat_gesture - 聊天时的手势
   high_five - 击掌
   group_dance - 多个女仆一起跳舞（需同步）
   对生物：
   pet_cat - 抚摸猫的动作
   feed_animal - 喂食动物
   scare_creeper - 看到苦力怕时害怕后退
   4️⃣ 工作间隙类动画
   在任务执行中的小动作：
   空闲待机增强：
   stretch_back - 伸懒腰
   check_nails - 检查指甲
   fix_hair - 整理头发
   adjust_clothes - 整理衣服
   humming - 哼歌时轻微摇摆
   daydream - 发呆，眼神飘忽
   工作完成后：
   task_complete_cheer - 完成任务时的小庆祝
   wipe_sweat - 擦汗
   proud_pose - 叉腰得意姿势
   5️⃣ 特殊情境类动画
   触发条件更复杂的趣味动作：
   战斗相关：
   victory_dance - 击败敌人后跳舞
   dodge_roll - 闪避翻滚
   shield_up - 举盾防御姿态
   weapon_spin - 武器旋转耍帅
   受伤时：
   hurt_flinch - 受伤时的退缩
   bandage_self - 自我包扎
   cry_for_help - 求助动作
   特殊物品：
   eat_delicious - 吃到美食时的幸福表情
   drink_tea - 优雅喝茶
   read_book - 看书时的专注姿态
   play_instrument - 演奏乐器（可联动 Immersive Melodies）
   6️⃣ 节日/彩蛋类动画
   birthday_celebrate - 生日庆祝（检测玩家生日）
   halloween_trick - 万圣节搞怪
   christmas_gift - 圣诞节送礼物
   new_year_bow - 新年鞠躬
## 实现方案

这里需要考虑以下问题：

- 如何让女仆表现某一动作？---ysm动作播放。-client动作渲染
  
  - 面临的问题是：女仆模组里只能通过ysm轮盘动画名称来播放ysm动画，但是轮盘动画名称又是由ysm模型自定义的，并且ysm闭源，难以获取模型数据

- function call 集成。