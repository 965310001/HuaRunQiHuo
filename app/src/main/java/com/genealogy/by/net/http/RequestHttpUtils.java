package com.genealogy.by.net.http;

import java.io.File;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RequestHttpUtils {
    private static final MediaType FROM_DATA = MediaType.parse("mulitiper/from-data");
//看着都不想动破电脑
    //我说你的程序
    public static String start(String url, File file, String typeName, String fileName) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody body = new MultipartBody.Builder()
                .setType(FROM_DATA)
                .addFormDataPart(typeName, fileName, fileBody)
                .build();
        Request request = new Request.Builder().post(body).url(url).build();
        return client.newCall(request).execute().body().string();
    }
}
