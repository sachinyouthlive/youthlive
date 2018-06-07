package com.yl.youthlive;

import com.yl.youthlive.getLivePOJO.getLiveBean;

import java.util.List;
import java.util.Map;

import bolts.Task;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

interface ApiConfig {
    @Multipart
    @POST("add_video.php")
    Call<ServerResponse> uploadFile(@Part MultipartBody.Part file,
                                    @Part("file") RequestBody name);




    @Multipart
    @POST("add_video.php")
    Call<ServerResponse> test(@Part MultipartBody.Part file,
                                    @Part("file") RequestBody name);





    @Multipart
    @POST("retrofit_example/upload_multiple_files.php")
    Call<ServerResponse> uploadMulFile(@Part MultipartBody.Part file1, @Part MultipartBody.Part file2);



}
