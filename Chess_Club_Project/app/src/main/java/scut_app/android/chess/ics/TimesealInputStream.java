package scut_app.android.chess.ics;

import java.io.IOException;
import java.io.InputStream;


class TimesealInputStream extends InputStream {

    private final TimesealPipe a;

    public TimesealInputStream(TimesealPipe c1) {
        a = c1;
    }

    public int available() {
        return a._mthcase();
    }

    public void close() throws IOException {
        a._mthnew();
    }

    public int read() throws IOException {
        return a._mthfor();
    }

    public int read(byte abyte0[], int i, int j) throws IOException {
        return a._mthif(abyte0, i, j);
    }
}
