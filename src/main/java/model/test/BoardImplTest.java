package model.test;


import model.Board;
import model.BoardImpl;
import model.Tile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Logger;

public class BoardImplTest {

    BoardImpl testBoard;
    final int defaultBoardSize = 3;

    @Before
    public void setUp() throws Exception {
        testBoard = new BoardImpl(defaultBoardSize);
    }

    @Test
    public void testGetSideSizeInSquares() throws Exception {

        Assert.assertEquals(defaultBoardSize, testBoard.getSideSizeInSquares());
    }

    /*
     * Identity test for packing
     * The current and next board should be the same
     */
    @Test
    public void testPackIntoDirection1() throws Exception {
        testBoard.loadBoard(new int[][]{{1, 0, 0}, {0, 0, 0}, {0, 0, 0}});
        Tile uniqueTile = testBoard.getTile(1, 1);
        Assert.assertNotNull(uniqueTile);
        testBoard.packIntoDirection(Board.Direction.LEFT);
        testBoard.commit();
        uniqueTile = testBoard.getTile(1, 1);
        Assert.assertNotNull(uniqueTile);
        Assert.assertEquals(1, uniqueTile.getRank());
    }

    /*
     * Check that one tile in (1,3) is moved to (1,1)
     */
    @Test
    public void testPackIntoDirection2() throws Exception {
        testBoard.loadBoard(new int[][]{{0, 0, 1}, {0, 0, 0}, {0, 0, 0}});
        testBoard.packIntoDirection(Board.Direction.LEFT);
        testBoard.commit();
        Tile uniqueTile = testBoard.getTile(1, 1);
        Assert.assertNotNull(uniqueTile);
        Assert.assertEquals(1, uniqueTile.getRank());
        Assert.assertNull(testBoard.getTile(1, 3));
    }

    /*
    * Check that tiles in (1,2) and (1,3) with different ranks
    * are moved in (1,1) and (1,2)
    */
    @Test
    public void testPackIntoDirection3() throws Exception {
        testBoard.loadBoard(new int[][]{{0, 1, 2}, {0, 0, 0}, {0, 0, 0}});
        testBoard.packIntoDirection(Board.Direction.LEFT);
        testBoard.commit();
        Tile uniqueTile = testBoard.getTile(1, 1);
        Assert.assertNotNull(uniqueTile);
        Assert.assertEquals(1, uniqueTile.getRank());
        Assert.assertNotNull(testBoard.getTile(1, 2));
        Assert.assertEquals(2, testBoard.getTile(1, 2).getRank());
    }

    /*
     * Check that a basic merge works,
     * namely that (1,2),(1,3) with rank 1 get merged into (1,1) with rank 2
     */
    @Test
    public void testPackIntoDirection4() throws Exception {
        testBoard.loadBoard(new int[][]{{0, 1, 1}, {0, 0, 0}, {0, 0, 0}});
        testBoard.packIntoDirection(Board.Direction.LEFT);
        testBoard.commit();
        Assert.assertNotNull(testBoard.getTile(1, 1));
        Assert.assertEquals(2, testBoard.getTile(1, 1).getRank());
        Assert.assertNull(testBoard.getTile(1, 2));
        Assert.assertNull(testBoard.getTile(1, 3));
    }

    /*
     * Check that a more complicated merge works,
     * namely that (1,2),(1,3) with rank 1 get merged into (1,2) with rank 2
     * and that (1,1) with rank 3 is copied into (1,1)
     */
    @Test
    public void testPackIntoDirection5() throws Exception {
        testBoard.loadBoard(new int[][]{{3, 1, 1}, {0, 0, 0}, {0, 0, 0}});
        testBoard.packIntoDirection(Board.Direction.LEFT);
        testBoard.commit();
        Assert.assertNotNull(testBoard.getTile(1, 1));
        Assert.assertEquals(3, testBoard.getTile(1, 1).getRank());
        Assert.assertNotNull(testBoard.getTile(1, 2));
        Assert.assertEquals(2, testBoard.getTile(1, 2).getRank());
        Assert.assertNull(testBoard.getTile(1, 3));
    }

