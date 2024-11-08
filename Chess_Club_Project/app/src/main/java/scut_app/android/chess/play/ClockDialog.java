package scut_app.android.chess.play;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import scut_app.android.chess.R;
import scut_app.android.chess.helpers.ResultDialog;
import scut_app.android.chess.helpers.ResultDialogListener;

public class ClockDialog extends ResultDialog {
    public ClockDialog(@NonNull Context context, ResultDialogListener listener, int requestCode, final SharedPreferences prefs) {
        super(context, listener, requestCode);

        setContentView(R.layout.clock_dialog);

        int increment = (int)(prefs.getLong("clockIncrement", 0) / 1000);
        int totalMinutes = (int)(prefs.getLong("clockTotalMillies", 0) / 60000);

        final EditText editMinutes = findViewById(R.id.EditMinutes);
        final EditText editIncrement = findViewById(R.id.EditMinutes);

        editMinutes.setText("" + totalMinutes);
        editIncrement.setText("" + increment);

        Button buttonOk = findViewById(R.id.ButtonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    SharedPreferences.Editor editor = prefs.edit();

                    long totalMillies = (long)Integer.parseInt(editMinutes.getText().toString()) * 60000;
                    long increment = (long)Integer.parseInt(editIncrement.getText().toString()) * 1000;

                    editor.putLong("clockWhiteMillies", totalMillies);
                    editor.putLong("clockBlackMillies", totalMillies);
                    editor.putLong("clockIncrement", increment);
                    editor.putLong("clockStartTime", System.currentTimeMillis());

                    editor.commit();

                    setResult(new Bundle());

                } catch (Exception e) {}
                dismiss();
            }
        });
        Button buttonCancel = findViewById(R.id.ButtonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
