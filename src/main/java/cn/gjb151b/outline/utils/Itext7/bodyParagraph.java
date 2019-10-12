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
 * @Data: Created on 5:46 PM 2019/7/15
 * @Modified By:
 */
public class bodyParagraph extends Paragraph {
    private static final PageSize pageSize = PageSize.A4;
    private static final float width = pageSize.getWidth();

    public bodyParagraph(String text, PdfFont font, int fontSize) {
        if (text == null)
            text = "";
        text = "\ua0a0\ua0a0\ua0a0\ua0a0\ua0a0\ua0a0\ua0a0\ua0a0\ua0a0\ua0a0" + text;
        List<TabStop> tabstops = new ArrayList();
        tabstops.add(new TabStop(width, TabAlignment.LEFT));
        super.addTabStops(tabstops);
        super.add(new Tab()).add(text).add(new Tab());
        super.setFont(font);
        super.setFontSize(fontSize);
    }

    public bodyParagraph() {
        List<TabStop> tabstops = new ArrayList();
        tabstops.add(new TabStop(width, TabAlignment.LEFT));
        super.addTabStops(tabstops);
    }

}

