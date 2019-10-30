package com.example.onemoretime;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NfcAdapter mNfcAdapter;
        mTextView = (TextView) findViewById(R.id.textView_explanation);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (!mNfcAdapter.isEnabled()) {
            mTextView.setText(R.string.disable_nfc);
        } else {
            Toast.makeText(this, "This device support NFC.", Toast.LENGTH_LONG).show();
            mTextView.setText(R.string.explanation);
        }

        handleIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
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
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if(tag == null){
                Toast.makeText(this,"Try again.", Toast.LENGTH_SHORT).show();
                mTextView.setText(R.string.tag_null);
            }else{
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
            }
        }
    }
}