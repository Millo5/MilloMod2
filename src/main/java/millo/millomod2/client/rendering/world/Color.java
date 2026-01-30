package millo.millomod2.client.rendering.world;

public record Color(float[] components) {
    public Color(float r, float g, float b) {
        this(new float[]{r, g, b, 1.0f});
    }
    public Color(float r, float g, float b, float a) {
        this(new float[]{r, g, b, a});
    }

    public float r() {
        return components[0];
    }
    public float g() {
        return components[1];
    }
    public float b() {
        return components[2];
    }
    public float a() {
        return components[3];
    }

    public int argb() {
        int a = (int) (a() * 255) & 0xFF;
        int r = (int) (r() * 255) & 0xFF;
        int g = (int) (g() * 255) & 0xFF;
        int b = (int) (b() * 255) & 0xFF;
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
