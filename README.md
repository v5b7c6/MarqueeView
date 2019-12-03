# MarqueeView
MarqueeView升级版，原版MarqueeView是每条数据只展示一行超出屏幕宽度后显示省略号，升级后可以根据手机屏幕宽度计算出每条数据展示所需要的行数自动换行显示完整。

效果图
---
<img src="/screenshots/gifhome_480x960_10s.gif"/>

示例Apk下载
---
[示例Apk下载](https://github.com/v5b7c6/MarqueeView/raw/master/sample-apk/app-debug.apk)

## 使用

### Gradle:
implementation 'com.github.v5b7c6:MarqueeView:1.0'

### 属性
MarqueeView属性

| Attribute 属性          | Description 描述 |
|:---				     |:---|
| marqueeAnimDuration         | 动画执行时间            |
| inAnimation         |  进入动画          |
| outAnimation         | 移除动画          |
| textSize         |    文字大小       |
| textColor         | 文字颜色            |
| gravity         |  文字位置          |
| singleLine | 文字是否单行显示 |

#### XML
```
<com.v5b7c6.android.marqueeview.MarqueeView
    android:id="@+id/marqueeView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:gravity="center_vertical"
    app:marqueeAnimDuration="500"
    app:inAnimResId="@anim/anim_bottom_in"
    app:outAnimResId="@anim/anim_top_out"
    app:singleLine="true"
    app:textSize="20sp" />
```

#### 设置数据
```
final List<String> strList = new ArrayList<>();
strList.add("小龙女原型夏梦离世 绝美旧照倾国倾城");
strList.add("王楠老公删光王宝强相关微博 这是出了什么事？");
strList.add("阿Sa自认桃花运不断：一向都有好多");
strList.add("中国民营企业500强发布，华为超联想夺第一");

//根据每条数据所显示的宽度自动换行
marqueeView.startWithFixedWidthList(strList);
//根据每条数据所显示的宽度超过屏幕宽按省略号显示
marqueeView.startWithList(strList);
```

#### 设置监听事件
```
marqueeView.setOnItemClickListener(new VerticalMarqueeTextView.OnItemClickListener() {
    @Override
    public void onItemClick(int position, TextView textView) {
        Toast.makeText(MainActivity.this, strList.get(position), Toast.LENGTH_SHORT).show();
    }
});
```
