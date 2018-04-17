package entities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GameChangeListener implements PropertyChangeListener {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("game modified");



    }
}
