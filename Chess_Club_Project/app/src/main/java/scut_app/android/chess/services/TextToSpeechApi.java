package scut_app.android.chess.services;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import scut_app.chess.Move;

public class TextToSpeechApi extends TextToSpeech {
    public TextToSpeechApi(Context context, OnInitListener listener) {
        super(context, listener);
    }

    public void setDefaults() {
        setSpeechRate(0.80F);
        setPitch(0.85F);
    }

    public void moveToSpeech(String sMove, int move) {

        String sMoveSpeech = sMove;
        if (sMoveSpeech.length() > 3 && !sMoveSpeech.equals("O-O-O")) {

            sMoveSpeech = sMoveSpeech.substring(0, 2) + " " + sMoveSpeech.substring(2, sMoveSpeech.length());
        }

        if (sMoveSpeech.length() > 3) {
            if (sMoveSpeech.charAt(sMoveSpeech.length() - 4) == ' ')
            {
                sMoveSpeech = sMoveSpeech.substring(0, sMoveSpeech.length() - 2) + " " + sMoveSpeech.substring(sMoveSpeech.length() - 2, sMoveSpeech.length());
            }
        }

        sMoveSpeech = sMoveSpeech.replace("a", "ay ");

        sMoveSpeech = sMoveSpeech.replace("b", "bee ");


        sMoveSpeech = sMoveSpeech.replace("x", " takes ");

        sMoveSpeech = sMoveSpeech.replace("=", " promotes to ");

        sMoveSpeech = sMoveSpeech.replace("K", "King ");
        sMoveSpeech = sMoveSpeech.replace("Q", "Queen ");
        sMoveSpeech = sMoveSpeech.replace("R", "Rook ");
        sMoveSpeech = sMoveSpeech.replace("B", "Bishop ");
        sMoveSpeech = sMoveSpeech.replace("N", "Knight ");

        sMoveSpeech = sMoveSpeech.replace("O-O-O", "Castle Queen Side");
        sMoveSpeech = sMoveSpeech.replace("O-O", "Castle King Side");

        sMoveSpeech = sMoveSpeech.replace("+", " check");
        sMoveSpeech = sMoveSpeech.replace("#", " checkmate");

        if (Move.isEP(move)) {
            sMoveSpeech = sMoveSpeech + " On Pesawnt";
        }

        speak(sMoveSpeech, TextToSpeech.QUEUE_FLUSH, null, sMove);
    }

}
