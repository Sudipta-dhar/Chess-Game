package scut_app.android.chess.tools;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.util.TreeSet;

import scut_app.android.chess.puzzle.MyPuzzleProvider;
import scut_app.android.chess.services.GameApi;
import scut_app.chess.JNI;
import scut_app.chess.PGNColumns;
import scut_app.chess.board.ChessBoard;

public class PracticeImportProcessor extends PGNProcessor {
    private static final String TAG = "PracticeImportProcessor";

    private JNI jni;
    private GameApi gameApi;
    private ContentResolver contentResolver;
    private TreeSet<Long> _arrKeys;

    public PracticeImportProcessor(int mode, Handler updateHandler, GameApi gameApi, ContentResolver contentResolver) {
        super(mode, updateHandler);
        jni = JNI.getInstance();
        _arrKeys = new TreeSet<Long>();
        this.gameApi = gameApi;
        this.contentResolver = contentResolver;
    }

    @Override
    public boolean processPGN(String sPGN) {

        if (gameApi.loadPGN(sPGN)) {

            if (jni.getState() == ChessBoard.MATE) {

                long lKey = jni.getHashKey();

                if (false == _arrKeys.contains(lKey)) {
                    _arrKeys.add(lKey);
                } else {
                    return false;
                }

                int startExport = gameApi.getPGNSize();

                int plies = 2, undos = 0, moves = 0;
                String s = "";
                String[] arrMoves = {
                        gameApi.exportMovesPGNFromPly(startExport),
                        "",
                        gameApi.exportMovesPGNFromPly(startExport - 3),
                };

                if (startExport >= 3) {

                    while (plies <= 4) {
                        undos = 0;
                        while (undos <= moves) {
                            gameApi.undoMove();
                            undos++;
                        }
                        String sFEN = jni.toFEN();

                        jni.searchDepth(plies);

                        int move = jni.getMove();
                        int value = jni.peekSearchBestValue();

                        if (value == 100000 * (plies % 2 == 0 ? 1 : -1) && jni.move(move) != 0) {
                            gameApi.addPGNEntry(jni.getNumBoard() - 1, jni.getMyMoveToString(), "", jni.getMyMove());

                            if (plies % 2 == 0) {
                                if (plies == 4) {
                                    Log.i(TAG, "YESS");
                                }
                                s = "[FEN \"" + sFEN + "\"]\n" + arrMoves[moves];
                            }
                            moves++;
                            plies++;

                        } else {

                            break;
                        }
                    }

                    if (s.length() > 0) {
                        try {

                            ContentValues values = new ContentValues();
                            values.put(PGNColumns.PGN, s);

                            Uri uri = MyPuzzleProvider.CONTENT_URI_PRACTICES;
                            Uri uriInsert = contentResolver.insert(uri, values);

                            return true;
                        } catch (Exception ex) {
                            Log.e(TAG, ex.toString());
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String getString() {
        return null;
    }
}
