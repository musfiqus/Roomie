package com.mushfiqussalehin.roomie.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mushfiqussalehin.roomie.R;
import com.mushfiqussalehin.roomie.model.EmptyRoom;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DateAdapter extends BaseQuickAdapter<Map<Long, List<EmptyRoom>>, BaseViewHolder> {
    private Context mContext;
    public DateAdapter(List<Map<Long, List<EmptyRoom>>> data, Context context) {
        super(R.layout.item_date, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Map<Long, List<EmptyRoom>> item) {
        Long itemKey = null;
        for (Long key: item.keySet()) {
            itemKey = key;
        }
        if (helper.getAdapterPosition() == 0) {
            helper.setVisible(R.id.item_date_top_separator, false);
        }
        List<EmptyRoom> emptyRooms = item.get(itemKey);
        Date currentDate = new Date(itemKey);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        helper.setText(R.id.item_date_date, String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        helper.setText(R.id.item_date_day, getDayByCalendar(calendar).substring(0, 3).toUpperCase());
        RecyclerView roomList = helper.getView(R.id.item_date_room_list);
        roomList.setLayoutManager(new LinearLayoutManager(mContext));
        RoomAdapter adapter = new RoomAdapter(emptyRooms, mContext);
        adapter.bindToRecyclerView(roomList);

    }

    static String getDayByCalendar(Calendar calendar) {
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SATURDAY:
                return "Saturday";
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            default:
                System.out.println("DAFUQ BRU?");
                return "Saturday";
        }
    }
}
