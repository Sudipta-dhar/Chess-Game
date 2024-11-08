package scut_app.android.chess.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import scut_app.android.chess.R;
import scut_app.android.chess.constants.ColorSchemes;
import scut_app.android.chess.constants.PieceSets;
import scut_app.android.chess.services.GameApi;

public class BoardPreferencesActivity extends ChessBoardActivity {
    private static final String TAG = "BoardPreferences";
    private CheckBox checkBoxCoordinates;
    private Spinner spinnerPieceSet, spinnerColorScheme, spinnerTileSet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chessboard_prefs);

        spinnerPieceSet = findViewById(R.id.SpinnerPieceSet);
        spinnerColorScheme = findViewById(R.id.SpinnerColorScheme);
        spinnerTileSet = findViewById(R.id.SpinnerTileSet);
        checkBoxCoordinates = findViewById(R.id.CheckBoxCoordinates);

        spinnerPieceSet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                PieceSets.selectedSet = pos;
                rebuildBoard();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

        });

        spinnerColorScheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                ColorSchemes.selectedColorScheme = pos;
                chessBoardView.invalidateSquares();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

        });

        spinnerTileSet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                ColorSchemes.selectedPattern = pos;
                chessBoardView.invalidateSquares();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

        });

        checkBoxCoordinates.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               ColorSchemes.showCoords = isChecked;
               chessBoardView.invalidateSquares();
           }
       });

        gameApi = new GameApi();

        afterCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getPrefs();

        jni.newGame();

        checkBoxCoordinates.setChecked(prefs.getBoolean("showCoords", false));

        spinnerPieceSet.setSelection(Integer.parseInt(prefs.getString("pieceset", "0")));
        spinnerColorScheme.setSelection(Integer.parseInt(prefs.getString("colorscheme", "0")));
        spinnerTileSet.setSelection(Integer.parseInt(prefs.getString("squarePattern", "0")));

        rebuildBoard();

        spinnerPieceSet.requestFocus();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = this.getPrefs().edit();

        editor.putString("pieceset", "" + spinnerPieceSet.getSelectedItemPosition());
        editor.putString("colorscheme", "" + spinnerColorScheme.getSelectedItemPosition());
        editor.putString("squarePattern", "" + spinnerTileSet.getSelectedItemPosition());
        editor.putBoolean("showCoords", checkBoxCoordinates.isChecked());

        editor.commit();
    }

    @Override
    public boolean requestMove(int from, int to) {
        return false;
    }

}
