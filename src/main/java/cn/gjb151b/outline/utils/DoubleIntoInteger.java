package cn.gjb151b.outline.utils;

public class DoubleIntoInteger {
    public static String[] doubleIntoInteger(double[] nums) {
        final int K = 1000;
        final int M = 1000000;
        final int G = 1000000000;
        if (nums == null) return new String[2];
        String[] res = new String[nums.length];
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] < K) {
                res[i] = String.valueOf((int)nums[i]);
            } else if (nums[i] >= K && nums[i] < M) {
                if (nums[i] == nums[i] / K * K) {
                    res[i] = String.valueOf((int)nums[i] / K) + "K";
                } else {
                    res[i] = String.valueOf(nums[i] / K) + "K";
                }
            } else if (nums[i] >= M && nums[i] < G) {
                if (nums[i] == nums[i] / M * M) {
                    res[i] = String.valueOf((int)(nums[i] / M)) + "M";
                } else {
                    res[i] = String.valueOf(nums[i] / M) + "M";
                }
            } else if (nums[i] >= G) {
                if (nums[i] == nums[i] / G * G) {
                    res[i] = String.valueOf((int)(nums[i] / G)) + "G";
                } else {
                    res[i] = String.valueOf(nums[i] / G) + "G";
                }
            }
        }
        return res;
    }
}
