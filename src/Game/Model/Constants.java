package Game.Model;

public class Constants {
    public static final int NUMBER_OF_ROWS = 8;
    public static final int NUMBER_OF_COLUMNS = 8;

    public static final int deadRow = -1;
    public static final int deadCol = -1;

    public enum pieceColor{
        white(false),
        black(true);

        private final boolean color;

        pieceColor(boolean b) {
            this.color = b;
        }
    }

    public enum pieceType{
        Bishop,
        King,
        Knight,
        Pawn,
        Queen,
        Rook,
        emptyCell
    }
}
