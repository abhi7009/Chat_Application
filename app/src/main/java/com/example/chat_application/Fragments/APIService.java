package com.example.chat_application.Fragments;

import com.example.chat_application.Notifications.MyResponse;
import com.example.chat_application.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                "Content-Type:application/json",
                    "Authorization:key=AAAAGBZ-Mxk:APA91bH16uXvtMvkc4-Gn9HP3240g7kn14Q6P7FDFZMyDvsVZv4xoCSpY2oAslBLxTduG6yVsPHJRIgnEAxnVsOlsHXhE8sx9ZGMzwq6kypGLD3xSbQvmEe821uKqsZ0c6Uc317LNfbD"

    }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifications(@Body Sender body);


}



