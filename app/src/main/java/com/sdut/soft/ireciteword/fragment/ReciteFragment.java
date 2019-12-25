package com.sdut.soft.ireciteword.fragment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.sdut.soft.ireciteword.DetailActivity;
import com.sdut.soft.ireciteword.R;
import com.sdut.soft.ireciteword.WordAddActivity;
import com.sdut.soft.ireciteword.bean.Unit;
import com.sdut.soft.ireciteword.bean.User;
import com.sdut.soft.ireciteword.dao.UnitDao;
import com.sdut.soft.ireciteword.dao.WordDao;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReciteFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "ReciteFragment";

    UnitDao unitDao;

    @BindView(R.id.tv_tb_title)
    TextView tbTitle;
    @BindView(R.id.pie_chart)
    PieChart pieChart;
    @BindView(R.id.period_spinner)
    Spinner periodSpinner;
    @BindView(R.id.unit_spinner)
    Spinner unitSpinner;
    @BindView(R.id.btn_word_add)
    FloatingActionButton wordAddBtn;
    List<Unit> units;
    //选择的速度
    int speedIndex = 2;
    //选择的词典
    int unitIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recite, container, false);
        ButterKnife.bind(this, view);
        unitDao = new UnitDao(getContext());
        initView();
        return view;
    }


    @OnClick({R.id.btn_start})
    public void start(View v) {
        Intent intent = null;
        intent = new Intent(getActivity(), DetailActivity.class);
        String[] speeds = getResources().getStringArray(R.array.speed);

        intent.putExtra("period", Integer.parseInt(speeds[speedIndex]) * 1000L);
        intent.putExtra("unitName",units.get(unitIndex).getName());
        startActivityForResult(intent, 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        tbTitle.setText("Learning");
        periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                speedIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        units  = unitDao.getUnits();
        List<String> uNameList = new ArrayList<>(units.size());
        for (Unit unit : units) {
            uNameList.add(unit.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,uNameList);
        unitSpinner.setAdapter(adapter);
        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Unit unit = units.get(position);
                unitIndex = position;
                showPieChart(unit);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        showPieChart(units.get(0));
        wordAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),WordAddActivity.class);
                startActivity(intent);
            }
        });
    }



    private void notifyUnReviewWord(String showStr) {
        NotificationManager notificationManager = (NotificationManager) getContext().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = null;
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel("my_channel_01", "测试", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(getContext().getApplicationContext(), "my_channel_01")
                    .setContentTitle("提示")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(showStr)
                    .setAutoCancel(true)
                    .build();
        } else {
            notification = new Notification.Builder(getContext().getApplicationContext())
                    .setContentTitle("提示")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(showStr)
                    .setAutoCancel(true)
                    .build();
        }
        notificationManager.notify(111, notification);

    }

    private void showPieChart(Unit unit) {
        WordDao wordDao = new WordDao(getContext());
        int total = unit.getCnt();
        int progress = unit.getProgress();
        List<PieEntry> pieList = new ArrayList<>();
        float review = 100.0f * progress / total;
        float newWord = 100.0f * (total - progress) / total;

        Log.i(TAG, "getPieChartData: " + review +":n" + newWord);
        pieList.add(new PieEntry(review, "已学习"));
        pieList.add(new PieEntry(newWord, "未学习"));


        PieDataSet dataSet = new PieDataSet(pieList, "Label");

        // 设置颜色list，让不同的块显示不同颜色，下面是我觉得不错的颜色集合，比较亮
        String[] colorStr = {"#0176da","#00FF00"};
        for (String s : colorStr) {
            dataSet.addColor(Color.parseColor(s));
        }
        PieData pieData = new PieData(dataSet);

        // 设置描述，我设置了不显示，因为不好看，你也可以试试让它显示，真的不好看
        Description description = new Description();
        description.setEnabled(false);
        pieChart.setDescription(description);
        //设置半透明圆环的半径, 0为透明
        pieChart.setTransparentCircleRadius(0f);

        //设置初始旋转角度
        pieChart.setRotationAngle(-15);

        //数据连接线距图形片内部边界的距离，为百分数
        dataSet.setValueLinePart1OffsetPercentage(80f);

        //设置连接线的颜色
        dataSet.setValueLineColor(Color.LTGRAY);
        // 连接线在饼状图外面
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        // 设置饼块之间的间隔
        dataSet.setSliceSpace(1f);
        dataSet.setHighlightEnabled(true);
        // 不显示图例
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);

        // 和四周相隔一段距离,显示数据
        pieChart.setExtraOffsets(26, 5, 26, 5);

        // 设置pieChart图表是否可以手动旋转
        pieChart.setRotationEnabled(false);
        // 设置piecahrt图表点击Item高亮是否可用
        pieChart.setHighlightPerTapEnabled(true);
        // 设置pieChart图表展示动画效果，动画运行1.4秒结束
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        //设置pieChart是否只显示饼图上百分比不显示文字
        pieChart.setDrawEntryLabels(true);
        //是否绘制PieChart内部中心文本
        pieChart.setDrawCenterText(true);
        pieChart.setCenterText("已经背过了" + progress + "个单词");
        // 绘制内容value，设置字体颜色大小
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.DKGRAY);

        pieChart.setDrawEntryLabels(true);
        pieChart.setData(pieData);
        // 更新 piechart 视图
        pieChart.postInvalidate();
    }
}
