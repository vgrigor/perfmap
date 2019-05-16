package maps;

import java.util.Arrays;

public class FastObjIntMap<T> {
    private static final int MAGIC = 0xB46394CD;
    private static final int MAX_SHIFT = 29;
    private static final int THRESHOLD = (int)((1L << 32) * 0.5); // 50% fill factor for speed

    private static class Core {
        final int shift;
        final int length;
        final Object[] keys;
        final int[] values;

        Core(int shift) {
            this.shift = shift;
            length = 1 << (32 - shift);
            keys = new Object[length];
            values = new int[length];
        }
    }

    private volatile Core core = new Core(MAX_SHIFT);
    private volatile int size;

    // does not need external synchronization
    public int get(T key) {
        return get(key, 0);
    }

    // does not need external synchronization
    public int get(T key, int defaultValue) {
        Core core = this.core; // atomic read;
        int i = (key.hashCode() * MAGIC) >>> core.shift;
        Object k;
        while (key != (k = core.keys[i])) {
            if (k == null)
                return defaultValue;
            if (i == 0)
                i = core.length;
            i--;
        }
        return core.values[i];
    }

    // needs external synchronization
    public void put(T key, int value) {
        if (putInternal(this.core, key, value))
            if (++size >= (THRESHOLD >>> core.shift))
                rehash();
    }

    private boolean putInternal(Core core, Object key, int value) {
        int i = (key.hashCode() * MAGIC) >>> core.shift;
        Object k;
        while (key != (k = core.keys[i])) {
            if (k == null) {
                core.keys[i] = key;
                core.values[i] = value;
                return true;
            }
            if (i == 0)
                i = core.length;
            i--;
        }
        core.values[i] = value;
        return false;
    }

    private void rehash() {
        Core oldCore = core;
        Core newCore = new Core(oldCore.shift - 1);
        for (int i = 0; i < oldCore.length; i++)
            if (oldCore.keys[i] != null)
                putInternal(newCore, oldCore.keys[i], oldCore.values[i]);
        core = newCore;
    }

    public void fill(int value) {
        Arrays.fill(core.values, value);
    }
}
