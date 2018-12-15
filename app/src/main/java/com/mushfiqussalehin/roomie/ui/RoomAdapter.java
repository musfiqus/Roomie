package com.mushfiqussalehin.roomie.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mushfiqussalehin.roomie.R;
import com.mushfiqussalehin.roomie.model.EmptyRoom;
import com.mushfiqussalehin.roomie.utils.InputHelper;

import java.util.List;

public class RoomAdapter extends BaseQuickAdapter<EmptyRoom, BaseViewHolder> {
    private Context mContext;
    private static final String BOOKING_REQUESTED = "requested";
    private static final String BOOKING_FINALIZED = "booked";
    public RoomAdapter(@Nullable List<EmptyRoom> data, Context context) {
        super(R.layout.item_room, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, EmptyRoom item) {
        helper.setText(R.id.item_room_no, item.getRoomNo());
        helper.setText(R.id.item_room_time, item.getTime());
        MaterialButton materialButton = helper.getView(R.id.item_room_book_now);
        if (InputHelper.isEmpty(item.getIsbooked())) {
            materialButton.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, BookingActivity.class);
                intent.putExtra(BookingActivity.BOOKING_DATA, item);
                MainActivity activity = (MainActivity) mContext;
                activity.startActivityForResult(intent, MainActivity.BOOK_REQUEST_CODE);
            });
        } else {
            if (item.getIsbooked().equals(BOOKING_REQUESTED)) {
                materialButton.setEnabled(false);
                materialButton.setText("Requested");
            } else if (item.getIsbooked().equals(BOOKING_FINALIZED)) {
                materialButton.setEnabled(false);
                materialButton.setText("Booked");
            }
        }

    }
}
