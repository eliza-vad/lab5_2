package storage;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import domain.Report;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class XmlReportSerializer {
    private final XmlMapper xmlMapper;

    public XmlReportSerializer() {
        this.xmlMapper = XmlMapper.builder()
                .addModule(new JavaTimeModule())  // для работы с Instant
                .enable(SerializationFeature.INDENT_OUTPUT)  // красивый формат
                .build();
    }

    /**
     * Сохраняет список отчетов в XML файл
     */
    public void saveToFile(List<Report> reports, String path) throws IOException {
        ReportListWrapper wrapper = new ReportListWrapper(reports);
        xmlMapper.writeValue(new File(path), wrapper);
    }

    /**
     * Загружает список отчетов из XML файла
     */
    public List<Report> loadFromFile(String path) throws IOException {
        ReportListWrapper wrapper = xmlMapper.readValue(new File(path), ReportListWrapper.class);
        return wrapper.getReports();
    }

    /**
     * Вспомогательный метод: сохранить в строку XML (для отладки)
     */
    public String toXml(List<Report> reports) throws IOException {
        ReportListWrapper wrapper = new ReportListWrapper(reports);
        return xmlMapper.writeValueAsString(wrapper);
    }

    /**
     * Вспомогательный метод: загрузить из строки XML (для отладки)
     */
    public List<Report> fromXml(String xml) throws IOException {
        ReportListWrapper wrapper = xmlMapper.readValue(xml, ReportListWrapper.class);
        return wrapper.getReports();
    }
}