package lecho.lib.hellocharts.model;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Color;

public class Axis {
	private static final int DEFAULT_AXIS_TEXT_SIZE_SP = 10;
	private static final int DEFAULT_AXIS_COLOR = Color.LTGRAY;
	private List<AxisValue> values = new ArrayList<AxisValue>();
	private String name;
	private int color = DEFAULT_AXIS_COLOR;
	private int textSize = DEFAULT_AXIS_TEXT_SIZE_SP;
	private AxisValueFormatter formatter = new DefaultAxisValueFormatter();

	public Axis() {

	}

	public Axis(List<AxisValue> values) {
		this.values = values;
	}

	public List<AxisValue> getValues() {
		return values;
	}

	public Axis setValues(List<AxisValue> values) {
		this.values = values;
		return this;
	}

	public String getName() {
		return name;
	}

	public Axis setName(String name) {
		this.name = name;
		return this;
	}

	public int getColor() {
		return color;
	}

	public Axis setColor(int color) {
		this.color = color;
		return this;
	}

	public int getTextSize() {
		return textSize;
	}

	public Axis setTextSize(int textSize) {
		this.textSize = textSize;
		return this;
	}

	public AxisValueFormatter getFormatter() {
		return formatter;
	}

	public Axis setFormatter(AxisValueFormatter formatter) {
		if (null == formatter) {
			this.formatter = new DefaultAxisValueFormatter();
		} else {
			this.formatter = formatter;
		}
		return this;
	}

	public interface AxisValueFormatter {
		public static final String DEFAULT_AXES_FORMAT = "%.0f";

		public String formatValue(AxisValue value);
	}

	@SuppressLint("DefaultLocale")
	public static class DefaultAxisValueFormatter implements AxisValueFormatter {

		@Override
		public String formatValue(AxisValue axisValue) {
			return String.format(DEFAULT_AXES_FORMAT, axisValue.getValue());
		}

	}
}
