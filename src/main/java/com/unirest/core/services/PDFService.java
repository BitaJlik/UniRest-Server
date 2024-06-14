package com.unirest.core.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class PDFService {
    private static final String UPLOAD_TEMPLATE_DIR = "./uploads/templates/";
    private static final String UPLOAD_PDF_DIR = "./uploads/pdf/";
    private static final boolean SAVE_CREATED_PDF = true;

    public File createFromTemplatePDF(String templateName, Map<String, String> replacements, String outputPdfName) throws IOException {
        File templateFile = Paths.get(UPLOAD_TEMPLATE_DIR, String.format("%s.docx", templateName)).toFile();
        FileInputStream fis = new FileInputStream(templateFile);
        XWPFDocument document = new XWPFDocument(fis);

        replaceText(document, replacements);
        File pdfDir = new File(UPLOAD_PDF_DIR);
        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }

        File pdfFile = new File(UPLOAD_PDF_DIR + outputPdfName + ".pdf");
        if (SAVE_CREATED_PDF) {
            if (!pdfFile.createNewFile()) {
                System.err.println(pdfFile.getName() + " exists!");
            }
        }
        convertDocxToPdfFile(document, pdfFile);

        document.close();

        return pdfFile;
    }

    private void replaceText(XWPFDocument document, Map<String, String> replacements) {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                String text = run.getText(0);
                if (text != null) {
                    for (Map.Entry<String, String> entry : replacements.entrySet()) {
                        text = text.replace(String.format("%%%S%%", entry.getKey()), entry.getValue());
                    }
                    run.setText(text, 0);
                }
            }
        }
    }

    private void convertDocxToPdfFile(XWPFDocument document, File pdfFile) throws IOException {
        PDDocument pdfDocument = new PDDocument();
        PDPage page = new PDPage();
        pdfDocument.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page);
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 14);
        contentStream.beginText();
        contentStream.setLeading(1.5f * 14);
        contentStream.newLineAtOffset(70.87F, 725);

        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                String text = run.getText(0);
                if (text != null) {
                    contentStream.showText(text);
                    contentStream.newLine();
                }
            }
        }

        contentStream.endText();
        contentStream.close();

        pdfDocument.save(pdfFile);
        pdfDocument.close();
    }

    public byte[] getPDFBytes(String pdfId) {
        try {
            return Files.readAllBytes(getPDF(pdfId).toPath());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public File getPDF(String pdfId) {
        return Paths.get(UPLOAD_PDF_DIR, String.format("%s.pdf", pdfId)).toFile();
    }
}