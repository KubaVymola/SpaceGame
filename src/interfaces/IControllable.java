package interfaces;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;

public interface IControllable {
    public void control(ArrayList<KeyCode> downKeys);
    public void oneTimeAction(KeyCode pressedKey);
}
