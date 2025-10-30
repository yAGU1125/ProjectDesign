package com.kit.projectdesign.net;

import com.kit.projectdesign.data.ApiResp;
import com.kit.projectdesign.data.EntryReq;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EntriesApi {
    @POST("entries")
    Call<ApiResp> submit(@Body EntryReq body);
}
