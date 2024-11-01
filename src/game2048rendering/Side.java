package game2048rendering;
// 通过不断重定向来完成四个方向的转移
public enum Side
{

    NORTH(0, 0, 0, 1),
    EAST(0, 1, 1, 0),
    SOUTH(1, 1, 0, -1),
    WEST(1, 0, -1, 0);

    Side(int col0, int row0, int dcol, int drow)
    {
        this._row0 = row0;
        this._col0 = col0;
        this._drow = drow;
        this._dcol = dcol;
    }

    int x(int x, int y, int size)
    {
        return _col0 * (size - 1) + x * _drow + y * _dcol;
    }
    int y(int x, int y, int size)
    {
        return _row0 * (size - 1) - x * _dcol + y * _drow;
    }

    private final int _row0, _col0, _drow, _dcol;
}
