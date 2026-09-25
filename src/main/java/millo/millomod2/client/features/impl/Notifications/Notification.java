package millo.millomod2.client.features.impl.Notifications;

import net.minecraft.network.chat.Component;

public final class Notification {
    private final Component message;
    private float age = 0f;
    private final float lifetime = 5f;
    private float visible = 0.1f;

    public Notification(Component message) {
        this.message = message;
    }

    public Component getMessage() {
        return message;
    }

    public boolean shouldRemove() {
        return age >= lifetime && visible <= 0.05f;
    }

    public boolean isFadingOut() {
        return age >= lifetime;
    }

    public float getVisible() {
        return visible;
    }

    public void setVisible(float visible) {
        this.visible = visible;
    }

    public void age(float time) {
        age += time;
    }

    public float getProgress() {
        return Math.min(age / lifetime, 1f);
    }
}
