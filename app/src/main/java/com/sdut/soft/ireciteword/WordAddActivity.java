package com.sdut.soft.ireciteword;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sdut.soft.ireciteword.bean.Unit;
import com.sdut.soft.ireciteword.bean.Word;
import com.sdut.soft.ireciteword.dao.UnitDao;
import com.sdut.soft.ireciteword.dao.WordDao;
import com.sdut.soft.ireciteword.user.UserService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WordAddActivity extends AppCompatActivity {

    UnitDao unitDao;
    @BindView(R.id.key_et)
    EditText keyEt;
    @BindView(R.id.phono_et)
    EditText phonoEt;
    @BindView(R.id.trans_et)
    EditText transEt;
    @BindView(R.id.example_et)
    EditText exampleEt;

    @BindView(R.id.unit_name_at)
    AutoCompleteTextView unitAuto;

    @BindView(R.id.word_add_btn)
    Button wordAddBtn;

    WordDao wordDao;
    UserService userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_add);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        userService = new UserService(this);
        unitDao = new UnitDao(this);
        wordDao = new WordDao(this);
        unitAuto.setText(userService.getUserScb());
        List<Unit> units = unitDao.getUnits();
        List<String> uNameList = new ArrayList<>(units.size());
        for (Unit unit : units) {
            uNameList.add(unit.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,uNameList);
        unitAuto.setAdapter(adapter);
        wordAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = keyEt.getText().toString().trim();
                String phono = phonoEt.getText().toString().trim();
                String trans = transEt.getText().toString().trim();
                String example = exampleEt.getText().toString().trim();
                String unitName = unitAuto.getText().toString().trim();
                Unit unit = unitDao.getUnitByName(unitName);
                if(unit == null) {
                    unitDao.createUnit(unitName);
                    unit = unitDao.getUnitByName(unitName);
                }
                Word word = new Word(key, phono, trans, example);
                boolean b = wordDao.insertWordToUnit(word, unit);
                Toast.makeText(WordAddActivity.this, b?"添加成功":"添加失败", Toast.LENGTH_SHORT).show();
                clearInput();
            }
        });
    }

    private void clearInput() {
        keyEt.setText("");
        phonoEt.setText("");
        transEt.setText("");
        exampleEt.setText("");
    }
}
