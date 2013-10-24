package uk.ks.jarvis.simple.geometry.holders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ks.jarvis.simple.geometry.shapes.Shape;

/**
 * Created by oem on 9/14/13.
 */
public class HistoryHolder {

    private Map<Integer, List<Shape>> history = new HashMap<Integer, List<Shape>>();
    private int historyPosition = 0;

    public void addShapeToHistory(Shape shape) {
        List currentState = null;
        if (history.isEmpty()) {
            currentState = new ArrayList<Shape>();
        } else {
            currentState = history.get(history.size());
        }
        currentState.add(shape);
        history.put(history.size()+1, currentState);
    }

    public List<Shape> getCurrentHistoryState() {
        List result = new ArrayList<Shape>();

        ArrayList<Integer> keys = new ArrayList<Integer>(history.keySet());
        for(int i = keys.size()-1 - historyPosition; i >= 0; i--){
            for(Shape shape: history.get(keys.get(i))){
                if (!contains(result, shape)) {
                    result.add(shape);
                }
            }
        }

        return result;
    }

    private boolean contains(List<Shape> shapes, Shape shape) {
        for (Shape currentShape: shapes) {
            if (currentShape.equals(shape)) {
                return true;
            }
        }
        return false;
    }
}
