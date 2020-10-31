
# react-native-thinking-data

react-native-thinking-data是数数科技提供给客户，方便客户导入用户数据的 ReactNative 接口实现。如需了解详细信息，请参考 [数数科技官方网站](https://www.thinkingdata.cn).


## 1.集成方式
react-native-thinking-data有自动和手动两种集成方法,推荐使用自动集成
### 1.1自动集成

#### 1. npm 安装react-native-thinking-data 模块

```sh
npm install react-native-thinking-data --save`
```

#### 2. `link` react-native-thinking-data 模块

```sh
react-native link react-native-thinking-data（React Native 0.60 以下版本）
cd ios&&pod install（React Native 0.60 以上版本）
```


### 详细文档请参考：[Android & iOS SDK 在 React Native 中使用说明](https://www.thinkingdata.cn)

----

###1.2手动集成
#### 1. npm 安装 react-native-thinking-data 模块

```sh
npm install react-native-thinking-data --save`
```
#### 2. Android&iOS平台配置

#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-thinking-data` and add `RNThinkingData.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNThinkingData.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import cn.thinking.RNThinkingDataPackage;` to the imports at the top of the file
  - Add `new RNThinkingDataPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-thinking-data'
  	project(':react-native-thinking-data').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-thinking-data/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-thinking-data')
  	```
