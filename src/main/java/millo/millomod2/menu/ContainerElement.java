package millo.millomod2.menu;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.impl.Debug;
import millo.millomod2.menu.elements.ClickableElement;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ContainerElement<T extends ContainerElement<?>> extends ClickableElement<T> {

    private ClickableWidget focus;
    private final ArrayList<ClickableWidget> children = new ArrayList<>();

    public ContainerElement(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    public List<ClickableWidget> getChildren() {
        return List.copyOf(children);
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

    public void layoutChildren() {}
    //

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (!active || !visible) return false;
        if (!isMouseOver(click.x(), click.y())) return false;

        if (focus != null) focus.setFocused(false);
        focus = null;

        click = transformClickToLocal(click);
        for (ClickableWidget child : getChildren()) {
            if (child.mouseClicked(click, doubled)) {
                focus = child;
                focus.setFocused(true);
                return true;
            }
        }
        return false;
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (!focused && focus != null) focus.setFocused(false);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (focus != null) return focus.keyPressed(input);
        return false;
    }

    @Override
    public boolean keyReleased(KeyInput input) {
        if (focus != null) return focus.keyReleased(input);
        return false;
    }

    @Override
    public boolean charTyped(CharInput input) {
        if (focus != null) {
            return focus.charTyped(input);
        }
        return false;
    }

    @Override
    public boolean mouseReleased(Click click) {
        if (!this.isValidClickButton(click.buttonInfo())) return false;
        for (ClickableWidget child : getChildren()) {
            child.mouseReleased(transformClickToLocal(click));
        }
        return false;
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        Click local = transformClickToLocal(click);
        if (focus != null) return focus.mouseDragged(local, offsetX, offsetY);

        for (ClickableWidget child : getChildren()) {
            child.mouseDragged(local, offsetX, offsetY);
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        for (ClickableWidget child : getChildren()) {
            if (child.mouseScrolled(mouseX - getX(), mouseY - getY(), horizontalAmount, verticalAmount)) {
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
        super.renderWidget(context, mouseX, mouseY, deltaTicks);
        context.getMatrices().translate(getX(), getY());
        context.enableScissor(0, 0, getWidth(), getHeight());

        if (Debug.showHudInfo()) {
            int nameColor = getClass().getSimpleName().hashCode() | 0x33000000;
            context.fill(0, 0, getWidth(), getHeight(), nameColor);
            context.drawText(MilloMod.MC.textRenderer, getClass().getSimpleName(), 0, 0, 0xFFFFFFFF, false);
            context.drawText(MilloMod.MC.textRenderer, "x:" + getX() + " y:" + getY() + " w:" + getWidth() + " h:" + getHeight(), 0, 10, 0xFFFFFFFF, false);
        }

        RenderArgs args = new RenderArgs(context, mouseX - getX(), mouseY - getY(), deltaTicks);
        renderElement(args);

        context.disableScissor();
        context.getMatrices().popMatrix();
    }

    protected void renderElement(RenderArgs args) {
        renderChildren(args);
    }

    protected void renderChildren(RenderArgs args) {
        for (ClickableWidget child : List.copyOf(getChildren())) {
            child.render(args.context, args.mouseX, args.mouseY, args.deltaTicks);
        }
    }

    protected Click transformClickToLocal(Click click) {
        return new Click(click.x() - getX(), click.y() - getY(), click.buttonInfo());
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }

    public <K extends ClickableWidget> void addChildren(Collection<K> children) {
        for (K child : children) {
            addChild(child);
        }
    }

    public void addChildren(ClickableWidget... children) {
        for (ClickableWidget child : children) {
            addChild(child);
        }
    }

    protected record RenderArgs(DrawContext context, int mouseX, int mouseY, float deltaTicks) {}
}
