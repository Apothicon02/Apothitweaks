package com.Apothic0n.Apothitweaks.core;

public class ApothitweaksMath {
    public static float invLerp(float value, float scale, float min, float max) {
        return (value - min) / (max - min)*scale;
    }
    public static double getMiddleDouble(double origin, double target) {
        double min = Math.min(origin, target);
        double max = Math.max(origin, target);
        return ((max-min)/2)+min;
    }
    public static double getOffsetDouble(double origin, double target) {
        double mid = getMiddleDouble(origin, target);
        double offset =  origin+1;
        if (origin > target) {
            offset = origin-0.5;
        }
        if (Math.abs(mid-origin) < Math.abs(offset-origin)) {
            return mid;
        } else {
            return offset;
        }
    }
}
