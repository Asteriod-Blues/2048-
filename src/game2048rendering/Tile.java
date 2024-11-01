package game2048rendering;

public class Tile
{

    private Tile(int value, int x, int y)
    {
        this._value = value;
        this._x = x;
        this._y = y;
        this._next = null;
        this._merged = false;
    }
    public boolean wasMerged() {
        return _merged;
    }

    void setMerged(boolean merged) {
        this._merged = merged;
    }

    int y() {
        return _y;
    }

    int x() {
        return _x;
    }

    public int value() {
        return _value;
    }

    Tile next() {
        return _next == null ? this : _next;
    }

    void setNext(Tile otherTile) {
        _next = otherTile;
    }

    public static Tile create(int value, int x, int y)
    {
        return new Tile(value, x, y);
    }

    int distToNext()
    {
        if (_next == null)
        {
            return 0;
        }
        else
        {
            return Math.max(Math.abs(_y - _next.y()),
                            Math.abs(_x - _next.x()));
        }
    }

    @Override
    public String toString() {
        return String.format("Tile %d at position (%d, %d)", value(), x(), y());
    }

    private final int _value;

    private final int _x;
    private final int _y;

    private boolean _merged;

    private Tile _next;
}
