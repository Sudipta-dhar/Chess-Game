package scut_app.android.chess.ics;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import scut_app.android.chess.R;

/**
 *
 */
public class ICSChatDlg extends Dialog {

    private ICSClient _parent;
    public TextView _tvChat;
    private EditText _editChat;

    public ICSChatDlg(Context context) {
        super(context);

        _parent = (ICSClient) context;

        setContentView(R.layout.ics_chat);

        _tvChat = (TextView) findViewById(R.id.TextViewChat);
        _editChat = (EditText) findViewById(R.id.EditChat);

        Button butYes = (Button) findViewById(R.id.ButtonChatOk);
        butYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dismiss();
            }
        });
        Button butNo = (Button) findViewById(R.id.ButtonChatCancel);
        butNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dismiss();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    public void prepare() {
        _editChat.setText("");
        _editChat.requestFocus();
    }
}