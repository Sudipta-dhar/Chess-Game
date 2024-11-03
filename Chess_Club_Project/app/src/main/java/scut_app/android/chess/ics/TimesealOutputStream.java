package scut_app.android.chess.ics;

import java.io.IOException;
import java.io.OutputStream;


public class TimesealOutputStream extends OutputStream {

    private final TimesealPipe a;

    public TimesealOutputStream(TimesealPipe c1) {
        a = c1;
    }

    @Override
    public void close() throws IOException {
        a._mthtry();
    }

    @Override
    public void write(byte abyte0[], int i, int j) throws IOException {
        a.write(abyte0, i, j);
    }

    @Override
    public void write(int i) throws IOException {
        a.a(i);
    }
}
