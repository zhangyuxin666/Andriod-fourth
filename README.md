# 实验六 多 Fragment 应用

应用标题为「张雨鑫2024110548」。底部导航在个人信息、两张牌和三张牌三个 Fragment 之间切换。

- 个人信息可编辑并保存。取消会重新载入最近一次保存的资料。
- 每种模式各有 24 张牌。达到该模式的选牌张数后，比较点数和花色，匹配成功的牌会锁定。每翻开一张扣 1 分，同点数加 16 分，同花色加 4 分，匹配失败扣 2 分并盖回。
- 资料和两局游戏分别序列化到应用私有目录。重启后打开对应页即可继续，重置只作用于当前模式。

在 Android Studio 打开仓库并运行 `app`，或通过 Gradle Wrapper 执行 `assembleDebug testDebugUnitTest`。项目使用 Android SDK 37，应用最低支持 API 23。在 Windows 上运行单元测试时，请将项目放在纯 ASCII 路径下。可直接安装 [调试 APK](deliverables/MultiFragment-debug.apk)。

实验报告位于 [deliverables](deliverables)，模拟器运行截图及核心代码图位于 [screenshots](screenshots)。`Dev` 保存实验五源码基线，`Frag` 保存实验六成果。
