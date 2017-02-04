package solstudios.app.anything.customizeViews;

import java.util.Arrays;

class AverageValueFilter {

    private float[] lastValues = new float[10];
    private boolean ready;
    private int index;
    private boolean waitForReadiness = false;

    AverageValueFilter(int size, boolean waitForReadiness) {
        lastValues = new float[size];
        this.waitForReadiness = waitForReadiness;
    }

    private boolean isReady() {
        return ready;
    }

    float handle(float value) {

        if (waitForReadiness && !ready) {
            fillLastValues(value);
            value = 0;
        } else {
            lastValues[index++] = value;
            value = getAverage();
            if (index == lastValues.length) index = 0;
        }

        return value;
    }

    private void fillLastValues(float value) {
        lastValues[index++] = value;
        if (index == lastValues.length) {
            ready = true;
            index = 0;
        }
    }

    private float getAverage() {
        float result = 0;
        for (float lastValue : lastValues) result += lastValue;
        return result / lastValues.length;
    }

    @Override
    public String toString() {
        return "isReady:" + isReady() + " avg:" + getAverage() + ":" + Arrays.toString(lastValues);
    }
}