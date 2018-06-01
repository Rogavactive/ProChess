package Game.Tests;

import Accounting.Model.AccountManager;
import Game.Model.Pieces.Pawn;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

class PawnTest extends AccountManager {
    @Test
    private void testColor() {
        Pawn pawn = new Pawn(true);
        assert (pawn.getColor() == true);

        pawn = new Pawn(false);
        assert (pawn.getColor() == false);
    }

}