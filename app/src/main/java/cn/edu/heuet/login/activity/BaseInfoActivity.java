package cn.edu.heuet.login.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import cn.edu.heuet.login.R;
import cn.edu.heuet.login.constant.NetConstant;
import cn.edu.heuet.login.databinding.ActivatityBaseInfoBinding;
import cn.edu.heuet.login.databinding.ActivityLoginBinding;
import cn.edu.heuet.login.util.ValidUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class BaseInfoActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

// 声明SharedPreferences对象
    SharedPreferences sp ;
    // 声明SharedPreferences编辑器对象
    SharedPreferences.Editor editor;
    //建立okhttp的client
    private  OkHttpClient okHttpClient;
    // Log打印的通用Tag
    private final String TAG = "BaseInfoActivity";
    private ActivatityBaseInfoBinding activatityBaseInfoBinding;
    String phoneNumber ;
    private Spinner ageSpinner;
    private Spinner yearSpinner;
    private Spinner monthSpinner;
    private Spinner genderSpinner;
    private String age ;
    private String year;
    private String month;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreenConfig();
        sp = getSharedPreferences("login_info", MODE_PRIVATE);
        phoneNumber = sp.getString("phone_number","");
//        setContentView(R.layout.activity_login);
        activatityBaseInfoBinding = DataBindingUtil.setContentView(this, R.layout.activatity_base_info);
        ArrayAdapter<CharSequence> fromResource = ArrayAdapter.createFromResource(this, R.array.list_age, R.layout.activatity_base_info);
        // 为点击事件设置监听器
        setOnClickListener();

        /*
            当输入框焦点失去时,检验输入数据，提示错误信息
            第一个参数：输入框对象
            第二个参数：输入数据类型
            第三个参数：输入不合法时提示信息
//         */
//        setOnFocusChangeErrMsg(loginBinding.etAccount, "telephone", "手机号格式不正确");
//        setOnFocusChangeErrMsg(loginBinding.etPassword, "optCode", "验证码必须为6位");
    }
    private void setOnClickListener() {
        activatityBaseInfoBinding.btSubmit.setOnClickListener(this);

        ageSpinner = (Spinner) findViewById(R.id.et_age);
        yearSpinner = (Spinner) findViewById(R.id.et_year);
        monthSpinner = (Spinner) findViewById(R.id.et_month);
        genderSpinner = (Spinner) findViewById(R.id.et_gender);
        ageSpinner.setOnItemSelectedListener(this);
        yearSpinner.setOnItemSelectedListener(this);
        monthSpinner.setOnItemSelectedListener(this);
        genderSpinner.setOnItemSelectedListener(this);
    }
    public void onClick(View v) {
        // 获取用户输入的账号和密码以进行验证
        String telephone = phoneNumber;
//        String age = activatityBaseInfoBinding.etAge.toString();
//        String password = loginBinding.etPassword.getText().toString();
//        String optCode = loginBinding.etPassword.getText().toString();
        switch (v.getId()) {

            case R.id.bt_submit:
                // 点击获取验证码按钮响应事件
                if (age=="") {
                    Toast.makeText(BaseInfoActivity.this, "请选择年龄", Toast.LENGTH_SHORT).show();
                } else if(year==""){
                    Toast.makeText(BaseInfoActivity.this, "请选择年份", Toast.LENGTH_SHORT).show();
                }else if(month==""){
                    Toast.makeText(BaseInfoActivity.this, "请选择月份", Toast.LENGTH_SHORT).show();
                }else if(gender==""){
                    Toast.makeText(BaseInfoActivity.this, "请选择性别", Toast.LENGTH_SHORT).show();
                }else{
                    asyncPostBaseInfoWithXHttp2(phoneNumber);
                }
                break;
        }
    }

    private void asyncPostBaseInfoWithXHttp2(String phoneNumber) {
        JsonObject obj = new JsonObject();
        obj.addProperty("phone_number",phoneNumber);
        obj.addProperty("age",age);
        obj.addProperty("gender",gender);
        obj.addProperty("birthday",year+"-"+month);

        MediaType type = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(type, obj.toString());
            Request request = new Request.Builder().url("http://192.168.1.168:8888/userInfo").post(requestBody).build();
        //准备好请求的Call对象
        okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i(TAG,"请求失败");
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){//当状态码code是200到299的时候，则代表请求成功
//                        Log.i(TAG,"请求URL成功： "+response.body().string());
                    String responseStr = response.toString();
                    if (responseStr.contains("200")) {
                        showToastInThread(BaseInfoActivity.this,"成功提交基本信息");
                        Intent it_login_to_main = new Intent(BaseInfoActivity.this, SASInfoActivity.class);
                        startActivity(it_login_to_main);
                    }else{
                        showToastInThread(BaseInfoActivity.this,"基本信息提交失败");
                    }
                }
            }
        });



    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String content  = parent.getItemAtPosition(position).toString();
        switch (parent.getId()){
            case R.id.et_age:
                Toast.makeText(BaseInfoActivity.this, "选择的年龄是"+content, Toast.LENGTH_SHORT).show();
                age = content;
                break;
            case R.id.et_year:
                Toast.makeText(BaseInfoActivity.this, "选择的年份是"+content, Toast.LENGTH_SHORT).show();
                year = content;
                break;
            case R.id.et_month:
                Toast.makeText(BaseInfoActivity.this, "选择的月份是"+content, Toast.LENGTH_SHORT).show();
                month = content;
                break;
            case R.id.et_gender:
                Toast.makeText(BaseInfoActivity.this, "选择的年龄是"+content, Toast.LENGTH_SHORT).show();
                if(position==0){
                    gender = "0";
                } else if (position==1) {
                    gender = "1";
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
