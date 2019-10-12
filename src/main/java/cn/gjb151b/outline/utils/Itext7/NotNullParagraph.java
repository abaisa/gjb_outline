package cn.gjb151b.outline.utils.Itext7;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.element.Paragraph;

import java.awt.*;

/**
 * <p>
 *
 * </p>
 *
 * @Author: fcupup 875894948@qq.com
 * @Data: Created on 6:02 PM 2019/8/27
 * @Modified By:
 */
public class NotNullParagraph extends Paragraph {
    public NotNullParagraph(String text, PdfFont font, int fontSize) {
        if (text == null) {
            text = "";
        }
        super.add(text);
        super.setFont(font);
        super.setFontSize(fontSize);
    }

    public NotNullParagraph() {

    }
}
