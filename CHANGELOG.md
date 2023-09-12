# Changelog

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [2023.09.10-1](#20230910-1)
  - [External](#external)
- [2023.09.10](#20230910)
  - [Common](#common)
  - [External](#external-1)
- [2023.09.09](#20230909)
  - [AndroidX](#androidx)
  - [Common](#common-1)
  - [Compose](#compose)
- [2023.08.29](#20230829)
  - [Compose](#compose-1)
- [2023.08.24](#20230824)
  - [AndroidX](#androidx-1)
  - [Accompanist](#accompanist)
  - [Common](#common-2)
  - [Compose](#compose-2)
  - [External](#external-2)
  - [Firebase](#firebase)
- [2023.08.19](#20230819)
  - [AndroidX](#androidx-2)
  - [Common](#common-3)
- [2023.08.12](#20230812)
  - [Common](#common-4)
  - [External](#external-3)
- [2023.08.11](#20230811)
  - [AndroidX](#androidx-3)
- [Common](#common-5)
  - [Compose](#compose-3)
- [2023.08.02](#20230802)
  - [Common](#common-6)
- [2023.07.27](#20230727)
  - [AndroidX](#androidx-4)
  - [Common](#common-7)
  - [Compose](#compose-4)
- [2023.07.22](#20230722)
  - [Common](#common-8)
- [2023.07.20](#20230720)
  - [AndroidX](#androidx-5)
  - [Common](#common-9)
  - [Compose](#compose-5)
  - [Firebase](#firebase-1)
- [2023.06.30](#20230630)
  - [Common](#common-10)
  - [Compose](#compose-6)
- [2023.06.26](#20230626)
  - [AndroidX](#androidx-6)
  - [Common](#common-11)
  - [Compose](#compose-7)
  - [Firebase](#firebase-2)
- [2023.06.08](#20230608)
  - [AndroidX](#androidx-7)
  - [Common](#common-12)
  - [Compose](#compose-8)
  - [Firebase](#firebase-3)
- [2023.05.25](#20230525)
  - [AndroidX](#androidx-8)
  - [Common](#common-13)
- [2023.05.22](#20230522)
  - [AndroidX](#androidx-9)
  - [Common](#common-14)
  - [Compose](#compose-9)
- [2023.05.05](#20230505)
  - [Common](#common-15)
- [2023.05.04](#20230504)
  - [AndroidX](#androidx-10)
  - [Common](#common-16)
  - [Compose](#compose-10)
  - [Firebase](#firebase-4)
- [2023.05.01](#20230501)
  - [AndroidX](#androidx-11)
  - [Common](#common-17)
  - [Compose](#compose-11)
- [2023.04.20](#20230420)
  - [Accompanist](#accompanist-1)
  - [AndroidX](#androidx-12)
  - [Common](#common-18)
  - [Firebase](#firebase-5)
- [2023.04.07](#20230407)
  - [Common](#common-19)
- [2023.04.05](#20230405)
  - [Accompanist](#accompanist-2)
  - [AndroidX](#androidx-13)
  - [Compose](#compose-12)
- [2023.04.03](#20230403)
  - [Common](#common-20)
- [2023.03.30](#20230330)
  - [Common](#common-21)
  - [Firebase](#firebase-6)
- [2023.03.26](#20230326)
  - [Accompanist](#accompanist-3)
  - [Common](#common-22)
  - [Firebase](#firebase-7)
- [2023.03.22](#20230322)
  - [AndroidX](#androidx-14)
  - [Compose](#compose-13)
- [2023.03.21](#20230321)
  - [Common](#common-23)
- [2023.03.16](#20230316)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## 2023.09.12

## Common

- Update gradle cachefix plugin to 2.8.0

### External

- Remove RMR plugins

## 2023.09.10-1

### External

- Add [kradle plugins](https://github.com/Lyxnx/kradle) to external catalog

## 2023.09.10

### Common

- Added android cache fix plugin

### External

- Added updated catalogs android plugins

## 2023.09.09

### AndroidX

- Update navigation to 2.7.2
- Update core to 1.12.0
- Update annotation to 1.7.0
- Update webkit to 1.8.0
- Update lifecycle to 2.6.2
- Update paging to 3.2.1

### Common

- Update hilt to 2.48

### Compose

- Update BOM to 2023.09.00
    - Note using the compose-ui module from this BOM will require target SDK be updated to 34

## 2023.08.29

### Compose

- Update compiler to 1.5.3

## 2023.08.24

### AndroidX

- Update navigation to 2.7.1

### Accompanist

- Update accompanist to 0.32.0

### Common

- Update ksp to 1.9.10-1.0.13
- Update kotlin to 1.9.10
- Update mockito to 5.5.0
- Update AGP to 8.1.1

### Compose

- Update compiler to 1.5.2

### External

- Update glide to 4.16.0

### Firebase

- Update BOM to 32.2.3

## 2023.08.19

### AndroidX

- Fix core-role dependency version pointing to wrong reference
- Update media3 to 1.1.1

### Common

- Update mockk to 1.13.7

## 2023.08.12

### Common

- Add kotlin multiplatform plugin

### External

- Add catalogs plugins

## 2023.08.11

- Added versions-external catalog that contain third party libraries and keep versions-common for standard android
  dependencies

### AndroidX

- Update wear-protolayout to 1.0.0

## Common

- Update mockito-kotlin to 5.1.0

### Compose

- Update BOM to 2023.08.00

## 2023.08.02

Starting from this release, the catalogs can be found in maven central

### Common

- Add [vanniktech publish plugin](https://github.com/vanniktech/gradle-maven-publish-plugin)

## 2023.07.27

### AndroidX

- Update paging to 3.2.0
- Update fragment to 1.6.1
- Update recyclerview to 1.3.1
- Update preference to 1.2.1

Remove libraries that don't have a stable version:

- ads
- appsearch
- core-animation
- core-performance
- core-remoteview
- core-uwb
- credentials
- glance
- graphics
- health
- health-connect
- heifwriter
- input
- javascriptengine
- metrics
- print
- privacysandbox-ads
- privacysandbox-sdkruntime
- privacysandbox-tools
- test-uiautomator
- textclassifier
- tv
- tvprovider
- wear-protolayout


- Downgrade biometric to latest stable 1.1.0

### Common

- Update AGP to 8.1.0
- Update kotlinx-coroutines to 1.7.3

### Compose

- Update compiler to 1.5.1

## 2023.07.22

### Common

- Add mockito-core, mockito-android, mockito-kotlin dependencies

## 2023.07.20

### AndroidX

-
- Update media3 to 1.1.0

### Common

- Update lottie to 6.1.0
- Update hilt to 2.47
- Update KSP to 1.9.0-1.0.12
- Update kotlin to 1.9.0

### Compose

- Update compiler to 1.5.0

### Firebase

- Update BOM to 32.2.0

## 2023.06.30

### Common

- Update kotlinx-coroutines to 1.7.2

### Compose

- Update compiler to 1.4.8

## 2023.06.26

### AndroidX

- Update wear-protolayout to 1.0.0-beta01
- Update room to 2.5.2
- Update privacysandbox-sdkruntime to 1.0.0-alpha06

### Common

- Update lottie to 6.0.1

### Compose

- Update BOM to 2023.06.01

### Firebase

- Update BOM to 32.1.1

## 2023.06.08

### AndroidX

- Update privacysandbox-ads to 1.0.0-beta05
- Update window to 1.1.0
- Update navigation to 2.6.0
- Update fragment to 1.6.0
- Update wear-protolayout to 1.0.0-alpha11
- Update tv to 1.0.0-alpha07
- Update privacysandbox-sdkruntime to 1.0.0-alpha05
- Update graphics-core to 1.0.0-alpha04
- Update credentials to 1.2.0-alpha05

### Common

- Update KSP to 1.8.22-1.0.11
- Update kotlin to 1.8.22
- Add redmadrobot publish plugin to plugins block
- Update kotlinpoet to 1.14.2

### Compose

- Update BOM to 2023.06.00

### Firebase

- Update BOM to 32.1.0

## 2023.05.25

### AndroidX

- Update camera to 1.2.3
- Update appsearch to 1.1.0-alpha03
- Update webkit to 1.7.0
- Update wear-protolayout to 1.0.0-alpha10
- Update privacysandbox-tools to 1.0.0-alpha04
- Update games-activity to 2.0.2
- Update activity to 1.7.2

### Common

- Add composeicons: simpleicons, feather, tablericons, evaicons, fontawesome, octicons, linea, lineawesome,
  weathericons, cssgg
- Update AGP to 8.0.2

## 2023.05.22

### AndroidX

- Update media3 to 1.0.2
- Update core to 1.10.1
- Update privacysandbox-sdkruntime to 1.0.0-alpha04
- Update glance-appwidget to 1.0.0-beta01
- Update wear-protolayout to 1.0.0-alpha09
- Update privacysandbox-ads to 1.0.0-beta04
- Update core-remoteviews to 1.0.0-beta04
- Update credentials-* to 1.2.0-alpha04

### Common

- Update coil to 2.4.0
- Update robolectric to 4.10.3
- Update hilt to 2.46.1
- Update kotlinx-coroutines-android to 1.7.1

### Compose

- Update BOM to 2023.05.01

## 2023.05.05

### Common

- Update kotlinx to 1.7.0
- Add google material 1.9.0
- Add [redmadrobot libraries](https://github.com/RedMadRobot/gradle-infrastructure)
- Update kotlinpoet to 1.13.2

## 2023.05.04

### AndroidX

- Update mediarouter to 1.4.0
- Update profileinstaller to 1.3.1

### Common

- Add kotlinpoet, kotlinpoet-ksp dependencies
- Update robolectric to 4.10.2
- Update AGP to 8.0.1

### Compose

- Update compiler to 1.4.7
- Update BOM to 2023.05.00

### Firebase

- Update BOM to 32.0.0

## 2023.05.01

### AndroidX

- Update wear-protolayout to 1.0.0-alpha08
- Update tv to 1.0.0-alpha06
- Update privacysandbox-ads to 1.0.0-beta03
- Update media3 to 1.0.1
- Update fragment to 1.5.7
- Update uiautomator to 2.3.0-alpha03
- Update core-splashscreen to 1.0.1
- Update activity to 1.7.1

### Common

- Add ksp-symbolprocessingapi dependency
- Update hilt to 2.46
- Update kotlin to 1.8.21
- Update okhttp to 4.11.0
- Update Google services plugin to 4.3.15
- Update KSP to 1.8.21-1.0.11

### Compose

- Update compiler to 1.4.6
- Update BOM to 2023.04.01

## 2023.04.20

### Accompanist

- Add pager-indicators dependency

### AndroidX

- Update credentials to 1.2.0-alpha03

### Common

- Update mockk to 1.13.5
- Update AGP to 8.0.0
- Update robolectric to 4.10

### Firebase

- Update BOM to 31.5.0

## 2023.04.07

### Common

- Revert kotlin back to 1.8.10 as compose only supports up to 1.8.10 currently

## 2023.04.05

### Accompanist

- Update BOM to 0.30.1

### AndroidX

- Update core to 1.10.0
- Update wear-protolayout to 1.0.0-alpha07
- Update privacysandbox-sdkruntime to 1.0.0-alpha03
- Update metrics to 1.0.0-alpha04
- Update javascriptengine to 1.0.0-alpha05
- Update health-services-client to 1.0.0-beta03
- Update games-activity to 2.0.1
- Update core-uwb to 1.0.0-alpha05

### Compose

- Update BOM to 2023.04.00

## 2023.04.03

### Common

- Update KSP to 1.8.20-1.0.10

## 2023.03.30

### Common

- Update kotlin to 1.8.20

### Firebase

- Update BOM to 31.4.0

## 2023.03.26

### Accompanist

- Update BOM to 0.30.0

### Common

- Update coil to 2.3.0

### Firebase

- Update BOM to 31.4.0

## 2023.03.22

### AndroidX

- Update profileinstaller to 1.3.0
- Update drawerlayout to 1.2.0
- Update tv to 1.0.0-alpha05
- Update fragment to 1.5.6
- Update webkit to 1.6.1
- Update savedstate to 1.2.1
- Update privacysandbox-sdkruntime to 1.0.0-alpha02
- Update emoji2 to 1.3.0
- Update work to 2.8.1
- Update wear-protolayout to 1.0.0-alpha06
- Update sqlite to 2.3.1
- Update room to 2.5.1
- Update privacysandbox-ads to 1.0.0-beta02
- Update lifecycle to 2.6.1
- Update camera to 1.2.2
- Update activity to 1.7.0
- Update input-motionprediction to 1.0.0-beta01
- Update graphics-core to 1.0.0-alpha03
- Update media3 to 1.0.0

### Compose

- Update BOM to 2023.03.00
- Update compiler to 1.4.4

## 2023.03.21

### Common

- Add mockk-android, mockk-agent dependencies

## 2023.03.16

- Initial release
