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
import uk.ks.jarvis.simple.geometry.holders.BaseHolder;
import uk.ks.jarvis.simple.geometry.shapes.Dot;
import uk.ks.jarvis.simple.geometry.shapes.Shape;
import uk.ks.jarvis.simple.geometry.shapes.ShapeList;


/**
 * Created by sayenko on 7/26/13.
 */
public class ShapeDialog extends DialogFragment implements View.OnClickListener, TextView.OnEditorActionListener {
    private static Button btnCancel;
    private static TextView btnDelete;
    private static TextView btnColor;
    private static TextView createFigure;
    private final BaseHolder baseHolder;
    private ShapeList shapeListWhichContainsTouchedShape;
    private Shape touchedShape;

    public ShapeDialog(BaseHolder baseHolder, ShapeList shapeList, Shape shape) {
        this.baseHolder = baseHolder;
        this.touchedShape = shape;
        this.shapeListWhichContainsTouchedShape = shapeList;
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        if (touchedShape.getClass() != Dot.class) {
            view = inflater.inflate(R.layout.shape_options_fragment, container);
        } else {
            view = inflater.inflate(R.layout.dot_options_fragment, container);
            assert view != null;

            createFigure = (TextView) view.findViewById(R.id.create_figure);
            createFigure.setOnClickListener(this);
        }

        assert view != null;
        btnDelete = (TextView) view.findViewById(R.id.delete_shape);
        btnDelete.setOnClickListener(this);

        btnColor = (TextView) view.findViewById(R.id.change_color);
        btnColor.setOnClickListener(this);

        btnCancel = (Button) view.findViewById(R.id.cancelButton);
        btnCancel.setOnClickListener(this);

        String title = "Properties";
        getDialog().setTitle(title);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnDelete.getId()) {
            baseHolder.getShapelist().removeAll();
            baseHolder.invalidate();
            this.dismiss();
        } else if (view.getId() == btnColor.getId()) {
            shapeListWhichContainsTouchedShape.setRandomColor();
            baseHolder.invalidate();
            this.dismiss();
        } else if (view.getId() == btnCancel.getId()) {
            this.dismiss();
        } else if (touchedShape.getClass() == Dot.class) {
            if (view.getId() == createFigure.getId()) {
                CreateFigureDialog createFigureDialog = new CreateFigureDialog(baseHolder, (Dot) touchedShape);
                createFigureDialog.show(baseHolder.getActivity().getSupportFragmentManager(), "");
                this.dismiss();
            }
        }
    }
}