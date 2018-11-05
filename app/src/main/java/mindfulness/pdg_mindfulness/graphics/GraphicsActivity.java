package mindfulness.pdg_mindfulness.graphics;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import mindfulness.pdg_mindfulness.R;

public class GraphicsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics);
        GraphView graph = (GraphView) findViewById(R.id.graphic);
        graph.setTitle("Nivel de estrés en el tratamiento");
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Semana");
        gridLabel.setVerticalAxisTitle("Puntaje");
        Intent intent = getIntent();
        String score = intent.getExtras().getString("score");
        // activate horizontal scrolling
        graph.getViewport().setScrollable(true);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {

                new DataPoint(0, 0),
                new DataPoint(1, Integer.parseInt(score)),
                new DataPoint(2, 5),
                new DataPoint(3, 5),
                new DataPoint(4, 5),
                new DataPoint(5, 5),
                new DataPoint(6, 5),
                new DataPoint(7, 5),
                new DataPoint(8, 0)
        });
        graph.addSeries(series);

        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(9);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(56);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);

    }

}