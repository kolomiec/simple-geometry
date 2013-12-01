package uk.ks.jarvis.simple.geometry.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import uk.ks.jarvis.simple.geometry.R;
import uk.ks.jarvis.simple.geometry.beans.Point;
import uk.ks.jarvis.simple.geometry.holders.BaseHolder;
import uk.ks.jarvis.simple.geometry.shapes.Circle;
import uk.ks.jarvis.simple.geometry.shapes.Dot;
import uk.ks.jarvis.simple.geometry.shapes.Line;
import uk.ks.jarvis.simple.geometry.utils.LettersGenerator;

import static uk.ks.jarvis.simple.geometry.R.id.create_dot;


/**
 * Created by root on 7/28/13.
 */
public class CreateFigureDialog extends DialogFragment implements View.OnClickListener {

    private static Button btnCancel;
    private final BaseHolder baseHolder;
    private View view;
    private TextView dotButton;
    private TextView lineButton;
    private Dot dot;
    private TextView circleButton;

    public CreateFigureDialog(BaseHolder baseHolder, Dot dot) {
        super();
        this.baseHolder = baseHolder;
        this.dot = dot;
    }

//    public CreateFigureDialog(BaseHolder baseHolder) {
//        super();
//        this.baseHolder = baseHolder;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_figure_dialog, container);

        getDialog().setTitle("Select the figure to create");
        setupButtons();
        return view;
    }

    private void setupButtons() {
        dotButton = (TextView) view.findViewById(create_dot);
        dotButton.setOnClickListener(this);

        circleButton = (TextView) view.findViewById(R.id.create_circle);
        circleButton.setOnClickListener(this);

        lineButton = (TextView) view.findViewById(R.id.create_line);
        lineButton.setOnClickListener(this);

        btnCancel = (Button) view.findViewById(R.id.cancel_button);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (dotButton.getId() == view.getId()) {
            Dot dot = new Dot(new Point(0f, 0f), LettersGenerator.getInstance().getNextUpperCaseName());
            baseHolder.setCreateFigureMode(dot);
            this.dismiss();

        } else if (circleButton.getId() == view.getId()) {
            Circle circle = new Circle(1f, dot);
            baseHolder.setCreateFigureMode(circle);
            this.dismiss();

        } else if (lineButton.getId() == view.getId()) {
            Line line = new Line(dot, 1f, LettersGenerator.getInstance().getNextLowCaseName());
            baseHolder.setCreateFigureMode(line);
            this.dismiss();

        } else if (view.getId() == btnCancel.getId()) {
            this.dismiss();
        }
    }
}