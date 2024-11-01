package game2048rendering;

import game2048logic.Model;

import java.awt.event.KeyEvent;
import java.util.Random;

import static game2048rendering.Side.*;

class Game {


    public Game(Model model, GUI gui, double tile2p, long seed)
    {
        _model = model;
        _playing = true;
        _gui = gui;
        _probOf2 = tile2p;

        if (seed == 0)
        {
            _random = new Random();
        }
        else
        {
            _random = new Random(seed);
        }
    }

    boolean playing() {
        return _playing;
    }

    void playGame(boolean hotStart)
    {

        if (!hotStart)
        {
            _model.clear();
            _model.addTile(getValidNewTile());
        }
        while (_playing)
        {
            if (!hotStart)
            {
                if (!_model.gameOver())
                {
                    _model.addTile(getValidNewTile());
                    _gui.update();
                }
            }
            if (hotStart)
            {
                _gui.update();
                hotStart = false;
            }

            boolean moved;
            moved = false;
            while (!moved)
            {
                String cmnd = _gui.getKey();
                switch (cmnd)
                {
                    case "Quit":
                        _playing = false;
                        return;
                    case "New Game":
                        return;
                    case KeyEvent.VK_UP + "": case KeyEvent.VK_DOWN + "": case KeyEvent.VK_LEFT + "": case KeyEvent.VK_RIGHT+ "":
                    case "\u2190": case "\u2191": case "\u2192": case "\u2193":
                        if (!_model.gameOver())
                        {
                            _gui.update();
                            moved = false;
                        }

                        String stateBefore = _model.toString();
                        _model.tiltWrapper(keyToSide(cmnd));
                        String stateAfter = _model.toString();

                        if (!stateBefore.equals(stateAfter))
                        {
                            _gui.update();
                            moved = true;
                        }

                        break;
                    default:
                        break;
                }

            }
        }
    }

    private Side keyToSide(String key)
    {
        return switch (key)
        {
            case KeyEvent.VK_UP + "", "\u2191" -> NORTH;
            case KeyEvent.VK_DOWN + "", "\u2193" -> SOUTH;
            case KeyEvent.VK_LEFT + "", "\u2190" -> WEST;
            case KeyEvent.VK_RIGHT+ "", "\u2192" -> EAST;
            default -> throw new IllegalArgumentException("unknown key designation");
        };
    }

    private Tile getValidNewTile()
    {
        while (true)
        {
            Tile tile = generateNewTile(_model.size());
            if (_model.tile(tile.x(), tile.y()) == null)
            {
                return tile;
            }
        }
    }

    private Tile generateNewTile(int size)
    {
        int c = _random.nextInt(size), r = _random.nextInt(size);
        int v = _random.nextDouble() <= _probOf2 ? 2 : 4;

        return Tile.create(v, c, r);
    }

    private final Model _model;

    private final GUI _gui;

    private final double _probOf2;

    private final Random _random;

    private boolean _playing;

}
