package cn.edu.heuet.login.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import com.xuexiang.xhttp2.XHttp;
//import com.xuexiang.xhttp2.callback.SimpleCallBack;
//import com.xuexiang.xhttp2.exception.ApiException;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import org.apache.commons.codec.*;
import cn.edu.heuet.login.R;
import cn.edu.heuet.login.constant.NetConstant;
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

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    // 声明SharedPreferences对象
    SharedPreferences sp;
    // 声明SharedPreferences编辑器对象
    SharedPreferences.Editor editor;
    //建立okhttp的client
    private  OkHttpClient okHttpClient;
    // Log打印的通用Tag
    private final String TAG = "LoginActivity";

    private ActivityLoginBinding loginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreenConfig();
//        setContentView(R.layout.activity_login);
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        // 为点击事件设置监听器
        setOnClickListener();

        /*
            当输入框焦点失去时,检验输入数据，提示错误信息
            第一个参数：输入框对象
            第二个参数：输入数据类型
            第三个参数：输入不合法时提示信息
         */
        setOnFocusChangeErrMsg(loginBinding.etAccount, "telephone", "手机号格式不正确");
        setOnFocusChangeErrMsg(loginBinding.etPassword, "optCode", "验证码必须为6位");
    }

    // 为点击事件的UI对象设置监听器
    private void setOnClickListener() {
        loginBinding.btLogin.setOnClickListener(this); // 登录按钮

        loginBinding.btGetOtp.setOnClickListener(this); // 获取验证码按钮
//        loginBinding.tvForgetPassword.setOnClickListener(this); // 忘记密码文字
        loginBinding.tvServiceAgreement.setOnClickListener(this); // 同意协议文字
//        loginBinding.ivThirdMethod1.setOnClickListener(this); // 第三方登录方式1
//        loginBinding.ivThirdMethod2.setOnClickListener(this); // 第三方登录方式2
//        loginBinding.ivThirdMethod3.setOnClickListener(this); // 第三方登录方式3
    }

    /*
    当账号输入框失去焦点时，校验账号是否是中国大陆手机号
    当密码输入框失去焦点时，校验密码是否不少于6位
    如有错误，提示错误信息
     */
    private void setOnFocusChangeErrMsg(EditText editText, String inputType, String errMsg) {
        editText.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        String inputStr = editText.getText().toString();
                        if (!hasFocus) {
                            switch (inputType) {
                                case "telephone":
                                    if (!ValidUtils.isPhoneValid(inputStr)) {
                                        editText.setError(errMsg);
                                    }
                                    break;
                                case "optCode":
                                    if (!ValidUtils.isPasswordValid(inputStr)) {
                                        editText.setError(errMsg);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
        );
    }


    // 因为 implements View.OnClickListener 加上已经 setOnClickListener()
    // 所以OnClick方法可以写到onCreate方法外
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        // 获取用户输入的账号和密码以进行验证
        String telephone = loginBinding.etAccount.getText().toString();
//        String password = loginBinding.etPassword.getText().toString();
        String optCode = loginBinding.etPassword.getText().toString();
        switch (v.getId()) {

            case R.id.bt_get_otp:
                // 点击获取验证码按钮响应事件
                if (TextUtils.isEmpty(telephone)) {
                    Toast.makeText(LoginActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (ValidUtils.isPhoneValid(telephone)) {
//                      asyncGetOtpCode(telephone);
                        asyncGetOtpCodeWithXHttp2(telephone);
                    } else {
                        Toast.makeText(LoginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    }
                }
                break;


            // 登录按钮 响应事件
            case R.id.bt_login:
                // 让密码输入框失去焦点,触发setOnFocusChangeErrMsg方法
                loginBinding.etPassword.clearFocus();
                // 发送URL请求之前,先进行校验
                if (!(ValidUtils.isPhoneValid(telephone) && ValidUtils.isPasswordValid(optCode))) {
                    Toast.makeText(this, "账号或验证码错误", Toast.LENGTH_SHORT).show();
                    break;
                }

                asyncLoginWithXHttp2(telephone,optCode);
                break;
            // 以下功能目前都没有实现
//            case R.id.tv_forget_password:
                // 跳转到修改密码界面
//                break;
            case R.id.tv_service_agreement:
                // 跳转到服务协议界面
                break;

        }
    }

    private void asyncGetOtpCodeWithXHttp2(String telephone)  {
//        FormBody formBody = new FormBody.Builder().add("phone_number", telephone).build();

        JsonObject obj = new JsonObject();
        obj.addProperty("phone_number",telephone);
        MediaType type = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(type, obj.toString());
        Request request = new Request.Builder().url("http://192.168.1.168:8888/verificationCode").post(requestBody).build();
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
                        showToastInThread(LoginActivity.this,"验证码已成功发送，请注意接收");
                    }else{
                        showToastInThread(LoginActivity.this,"验证码发送失败");
                    }
                }
            }
        });
    }

    private void asyncLoginWithXHttp2(String telephone, String optCode) {
        FormBody formBody = new FormBody.Builder().add("phone_number", telephone).add("verification_code",optCode).build();
        JsonObject obj = new JsonObject();
        obj.addProperty("phone_number",telephone);
        obj.addProperty("verification_code",optCode);
        MediaType type = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(type, obj.toString());
        Request request = new Request.Builder().url("http://192.168.1.168:8888/login").post(requestBody).build();
        okHttpClient = new OkHttpClient();
        //准备好请求的Call对象
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "请求URL失败： " + e.getMessage());
                showToastInThread(LoginActivity.this, "请求URL失败, 请重试！");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseStr = response.toString();
                if (responseStr.contains("200")||responseStr.contains("201")) {
//                    Log.i(TAG,"请求URL成功： "+response.body().string());
                        sp = getSharedPreferences(telephone, MODE_WORLD_WRITEABLE);
                        editor = sp.edit();
                        editor.putString("phone_number", telephone);
//                                editor.putString("password", password);
                        if (editor.commit()) {
                            Log.d("SP",sp.getString("phone_number","none"));
                            showToastInThread(LoginActivity.this, "登录成功");
                            if(responseStr.contains("200")){
                            Intent it_login_to_main = new Intent(LoginActivity.this, BaseInfoActivity.class);
                            startActivity(it_login_to_main);}
                            else if(responseStr.contains("201")){
                                Intent it_login_to_main = new Intent(LoginActivity.this, BaseInfoActivity.class);
                                startActivity(it_login_to_main);
                            }
                            // 登录成功后，登录界面就没必要占据资源了
                            finish();
                        } else {
                            showToastInThread(LoginActivity.this, "请重新登录");
                        }

                } else if(responseStr.contains("403")){
                     showToastInThread(LoginActivity.this, "手机或验证码错误");
                        Log.d(TAG, "账号或验证码错误");

                }else{
                    Log.d(TAG, "服务器异常");
                    showToastInThread(LoginActivity.this, responseStr);
                }
            }
        });
    }



}
