package scut_app.android.chess.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.TypedValue;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;
import scut_app.android.chess.R;
import scut_app.chess.board.ChessBoard;

public class ChessPieceLabelView extends AppCompatTextView {
    private int position;

    public ChessPieceLabelView(Context context, int position, int color, String label) {
        super(context);

        this.position = position;

        setWillNotDraw(false);
        setGravity(Gravity.CENTER);

        if (color == ChessBoard.BLACK) {
            setTextColor(0xFFFFFFFF);
            setBackgroundResource(R.drawable.turnblack);
        } else {
            setBackgroundResource(R.drawable.turnwhite);
            setTextColor(0xFF000000);
        }
        setText(label);
    }

    public int getPos() {
        return position;
    }

    public void onDraw(Canvas canvas) {
        int textSize = 3 * getHeight() / 4;
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize >= 8 ? textSize : 8);

        super.onDraw(canvas);
    }
}
