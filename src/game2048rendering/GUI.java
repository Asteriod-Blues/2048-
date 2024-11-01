package game2048rendering;

import game2048logic.Model;
import ucb.gui2.TopLevel;
import ucb.gui2.LayoutSpec;

import java.util.concurrent.ArrayBlockingQueue;

import java.awt.event.KeyEvent;


class GUI extends TopLevel
{

    GUI(String title, Model model)
    {
        super(title, true);
        addMenuButton("Game->New", this::newGame);
        addMenuButton("Game->Quit", this::quit);

        addLabel("", "Score", new LayoutSpec("y", 1));

        _model = model;

        _widget = new BoardWidget(model.size());
        add(_widget,
            new LayoutSpec("y", 0,
                           "height", "REMAINDER",
                           "width", "REMAINDER"));

        _widget.requestFocusInWindow();
        _widget.setKeyHandler("keypress", this::keyPressed);
        setPreferredFocus(_widget);
        setScore(0);
    }

    private void quit(String dummy)
    {
        _pendingKeys.offer("Quit");
        _widget.requestFocusInWindow();
    }

    private void newGame(String dummy)
    {
        _pendingKeys.offer("New Game");
        _widget.requestFocusInWindow();
    }

    private void keyPressed(String unused, KeyEvent e) {
        _pendingKeys.offer(e.getKeyCode() + "");
    }

    private String readKey()
    {
        try
        {
            return _pendingKeys.take();
        }
        catch (InterruptedException excp)
        {
            throw new Error("unexpected interrupt");
        }
    }

    String getKey()
    {
        String command = readKey();
        switch (command)
        {
            case "↑" -> command = "Up";
            case "→" -> command = "Right";
            case "↓" -> command = "Down";
            case "←" -> command = "Left";
            default -> {}
        }

        return command;
    }

    private void setScore(int score) {
        setLabel("Score", String.format("Score: %6d", score));
    }

    void update()
    {
        _widget.update(_model);
        setScore(_model.score());
    }

    private final BoardWidget _widget;

    private final Model _model;

    private final ArrayBlockingQueue<String> _pendingKeys =
        new ArrayBlockingQueue<>(5);

}
