package scut_app.android.chess;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;
import scut_app.android.chess.activities.BaseActivity;

public class HtmlActivity extends BaseActivity {

    public static final String TAG = "HtmlActivity";
    public static String HELP_STRING_RESOURCE = "HELP_STRING_RESOURCE";

    private TextView _TVversionName, textViewHelp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.help);

        _TVversionName = (TextView) findViewById(R.id.textVersionName);
        _TVversionName.setText(getString(R.string.version_number, BuildConfig.VERSION_NAME)); // "Version:  " + BuildConfig.VERSION_NAME

        textViewHelp = findViewById(R.id.TextViewHelp);

    }

    @Override
    protected void onResume() {
        super.onResume();

        final Intent intent = getIntent();
        // final Uri uri = intent.getData();

        Bundle extras = intent.getExtras();
        if (extras != null) {
            int resource = extras.getInt(HELP_STRING_RESOURCE);
            textViewHelp.setText(HtmlCompat.fromHtml(getString(resource), HtmlCompat.FROM_HTML_MODE_LEGACY));

            _TVversionName.setVisibility(resource == R.string.about_help ? View.VISIBLE : View.GONE);

        }
    }
}
