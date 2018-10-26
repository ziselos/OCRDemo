package gr.mobile.ocr.ocrdemo;

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
import io.card.payment.CardIOActivity;
import io.card.payment.CardType;
import io.card.payment.CreditCard;

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

        if (requestCode == REQUEST_SCAN && data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            CreditCard result = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
            if (result != null) {
                outStr += "Card number: " + result.getRedactedCardNumber() + "\n";
                cardNumber = result.getFormattedCardNumber();

                CardType cardType = result.getCardType();
                cardTypeImage = cardType.imageBitmap(this);
                outStr += "Card type: " + cardType.name() + cardType.getDisplayName(null) + "\n";
            }

        Bitmap card = CardIOActivity.getCapturedCardImage(data);
        resultImage.setImageBitmap(card);
        resultCardTypeImage.setImageBitmap(cardTypeImage);

        Log.i(TAG, "Set result: " + outStr);

        resultTextView.setText(outStr);

        startNextActivity(cardNumber);

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
        startActivity(new Intent(MainActivity.this, ScanCardActivity.class));
//        Intent scannCardIntent = new Intent(this, CardIOActivity.class)
//                .putExtra(CardIOActivity.EXTRA_NO_CAMERA, false)
//                .putExtra(CardIOActivity.EXTRA_GUIDE_COLOR, Color.BLUE)
//                .putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, true)
//                .putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, true);
//        try {
//            scannCardIntent.putExtra(CardIOActivity.EXTRA_UNBLUR_DIGITS, unblurDigits);
//        } catch(NumberFormatException ignored) {}
//
//        startActivityForResult(scannCardIntent, REQUEST_SCAN);
    }


    private void startNextActivity(String cardNumber) {
        Intent intent = new Intent(MainActivity.this, InfoCardActivity.class);
        Bundle passDataBundle = new Bundle();
        passDataBundle.putString(ARG_CARD_NUMBER, cardNumber);
        intent.putExtras(passDataBundle);
        startActivity(intent);
    }
}
