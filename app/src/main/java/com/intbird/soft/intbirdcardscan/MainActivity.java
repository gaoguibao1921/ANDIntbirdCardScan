package com.intbird.soft.intbirdcardscan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class MainActivity extends AppCompatActivity {

    private final int MY_SCAN_REQUEST_CODE = 1000;
    private String scanResultStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent scanIntent = new Intent(MainActivity.this, CardIOActivity.class);

                // customize these values to suit your needs.
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

                // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
                startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == MY_SCAN_REQUEST_CODE){
                if(data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)){
                    CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                    String cardNo = scanResult.cardNumber ;

                    scanResultStr += cardNo +"\n";

                    if(scanResult.isExpiryValid()) {
                        String extra = scanResult.expiryMonth + "\t" + scanResult.expiryYear + "\t";

                        scanResultStr += extra +"\n";
                    }

                    if (scanResult.cvv != null) {
                        // Never log or display a CVV
                        String cvv = scanResult.cvv;

                        scanResultStr += cvv +"\n";
                    }

                    if (scanResult.postalCode != null) {
                        String postCode = scanResult.postalCode;

                        scanResultStr += postCode +"\n";
                    }
                }else{
                    scanResultStr = "scan cacel";
                }
                showScanResult(scanResultStr);
            }
    }

    private void showScanResult(String result){
        TextView textView = (TextView)findViewById(R.id.tv_cardinfo);
        textView.setText(result);
    }
}
