package millo.millomod2.menu;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ContainerElement extends ClickableWidget implements FadeElement {

    private final ArrayList<ClickableWidget> children = new ArrayList<>();

    public ContainerElement(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    public List<ClickableWidget> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public void addChild(ClickableWidget child) {
        if (child instanceof FadeElement fadeChild) fadeChild.getFade().lock(getFade());
        if (children.add(child)) childrenUpdated();
    }

    public void removeChild(ClickableWidget child) {
        if (children.remove(child)) {
            if (child instanceof FadeElement fadeChild) fadeChild.getFade().unlock();
            childrenUpdated();
        }
    }

    public void clearChildren() {
        if (children.isEmpty()) return;
        children.clear();
        childrenUpdated();
    }

    protected void childrenUpdated() {}

    //

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (!active || !visible) return false;
        if (!isValidClickButton(click.buttonInfo())) return false;
        if (!isMouseOver(click.x(), click.y())) return false;

        for (ClickableWidget child : getChildren()) {
            if (child.mouseClicked(click, doubled)) {
                if (MinecraftClient.getInstance().currentScreen != null) {
                    MinecraftClient.getInstance().currentScreen.setFocused(child);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        getFade().progress(deltaTicks);
        context.getMatrices().pushMatrix();
        getFade().applyTranslation(context.getMatrices());
        context.enableScissor(getX(), getY(), getRight(), getBottom());

        for (ClickableWidget child : getChildren()) {
            child.render(context, mouseX, mouseY, deltaTicks);
        }

        context.disableScissor();
        context.getMatrices().pushMatrix();
    }

    private final Fade fade = new Fade(Fade.Direction.UP);
    @Override
    public Fade getFade() {
        return fade;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    public <T extends ClickableWidget> void addChildren(List<T> children) {
        for (T child : children) {
            addChild(child);
        }
    }
}
