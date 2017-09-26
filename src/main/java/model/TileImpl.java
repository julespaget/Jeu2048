package model;

/**
 * Created by plouzeau on 2014-10-09.
 */
public class TileImpl implements Tile  {

    private  int rank;

    public TileImpl(int rank) {
        this.rank = rank;
    }

    @Override
    public int getRank() {
        return this.rank;
    }

    @Override
    public void incrementRank() {
        this.rank++;
    }
}
