package com.mushfiqussalehin.roomie.ui;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.mushfiqussalehin.roomie.R;
import com.mushfiqussalehin.roomie.data.Repository;
import com.mushfiqussalehin.roomie.model.EmptyRoom;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public static final int BOOK_REQUEST_CODE = 42069;

    @BindView(R.id.main_date_list)
    RecyclerView mList;
    @BindView(R.id.main_appbar_layout)
    AppBarLayout mAppbarLayout;
    @BindView(R.id.main_curtain)
    FrameLayout mCurtain;
    private Disposable mDisposable;
    @BindView(R.id.main_content)
    CoordinatorLayout mContent;
    private Repository mRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mList.setLayoutManager(layoutManager);
        loadData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.md_white_1000));
        }
    }

    private void loadData() {
        if (mRepository == null) {
            mRepository = Repository.getInstance();
        }
        mRepository
                .getEmptyRooms()
                .flatMap(responses -> mRepository.responseToEmptyRoomAsync(responses))
                .flatMap(emptyRooms -> mRepository.groupifyEmptyRoomsAsync(emptyRooms))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<Map<Long, List<EmptyRoom>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "onSubscribe: subbed");
                        mDisposable = d;
                        mCurtain.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onSuccess(List<Map<Long, List<EmptyRoom>>> maps) {
                        DateAdapter dateAdapter = new DateAdapter(maps, MainActivity.this);
                        dateAdapter.bindToRecyclerView(mList);
                        mCurtain.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(mContent, "Unable to load free rooms", Snackbar.LENGTH_SHORT).show();
                        mCurtain.setVisibility(View.INVISIBLE);
                        Log.e(TAG, "onError: ", e);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == BOOK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                loadData();
                Snackbar.make(mContent, "Booking requested", Snackbar.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
