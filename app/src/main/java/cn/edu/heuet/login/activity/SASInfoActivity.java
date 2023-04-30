package cn.edu.heuet.login.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.google.gson.JsonObject;

import java.io.IOException;

import cn.edu.heuet.login.R;
import cn.edu.heuet.login.databinding.SasInfoBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SASInfoActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    SharedPreferences sp ;
    // 声明SharedPreferences编辑器对象
    SharedPreferences.Editor editor;
    //建立okhttp的client
    private OkHttpClient okHttpClient;
    // Log打印的通用Tag
    private final String TAG = "SASInfoActivity";
    private SasInfoBinding sasInfoBinding;
    private String SASResult ;
    private int score  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreenConfig();
        sp = getSharedPreferences("login_info", MODE_PRIVATE);
        sasInfoBinding = DataBindingUtil.setContentView(this, R.layout.sas_info);
        ArrayAdapter<CharSequence> fromResource = ArrayAdapter.createFromResource(this, R.array.list_age, R.layout.activatity_base_info);
        // 为点击事件设置监听器
        setOnClickListener();
    }
    private void setOnClickListener() {
        sasInfoBinding.btSubmitSAS.setOnClickListener(this);
        sasInfoBinding.etSAS1.setOnItemSelectedListener(this);
        sasInfoBinding.etSAS2.setOnItemSelectedListener(this);
        sasInfoBinding.etSAS3.setOnItemSelectedListener(this);
        sasInfoBinding.etSAS4.setOnItemSelectedListener(this);
        sasInfoBinding.etSAS5.setOnItemSelectedListener(this);
        sasInfoBinding.etSAS6.setOnItemSelectedListener(this);
        sasInfoBinding.etSAS7.setOnItemSelectedListener(this);
        sasInfoBinding.etSAS8.setOnItemSelectedListener(this);
        sasInfoBinding.etSAS9.setOnItemSelectedListener(this);
        sasInfoBinding.etSAS10.setOnItemSelectedListener(this);
        sasInfoBinding.etSAS11.setOnItemSelectedListener(this);
        sasInfoBinding.etSAS12.setOnItemSelectedListener(this);
        sasInfoBinding.etSAS13.setOnItemSelectedListener(this);
        sasInfoBinding.etSAS14.setOnItemSelectedListener(this);
        sasInfoBinding.etSAS15.setOnItemSelectedListener(this);
        sasInfoBinding.etSAS16.setOnItemSelectedListener(this);
        sasInfoBinding.etSAS17.setOnItemSelectedListener(this);
        sasInfoBinding.etSAS18.setOnItemSelectedListener(this);
        sasInfoBinding.etSAS19.setOnItemSelectedListener(this);
        sasInfoBinding.etSAS20.setOnItemSelectedListener(this);
    }
    public void onClick(View v) {
        // 获取用户输入的账号和密码以进行验证
        switch (v.getId()) {
            case R.id.bt_submit_SAS:
                // 点击获取验证码按钮响应事件
                saveSASResult();
                break;
        }
    }

    private void saveSASResult() {
        score = (int) (score*1.25);
        if(score>=50&&score<=59){
            SASResult = "1";
        }else if(score>=60&&score<=69){
            SASResult = "2";
        }else if(score>69){
            SASResult = "3";
        }else{
            SASResult = "0";
        }
        sp = getSharedPreferences("login_info", MODE_WORLD_WRITEABLE);
        editor = sp.edit();
        editor.putString("sas",SASResult);
        if (editor.commit()) {
            Log.d("SP",sp.getString("sas","none"));
            showToastInThread(SASInfoActivity.this, "SAS量表保存成功");
            showToastInThread(SASInfoActivity.this, "最终sas分数为"+score);
            Intent it_login_to_main = new Intent(SASInfoActivity.this, SDSInfoActivity.class);
            startActivity(it_login_to_main);
            // 登录成功后，登录界面就没必要占据资源了
            finish();
        } else {
            showToastInThread(SASInfoActivity.this, "请重新提交保存");
        }
    }

    public int calNegativeGrade(int num){
        switch (num){
            case 4:
                return 1;
            case 3:
                return 2;
            case 2:
                return 3;
            case 1:
                return 4;
        }
        return  0;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        String content  = parent.getItemAtPosition(position).toString();
        position = position+1;
        switch (parent.getId()){
            case R.id.et_SAS_1:
               score += position;
                break;
            case R.id.et_SAS_2:
                score += position;
                break;
            case R.id.et_SAS_3:
                score += position;
                break;
            case R.id.et_SAS_4:
                score += position;
                break;
            case R.id.et_SAS_5:
                score += calNegativeGrade(position);
                break;
            case R.id.et_SAS_6:
                score += position;
                break;
            case R.id.et_SAS_7:
                score += position;
                break;
            case R.id.et_SAS_8:
                score += position;
                break;
            case R.id.et_SAS_9:
                score += calNegativeGrade(position);
                break;
            case R.id.et_SAS_10:
                score += position;
                break;
            case R.id.et_SAS_11:
                score += position;
                break;
            case R.id.et_SAS_12:
                score += position;
                break;
            case R.id.et_SAS_13:
                score += calNegativeGrade(position);
                break;
            case R.id.et_SAS_14:
                score += position;
                break;
            case R.id.et_SAS_15:
                score += position;
                break;
            case R.id.et_SAS_16:
                score += position;
                break;
            case R.id.et_SAS_17:
                score += calNegativeGrade(position);
                break;
            case R.id.et_SAS_18:
                score += position;
                break;
            case R.id.et_SAS_19:
                score += calNegativeGrade(position);
                break;
            case R.id.et_SAS_20:
                score += position;
                break;
            default:
                break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



}
