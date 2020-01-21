package com.example.electronicstudentid.Nfc;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcBarcode;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.electronicstudentid.R;

import butterknife.BindView;

public class NfcActivity extends AppCompatActivity {
    public Tag tag = null;
    @BindView(R.id.textView)
    TextView textView;
    private boolean readTag = true;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED)) {
                final int state = intent.getIntExtra(NfcAdapter.EXTRA_ADAPTER_STATE,
                        NfcAdapter.STATE_OFF);
                switch (state) {
                    case NfcAdapter.STATE_OFF:
                        nfcDetected();
                    case NfcAdapter.STATE_ON:
                        nfcDetected();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        nfcDetected();
        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED);
        this.registerReceiver(mReceiver, filter);
    }

    private void nfcDetected() {
        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        textView = findViewById(R.id.textView);

        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC is disabled.", Toast.LENGTH_LONG).show();
            textView.setText("NFC is disabled. You should turn on NFC on your phone");

        } else {
            Toast.makeText(this, "NFC is enabled.", Toast.LENGTH_LONG).show();
            textView.setText("Keep your student card near the NFC reader (back of the phone)");

        }
    }

    private void enableNfcForegroundDispatch() {
        String[][] techList = new String[][]{
                new String[]{NfcA.class.getName()},
                new String[]{NfcB.class.getName()},
                new String[]{NfcF.class.getName()},
                new String[]{NfcV.class.getName()},
                new String[]{NfcBarcode.class.getName()},
        };

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(
                        this, getClass()).addFlags(
                                Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        nfcAdapter.enableForegroundDispatch(
                this, pendingIntent, new IntentFilter[]{
                        new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED),
                        new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)}, techList);
    }

    private String getTagID(Tag tag) {
        byte[] tagId = tag.getId();
        String id = "";
        for (int i = 0; i < tagId.length; i++) {
            id += Integer.toHexString(tagId[i] & 0xFF) + "";
        }
        return id;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            if (tag == null) {
                Toast.makeText(this, "Try again.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent1 = new Intent();

                intent1.putExtra("tag", getTagID(tag));
                setResult(RESULT_OK, intent1);

                Toast.makeText(this, "Student Card was registered.",
                        Toast.LENGTH_SHORT).show();
                readTag = false;
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (readTag) {
            enableNfcForegroundDispatch();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        NfcAdapter.getDefaultAdapter(this).disableForegroundDispatch(this);
    }

    @Override
    protected void onStop() {
        unregisterReceiver(mReceiver);
        super.onStop();
    }

}