package scut_app.chess;

// Valuation. Wrapper class for evaluation

public class Valuation {
    public Valuation() {
        MPD = 70;

        DOUBLED_PAWN = 10;
        PASSED_PAWN = 10;
        CASTLED_KING_POS = 10;
        EARLY_QUEEN = 30;
        CENTER_KNIGHT = 5;
        DEVELOPED_KNIGHT = 2;
        BISHOP_MOVE = 2;
        ROOK_MOVE = 2;
        ROOK_RANK_7 = 10;
        ROOK_OPEN_FILE = 10;
    }

    public void setWeights4Test() {
        MPD = 150;

        DOUBLED_PAWN = 10 * 3;
        PASSED_PAWN = 10 * 3;
        CASTLED_KING_POS = 10 * 3;
        EARLY_QUEEN = 30 * 3;
        CENTER_KNIGHT = 5 * 3;
        DEVELOPED_KNIGHT = 2 * 3;
        BISHOP_MOVE = 2 * 3;
        ROOK_MOVE = 2 * 3;
        ROOK_RANK_7 = 10 * 3;
        ROOK_OPEN_FILE = 10 * 3;
    }


    public static final int MATE = 10000;
    public static final int DRAW = 0;
    public static int DRAW_REPEAT = -10;
    public static int PIECES[] = {100, 300, 305, 600, 1000, 0};

    public static int TRANSPOSITION_MOVE_SCORE = 2000;
    public static int KILLER_MOVE_SCORE = 1500;
    public static int LONE_KING = 6;
    public static int LONE_KING_BONUS = 150;
    public static int MPD;
    public static int DOUBLED_PAWN;
    public static int PASSED_PAWN;
    public static int CASTLED_KING_POS;
    public static int EARLY_QUEEN;
    public static int CENTER_KNIGHT;
    public static int DEVELOPED_KNIGHT;
    public static int BISHOP_MOVE;
    public static int ROOK_MOVE;
    public static int ROOK_RANK_7;
    public static int ROOK_OPEN_FILE;


    public static final long CENTER_4x4_SQUARES = 0x00003C3C3C3C0000L;
    public static final long CENTER_SQUARES = 0x00003C7E7E3C0000L;


    public static final int[] KING_ENDINGS = {
            0, 6, 12, 18, 18, 12, 6, 0,
            6, 12, 18, 24, 24, 18, 12, 6,
            12, 18, 24, 32, 32, 24, 18, 12,
            18, 24, 32, 48, 48, 32, 24, 18,
            18, 24, 32, 48, 48, 32, 24, 18,
            12, 18, 24, 32, 32, 24, 18, 12,
            6, 12, 18, 24, 24, 18, 12, 6,
            0, 6, 12, 18, 18, 12, 6, 0
    };

    public static final int KBNK_SCORE[][] = {
            {
                    0, 10, 20, 30, 40, 50, 60, 70,
                    10, 20, 30, 40, 50, 60, 70, 60,
                    20, 30, 40, 50, 60, 70, 60, 50,
                    30, 40, 50, 60, 70, 60, 50, 40,
                    40, 50, 60, 70, 60, 50, 40, 30,
                    50, 60, 70, 60, 50, 40, 30, 20,
                    60, 70, 60, 50, 40, 30, 20, 10,
                    70, 60, 50, 40, 30, 20, 10, 0
            },
            {
                    70, 60, 50, 40, 30, 20, 10, 0,
                    60, 70, 60, 50, 40, 30, 20, 10,
                    50, 60, 70, 60, 50, 40, 30, 20,
                    40, 50, 60, 70, 60, 50, 40, 30,
                    30, 40, 50, 60, 70, 60, 50, 40,
                    20, 30, 40, 50, 60, 70, 60, 50,
                    10, 20, 30, 40, 50, 60, 70, 60,
                    0, 10, 20, 30, 40, 50, 60, 70,
            }

    };

}