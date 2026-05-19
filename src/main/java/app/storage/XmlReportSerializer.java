package app.storage;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import app.domain.Report;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class XmlReportSerializer {
    private final XmlMapper xmlMapper;

    public XmlReportSerializer() {
        this.xmlMapper = XmlMapper.builder()
                .addModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT)
                .build();
    }

    public void saveToFile(List<Report> reports, String path) throws IOException {
        ReportListWrapper wrapper = new ReportListWrapper(reports);
        xmlMapper.writeValue(new File(path), wrapper);
    }

    public List<Report> loadFromFile(String path) throws IOException {
        ReportListWrapper wrapper = xmlMapper.readValue(new File(path), ReportListWrapper.class);
        return wrapper.getReports();
    }

    public String toXml(List<Report> reports) throws IOException {
        ReportListWrapper wrapper = new ReportListWrapper(reports);
        return xmlMapper.writeValueAsString(wrapper);
    }

    public List<Report> fromXml(String xml) throws IOException {
        ReportListWrapper wrapper = xmlMapper.readValue(xml, ReportListWrapper.class);
        return wrapper.getReports();
    }
}