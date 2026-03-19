package millo.millomod2.menu;

/**
 * Marker interface for menu elements that should be positioned absolutely rather than in the flow of the menu.
 * Are still relative to the menu, they do not affect the layout of other elements and are not affected by it.
 */
public interface AbsoluteElement {

    int getAbsoluteX();
    int getAbsoluteY();

}
