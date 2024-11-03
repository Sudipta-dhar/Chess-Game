package scut_app.android.chess.activities;

import android.os.Bundle;

import scut_app.android.chess.R;

public class GlobalPreferencesActivity extends BasePreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.global_prefs);

    }
}

