package cn.gjb151b.outline.utils.Itext7;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.property.TabAlignment;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class  catalogText {
    class MyLine implements ILineDrawer {
        private float lineWidth = 1;
        private float offset = 5;
        private Color color = Color.BLACK;
        //        @Override
        public void draw(PdfCanvas canvas, Rectangle drawArea) {
            canvas.saveState()
                    .setStrokeColor(color)
                    .setLineWidth(lineWidth)
                    .setLineDash(1, 3, 8)
                    .moveTo(drawArea.getX(), drawArea.getY() + lineWidth / 2)
                    .lineTo(drawArea.getX() + drawArea.getWidth(), drawArea.getY() + lineWidth / 2)
                    .stroke()
                    .restoreState();
        }

        //        @Override
        public float getLineWidth() {
            return lineWidth;
        }
        //        @Override
        public void setLineWidth(float lineWidth) {
            this.lineWidth = lineWidth;
        }
        //        @Override
        public Color getColor() {
            return color;
        }
        //        @Override
        public void setColor(Color color) {
            this.color = color;
        }
        public float getOffset() {
            return offset;
        }
        public void setOffset(float poffset) {
            this.offset = offset;
        }

    }

    public static final String DEST = "result/pdf/catalog.pdf";

//    public static void main(String[] args) throws IOException {
//        File file = new File(DEST);
//        file.getParentFile().mkdirs();
//        new catalogText().createPdf(DEST, "1234", 1);
//    }
    public void createPdf(Document document, String dest, String title,
                          int numberOfPage, PdfFont font, int fontSize) throws IOException {
//        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
        PageSize pagesize = PageSize.A4;
//        Document document = new Document(pdf, pagesize);
        float w = pagesize.getWidth() - 36;
        MyLine line = new MyLine();
        List<TabStop> tabstops = new ArrayList();
        tabstops.add(new TabStop(w - 36, TabAlignment.CENTER, line));
        tabstops.add(new TabStop(w, TabAlignment.LEFT, line));
        MidParagraph p = new MidParagraph(font, fontSize);
        p.addTabStops(tabstops);
        p.add(title).add(new Tab()).add(String.valueOf(numberOfPage));
        document.add(p);
//        document.close();
    }
}
