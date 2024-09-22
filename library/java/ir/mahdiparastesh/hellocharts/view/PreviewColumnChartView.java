package ir.mahdiparastesh.hellocharts.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.core.view.ViewCompat;

import ir.mahdiparastesh.hellocharts.calculator.PreviewChartCalculator;
import ir.mahdiparastesh.hellocharts.gesture.PreviewChartTouchHandler;
import ir.mahdiparastesh.hellocharts.model.ColumnChartData;
import ir.mahdiparastesh.hellocharts.renderer.PreviewColumnChartRenderer;

public class PreviewColumnChartView extends ColumnChartView {
    protected PreviewColumnChartRenderer previewChartRenderer;

    public PreviewColumnChartView(Context context) {
        this(context, null, 0);
    }

    public PreviewColumnChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PreviewColumnChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        chartCalculator = new PreviewChartCalculator();
        previewChartRenderer = new PreviewColumnChartRenderer(context, this, this);
        touchHandler = new PreviewChartTouchHandler(context, this);
        setChartRenderer(previewChartRenderer);
        setColumnChartData(ColumnChartData.generateDummyData());
    }

    public int getPreviewColor() {
        return previewChartRenderer.getPreviewColor();
    }

    public void setPreviewColor(int color) {
        previewChartRenderer.setPreviewColor(color);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        final int offset = computeHorizontalScrollOffset();
        final int range = computeHorizontalScrollRange() - computeHorizontalScrollExtent();
        if (range == 0) return false;
        if (direction < 0) return offset > 0;
        else return offset < range - 1;
    }
}