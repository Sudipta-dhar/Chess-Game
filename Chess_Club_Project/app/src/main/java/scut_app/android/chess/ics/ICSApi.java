package scut_app.android.chess.ics;

import android.util.Log;

import java.util.StringTokenizer;

import scut_app.android.chess.services.GameApi;
import scut_app.chess.Pos;
import scut_app.chess.board.BoardConstants;

public class ICSApi extends GameApi {
    public static final String TAG = "GameApi";
    public static final int VIEW_NONE = 0, VIEW_PLAY = 1, VIEW_OBSERVE = 2, VIEW_EXAMINE = 3;

    private int gameNum, myTurn, turn, lastTo = -1;
    private String whitePlayer;
    private String blackPlayer;
    private String playerMe;
    private String opponent;
    private int viewMode = VIEW_NONE;
    private int iTime, iIncrement;
    private int whiteRemaining;
    private int blackRemaining;
    private String lastMove;


    public synchronized boolean parseGame(String line, String sMe) {
        try {
            jni.reset();

            int p = 0, t = 0, index = -1; // !!

            for (int i = 0; i < 64; i++) {
                if (i % 8 == 0) {
                    index++;
                }
                char c = line.charAt(index++);
                if (c != '-') {
                    if (c == 'k' || c == 'K') {
                        p = BoardConstants.KING;
                        t = (c == 'k' ? BoardConstants.BLACK : BoardConstants.WHITE);
                    } else if (c == 'q' || c == 'Q') {
                        p = BoardConstants.QUEEN;
                        t = (c == 'q' ? BoardConstants.BLACK : BoardConstants.WHITE);
                    } else if (c == 'r' || c == 'R') {
                        p = BoardConstants.ROOK;
                        t = (c == 'r' ? BoardConstants.BLACK : BoardConstants.WHITE);
                    } else if (c == 'n' || c == 'N') {
                        p = BoardConstants.KNIGHT;
                        t = (c == 'n' ? BoardConstants.BLACK : BoardConstants.WHITE);
                    } else if (c == 'b' || c == 'B') {
                        p = BoardConstants.BISHOP;
                        t = (c == 'b' ? BoardConstants.BLACK : BoardConstants.WHITE);
                    } else if (c == 'p' || c == 'P') {
                        p = BoardConstants.PAWN;
                        t = (c == 'p' ? BoardConstants.BLACK : BoardConstants.WHITE);
                    } else
                        continue;
                    jni.putPiece(i, p, t);
                }
            }
            index++;

            line = line.substring(index);
            StringTokenizer st = new StringTokenizer(line);
            String _sTurn = st.nextToken();  // _sTurn is "W" or "B"
            turn = BoardConstants.WHITE;  //  _iTurn is  1  or  0
            if (_sTurn.equals("B")) {
                jni.setTurn(BoardConstants.BLACK);
                turn = BoardConstants.BLACK;
            } else {
                jni.setTurn(BoardConstants.WHITE);
            }
            int iEPColumn = Integer.parseInt(st.nextToken());
            int wccs = Integer.parseInt(st.nextToken());
            int wccl = Integer.parseInt(st.nextToken());
            int bccs = Integer.parseInt(st.nextToken());
            int bccl = Integer.parseInt(st.nextToken());
            int r50 = Integer.parseInt(st.nextToken());
            int ep = -1;
            if (iEPColumn >= 0) {
                if (turn == BoardConstants.WHITE) {
                    ep = iEPColumn + 16;
                } else {
                    ep = iEPColumn + 40;
                }
                Log.i("parseGame", "EP: " + ep);
            }

            int iTmp = Integer.parseInt(st.nextToken());
            gameNum = iTmp;

            whitePlayer = st.nextToken();
            blackPlayer = st.nextToken();


            final int relationNumber = Integer.parseInt(st.nextToken());
            if ((relationNumber == 2 || relationNumber == -2)) {

                viewMode = VIEW_EXAMINE;
            } else if (relationNumber == 1 || relationNumber == -1) {
                viewMode = VIEW_PLAY;
            } else if (relationNumber == 0) {
                viewMode = VIEW_OBSERVE;
            } else {
                viewMode = VIEW_NONE;
            }

            if (viewMode == VIEW_PLAY) {
                if (blackPlayer.equalsIgnoreCase(sMe)) {
                    myTurn = BoardConstants.BLACK;
                    playerMe = blackPlayer;
                    opponent = whitePlayer;
                } else if (whitePlayer.equalsIgnoreCase(sMe)) {
                    myTurn = BoardConstants.WHITE;
                    playerMe = whitePlayer;
                    opponent = blackPlayer;
                }
            } else {
                myTurn = BoardConstants.WHITE;
                playerMe = whitePlayer;
                opponent = blackPlayer;
            }

            Log.d(TAG, "ViewMode " + viewMode);

            iTime = Integer.parseInt(st.nextToken());
            iIncrement = Integer.parseInt(st.nextToken());
            int iWhiteMaterialStrength = Integer.parseInt(st.nextToken());
            int iBlackMaterialStrength = Integer.parseInt(st.nextToken());
            whiteRemaining = Integer.parseInt(st.nextToken());
            blackRemaining = Integer.parseInt(st.nextToken());
            String _sNumberOfMove = st.nextToken();
            String sMoveNotation = st.nextToken();
            String _sTimePerMove = st.nextToken();
            lastMove = st.nextToken();

            lastTo = -1;

            if (lastMove.equals("o-o")) {
                if (turn == BoardConstants.WHITE)
                    lastTo = Pos.fromString("g8");
                else
                    lastTo = Pos.fromString("g1");
            } else if (lastMove.equals("o-o-o")) {
                if (turn == BoardConstants.WHITE)
                    lastTo = Pos.fromString("c8");
                else
                    lastTo = Pos.fromString("c1");
            } else {
                try {
                    String lastClean = lastMove.replaceAll("(#|=.)", "");
                    if (lastClean.length() >= 2) {
                        lastTo = Pos.fromString(lastClean.substring(lastClean.length() - 2));
                    } else {
                        lastTo = -1;
                        Log.i(TAG, "Could not parse move: " + lastClean);
                    }
                    //iFrom = Pos.fromString(lastMove.substring(lastMove.length()-5, 2));
                } catch (Exception ex2) {
                    lastTo = -1;
                    Log.i(TAG, "Could not parse move: " + lastMove + " in " + lastMove.substring(lastMove.length() - 2));
                }
            }

            jni.setCastlingsEPAnd50(wccl, wccs, bccl, bccs, ep, r50);

            jni.commitBoard();

            Log.d(TAG, "FEN " + jni.toFEN() + "  " + jni.getState());
            dispatchState();
            return true;

        } catch (Exception ex) {
            Log.e("parseGame", ex.toString());

            dispatchState();
            return false;
        }

    }

    public int getViewMode() {
        return viewMode;
    }

    public void resetViewMode() {
        viewMode = VIEW_NONE;
    }

    @Override
    public String getMyPlayerName(int myTurn) {
        return playerMe;
    }

    @Override
    public String getOpponentPlayerName(int myTurn) {
        return opponent;
    }

    public long getTime() {
        return (long)iTime * 1000;
    }

    public long getIncrement() {
        return (long)iIncrement * 1000;
    }

    public long getWhiteRemaining() {
        return (long)whiteRemaining * 1000;
    }

    public long getBlackRemaining() {
        return (long)blackRemaining * 1000;
    }

    public String getLastMove() {
        return lastMove;
    }

    public int getMyTurn() {
        return myTurn;
    }

    public int getTurn() {
        return turn;
    }

    public int getLastTo() {
        return lastTo;
    }
}
