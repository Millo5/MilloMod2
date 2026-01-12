package millo.millomod2.client.features.impl.Editor.elements;

import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.text.Text;

public class CodeBrowser extends FlexElement<CodeBrowser> {

    private ListElement tabList;

    protected CodeBrowser(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    public CodeBrowser(int width, int height) {
        super(0, 0, width, height, Text.empty());

        direction(ElementDirection.COLUMN);
        mainAlign(MainAxisAlignment.START);
        crossAlign(CrossAxisAlignment.STRETCH);
//        background(0x22000000);

        tabList = ListElement.create(width, 20)
                .gap(1)
                .padding(0)
                .border(new Border().top(0xFFFFFFFF).bottom(0xFFFFFFFF))
                .direction(ElementDirection.ROW)
                .crossAlign(CrossAxisAlignment.CENTER);

        addChildren(
                tabList
        );
    }

}
