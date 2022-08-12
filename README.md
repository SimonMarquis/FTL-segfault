# FTL-segfault

[![Status](https://github.com/SimonMarquis/FTL-segfault/actions/workflows/ftl.yml/badge.svg)](https://github.com/SimonMarquis/FTL-segfault/actions/workflows/ftl.yml)

This simple test code produces random segmentation faults when running on [Firebase Test Lab](https://firebase.google.com/docs/test-lab) **virtual** devices (**physical** devices are not affected).

## 🔐 Authentication

Follow [these instructions](https://runningcode.github.io/fladle/authentication) to configure Flank.

> **Note**: By default, this project relies on a [serviceAccountCredentials.json](./serviceAccountCredentials.json) file to authenticate.

## 🧪 Run tests

- Build APKs
  `./gradlew assembleDebug assembleDebugAndroidTest`
- Execute flank
  `./gradlew runFlank`
- Check logs
  ```
  ┌─────────┬───┬────────────────────────────┬───────────────────────────────┐
  │ OUTCOME │ … │      TEST AXIS VALUE       │         TEST DETAILS          │
  ├─────────┼───┼────────────────────────────┼───────────────────────────────┤
  │ failure │ … │ NexusLowRes-28-en-portrait │ 1 test cases failed, 3 passed │
  │ failure │ … │ NexusLowRes-29-en-portrait │ Application crashed           │
  │ failure │ … │ NexusLowRes-30-en-portrait │ 1 test cases failed           │
  │ failure │ … │ Pixel2-28-en-portrait      │ 1 test cases failed           │
  │ failure │ … │ Pixel2-29-en-portrait      │ 1 test cases failed           │
  │ failure │ … │ Pixel2-30-en-portrait      │ 1 test cases failed           │
  │ success │ … │ redfin-30-en-portrait      │ 10 test cases passed          │
  └─────────┴───┴────────────────────────────┴───────────────────────────────┘
  ```
  > **Note**: `redfin` is the only **physical** device.

  Here are some scenarios that can happen:
   - All tests pass `10 test cases passed`
   - All tests fail `Application crashed`
   - Some tests pass, other don't `4 test cases failed, 6 passed`
   - Some tests pass, other don't and not all tests are executed `1 test cases failed, 1 passed`


This currently runs on [GitHub Actions](https://github.com/SimonMarquis/FTL-segfault/actions/) every day so you can take a look at the results for yourself.

## 💥 Crash

Here is an example of native crash (`signal 11 (SIGSEGV), code 1 (SEGV_MAPERR)`):

```
*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***
Build fingerprint: 'google/aosp_cf_x86_phone_gms/vsoc_x86:11/RJR1.201002.001/6879612:userdebug/test-keys'
Revision: '0'
ABI: 'x86'
Timestamp: 2022-08-13 02:44:22-0700
pid: 17398, tid: 17410, name: HeapTaskDaemon  >>> com.example.ftl <<<
uid: 10152
signal 11 (SIGSEGV), code 1 (SEGV_MAPERR), fault addr 0x0
Cause: null pointer dereference
    eax 00000000  ebx f14d633c  ecx 00000000  edx f14da0e8
    edi f71a3ed4  esi f71a3ed0
    ebp eff84a58  esp eff84980  eip f0f8d182
```

Unfortunately, the call stack does not contain any stack frame from our code (it might be filtered though), so we can't use [ndk-stack](https://developer.android.com/ndk/guides/ndk-stack).

<details><summary><code>Show complete logcat</code></summary>

```
A/libc(17398): Fatal signal 11 (SIGSEGV), code 1 (SEGV_MAPERR), fault addr 0x0 in tid 17410 (HeapTaskDaemon), pid 17398 (com.example.ftl)
I/crash_dump32(17807): obtaining output fd from tombstoned, type: kDebuggerdTombstone
I/tombstoned(283): received crash request for pid 17410
W/com.example.ftl(17398): type=1701 audit(0.0:655): auid=4294967295 uid=10152 gid=10152 ses=4294967295 subj=u:r:untrusted_app:s0:c152,c256,c512,c768 exe="/system/bin/app_process32" sig=11 res=1 app=com.example.ftl
I/crash_dump32(17807): performing dump of process 17398 (target tid = 17410)
I/init(0): Untracked pid 17813 exited with status 0
A/DEBUG(17807): *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***
A/DEBUG(17807): Build fingerprint: 'google/aosp_cf_x86_phone_gms/vsoc_x86:11/RJR1.201002.001/6879612:userdebug/test-keys'
A/DEBUG(17807): Revision: '0'
A/DEBUG(17807): ABI: 'x86'
A/DEBUG(17807): Timestamp: 2022-08-13 02:44:22-0700
A/DEBUG(17807): pid: 17398, tid: 17410, name: HeapTaskDaemon  >>> com.example.ftl <<<
A/DEBUG(17807): uid: 10152
A/DEBUG(17807): signal 11 (SIGSEGV), code 1 (SEGV_MAPERR), fault addr 0x0
A/DEBUG(17807): Cause: null pointer dereference
A/DEBUG(17807):     eax 00000000  ebx f14d633c  ecx 00000000  edx f14da0e8
A/DEBUG(17807):     edi f71a3ed4  esi f71a3ed0
A/DEBUG(17807):     ebp eff84a58  esp eff84980  eip f0f8d182
I/WindowManager(481): WIN DEATH: Window{6230224 u0 com.example.ftl/com.example.ftl.FeatureActivity}
I/Zygote(290): Process 17398 exited due to signal 11 (Segmentation fault)
I/ActivityManager(481): Process com.example.ftl (pid 17398) has died: fg  FGS 
W/ActivityManager(481): Crash of app com.example.ftl running instrumentation ComponentInfo{com.example.ftl.test/com.example.ftl.HiltTestRunner}
I/ActivityManager(481): Force stopping com.example.ftl appid=10152 user=0: finished inst
D/AndroidRuntime(17369): Shutting down VM
```

</details>

## 📱 Devices

> **Note**: This project uses `NexusLowRes (VIRTUAL) API 28–30 `, `Pixel2 (VIRTUAL) API 28–30 ` and `redfin (PHYSICAL) API 30`, but you can change it in [build.gradle](app/build.gradle).

<details><summary><code>gcloud firebase test android models list</code></summary>

```
+---------------------+--------------------+------------------------------------------+----------+-------------+-------------------------+---------------+
|       MODEL_ID      |        MAKE        |                MODEL_NAME                |   FORM   |  RESOLUTION |      OS_VERSION_IDS     |      TAGS     |
+---------------------+--------------------+------------------------------------------+----------+-------------+-------------------------+---------------+
| 1610                | Vivo               | vivo 1610                                | PHYSICAL | 1280 x 720  | 23                      |               |
| ASUS_X00T_3         | Asus               | ASUS_X00TD                               | PHYSICAL | 1080 x 2160 | 27,28                   |               |
| AmatiTvEmulator     | Google             | Google TV Amati                          | EMULATOR | 1080 x 1920 | 29                      | beta=29       |
| AndroidTablet270dpi | Generic            | Generic 720x1600 Android tablet @ 270dpi | VIRTUAL  | 1600 x 720  | 30                      |               |
| F01L                | FUJITSU            | F-01L                                    | PHYSICAL | 1280 x 720  | 27                      |               |
| FRT                 | HMD Global         | Nokia 1                                  | PHYSICAL |  854 x 480  | 27                      |               |
| G8142               | Sony               | G8142                                    | PHYSICAL | 1080 x 1920 | 25                      |               |
| G8342               | Sony               | G8342                                    | PHYSICAL | 1080 x 1920 | 26                      |               |
| G8441               | Sony               | G8441                                    | PHYSICAL |  720 x 1280 | 26                      | deprecated=26 |
| GoogleTvEmulator    | Google             | Google TV                                | EMULATOR |  720 x 1280 | 30                      | beta=30       |
| H8314               | Sony               | H8314                                    | PHYSICAL | 1080 x 2160 | 26                      | deprecated=26 |
| H9493               | Sony               | H9493                                    | PHYSICAL | 2880 x 1440 | 28                      |               |
| HWANE-LX1           | Huawei             | ANE-LX1                                  | PHYSICAL | 1080 x 2280 | 28                      | deprecated=28 |
| HWANE-LX2           | Huawei             | ANE-LX2                                  | PHYSICAL | 1080 x 2280 | 28                      |               |
| HWCOR               | Huawei             | COR-L29                                  | PHYSICAL | 1080 x 2340 | 27                      |               |
| HWMHA               | Huawei             | MHA-L29                                  | PHYSICAL | 1920 x 1080 | 24                      |               |
| Nexus10             | Samsung            | Nexus 10                                 | VIRTUAL  | 2560 x 1600 | 19,21,22                |               |
| Nexus4              | LG                 | Nexus 4                                  | VIRTUAL  | 1280 x 768  | 19,21,22                |               |
| Nexus5              | LG                 | Nexus 5                                  | VIRTUAL  | 1920 x 1080 | 19,21,22,23             |               |
| Nexus5X             | LG                 | Nexus 5X                                 | VIRTUAL  | 1920 x 1080 | 23,24,25,26             |               |
| Nexus6              | Motorola           | Nexus 6                                  | VIRTUAL  | 2560 x 1440 | 21,22,23,24,25          |               |
| Nexus6P             | Google             | Nexus 6P                                 | VIRTUAL  | 2560 x 1440 | 23,24,25,26,27          |               |
| Nexus7              | Asus               | Nexus 7 (2012)                           | VIRTUAL  | 1280 x 800  | 19,21,22                |               |
| Nexus7_clone_16_9   | Generic            | Nexus7 clone, DVD 16:9 aspect ratio      | VIRTUAL  | 1280 x 720  | 23,24,25,26             | beta          |
| Nexus9              | HTC                | Nexus 9                                  | VIRTUAL  | 2048 x 1536 | 21,22,23,24,25          |               |
| NexusLowRes         | Generic            | Low-resolution MDPI phone                | VIRTUAL  |  640 x 360  | 23,24,25,26,27,28,29,30 |               |
| OnePlus5T           | OnePlus            | ONEPLUS A5010                            | PHYSICAL | 1080 x 2160 | 28                      |               |
| Pixel2              | Google             | Pixel 2                                  | VIRTUAL  | 1920 x 1080 | 26,27,28,29,30          |               |
| Pixel3              | Google             | Pixel 3                                  | VIRTUAL  | 2160 x 1080 | 30                      |               |
| SC-02K              | Samsung            | SC-02K                                   | PHYSICAL | 2220 x 1080 | 28                      |               |
| SH-01L              | SHARP              | SH-01L                                   | PHYSICAL | 2160 x 1080 | 28                      |               |
| TC77                | Zebra Technologies | TC77                                     | PHYSICAL | 1280 x 720  | 27                      |               |
| a10                 | Samsung            | SM-A105FN                                | PHYSICAL |  720 x 1520 | 29                      |               |
| b2q                 | Samsung            | SM-F711U1                                | PHYSICAL |  260 x 512  | 30,31                   |               |
| bluejay             | Google             | Pixel 6a                                 | PHYSICAL | 2400 x 1080 | 32                      |               |
| blueline            | Google             | Pixel 3                                  | PHYSICAL | 2160 x 1080 | 28                      |               |
| cactus              | Xiaomi             | Redmi 6A                                 | PHYSICAL | 1440 x 720  | 27                      |               |
| cruiserlteatt       | Samsung            | SM-G892A                                 | PHYSICAL | 1080 x 2220 | 26                      |               |
| dreamlte            | Samsung            | SM-G950F                                 | PHYSICAL | 1080 x 2220 | 28                      |               |
| f2q                 | Samsung            | SM-F916U1                                | PHYSICAL | 2208 x 1768 | 30                      |               |
| flo                 | Asus               | Nexus 7                                  | PHYSICAL | 1200 x 1920 | 19                      |               |
| grandppltedx        | Samsung            | SM-G532G                                 | PHYSICAL |  540 x 960  | 23                      |               |
| griffin             | Motorola           | XT1650                                   | PHYSICAL | 1440 x 2560 | 24                      |               |
| gts3lltevzw         | Samsung            | SM-T827V                                 | PHYSICAL | 1536 x 2048 | 28                      |               |
| gts4lltevzw         | Samsung            | SM-T837V                                 | PHYSICAL | 2560 x 1600 | 28                      |               |
| hammerhead          | LG                 | Nexus 5                                  | PHYSICAL | 1920 x 1080 | 23                      |               |
| harpia              | Motorola           | Moto G Play                              | PHYSICAL | 1280 x 720  | 23                      |               |
| heroqlteaio         | Samsung            | SAMSUNG-SM-G930AZ                        | PHYSICAL | 1080 x 1920 | 26                      |               |
| htc_pmeuhl          | HTC                | HTC 10                                   | PHYSICAL | 1440 x 2560 | 26                      |               |
| hwALE-H             | Huawei             | ALE-L23                                  | PHYSICAL | 1280 x 720  | 21                      |               |
| joan                | LG                 | LG-H932                                  | PHYSICAL | 1440 x 2880 | 26                      |               |
| lt02wifi            | Samsung            | SM-T210                                  | PHYSICAL |  600 x 1024 | 19                      |               |
| lv0                 | LG                 | LG-AS110                                 | PHYSICAL |  854 x 480  | 23                      |               |
| oriole              | Google             | Pixel 6                                  | PHYSICAL | 2400 x 1080 | 31,32,33                |               |
| pettyl              | Motorola           | moto e5 play                             | PHYSICAL |  960 x 480  | 27                      |               |
| phoenix_sprout      | LG                 | LM-Q910                                  | PHYSICAL | 3120 x 1440 | 28                      | deprecated=28 |
| q2q                 | Samsung            | SM-F926U1                                | PHYSICAL | 2208 x 1768 | 30,31                   |               |
| redfin              | Google             | Pixel 5                                  | PHYSICAL | 2340 x 1080 | 30                      | default       |
| sailfish            | Google             | Pixel                                    | PHYSICAL |  640 x 480  | 25                      |               |
| starqlteue          | Samsung            | SM-G960U1                                | PHYSICAL | 1080 x 2220 | 26                      |               |
| walleye             | Google             | Pixel 2                                  | PHYSICAL | 1920 x 1080 | 27                      |               |
| x1q                 | Samsung            | SM-G981U1                                | PHYSICAL | 3200 x 1440 | 29                      |               |
+---------------------+--------------------+------------------------------------------+----------+-------------+-------------------------+---------------+
```

</details>
