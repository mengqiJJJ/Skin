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

import java.io.IOException;

import cn.edu.heuet.login.R;
import cn.edu.heuet.login.databinding.MedicalRecordInfoBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MedicalRecordInfoActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    SharedPreferences sp ;
    // 声明SharedPreferences编辑器对象
    SharedPreferences.Editor editor;
    //建立okhttp的client
    private OkHttpClient okHttpClient;

    // Log打印的通用Tag
    private final String TAG = "MedicalRecordInfoActivity";
    private MedicalRecordInfoBinding medicalRecordInfoBinding;
    private String MRInfoResult ;
    private String telephone  ;
    private int    culture ;
    private int    profession     ;
    private int    smoke ;
    private int    wine ;
    private int   height  ;
    private int     weight;
    private int    F ;
    private String    date_one ;
    private int     ill_year;
    private int   now_white_place  ;
    private int     nian_mo;
    private int     hair_white;
    private int    distribution ;
    private int    white_ill ;
    private int    season_ill ;
    private int    physics ;
    private int   chemistry  ;
    private int    infect ;
    private int    spirit ;
    private int     drugs;
    private int   pregnancy  ;
    private int    food ;
    private int    concomitant ;
    private int    family ;
    private int   myself_family  ;
    private int    covid ;
    private int    covid_infect ;
    private int    before ;
    private String year;
    private String ill_year;
    private String month;
    private String ill_month;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreenConfig();
        sp = getSharedPreferences("login_info", MODE_PRIVATE);
        telephone = sp.getString("phone_number","");
        medicalRecordInfoBinding = DataBindingUtil.setContentView(this, R.layout.medical_record_info);
        // 为点击事件设置监听器
        setOnClickListener();
    }
    private void setOnClickListener() {
        medicalRecordInfoBinding.btSubmitMedicalInfo.setOnClickListener(this);
        medicalRecordInfoBinding.etBefore.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etChemistry.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etConcomitant.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etCovid.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etCulture.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etCovidInfect.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etDistribution.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etDrugs.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etF.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etFamily.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etFood.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etHairWhite.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etInfect.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etMonth.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etMonth1.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etNiianMo.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etNowWhitePlace.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etWine.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etPhysics.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etSmoke.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etSeasonIll.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etConcomitant.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etSpirit.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etYear1.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etYear.setOnItemSelectedListener(this);
        medicalRecordInfoBinding.etProfession.setOnItemSelectedListener(this);
    }
    public void onClick(View v) {
        // 获取用户输入的账号和密码以进行验证
        switch (v.getId()) {
            case R.id.bt_submit_medical_info:
                // 点击获取验证码按钮响应事件
                asyncPostMRInfoWithXHttp2(telephone);
                break;
        }
    }

    private void asyncPostMRInfoWithXHttp2(String telephone) {
//        FormBody formBody = new FormBody.Builder().add("phone_number", telephone).build();
        JsonObject obj = new JsonObject();
        obj.addProperty("phone_number",telephone);
        obj.addProperty("culture",culture);
        obj.addProperty("profession",profession);
        obj.addProperty("smoke",smoke);
        obj.addProperty("wine",wine);
        obj.addProperty("height",height);
        obj.addProperty("weight",weight);
        obj.addProperty("F",F);
        obj.addProperty("date_one",date_one);
        obj.addProperty("ill_year",ill_year);
        obj.addProperty("ill_month",ill_month);
        obj.addProperty("now_white_place",now_white_place);
        obj.addProperty("nian_mo",nian_mo);
        obj.addProperty("hair_white",hair_white);
        obj.addProperty("distribution",distribution);
        obj.addProperty("white_ill",white_ill);
        obj.addProperty("season_ill",season_ill);
        obj.addProperty("physics",physics);
        obj.addProperty("chemistry",chemistry);
        obj.addProperty("infect",infect);
        obj.addProperty("spirit",spirit);
        obj.addProperty("drugs",drugs);
        obj.addProperty("pregnancy",pregnancy);
        obj.addProperty("food",food);
        obj.addProperty("concomitant",concomitant);
        obj.addProperty("family",family);
        obj.addProperty("myself_family",myself_family);
        obj.addProperty("covid",covid);
        obj.addProperty("covid_infect",covid_infect);
        obj.addProperty("before",before);
        MediaType type = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(type, obj.toString());
        Request request = new Request.Builder().header("Transfer-Encoding","chunked").url("http://192.168.1.168:8888/verificationCode").post(requestBody).build();
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
                        showToastInThread(MedicalRecordInfoActivity.this,"验证码已成功发送，请注意接收");
                    }else{
                        showToastInThread(MedicalRecordInfoActivity.this,"验证码发送失败");
                    }
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
        String content  = parent.getItemAtPosition(position).toString();
        position = position+1;
        switch (parent.getId()){
            case R.id.et_culture:
                culture = position;
                break;
            case R.id.et_profession:
                profession= position;
                break;
            case R.id.et_smoke:
                smoke= position;
                break;
            case R.id.et_wine:
                wine = position;
                break;
            case R.id.et_F:
                F = position;
                break;
            case R.id.et_year:
                year = content;
                break;
            case R.id.et_month:
                month = content;
                date_one = year+"-"+month;
                break;
            case R.id.et_year1:
                ill_year = content;
                break;
            case R.id.et_month1:
                ill_month = content;
                break;
            case R.id.et_now_white_place:
                now_white_place = position;
                break;
            case R.id.et_niian_mo:
                nian_mo = position;
                break;
            case R.id.et_hair_white:
                hair_white= position;
                break;
            case R.id.et_distribution:
                distribution = position;
                break;
            case R.id.et_white_ill:
                white_ill = position;
                break;
            case R.id.et_season_ill:
                season_ill= position;
                break;
            case R.id.et_physics:
                physics = position;
                break;
            case R.id.et_chemistry:
                chemistry =position;
                break;
            case R.id.et_infect:
                infect += position;
                break;
            case R.id.et_spirit:
                spirit =position;
                break;
            case R.id.et_pregnancy:
                pregnancy = position;
                break;
            case R.id.et_food:
                food = position;
                break;
            case R.id.et_concomitant:
                concomitant = position;
                break;
            case R.id.et_family:
                family = position;
                break;
            case R.id.et_myself_family:
                myself_family = position;
                break;
            case R.id.et_covid:
                covid = position;
                break;
            case R.id.et_covid_infect:
                covid_infect = position;
                break;
            case R.id.et_before:
                before = position;
                break;
            default:
                break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
