package com.mushfiqussalehin.roomie.data;

import com.mushfiqussalehin.roomie.model.EmptyRoomResponse;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RoomieApi {
    @Headers("Content-Type: application/json")
    @GET("room/read.php")
    Single<List<EmptyRoomResponse>> getEmptyRooms();

    @POST("room/update.php")
    Single<ResponseBody> bookEmptyRoom(@Body HashMap<String, Object> body);
}
