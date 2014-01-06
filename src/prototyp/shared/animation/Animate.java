package prototyp.shared.animation;

import org.vaadin.gwtgraphics.client.animation.Animatable;

import com.google.gwt.animation.client.Animation;

public class Animate {
	private Animatable target;

	private String property;

	private double startValue;

	private double endValue;

	private int duration;

	private Animation animation = new Animation() {

		@Override
		protected void onComplete() {
			Animate.this.onComplete();

		}

		@Override
		protected void onUpdate(double progress) {
			double value = (Animate.this.endValue - Animate.this.startValue)
					* progress + Animate.this.startValue;
			Animate.this.target.setPropertyDouble(Animate.this.property, value);
		}
	};

	public Animate(Animatable target, String property, double startValue,
			double endValue, int duration) {
		this.target = target;
		this.property = property;
		this.startValue = startValue;
		this.endValue = endValue;
		this.duration = duration;
	}

	public int getDuration() {
		return this.duration;
	}

	public double getEndValue() {
		return this.endValue;
	}

	public String getProperty() {
		return this.property;
	}

	public double getStartValue() {
		return this.startValue;
	}

	public Animatable getTarget() {
		return this.target;
	}

	public void onComplete() {
	}

	public void onStart() {
	}

	/**
	 * Start the animation.
	 */
	public void start() {
		onStart();
		this.animation.run(this.duration);
	}

	/**
	 * Stop the animation.
	 */
	public void stop() {
		this.animation.cancel();
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	

}
