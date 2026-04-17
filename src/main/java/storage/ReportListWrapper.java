package storage;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import domain.Report;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "reports")
public class ReportListWrapper {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "report")
    private List<Report> reports;

    public ReportListWrapper() {
        this.reports = new ArrayList<>();
    }

    public ReportListWrapper(List<Report> reports) {
        this.reports = reports;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }
}