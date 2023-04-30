package cn.edu.heuet.login.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.google.gson.JsonObject;
import com.xuexiang.xhttp2.request.PostRequest;

import java.io.IOException;

import cn.edu.heuet.login.R;
import cn.edu.heuet.login.databinding.SdsInfoBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SDSInfoActivity  extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    SharedPreferences sp ;
    // 声明SharedPreferences编辑器对象
    SharedPreferences.Editor editor;
    //建立okhttp的client
    private OkHttpClient okHttpClient;
    // Log打印的通用Tag
    private final String TAG = "SDSInfoActivity";
    private  SdsInfoBinding sdsInfoBinding;
    private String SDSResult;
    private String SASResult;

    private int SDSScore;
    private String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreenConfig();
        sp = getSharedPreferences("login_info", MODE_PRIVATE);
        phoneNumber = sp.getString("phone_number","");
        SASResult = sp.getString("sas","");
        sdsInfoBinding= DataBindingUtil.setContentView(this, R.layout.sds_info);
        // 为点击事件设置监听器
        setOnClickListener();
    }
    private void setOnClickListener() {
        sdsInfoBinding.btSubmitSDS.setOnClickListener(this);
//        activatityBaseInfoBinding.etAge.setOnItemSelectedListener(this);
        sdsInfoBinding.etSDS1.setOnItemSelectedListener(this);
        sdsInfoBinding.etSDS2.setOnItemSelectedListener(this);
        sdsInfoBinding.etSDS3.setOnItemSelectedListener(this);
        sdsInfoBinding.etSDS4.setOnItemSelectedListener(this);
        sdsInfoBinding.etSDS5.setOnItemSelectedListener(this);
        sdsInfoBinding.etSDS6.setOnItemSelectedListener(this);
        sdsInfoBinding.etSDS7.setOnItemSelectedListener(this);
        sdsInfoBinding.etSDS8.setOnItemSelectedListener(this);
        sdsInfoBinding.etSDS9.setOnItemSelectedListener(this);
        sdsInfoBinding.etSDS10.setOnItemSelectedListener(this);
        sdsInfoBinding.etSDS11.setOnItemSelectedListener(this);
        sdsInfoBinding.etSDS12.setOnItemSelectedListener(this);
        sdsInfoBinding.etSDS13.setOnItemSelectedListener(this);
        sdsInfoBinding.etSDS14.setOnItemSelectedListener(this);
        sdsInfoBinding.etSDS15.setOnItemSelectedListener(this);
        sdsInfoBinding.etSDS16.setOnItemSelectedListener(this);
        sdsInfoBinding.etSDS17.setOnItemSelectedListener(this);
        sdsInfoBinding.etSDS18.setOnItemSelectedListener(this);
        sdsInfoBinding.etSDS19.setOnItemSelectedListener(this);
        sdsInfoBinding.etSDS20.setOnItemSelectedListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit_SDS:
                // 点击获取验证码按钮响应事件
                postAndSaveSDSResult();
                break;
        }

    }

    private void postAndSaveSDSResult() {
        SDSScore = (int) (SDSScore*1.25);
        if(SDSScore>=53&&SDSScore<=62){
            SDSResult = "1";
        }else if(SDSScore>=63&&SDSScore<=72){
            SDSResult = "2";
        }else if(SDSScore>72){
            SDSResult = "3";
        }else{
            SDSResult = "0";
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("phone_number",phoneNumber);
        obj.addProperty("sas",SASResult);
        obj.addProperty("sds",SDSResult);
        MediaType type = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(type, obj.toString());
        Request request = new Request.Builder().url("http://192.168.1.168:8888/scaleInfo").post(requestBody).build();
        okHttpClient = new OkHttpClient();
        //准备好请求的Call对象
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "请求URL失败： " + e.getMessage());
                showToastInThread(SDSInfoActivity.this, "请求URL失败, 请重试！");
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseStr = response.toString();
                if (responseStr.contains("200")) {
//                    Log.i(TAG,"请求URL成功： "+response.body().string());
                    editor = sp.edit();
                    editor.putString("sds", SDSResult);
                    if (editor.commit()) {
                        Log.d("SP",sp.getString("phone_number","none"));
                        showToastInThread(SDSInfoActivity.this, "SDS量表提交成功");
                        showToastInThread(SDSInfoActivity.this, "最终sds分数为"+SDSScore);
                        Intent it_login_to_main = new Intent(SDSInfoActivity.this, MainActivity.class);
                        startActivity(it_login_to_main);
                        // 登录成功后，登录界面就没必要占据资源了
                        finish();
                    } else {
                        showToastInThread(SDSInfoActivity.this, "请重新提交");
                    }
    }
                else {
                    showToastInThread(SDSInfoActivity.this, "服务器出现异常，请重新提交");
                }
            }
        });
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
        position = position+1;
        switch (parent.getId()){
            case R.id.et_SDS_1:
                SDSScore += position;
                showToastInThread(SDSInfoActivity.this, "当前加分为"+position);
                break;
            case R.id.et_SDS_2:
                SDSScore += calNegativeGrade(position);
                break;
            case R.id.et_SDS_3:
                SDSScore += position;
                break;
            case R.id.et_SDS_4:
                SDSScore += position;
                break;
            case R.id.et_SDS_5:
                SDSScore += calNegativeGrade(position);
                break;
            case R.id.et_SDS_6:
                SDSScore +=calNegativeGrade(position);
                break;
            case R.id.et_SDS_7:
                SDSScore += position;
                break;
            case R.id.et_SDS_8:
                SDSScore += position;
                break;
            case R.id.et_SDS_9:
                SDSScore += position;
                break;
            case R.id.et_SDS_10:
                SDSScore += position;
                break;
            case R.id.et_SDS_11:
                SDSScore += calNegativeGrade(position);
                break;
            case R.id.et_SDS_12:
                SDSScore +=calNegativeGrade(position);
                break;
            case R.id.et_SDS_13:
                SDSScore += position;
                break;
            case R.id.et_SDS_14:
                SDSScore +=calNegativeGrade(position);
                break;
            case R.id.et_SDS_15:
                SDSScore += position;
                break;
            case R.id.et_SDS_16:
                SDSScore +=calNegativeGrade(position);
                break;
            case R.id.et_SDS_17:
                SDSScore += calNegativeGrade(position);
                break;
            case R.id.et_SDS_18:
                SDSScore +=calNegativeGrade(position);;
                break;
            case R.id.et_SDS_19:
                SDSScore += position;
                break;
            case R.id.et_SDS_20:
                SDSScore +=calNegativeGrade(position);;
                break;
            default:
                break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
