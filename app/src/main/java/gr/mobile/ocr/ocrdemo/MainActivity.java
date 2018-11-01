package gr.mobile.ocr.ocrdemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import cards.pay.paycardsrecognizer.sdk.Card;
import cards.pay.paycardsrecognizer.sdk.ScanCardIntent;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final static String ARG_CARD_NUMBER = "card_number";
    private static final int REQUEST_SCAN = 100;
    private static final int unblurDigits = 4;
    private String cardNumber;

    @BindView(R.id.infoTextView)
    AppCompatTextView infoTextView;

    @BindView(R.id.scanButton)
    AppCompatButton scannButton;

    @BindView(R.id.resultImage)
    AppCompatImageView resultImage;

    @BindView(R.id.resultCardTypeImage)
    AppCompatImageView resultCardTypeImage;

    @BindView(R.id.result)
    AppCompatTextView resultTextView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initLayout();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(TAG, "onActivityResult(" + requestCode + ", " + resultCode + ", " + data + ")");

        String outStr = new String();
        Bitmap cardTypeImage = null;

        if (requestCode == REQUEST_SCAN) {
            if (resultCode == Activity.RESULT_OK) {
                Card card = data.getParcelableExtra(ScanCardIntent.RESULT_PAYCARDS_CARD);
                if (BuildConfig.DEBUG) Log.i(TAG, "Card info: " + card);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                @ScanCardIntent.CancelReason final int reason;
                if (data != null) {
                    reason = data.getIntExtra(ScanCardIntent.RESULT_CANCEL_REASON, ScanCardIntent.BACK_PRESSED);
                } else {
                    reason = ScanCardIntent.BACK_PRESSED;
                }

            } else if (resultCode == ScanCardIntent.RESULT_CODE_ERROR) {
                Log.i(TAG, "Scan failed");
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initLayout() {
        scannButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScannButtonClicked();
            }
        });
    }

    private void onScannButtonClicked() {
        Intent intent = new ScanCardIntent.Builder(this).build();
        startActivityForResult(intent, REQUEST_SCAN);
    }


    private void startNextActivity(String cardNumber) {
        Intent intent = new Intent(MainActivity.this, InfoCardActivity.class);
        Bundle passDataBundle = new Bundle();
        passDataBundle.putString(ARG_CARD_NUMBER, cardNumber);
        intent.putExtras(passDataBundle);
        startActivity(intent);
    }
}
