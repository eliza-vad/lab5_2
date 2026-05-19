package app.storage;

import app.domain.Report;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileStorage {
    private final XmlReportSerializer serializer;
    private final FileValidator validator;

    public FileStorage() {
        this.serializer = new XmlReportSerializer();
        this.validator = new FileValidator();
    }

    public void save(String path, List<Report> reports) throws IOException {
        serializer.saveToFile(reports, path);
    }

    public List<Report> load(String path) throws IOException, ValidationException {
        List<Report> reports = serializer.loadFromFile(path);
        validator.validate(reports);
        return reports;
    }

    public List<String> getSavedXmlDisplayNames() {
        File currentDir = new File(".");
        File[] files = currentDir.listFiles((dir, name) ->
                name != null && name.toLowerCase().endsWith(".xml"));

        List<String> result = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    result.add(buildDisplayName(file));
                }
            }
        }

        Collections.sort(result);
        return result;
    }

    private String buildDisplayName(File file) {
        try {
            String reportName = extractReportNameFromXml(file);

            if (reportName != null && !reportName.isBlank()) {
                return reportName + " (" + file.getName() + ")";
            }
        } catch (Exception ignored) {
        }

        return file.getName();
    }

    private String extractReportNameFromXml(File file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();

        NodeList nameNodes = document.getElementsByTagName("name");
        if (nameNodes.getLength() > 0) {
            String value = nameNodes.item(0).getTextContent();
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }

        NodeList titleNodes = document.getElementsByTagName("title");
        if (titleNodes.getLength() > 0) {
            String value = titleNodes.item(0).getTextContent();
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }

        return null;
    }
}