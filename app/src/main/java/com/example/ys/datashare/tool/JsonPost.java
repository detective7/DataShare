package com.example.ys.datashare.tool;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Ys on 2016/4/8.
 * Json和Http网络请求 工具类
 */
public class JsonPost {

    static InputStream is = null;
    static JSONObject jobj = null;
    static String json = "";

    public JsonPost() {

    }

    /**
     * @param url
     * @param method
     * @param params
     * @return
     */
    public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            //设置超时，3秒连接不到即为超时
            HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000);
            HttpConnectionParams.setSoTimeout(httpClient.getParams(), 3000);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            Log.d("json", json.toString());
        } catch (Exception e) {
            Log.e("json Buffer Error", "Error converting result " + e.toString());
//            Log.d("json", json.toString());
        }

        try {
            jobj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("json JSON Parser", "Error parsing data " + e.toString());
        }

        // 返回Json对象
        return jobj;


    }


}
