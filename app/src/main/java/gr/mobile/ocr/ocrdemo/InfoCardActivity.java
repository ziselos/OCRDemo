package gr.mobile.ocr.ocrdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoCardActivity extends AppCompatActivity {

    private final static  String ARG_CARD_NUMBER = "card_number";

    @BindView(R.id.cardNumberTextView)
    AppCompatTextView cardNumberTextView;

    private String cardNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_card_number_info);
        ButterKnife.bind(this);
        getPassData();
        initLayout();
    }


    private void initLayout() {
        cardNumberTextView.setText(cardNumber);
    }


    private void getPassData() {
        Bundle getPassDataBundle = getIntent().getExtras();
        if (getPassDataBundle != null) {
            cardNumber = getPassDataBundle.getString(ARG_CARD_NUMBER);
        }
    }
}
