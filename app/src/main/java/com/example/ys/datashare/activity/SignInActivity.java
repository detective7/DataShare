package com.example.ys.datashare.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ys.datashare.R;
import com.example.ys.datashare.config.Constant;
import com.example.ys.datashare.tool.JsonPost;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class SignInActivity extends Activity {

    private TextView zhuCeBack, faSongYanZheng;
    private EditText zhuCeXueHao, zhuCeMiMa, zhuCeMiMa2, zhuCeShouJi, yanZhengMa;
    private Button zhuCe;
    private static String urlZhuCe = Constant.MYURL + "signIn.php";
    private ProgressDialog pDialog;
    private String userNum, password, password_copy, phone;
    private JsonPost jsonParser = new JsonPost();
    private String APPKEY = "11bc365b9eff2";
    private String APPSECRETE = "658f26cd66e813667521d0a915d95b56";
    //倒计时是秒数
    private int i = 30;
    //判断输入是否通过，才可注册
    private boolean SmsPass = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        zhuCeBack = (TextView) this.findViewById(R.id.zhuCeBack);
        faSongYanZheng = (TextView) this.findViewById(R.id.faSongYanZheng);
        zhuCeXueHao = (EditText) this.findViewById(R.id.zhuCeXueHao);
        zhuCeMiMa = (EditText) this.findViewById(R.id.zhuCeMiMa);
        zhuCeMiMa2 = (EditText) this.findViewById(R.id.zhuCeMiMa2);
        zhuCeShouJi = (EditText) this.findViewById(R.id.zhuCeShouJi);
        yanZhengMa = (EditText) this.findViewById(R.id.yanZhengMa);

        SMSSDK.initSDK(this, APPKEY, APPSECRETE);
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);

        zhuCe = (Button) this.findViewById(R.id.zhuCe);

        zhuCeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        zhuCe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNum = zhuCeXueHao.getText().toString();
                password = zhuCeMiMa.getText().toString();
                phone = zhuCeShouJi.getText().toString();
                //TODO 这里只是测试时候这么设置，检测时候重新申请
//                if (dataTrue() && SmsPass) {
                if (dataTrue()) {
                    new Send().execute();
                }
            }
        });

        zhuCeXueHao.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                userNum = zhuCeXueHao.getText().toString();
                if (hasFocus) {

                } else if ("".equals(userNum) || userNum == null) {
                    Toast.makeText(SignInActivity.this, "请输入学号", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO 验证学号是否存在已经放在点击注册那里，有空再完成这里，异步有两次连接
                }
            }
        });

        zhuCeMiMa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                password = zhuCeMiMa.getText().toString();
                if (hasFocus) {

                } else if ("".equals(password) || password == null) {
                    Toast.makeText(SignInActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                }
            }
        });

        zhuCeMiMa2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                password_copy = zhuCeMiMa2.getText().toString();
                if (hasFocus) {

                } else if ("".equals(password_copy) || password_copy == null) {
                    Toast.makeText(SignInActivity.this, "请再次密码", Toast.LENGTH_SHORT).show();
                } else if (!password_copy.equals(password)) {
                    Toast.makeText(SignInActivity.this, "重复密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        faSongYanZheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataTrue()) {
                    phone = zhuCeShouJi.getText().toString();
                    // 1. 通过规则判断手机号
                    if (!judgePhoneNums(phone)) {
                        return;
                    } // 2. 通过sdk发送短信验证
                    SMSSDK.getVerificationCode("86", phone);
                    // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                    faSongYanZheng.setClickable(false);
                    faSongYanZheng.setText("重新发送(" + i + ")");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (; i > 0; i--) {
                                handler.sendEmptyMessage(-9);
                                if (i <= 0) {
                                    break;
                                }
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            handler.sendEmptyMessage(-8);
                        }
                    }).start();
                }
            }
        });
    }

    /**
     * @return 信息是否填写完整
     * 任何一个为空，都返回false;
     */
    private boolean dataTrue() {
        userNum = zhuCeXueHao.getText().toString().trim();
        password = zhuCeMiMa.getText().toString().trim();
        password_copy = zhuCeMiMa2.getText().toString().trim();
        phone = zhuCeShouJi.getText().toString().trim();
        if (userNum == null || userNum.equals("")) {
            Toast.makeText(SignInActivity.this, "请填写学号", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password == null || password.equals("")) {
            Toast.makeText(SignInActivity.this, "请填写密码", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password_copy == null || password_copy.equals("")) {
            Toast.makeText(SignInActivity.this, "请填写重复密码", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password_copy.equals(password)) {
            Toast.makeText(SignInActivity.this, "重复密码错误", Toast.LENGTH_SHORT).show();
            return false;
        } else if (phone == null || phone.equals("")) {
            Toast.makeText(SignInActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private class Send extends AsyncTask<String, String, String> {

        private int success = 5;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignInActivity.this);
            pDialog.setMessage("正在注册");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("user_num", userNum));
            args.add(new BasicNameValuePair("password", password));
            args.add(new BasicNameValuePair("phone", phone));
            try {
                JSONObject json = jsonParser.makeHttpRequest(urlZhuCe, "POST", args);
                success = json.getInt("success");
                String message = json.getString("message");
                return message;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            //doInBackground返回值-->s
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            if (success == 0) {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                SignInActivity.this.startActivity(intent);
                SignInActivity.this.finish();
            }
        }
    }

    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11)
                && isMobileNO(phoneNums)) {
            return true;
        }
        Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    /**
     * 消息处理
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                faSongYanZheng.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                faSongYanZheng.setText("获取验证码");
                faSongYanZheng.setClickable(true);
                i = 30;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event + "---result:" + result);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //短信注册成功后，返回MainActivity,然后提示新好友
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功,验证通过
                        SmsPass = true;
                        Toast.makeText(getApplicationContext(), "验证码校验成功", Toast.LENGTH_SHORT).show();
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//服务器验证码发送成功
                        Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignInActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}
