package com.mushfiqussalehin.roomie.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mushfiqussalehin.roomie.model.EmptyRoom;
import com.mushfiqussalehin.roomie.model.EmptyRoomResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private static final String TAG = "Repository";
    private static String BASE_URL = "http://192.168.31.146/api/";

    private static Repository sInstance;

    private RoomieApi api;

    private Repository() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        api = retrofit.create(RoomieApi.class);
    }

    public static synchronized Repository getInstance() {
        if (sInstance == null) {
            sInstance = new Repository();
        }
        return sInstance;
    }

    public Single<List<EmptyRoomResponse>> getEmptyRooms() {
        return api.getEmptyRooms();
    }

    public Single<List<EmptyRoom>> responseToEmptyRoomAsync(List<EmptyRoomResponse> responses) {
        return Single.fromCallable(() -> responseToEmptyRoom(responses));
    }

    private List<EmptyRoom> responseToEmptyRoom(List<EmptyRoomResponse> responses) {
        List<EmptyRoom> emptyRooms = new ArrayList<>();
        for (EmptyRoomResponse emptyRoomResponse: responses) {
            emptyRooms.add(new EmptyRoom(emptyRoomResponse.getId(), emptyRoomResponse.getRoomNo(), emptyRoomResponse.getDay(), emptyRoomResponse.getTime(), Long.valueOf(emptyRoomResponse.getTimeStamp()), emptyRoomResponse.getIsbooked(), emptyRoomResponse.getContactNo(), emptyRoomResponse.getCourseCode()));
        }
        Collections.sort(emptyRooms, (o1, o2) -> Long.compare(o1.getTimeStamp(), o2.getTimeStamp()));
        return emptyRooms;
    }

    public Single<List<Map<Long, List<EmptyRoom>>>> groupifyEmptyRoomsAsync(List<EmptyRoom> emptyRooms) {
        return Single.fromCallable(() -> groupifyEmptyRooms(emptyRooms));
    }

    private List<Map<Long, List<EmptyRoom>>> groupifyEmptyRooms(List<EmptyRoom> emptyRooms) {
        List<Map<Long, List<EmptyRoom>>> mapList = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        Date handlerDate = null;
        List<EmptyRoom> handlerList = new ArrayList<>();
        for (EmptyRoom emptyRoom: emptyRooms) {
            Date currentDate = new Date(emptyRoom.getTimeStamp());
            if (handlerDate == null) {
                handlerDate = currentDate;
            } else if (!format.format(handlerDate).equals(format.format(currentDate))) {
                Map<Long, List<EmptyRoom>> map = new HashMap<>();
                map.put(handlerDate.getTime(), handlerList);
                mapList.add(map);
                handlerDate = currentDate;
                handlerList.clear();
                Log.e(TAG, "groupifyEmptyRooms: "+handlerDate );
            }
            handlerList.add(emptyRoom);
        }
        return mapList;
    }

    public Single<ResponseBody> bookEmptyRoom(String id, String isBooked, String contactNo, String courseCode) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", Integer.valueOf(id));
        hashMap.put("isbooked", isBooked);
        hashMap.put("contact_no", contactNo);
        hashMap.put("courseCode", courseCode);
        return api.bookEmptyRoom(hashMap);
    }
}
