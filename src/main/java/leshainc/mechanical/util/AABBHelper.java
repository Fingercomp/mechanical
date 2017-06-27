package leshainc.mechanical.util;

import net.minecraft.util.math.AxisAlignedBB;

public class AABBHelper {
    private static int[] COS = new int[] {1, 0, -1, 0};
    private static int[] SIN = new int[] {0, 1, 0, -1};

    public static AxisAlignedBB rotateY(AxisAlignedBB instance, int angle) {
        int c = COS[angle];
        int s = SIN[angle];

        return new AxisAlignedBB(
                c * (instance.minX - 0.5) + s * (instance.minZ - 0.5) + 0.5,
                instance.minY,
                -s * (instance.minX - 0.5) + c * (instance.minZ - 0.5) + 0.5,
                c * (instance.maxX - 0.5) + s * (instance.maxZ - 0.5) + 0.5,
                instance.maxY,
                -s * (instance.maxX - 0.5) + c * (instance.maxZ - 0.5) + 0.5);
    }
}