    /*
     * Check that a simple right side packing works.
     */
    @Test
    public void testPackIntoDirection10() throws Exception {
        testBoard.loadBoard(new int[][]{{3, 1, 0}, {0, 0, 0}, {0, 0, 0}});
        testBoard.packIntoDirection(Board.Direction.RIGHT);
        testBoard.commit();
        Assert.assertNull(testBoard.getTile(1, 1));
        Assert.assertNotNull(testBoard.getTile(1, 2));
        Assert.assertEquals(3, testBoard.getTile(1, 2).getRank());
        Assert.assertNotNull(testBoard.getTile(1, 3));
        Assert.assertEquals(1, testBoard.getTile(1, 3).getRank());
    }

    /*
     * Check that a simple top side packing works.
     */
    @Test
    public void testPackIntoDirection20() throws Exception {
        testBoard.loadBoard(new int[][]{{0, 0, 0}, {0, 0, 0}, {1, 2, 1}});
        testBoard.packIntoDirection(Board.Direction.TOP);
        testBoard.commit();
        Assert.assertNotNull(testBoard.getTile(1, 1));
        Assert.assertEquals(1, testBoard.getTile(1, 1).getRank());
        Assert.assertNotNull(testBoard.getTile(1, 2));
        Assert.assertEquals(2, testBoard.getTile(1, 2).getRank());
        Assert.assertNotNull(testBoard.getTile(1, 3));
        Assert.assertEquals(1, testBoard.getTile(1, 3).getRank());
    }

    /*
     * Check that a simple bottom side packing works.
     */
    @Test
    public void testPackIntoDirection30() throws Exception {
        testBoard.loadBoard(new int[][]{{1, 2, 3}, {0, 0, 0}, {0, 0, 0}});
        testBoard.packIntoDirection(Board.Direction.BOTTOM);
        testBoard.commit();
        Assert.assertNotNull(testBoard.getTile(3, 1));
        Assert.assertEquals(1, testBoard.getTile(3, 1).getRank());
        Assert.assertNotNull(testBoard.getTile(3, 2));
        Assert.assertEquals(2, testBoard.getTile(3, 2).getRank());
        Assert.assertNotNull(testBoard.getTile(3, 3));
        Assert.assertEquals(3, testBoard.getTile(3, 3).getRank());

        // To check the assertEquals ancillary operation
        assertEquals(new int [][] {{0,0,0},{0,0,0},{1,2,3}},testBoard);

    }

    /*
     * Check a fairly complex configuration.
     * Starting from {{1, 2, 3}, {1, 1, 0}, {0,0,3}},
     * should give {{0, 0, 0}, {0,2,3}, {2, 1, 3}}
     */
    @Test
    public void testPackIntoDirection40() throws Exception {
        testBoard.loadBoard(new int[][]{{1, 2, 3}, {1, 1, 0}, {0, 0, 3}});
        testBoard.printBoard(Logger.getGlobal(),"Before");
        testBoard.packIntoDirection(Board.Direction.BOTTOM);
        testBoard.commit();
        assertEquals(new int[][]{{0, 0, 0}, {0, 2, 0}, {2, 1, 4}}, testBoard);

        testBoard.printBoard(Logger.getGlobal(),"After");
    }

    /**
     * Ancillary operation to compare two boards
     * Calls appropriate Junit Assert statements to check that provided is equal to expected
     *
     * @param expected the expected rank matrix
     * @param provided the board to compare with the the one generated from the expected rank matrix
     */
    private void assertEquals(int[][] expected, Board provided) {
        for (int i = 0; i < defaultBoardSize; i++) {
            for (int j = 0; j < defaultBoardSize; j++) {
                if (expected[i][j] != 0) {
                    Assert.assertNotNull(provided.getTile(i + 1, j + 1));
                    Assert.assertEquals(expected[i][j], provided.getTile(i + 1, j + 1).getRank());
                } else {
                    Assert.assertNull(provided.getTile(i + 1, j + 1));
                }
            }
        }

    }
}