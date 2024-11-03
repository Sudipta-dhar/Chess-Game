package scut_app.chess;


public class Move {
    private static final int MASK_POS = 0x3F;
    private static final int MASK_BOOL = 1;
    private static final int SHIFT_TO = 6;
    private static final int SHIFT_EP = 13;
    private static final int SHIFT_OO = 14;
    private static final int SHIFT_OOO = 15;
    private static final int SHIFT_HIT = 16;
    private static final int SHIFT_FIRSTPAWN = 17;
    private static final int SHIFT_CHECK = 18;
    private static final int SHIFT_PROMOTION = 19;
    private static final int SHIFT_PROMOTIONPIECE = 20;
    public static final int makeMove(final int from, final int to) {
        return from | (to << SHIFT_TO);
    }

    public static final int makeMoveFirstPawn(final int from, final int to) {
        return from | (to << SHIFT_TO) | (1 << SHIFT_FIRSTPAWN);
    }
    public static final int makeMoveHit(final int from, final int to) {
        return from | (to << SHIFT_TO) | (1 << SHIFT_HIT);
    }

    public static final int makeMoveEP(final int from, final int to) {
        return from | (to << SHIFT_TO) | (1 << SHIFT_HIT) | (1 << SHIFT_EP);
    }

    public static final int makeMoveOO(final int from, final int to) {
        return from | (to << SHIFT_TO) | (1 << SHIFT_OO);
    }

    public static final int makeMoveOOO(final int from, final int to) {
        return from | (to << SHIFT_TO) | (1 << SHIFT_OOO);
    }

    public static final int makeMovePromotion(final int from, final int to, final int piece, boolean bHit) {
        return from | (to << SHIFT_TO) | (1 << SHIFT_PROMOTION) | (piece << SHIFT_PROMOTIONPIECE) | (bHit == true ? (1 << SHIFT_HIT) : 0);
    }

    public static final int setCheck(final int move) {
        return move | (1 << SHIFT_CHECK);
    }

    public static final boolean equalPositions(final int m, final int m2) {
        return (m & MASK_POS) == (m2 & MASK_POS) && ((m >> SHIFT_TO) & MASK_POS) == ((m2 >> SHIFT_TO) & MASK_POS);
    }
    public static final boolean equalTos(int m, int m2) {
        return ((m >> SHIFT_TO) & MASK_POS) == ((m2 >> SHIFT_TO) & MASK_POS);
    }


    public static final int getFrom(final int move) {
        return move & MASK_POS;
    }

    public static final int getTo(final int move) {
        return (move >> SHIFT_TO) & MASK_POS;
    }

    public static final boolean isEP(final int move) {
        return ((move >> SHIFT_EP) & MASK_BOOL) == MASK_BOOL;
    }

    public static final boolean isOO(final int move) {
        return ((move >> SHIFT_OO) & MASK_BOOL) == MASK_BOOL;
    }

    public static final boolean isOOO(final int move) {
        return ((move >> SHIFT_OOO) & MASK_BOOL) == MASK_BOOL;
    }

    public static final boolean isHIT(final int move) {
        return ((move >> SHIFT_HIT) & MASK_BOOL) == MASK_BOOL;
    }

    public static final boolean isCheck(final int move) {
        return ((move >> SHIFT_CHECK) & MASK_BOOL) == MASK_BOOL;
    }
    public static final boolean isFirstPawnMove(final int move) {
        return ((move >> SHIFT_FIRSTPAWN) & MASK_BOOL) == MASK_BOOL;
    }
    public static final boolean isPromotionMove(final int move) {
        return ((move >> SHIFT_PROMOTION) & MASK_BOOL) == MASK_BOOL;
    }
    public static final int getPromotionPiece(final int move) {
        return move >> SHIFT_PROMOTIONPIECE;
    }


    public static final String toDbgString(final int move) {
        if (Move.isOO(move))
            return "O-O";
        if (Move.isOOO(move))
            return "O-O-O";
        return "[" + Pos.toString(Move.getFrom(move)) + (Move.isHIT(move) ? "x" : "-") + Pos.toString(Move.getTo(move)) + (Move.isCheck(move) ? "+" : "") + (Move.isEP(move) ? " ep" : "") + "]";
    }

}
