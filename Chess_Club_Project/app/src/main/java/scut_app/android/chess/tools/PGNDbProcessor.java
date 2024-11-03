package scut_app.android.chess.tools;

import android.os.Handler;
import android.util.Log;

import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.TreeSet;

import scut_app.android.chess.services.GameApi;
import scut_app.chess.JNI;

public class PGNDbProcessor extends PGNProcessor {
    private GameApi gameApi;
    private int _untilPly = 17;
    private JNI jni;
    private TreeSet<Long> _arrKeys;

    public PGNDbProcessor(int mode, Handler updateHandler, GameApi gameApi) {
        super(mode, updateHandler);

        _arrKeys = new TreeSet<Long>();
        jni = JNI.getInstance();
        this.gameApi = gameApi;
    }

    @Override
    public synchronized boolean processPGN(final String sPGN) {

        long lKey;
        if (gameApi.loadPGN(sPGN)) {
            int ply = 0, pgnSize = gameApi.getPGNSize();
            int existingCnt = 0;
            //Log.i("import", "processPGN - gameSize:" + pgnSize);
            while (ply <= pgnSize && ply <= _untilPly) {
                //_jni.getNumBoard();
                gameApi.jumptoMove(ply);

                lKey = jni.getHashKey();

                if (false == _arrKeys.contains(lKey)) {
                    _arrKeys.add(lKey);
                } else {
                    existingCnt++;
                }
                ply++;
            }
            return true;
        }
        return false;
    }

    @Override
    public String getString() {
        // TODO Auto-generated method stub
        return null;
    }

    public void writeHashKeysToFile(String outFile) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(outFile);
            long l;
            byte[] bytes = new byte[8];


            Iterator<Long> it = _arrKeys.iterator();
            while (it.hasNext()) {

                l = it.next();

                if (l == 0)
                    break;

                bytes[0] = (byte) (l >>> 56);
                bytes[1] = (byte) (l >>> 48);
                bytes[2] = (byte) (l >>> 40);
                bytes[3] = (byte) (l >>> 32);
                bytes[4] = (byte) (l >>> 24);
                bytes[5] = (byte) (l >>> 16);
                bytes[6] = (byte) (l >>> 8);
                bytes[7] = (byte) (l);

                fos.write(bytes);

            }

            fos.flush();
            fos.close();
            Log.i("import", "wrote hash keys to " + outFile);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("import", "writeHashkeys: " + e.toString());
            e.printStackTrace();
        }
    }
}