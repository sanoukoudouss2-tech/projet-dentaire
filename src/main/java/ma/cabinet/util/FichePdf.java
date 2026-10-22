package ma.cabinet.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;

/**
 * Export minimal d'une fiche texte en PDF avec PDFBox 3.
 * Version de départ : Koudouss pourra l'améliorer (logo, tableau, couleurs).
 */
public final class FichePdf {

    private FichePdf() {
    }

    public static void exporter(String texte, File fichier) throws IOException {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            PDType1Font titre = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font normal = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                float y = page.getMediaBox().getHeight() - 70;
                String[] lignes = texte.split("\n");
                for (int i = 0; i < lignes.length; i++) {
                    boolean estTitre = i < 2;
                    cs.beginText();
                    cs.setFont(estTitre ? titre : normal, estTitre ? 16 : 12);
                    cs.newLineAtOffset(60, y);
                    cs.showText(lignes[i]);
                    cs.endText();
                    y -= estTitre ? 24 : 18;
                }
            }
            doc.save(fichier);
        }
    }
}
