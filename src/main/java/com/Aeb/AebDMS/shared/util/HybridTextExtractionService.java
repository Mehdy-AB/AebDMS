package com.Aeb.AebDMS.shared.util;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TessAPI;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;

@Service
public class HybridTextExtractionService {

    private static final float DESIRED_DPI = 300f;
    private static final float PDF_DEFAULT_DPI = 72f;
    private static final float SCALE = DESIRED_DPI / PDF_DEFAULT_DPI; // ~4.17

    private final Tika tika = new Tika();
    private final ITesseract tesseract;

    public HybridTextExtractionService(ITesseract tesseract) {
        this.tesseract = tesseract;
        this.tesseract.setOcrEngineMode(TessAPI.TessOcrEngineMode.OEM_LSTM_ONLY);
        this.tesseract.setPageSegMode(TessAPI.TessPageSegMode.PSM_AUTO);
    }

    public String extractText(
            InputStream inputStream, String originalFilename, String contentType, ExtractorLanug lang) {

        this.tesseract.setLanguage(lang.toString());

        try {
            // Read stream content once into byte array
            byte[] fileBytes = inputStream.readAllBytes();

            // First: try Tika text extraction
            String text = tika.parseToString(new ByteArrayInputStream(fileBytes)).trim();

            // Process based on file type and content
            if (isPdf(originalFilename, contentType)) {
                if (isImageOnlyPdf(fileBytes) || text.length() < 50) {
                    return ocrPdfPages(fileBytes);
                }
                return text;
            }

            // For non-PDF files, return text or OCR if needed
            if (text.length() < 50) {
                return ocrPdfPages(fileBytes);
            }
            return text;
        } catch (Exception e) {
            throw new RuntimeException("Text extraction failed", e);
        }
    }

    private boolean isPdf(String originalFilename, String contentType) {
        return "application/pdf".equalsIgnoreCase(contentType) ||
                (originalFilename != null && originalFilename.toLowerCase().endsWith(".pdf"));
    }

    private boolean isImageOnlyPdf(byte[] fileBytes) {
        try (PDDocument doc = Loader.loadPDF(fileBytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setEndPage(1);
            String page1 = stripper.getText(doc).trim();
            return page1.length() < 20;
        } catch (Exception e) {
            return true; // Assume image-only if parsing fails
        }
    }

    private String ocrPdfPages(byte[] fileBytes) {
        try (PDDocument doc = Loader.loadPDF(fileBytes)) {
            PDFRenderer renderer = new PDFRenderer(doc);
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < doc.getNumberOfPages(); i++) {
                BufferedImage raw = renderer.renderImage(i, SCALE);
                BufferedImage bin = binarize(raw);
                sb.append(tesseract.doOCR(bin))
                        .append("\n\n");
            }
            return sb.toString().trim();
        } catch (Exception e) {
            throw new RuntimeException("PDF OCR failed", e);
        }
    }

    private BufferedImage binarize(BufferedImage src) {
        // 1) Gray
        BufferedImage gray = new BufferedImage(
                src.getWidth(),
                src.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY
        );
        Graphics2D g = gray.createGraphics();
        g.drawImage(src, 0, 0, null);
        g.dispose();

        // 2) Binarize
        BufferedImage bw = new BufferedImage(
                gray.getWidth(),
                gray.getHeight(),
                BufferedImage.TYPE_BYTE_BINARY
        );
        Graphics2D g2 = bw.createGraphics();
        g2.drawImage(gray, 0, 0, null);
        g2.dispose();

        return bw;
    }
}