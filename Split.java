public class Split {
    private double threshold;
    private String feature;

    public Split(String feature, double threshold) {
        this.threshold = threshold;
        this.feature = feature;
    }

    public double getThreshold() {
        return threshold;
    }

    public String getFeature() {
        return feature;
    }

    public String getBaseFeature() {
        return feature.split(Classifiable.SPLITTER)[1];
    }

    public boolean evaluate(Classifiable value) {
        return value.get(this.getBaseFeature()) < this.threshold;
    }

    @Override
    public String toString() {
        return "Feature: " + this.feature + "\n" + "Threshold: " + this.threshold;
    }

    public static double midpoint(double one, double two) {
        return Math.min(one, two) + (Math.abs(one - two) / 2.0);
    }
}
