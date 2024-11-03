package scut_app.chess;


public class Pos {

    public static int fromString(final String s) {
        char c = s.charAt(0);
        int col, row = Integer.parseInt(s.substring(1));
        col = (int) c - (int) 'a';
        return ((8 - row) * 8) + col;
    }
    public static final int fromColAndRow(final int col, final int row) {
        return (row * 8) + col;
    }
    public static int row(final int val) {
        return (val >> 3) & 7;
    }
    public static int col(final int val) {
        return val % 8;
    }
    public static String toString(final int val) {
        return "" + ((char) (Pos.col(val) + (int) 'a')) + "" + (8 - Pos.row(val));
    }


    public static String rowToString(final int val) {
        return "" + (8 - Pos.row(val));
    }
    public static String colToString(final int val) {
        return "" + ((char) (Pos.col(val) + (int) 'a'));
    }
}
