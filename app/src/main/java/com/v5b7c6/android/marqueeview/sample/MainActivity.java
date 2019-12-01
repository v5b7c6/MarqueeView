package com.v5b7c6.android.marqueeview.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.v5b7c6.android.marqueeview.MarqueeView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MarqueeView marqueeView1, marqueeView2, marqueeView3, marqueeView4, marqueeView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        marqueeView1 = findViewById(R.id.marqueeView1);
        final List<String> strList = new ArrayList<>();
        strList.add("小龙女原型夏梦离世 绝美旧照倾国倾城");
        strList.add("王楠老公删光王宝强相关微博 这是出了什么事？");
        strList.add("阿Sa自认桃花运不断：一向都有好多");
        strList.add("中国民营企业500强发布，华为超联想夺第一");
        marqueeView1.startWithFixedWidthList(strList);
        marqueeView1.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                Toast.makeText(MainActivity.this, strList.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        marqueeView2 = findViewById(R.id.marqueeView2);
        marqueeView2.startWithList(strList);

        marqueeView3 = findViewById(R.id.marqueeView3);
        final List<String> strList2 = new ArrayList<>();
        strList2.add("<font color='#ff0000'>小龙女</font>原型夏梦离世 绝美旧照倾国倾城");
        strList2.add("王楠老公删光王宝强<font color='#ebc554'>相关微博</font> 这是出了什么事？");
        strList2.add("<font color='#ffdc67'>阿Sa</font>自认桃花运不断：一向都有好多");
        strList2.add("中国民营企业<font color='#cccccc'>500</font>强发布，华为超联想夺第一");
        marqueeView3.startWithList(strList2);

        marqueeView4 = findViewById(R.id.marqueeView4);
        marqueeView4.startWithList(strList);

        marqueeView5 = findViewById(R.id.marqueeView5);
        marqueeView5.startWithList(strList);
    }

}
