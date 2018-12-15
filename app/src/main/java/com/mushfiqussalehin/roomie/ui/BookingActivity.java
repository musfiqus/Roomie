package com.mushfiqussalehin.roomie.ui;

import android.support.design.button.MaterialButton;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.mushfiqussalehin.roomie.R;
import com.mushfiqussalehin.roomie.data.Repository;
import com.mushfiqussalehin.roomie.model.EmptyRoom;
import com.mushfiqussalehin.roomie.utils.InputHelper;

import java.io.IOException;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import okhttp3.ResponseBody;

public class BookingActivity extends AppCompatActivity {
    private static final String TAG = "BookingActivity";

    public static final String BOOKING_DATA = "booking_data";
    private static final String BOOK_REQUESTED = "requested";

    private static final String RESPONSE_MESSAGE = "message";
    private static final String RESPONSE_SUCCESSFUL = "successful";
    private EmptyRoom mRoom;

    @BindView(R.id.booking_code_input)
    TextInputEditText mCodeInput;
    @BindView(R.id.booking_code_layout)
    TextInputLayout mCodeLayout;
    @BindView(R.id.booking_contact_input)
    TextInputEditText mContactInput;
    @BindView(R.id.booking_contact_layout)
    TextInputLayout mContactLayout;
    @BindView(R.id.booking_room)
    TextView mRoomNo;
    @BindView(R.id.booking_time)
    TextView mTime;
    @BindView(R.id.booking_button)
    MaterialButton mBookNow;
    @BindView(R.id.booking_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.booking_progress)
    MaterialProgressBar mProgress;
    @BindView(R.id.booking_content)
    CoordinatorLayout mContent;
    private Disposable mDisposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        if (savedInstanceState != null && savedInstanceState.getParcelable(BOOKING_DATA) != null) {
            mRoom = savedInstanceState.getParcelable(BOOKING_DATA);
            setupUi(mRoom);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null && bundle.getParcelable(BOOKING_DATA) != null) {
                mRoom = bundle.getParcelable(BOOKING_DATA);
                if (mRoom != null) {
                    setupUi(mRoom);
                }
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BOOKING_DATA, mRoom);
        super.onSaveInstanceState(outState);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupUi(EmptyRoom emptyRoom) {
        mBookNow.setOnClickListener(v -> {
            if (isValid()) {
                Repository repository = Repository.getInstance();
                repository
                        .bookEmptyRoom(mRoom.getId(), BOOK_REQUESTED, mContactInput.getText().toString(), mCodeInput.getText().toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<ResponseBody>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.e(TAG, "onSubscribe: ID "+mRoom.getId() );
                                mDisposable = d;
                                mProgress.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onSuccess(ResponseBody response) {
                                mProgress.setVisibility(View.INVISIBLE);
                                Gson gson = new Gson();
                                try {
                                    Map<String, String> responseMap = gson.fromJson(response.string(), new TypeToken<Map<String, String>>(){}.getType());
                                    if (!InputHelper.isEmpty(responseMap.get(RESPONSE_MESSAGE)) && responseMap.get(RESPONSE_MESSAGE).equals(RESPONSE_SUCCESSFUL)) {
                                        setResult(RESULT_OK);
                                        finish();
                                    } else {
                                        Snackbar.make(mContent, "Unable to book, try again!", Snackbar.LENGTH_SHORT).show();
                                    }
                                } catch (IOException e) {
                                    Snackbar.make(BookingActivity.this.getWindow().getDecorView(), "Unable to book, try again!", Snackbar.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: ", e);
                                Snackbar.make(mContent, "Unable to book, try again!", Snackbar.LENGTH_SHORT).show();
                                mProgress.setVisibility(View.INVISIBLE);
                            }
                        });
            }
        });
        mRoomNo.setText(emptyRoom.getRoomNo());
        mTime.setText(emptyRoom.getTime());
        mContactInput.setText("+880");

    }

    private boolean isValid() {
        if (InputHelper.isEmpty(mCodeInput)) {
            mCodeLayout.setError("Course code can't be empty");
            return false;
        }
        if (InputHelper.isEmpty(mContactInput)) {
            mContactLayout.setError("Contact number can't be empty");
            return false;
        }
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String phoneNumber = mContactInput.getText().toString();
        try
        {
            Phonenumber.PhoneNumber numberProto = phoneNumberUtil.parse(phoneNumber, "+880");
            if (!phoneNumberUtil.isValidNumber(numberProto)) {
                mContactLayout.setError("Enter a valid contact number");
                return false;
            }
        }
        catch (NumberParseException e)
        {
            mContactLayout.setError("Enter a valid contact number");
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
