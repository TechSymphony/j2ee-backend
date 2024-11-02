package com.tech_symfony.resource_server.system.export;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

public interface ExportPdfService<T> {
    File from(List<T> list, String viewName) throws IOException;
}

@Service
class DefaultExportPdf<T> implements ExportPdfService<T> {
    private static final Logger log = LoggerFactory.getLogger(DefaultExportPdf.class);

    public File from(List<T> list, String viewName) throws IOException {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("list", list);

        templateResolver.setPrefix("templates/");
        String html = templateEngine.process(viewName+".html", context);

        File f = File.createTempFile("pdf_", ".pdf");

        URL fontResourceURL = getClass().getResource("/fonts/DejaVuSans.ttf");



        try (OutputStream outputStream = new FileOutputStream(f)) {
            ITextRenderer renderer = new ITextRenderer();

            renderer.setDocumentFromString(html);
            renderer.getFontResolver().addFont(fontResourceURL.getPath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.layout();
            renderer.createPDF(outputStream);
        }
        f.deleteOnExit();
        return f;
    }
}
