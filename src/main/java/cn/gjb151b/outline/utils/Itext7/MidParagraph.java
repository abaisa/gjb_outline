package cn.gjb151b.outline.utils.Itext7;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.property.TabAlignment;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @Author: fcupup 875894948@qq.com
 * @Data: Created on 6:01 PM 2019/7/15
 * @Modified By:
 */
public class MidParagraph extends Paragraph {
    private static final PageSize pageSize = PageSize.A4;
    private static final float width = pageSize.getWidth() - 72;

    public MidParagraph(String text, PdfFont font, int fontSize) {
        if (text == null)
            text = "";
        List<TabStop> tabstops = new ArrayList();
        tabstops.add(new TabStop(width / 2, TabAlignment.CENTER));
        tabstops.add(new TabStop(width, TabAlignment.LEFT));
        super.addTabStops(tabstops);
        super.add(new Tab()).add(text).add(new Tab());
        super.setFont(font);
        super.setFontSize(fontSize);
    }

    public MidParagraph() {

    }

    public MidParagraph(PdfFont font, int fontSize) {
        super.setFont(font);
        super.setFontSize(fontSize);
    }
}
