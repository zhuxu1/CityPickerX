<p align="center">
<img src="art/CityPickerX.png">
</p>

# CityPicker-X ：城市选择/联系人列表

#### CityPicker-X 是一个可用于任何分组式列表+侧导航需求的开源框架

#### 如果您的需求无法实现或者您有更好的想法，非常欢迎您在issue中与我交流。我认为这是很重要的学习提升的过程

<br />

[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-16%2B-yellow.svg?style=flat)](https://android-arsenal.com/api?level=23)
[ ![Download](https://api.bintray.com/packages/zhuxu820/citypickerx/CityPickerX/images/download.svg?version=0.6.0) ](https://bintray.com/zhuxu820/citypickerx/CityPickerX/0.6.0/link)

<br />

>初衷是由于有“城市选择”需求，但手上没有很趁手的框架，就根据自己的需求半天时间写了一个，并且学习了git上其它优秀框架的经验(后面会列出)。
由于时间仓促，需求比较急，所以代码方面可能有些瑕疵，稍微空闲下来就会重新优化一下。
>
<br />

#### 主要需求目的
* 优化启动速度
* 搭建侧边导航栏与列表的沟通
* 添加自动首字母识别与自动排序(为提高效率目前需要手动调用)
* 快速设置头部布局
* 支持AndroidX与support

#### 未来打算
* 优化代码结构
* 优化启动方法
* 添加单独设置item icon功能
* 解决BUG

[点击下载demo(如无法下载请issue提交)](http://d.7short.com/CityPickerX)

<p align="center">
<img src="art/sample_screen.png">
</p>

#### 如何使用
```
<dependency>
	<groupId>com.cocoz.utilsz</groupId>
	<artifactId>citypickerx</artifactId>
	<version>0.6.0</version>
	<type>pom</type>
</dependency>
```
```
implementation 'com.cocoz.utilsz:citypickerx:0.6.0'   //必选
implementation 'com.android.support:recyclerview-v7:27.1.1'	//必选
```
```
<dependency org="com.cocoz.utilsz" name="citypickerx" rev="0.6.0">
	<artifact name="citypickerx" ext="pom"></artifact>
</dependency>
```

#### 代码详解
##### 如何启动
```java
CityPickerXFragment cityPickerXFragment = CityPickerXFragment.startShow(MainTestActivity.this, getCityPickerConfig());
cityPickerXFragment.setPickerXInterface(new CommonPickerXInterface() {
            @Override
            public void onClick(CityBean cityBean) {
                // 在此实现你的点击逻辑
                Toast.makeText(getApplicationContext(), "you clicked " + cityBean.getName() + " , this is a " + cityBean.getType(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDismiss() {
                // 在此实现dismiss触发逻辑
                Toast.makeText(getApplicationContext(), "dismiss", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSearch(String s) {
                // 在此实现你的搜索逻辑
                Toast.makeText(getApplicationContext(), "you search " + s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReset() {
                // 在此实现reset触发逻辑
                Toast.makeText(getApplicationContext(), "reset", Toast.LENGTH_SHORT).show();
            }
        });
```
##### 如何自定义头部模块
>共三个模块,每个模块都可以使用HeadModelConfig进行配置.具体使用可查阅HeadModelConfig类中的备注

>通过CityPickerConfig进行整个框架的配置
通过HeadModelConfig进行头部布局配置

```java
    // 您可前往MainTestActivity查看详细用法
    // 或查看CityPickerConfig类中的备注
    // 生成配置类
private CityPickerConfig getCityPickerConfig() {
    HeadModelConfig locationConfig = new HeadModelConfig("当前定位", listLocation);
    // setTag以用于更新数据
    // setTag是头部配置非常重要的属性，是您修改更新头部数据重要的依据
    // 建议您将tag设置为静态变量已方便调用，减少出错
    locationConfig.setTag("当前定位");
    HeadModelConfig recentConfig = new HeadModelConfig("最近访问", listRecent, true, "近", 0, 0);
    recentConfig.setTag("最近访问");
    HeadModelConfig hotConfig = new HeadModelConfig("热门城市", listHot, true, "热", 0, 0);
    hotConfig.setTag("热门城市");
    // 生成配置类 CityPickerConfig
    // 最后cityPickerConfig中的listdata参数设置为null  则表示使用自带的数据库列表 否则可在此实现自定义列表数据
    // 可查看下方备注
    CityPickerConfig cityPickerConfig = new CityPickerConfig(locationConfig, recentConfig, hotConfig, null);
    return cityPickerConfig;
}
```
```
    // 应用配置
    CityPickerXFragment.startShow(FragmentActivity activity, CityPickerConfig cityPickerConfig)
```

<br/>

##### 只使用列表
>如果您只需要列表，在startShow时生成默认配置文件(直接调用new CityPickerConfig())即可。
>
<br/>

#### 使用自定义列表
>默认会读取数据库文件中的城市列表<br/>
```
    // 如需自定义列表,可使用设置数据
    CityPickerConfig.setListData(List<CityBean> listData)
    // 或在CityPickerConfig中设置数据
```

#### 不适用自定义列表、使用自带数据库信息
```
    // 切换自定义/原始数据需要重新初始化CityPickerConfig<br/>
    // 在CityPickerConfig中设置listData为空
```

#### 数据初始化建议
>建议您在设置列表之前(启动APP或获取城市列表后)使用**CityDataInitUtils.initData**进行初始化(识别首字母与排序)

#### 如何更新数据
>使用以下方法:注意此处的tag与“自定义头部模块”部分的**setTag**为同一值
```
    // 如何更新头部数据
    cityPickerXFragment.updateData(String tag, List<CityBean> _listBeans);
    // 如何更新列表数据  
    // isALL : 是否需要添加头部显示  false为只有列表  true为带头部模块
    cityPickerXFragment.updateListData(List<CityBean> _listBeans, boolean isALL)
```

#### 更多自定义
>CityBean与HeadModelConfig均支持自定义未使用拓展字段"tag"，或许您可以用得到

**如果您的需求无法实现或者您有更好的想法，非常欢迎您在issue中与我交流。我认为这是很重要的学习提升的过程**

### 更新日志

##### 2020-7-28
* 重新上传jcenter,更新库地址

##### 2020-7-27
* 解决添加导入的时候报错gradle版本不匹配的bug
* 修复“头部模块”与侧导航栏联动不一致的问题
* 修复一个添加默认配置文件时数据异常的问题
* 更新dmeo
* 更新README.md

##### 2020-7-25
* 优化事件接口,统一修改为PickerXInterface
* 新增onReset事件
* 分割sample与lib包
* 新增列表刷新方法
* 修复列表数据更新BUG
* 修复列表与侧导航栏字母不匹配的BUG
* 新增头部与侧导航栏关联

<br/>
<br/>

#### 参考的资料
[zaaach的CityPicker城市选择框架](https://github.com/zaaach/CityPicker "zaaach / CityPicker")
<br/>
[侧边分组导航栏](https://github.com/yuanshuaiding/LetterBar/tree/feffad117c4631badde220de0736b38f132493c0 "侧边分组导航栏")
<br/>
lichenwei.me的自定义分割线和组头实现(未找到原文链接)