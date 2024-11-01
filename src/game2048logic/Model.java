package game2048logic;

import game2048rendering.Board;
import game2048rendering.Side;
import game2048rendering.Tile;

import java.util.Formatter;


public class Model
{
    private final Board board;
    // 现在分数
    private int score;


    // 单个元素最大值
    public static final int MAX_PIECE = 2048;

    // 构造函数进行初始化
    public Model(int size)
    {
        board = new Board(size);
        score = 0;
    }

    public Model(int[][] rawValues, int score)
    {
        board = new Board(rawValues);
        this.score = score;
    }

    public Tile tile(int x, int y)
    {
        return board.tile(x, y);
    }

    // 返回边长
    public int size()
    {
        return board.size();
    }

    // 返回现在的分数
    public int score()
    {
        return score;
    }


    // 重置清0
    public void clear()
    {
        score = 0;
        board.clear();
    }

    public void addTile(Tile tile) {
        board.addTile(tile);
    }

    // 游戏结束标志，达到最大值或者无法移动
    public boolean gameOver()
    {
        return maxTileExists() || !atLeastOneMoveExists();
    }

    // 返回当前面板
    public Board getBoard() {
        return board;
    }

    public boolean emptySpaceExists()
    {
        int s = size();
        for (int x = 0;x < s;++x)
        {
           for (int y = 0; y < s;++y)
            {
                if (tile(x,y) == null )
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean maxTileExists()
    {
        for(int x = 0;x < size();++x)
        {
            for(int y = 0;y < size();++y)
            {
               if(tile(x,y) != null)
                    if(tile(x,y).value() == MAX_PIECE) return true;
            }
        }
        return false;
    }

    public boolean atLeastOneMoveExists()
    {
        if (emptySpaceExists()) return true;
        for(int x = 0;x < size();++x)
        {
            for(int y = 0;y < size();++y)
            {
                if(x - 1 >= 0 && tile(x - 1,y).value() == tile(x,y).value()) return true;
                if(x + 1 < size() && tile(x + 1,y).value() == tile(x,y).value()) return true;
                if(y - 1 >= 0 && tile(x,y - 1).value() == tile(x,y).value()) return true;
                if(y + 1 < size() && tile(x,y + 1).value() == tile(x,y).value()) return true;
            }
        }
        return false;
    }
    public void moveTileUpAsFarAsPossible(int x, int y)
    {
        Tile currTile = board.tile(x, y);
        int myValue = currTile.value();
        int targetY = y;
        int s = size();
        for(int t = targetY + 1;t < s;++t)
        {
            if(t == s - 1 && tile(x,t) == null)
            {
                board.move(x,t,currTile);
            }
            else if(tile(x,t) != null)
            {
                if(tile(x,t).value() == myValue && !tile(x,t).wasMerged())
                {
                    score += 2 * myValue;
                    board.move(x,t,currTile);
                    break;
                }
                else
                {
                    if (t == targetY + 1) break;
                    if ( tile(x,t - 1) == null)   board.move(x,t - 1,currTile);
                }

            }
            if(tile(x,y) != null && tile(x,y).wasMerged()) break;
        }
    }

    public void tiltColumn(int x)
    {
        for (int y = size() - 1;y >=0;--y)
        {
            if (board.tile(x,y) != null) moveTileUpAsFarAsPossible(x,y);
        }
    }

    public void tilt(Side side)
    {
        board.setViewingPerspective(side);
        for (int x = 0;x < size();++x)
        {
            tiltColumn(x);
        }
        board.setViewingPerspective(Side.NORTH);
    }

    public void tiltWrapper(Side side)
    {
        board.resetMerged();
        tilt(side);
    }


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
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (game is %s) %n", score(), over);
        return out.toString();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Model m) && this.toString().equals(m.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
