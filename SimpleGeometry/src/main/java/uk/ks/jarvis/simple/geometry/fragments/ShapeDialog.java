package uk.ks.jarvis.simple.geometry.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import uk.ks.jarvis.simple.geometry.R;


/**
 * Created by sayenko on 7/26/13.
 */
public class ShapeDialog extends DialogFragment implements View.OnClickListener, TextView.OnEditorActionListener {
    private static Button btnCancel;
    private static TextView btnDelete;
    private static TextView btnColor;
    private static TextView btnUnMergeAll;
    private static TextView btnDisconnectFigure;
    private final BaseHolder baseHolder;
    private String title;
    private ShapeList shapeListWhichContainsTouchedShape;
    private Shape touchedShape;

    public ShapeDialog(BaseHolder baseHolder, ShapeList shapeList, Shape shape) {
        this.baseHolder = baseHolder;
        this.touchedShape = shape;
        title = getTitle();
        this.shapeListWhichContainsTouchedShape = shapeList;
    }

    private String getTitle() {
        if (touchedShape.getClass() == (Line.class)) {
            return "line";
        } else if (touchedShape.getClass() == (Circle.class)) {
            return "circle";
        } else if (touchedShape.getClass() == (Dot.class)) {
            return "dot";
        }
        return "figure";
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        if (shapeListWhichContainsTouchedShape.getShapeArray().size() == 1) {
            view = inflater.inflate(R.layout.shape_options_fragment, container);
        } else {
            view = inflater.inflate(R.layout.merge_shape_options_fragment, container);
            assert view != null;

            btnUnMergeAll = (TextView) view.findViewById(R.id.unmerge_all);
            btnUnMergeAll.setOnClickListener(this);

            btnDisconnectFigure = (TextView) view.findViewById(R.id.disconnect_figure);
            btnDisconnectFigure.setOnClickListener(this);
            btnDisconnectFigure.setText("Disconnect " + title);
        }

        assert view != null;
        btnDelete = (TextView) view.findViewById(R.id.delete_shape);
        btnDelete.setOnClickListener(this);

        btnColor = (TextView) view.findViewById(R.id.change_color);
        btnColor.setOnClickListener(this);

        btnCancel = (Button) view.findViewById(R.id.cancelButton);
        btnCancel.setOnClickListener(this);

        if (shapeListWhichContainsTouchedShape.getShapeArray().size() == 1) {
            title = "Properties of the " + title;
        } else {
            title = "Properties of the figure";
        }

        getDialog().setTitle(title);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnDelete.getId()) {
            baseHolder.removeFirstShape();
            baseHolder.invalidate();
            this.dismiss();
        } else if (view.getId() == btnColor.getId()) {
            shapeListWhichContainsTouchedShape.setRandomColor();
            baseHolder.invalidate();
            this.dismiss();
        } else if (view.getId() == btnCancel.getId()) {
            this.dismiss();
        } else if (shapeListWhichContainsTouchedShape.getShapeArray().size() > 1) {
            if (view.getId() == btnUnMergeAll.getId()) {
                baseHolder.unMergeAllFigures(shapeListWhichContainsTouchedShape);
                baseHolder.invalidate();
                this.dismiss();
            }
            if (view.getId() == btnDisconnectFigure.getId()) {
                baseHolder.disconnectFigure(shapeListWhichContainsTouchedShape, touchedShape);
                baseHolder.invalidate();
                this.dismiss();
            }
        }
    }
}