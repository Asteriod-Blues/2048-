package game2048rendering;

import java.util.Arrays;
import java.util.Formatter;

public class Board
{
    // 用一个二维数组来存储面板上的值
    private final Tile[][] _values;
    private Side _viewPerspective;
    // 初始化面板
    public Board(int size)
    {
        _values = new Tile[size][size];
        _viewPerspective = Side.NORTH;
    }
    public void setViewingPerspective(Side s)
    {
        _viewPerspective = s;
    }

    public Board(int[][] rawValues)
    {
        int size = rawValues.length;
        _values = new Tile[size][size];
        _viewPerspective = Side.NORTH;
        for (int x = 0; x < size; x += 1)
        {
            for (int y = 0; y < size; y += 1)
            {
                int value = rawValues[size - 1 - y][x];
                Tile tile;
                if (value == 0)
                {
                    tile = null;
                }
                else
                {
                    tile = Tile.create(value, x, y);
                }
                _values[x][y] = tile;
            }
        }
    }

    // 返回边长
    public int size()
    {
        return _values.length;
    }

    private Tile vtile(int x, int y, Side side)
    {
        return _values[side.x(x, y, size())][side.y(x, y, size())];
    }

    public Tile tile(int x, int y)
    {
        return vtile(x, y, _viewPerspective);
    }

    // 重置
    public void clear()
    {
        for (Tile[] column : _values)
        {
            Arrays.fill(column, null);
        }
    }

    // 在特定位置加入值t
    public void addTile(Tile t) {
        _values[t.x()][t.y()] = t;
    }

    
    public void move(int x, int y, Tile tile)
    {
        int px = _viewPerspective.x(x, y, size());
        int py = _viewPerspective.y(x, y, size());

        Tile tile1 = vtile(x, y, _viewPerspective);
        _values[tile.x()][tile.y()] = null;

        Tile next;
        if (tile1 == null)
        {
            next = Tile.create(tile.value(), px, py);
        }
        else
        {
            if (tile.value() != tile1.value())
            {
                throw new IllegalArgumentException("Tried to merge two unequal tiles: " + tile + " and " + tile1);
            }
            next = Tile.create(2 * tile.value(), px, py);
            tile1.setNext(next);
        }
        tile.setMerged(tile1 != null);
        next.setMerged(tile.wasMerged());
        tile.setNext(next);
        _values[px][py] = next;
    }

    public void resetMerged()
    {
        for (int x = 0; x < size(); x += 1)
        {
            for (int y = 0; y < size(); y += 1)
            {
                if (_values[x][y] != null)
                {
                    _values[x][y].setMerged(false);
                }
            }
        }
    }

    // 便于debug
    @Override
    public String toString()
    {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int y = size() - 1; y >= 0; y -= 1)
        {
            for (int x = 0; x < size(); x += 1)
            {
                if (tile(x, y) == null)
                {
                    out.format("|    ");
                }
                else
                {
                    out.format("|%4d", tile(x, y).value());
                }
            }
            out.format("|%n");
        }
        return out.toString();
    }
}
