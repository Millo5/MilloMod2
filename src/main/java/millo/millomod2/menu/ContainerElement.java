package millo.millomod2.menu;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.impl.Debug;
import millo.millomod2.client.util.logging.MilloLog;
import millo.millomod2.menu.elements.ClickableElement;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ContainerElement<T extends ContainerElement<?>> extends ClickableElement<T> {

    private AbstractWidget focus;
    private final ArrayList<AbstractWidget> children = new ArrayList<>();

    public ContainerElement(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    public List<AbstractWidget> getChildren() {
        return List.copyOf(children);
    }

    public void addChild(AbstractWidget child) {
        if (child instanceof FadeElement fadeChild) fadeChild.getFade().lock(getFade());
        if (children.add(child)) childrenUpdated();
    }

    public void removeChild(AbstractWidget child) {
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
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        if (!active || !visible) return false;
        if (!isMouseOver(click.x(), click.y())) return false;

        if (focus != null) focus.setFocused(false);
        focus = null;

        click = transformClickToLocal(click);
        for (AbstractWidget child : getChildren()) {
            if (child.mouseClicked(click, doubled)) {
                focus = child;
                focus.setFocused(true);
                return true;
            }
        }
        return false;
    }

    public void setFocus(AbstractWidget widget) {
        if (focus != null) focus.setFocused(false);
        focus = widget;
        if (focus != null) focus.setFocused(true);
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (!focused && focus != null) focus.setFocused(false);
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
        if (focus != null) return focus.keyPressed(input);
        return false;
    }

    @Override
    public boolean keyReleased(KeyEvent input) {
        if (focus != null) return focus.keyReleased(input);
        return false;
    }

    @Override
    public boolean charTyped(CharacterEvent input) {
        if (focus != null) {
            return focus.charTyped(input);
        }
        return false;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent click) {
        for (AbstractWidget child : getChildren()) {
            child.mouseReleased(transformClickToLocal(click));
        }
        return false;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent click, double offsetX, double offsetY) {
        MouseButtonEvent local = transformClickToLocal(click);
        if (focus != null) return focus.mouseDragged(local, offsetX, offsetY);

        for (AbstractWidget child : getChildren()) {
            child.mouseDragged(local, offsetX, offsetY);
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        for (AbstractWidget child : getChildren()) {
            if (child.mouseScrolled(mouseX - getX(), mouseY - getY(), horizontalAmount, verticalAmount)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float deltaTicks) {
        getFade().progress(deltaTicks);
        graphics.pose().pushMatrix();
        getFade().applyTranslation(graphics.pose());
        super.extractWidgetRenderState(graphics, mouseX, mouseY, deltaTicks);
        graphics.pose().translate(getX(), getY());
        graphics.enableScissor(0, 0, getWidth(), getHeight());

        if (Debug.showHudInfo()) {
            int nameColor = getClass().getSimpleName().hashCode() | 0x33000000;
            graphics.fill(0, 0, getWidth(), getHeight(), nameColor);
            graphics.text(MilloMod.MC.font, getClass().getSimpleName(), 0, 0, 0xFFFFFFFF, false);
            graphics.text(MilloMod.MC.font, "x:" + getX() + " y:" + getY() + " w:" + getWidth() + " h:" + getHeight(), 0, 10, 0xFFFFFFFF, false);
        }

        RenderArgs args = new RenderArgs(graphics, mouseX - getX(), mouseY - getY(), deltaTicks);
        renderElement(args);

        graphics.disableScissor();
        graphics.pose().popMatrix();
    }

    protected void renderElement(RenderArgs args) {
        renderChildren(args);
    }

    protected void renderChildren(RenderArgs args) {
        for (AbstractWidget child : List.copyOf(getChildren())) {
            try {
                child.extractRenderState(args.context, args.mouseX, args.mouseY, args.deltaTicks);
            } catch (Exception e) {
                MilloLog.error("Failed to render child element: " + child);
                MilloLog.stackTrace(e);
            }
        }
    }

    protected MouseButtonEvent transformClickToLocal(MouseButtonEvent click) {
        return new MouseButtonEvent(click.x() - getX(), click.y() - getY(), click.buttonInfo());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput builder) {
    }

    public <K extends AbstractWidget> void addChildren(Collection<K> children) {
        for (K child : children) {
            addChild(child);
        }
    }

    public void addChildren(AbstractWidget... children) {
        for (AbstractWidget child : children) {
            addChild(child);
        }
    }

    protected record RenderArgs(GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks) {}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append(" {");
        sb.append(toStringChildren());
        sb.append("\n}");
        return sb.toString();
    }

    protected String toStringChildren() {
        StringBuilder sb = new StringBuilder();
        for (AbstractWidget child : getChildren()) {
            sb.append("\n  ").append(child.toString().replace("\n", "\n  "));
        }
        return sb.toString();
    }
}
