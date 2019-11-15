package com.example.onemoretime;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;

public class NfcActivity extends AppCompatActivity {
    private TextView mTextView;
    private boolean readTag = true;
    public Tag tag = null;

    @BindView(R.id.btn_nfc_card) Button _nfcCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nfcDetected();
        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED);
        this.registerReceiver(mReceiver, filter);
    }

    private void chooseContentView(NfcAdapter mNfcAdapter){

        if(tag == null && mNfcAdapter.isEnabled()) {
            setContentView(R.layout.activity_nfc);
            _nfcCard = (Button) findViewById(R.id.btn_nfc_card);
            _nfcCard.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                }
            });
        }
        else {
            setContentView(R.layout.activity_main);
        }
    }

    private void nfcDetected(){
        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC is disabled.", Toast.LENGTH_LONG).show();
            chooseContentView(mNfcAdapter);
        } else {
            Toast.makeText(this, "NFC is enabled.", Toast.LENGTH_LONG).show();
            chooseContentView(mNfcAdapter);
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED)) {
                final int state = intent.getIntExtra(NfcAdapter.EXTRA_ADAPTER_STATE,
                        NfcAdapter.STATE_OFF);
                switch (state) {
                    case NfcAdapter.STATE_OFF:
                        Log.d("null", "STATE_OFF");
                        nfcDetected();
                    case NfcAdapter.STATE_ON:
                        Log.d("null", "STATE_ON");
                        nfcDetected();
                }
            }
        }
    };

    private void enableNfcForegroundDispatch(){
        String[][] techList = new String[][] {
                new String[] { NfcA.class.getName() },
                new String[] { NfcB.class.getName() },
                new String[] { NfcF.class.getName() },
                new String[] { NfcV.class.getName() },
                new String[] { NfcBarcode.class.getName() },
        };

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[] {new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED), new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)}, techList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        if(readTag) {
            enableNfcForegroundDispatch();
        }
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        super.onPause();

        NfcAdapter.getDefaultAdapter(this).disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) ) {

            tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Log.d("tag", " "+ tag);

            if(tag == null){
                Toast.makeText(this,"Try again.", Toast.LENGTH_SHORT).show();
            }else{
                setContentView(R.layout.activity_main);
                mTextView = (TextView) findViewById(R.id.textView_explanation);

                TagHandler tagHandler = new TagHandler(tag);
                String tagInfo = tagHandler.getTagInfo() + "\n";
                tagInfo += "\nTag Id: \n";
                tagInfo += "length = " + tagHandler.getTagIDLength() +"\n";
                tagInfo += tagHandler.getTagID() + "\n";
                tagInfo += "\nTech List\n";
                tagInfo += "length = " + tagHandler.getTechListLength() +"\n";
                tagInfo += tagHandler.getTechList() + "\n" ;
                tagInfo += "\nDescribeContents\n";
                tagInfo += tagHandler.getDescribeContents() + "\n";
                tagInfo += "\nHashCode\n";
                tagInfo += tagHandler.getHashCode() + "\n";
                tagInfo += "\nSAK\n";
                tagInfo += tagHandler.getSak() + "\n";
                tagInfo += "\nATQA\n";
                tagInfo += tagHandler.getAtqa() + "\n";
                tagInfo += "\nATS\n";
                tagInfo += tagHandler.getAts() + "\n";
                tagInfo += "\nTag Type\n";
                tagInfo += tagHandler.getTagType(this) + "\n";
                mTextView.setText(tagInfo);
                Toast.makeText(this, "Student Card was registered.",
                        Toast.LENGTH_SHORT).show();
                readTag = false;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Toast.makeText(this, "Logout",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}