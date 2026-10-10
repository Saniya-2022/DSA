
class Solution {
    public long minSumSquareDiff(int[] nums1, int[] nums2, int k1, int k2) {

        int n = nums1.length;
        int[] diff = new int[n];

        long k = (long) k1 + k2;
        long total = 0;
        int maxv = 0;

        // Step 1: Calculate differences
        for (int i = 0; i < n; i++) {
            diff[i] = Math.abs(nums1[i] - nums2[i]);
            total += diff[i];
            maxv = Math.max(maxv, diff[i]);
        }

        // Edge case 1: All differences can become zero
        if (total <= k) {
            return 0;
        }

        // Step 2: Binary search for the smallest feasible target
        int low = 0;
        int high = maxv;

        while (low < high) {
            int mid = low + (high - low) / 2;
            long needed = 0;

            for (int d : diff) {
                if (d > mid) {
                    needed += d - mid;
                }
            }

            if (needed <= k) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }

        // Step 3: Calculate squared sum at the target
        int target = low;
        long needed = 0;
        long sum = 0;

        for (int d : diff) {
            if (d > target) {
                needed += d - target;
            }

            long v = Math.min(d, target);
            sum += v * v;
        }

        // Step 4: Use remaining operations
        long rem = k - needed;

        sum -= rem * (2L * target - 1);

        return sum;
    }
}
