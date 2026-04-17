import domain.MeasurementParam;
import domain.Report;
import domain.ReportLine;
import domain.ReportStatus;
import storage.XmlReportSerializer;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class TestXML {
    public static void main(String[] args) {
        try {
            XmlReportSerializer serializer = new XmlReportSerializer();

            // Создаем тестовый отчет
            Report report = new Report();
            report.setId(UUID.randomUUID());
            report.setName("Тестовый отчет");
            report.setSampleId(UUID.randomUUID());
            report.setOwnerUsername("testUser");
            report.setStatus(ReportStatus.DRAFT);
            report.setCreatedAt(Instant.now());
            report.setUpdatedAt(Instant.now());

            // Создаем строку отчета
            ReportLine line = new ReportLine();
            line.setId(UUID.randomUUID());
            line.setReportId(report.getId());
            line.setParam(MeasurementParam.PH);
            line.setValue(7.2);
            line.setUnit("pH");
            line.setCreatedAt(Instant.now());
            line.setUpdatedAt(Instant.now());

            // Добавляем строку к отчету
            report.setLines(List.of(line));

            // Сохраняем в файл
            String filePath = "test.xml";
            serializer.saveToFile(List.of(report), filePath);
            System.out.println("✅ Сохранено в " + filePath);

            // Загружаем из файла
            List<Report> loaded = serializer.loadFromFile(filePath);
            System.out.println("✅ Загружено отчетов: " + loaded.size());

            // Выводим информацию о загруженном отчете
            if (!loaded.isEmpty()) {
                Report loadedReport = loaded.get(0);
                System.out.println("\n=== ИНФОРМАЦИЯ О ЗАГРУЖЕННОМ ОТЧЕТЕ ===");
                System.out.println("ID:        " + loadedReport.getId());
                System.out.println("Название:  " + loadedReport.getName());
                System.out.println("Статус:    " + loadedReport.getStatus());
                System.out.println("Владелец:  " + loadedReport.getOwnerUsername());
                System.out.println("Sample ID: " + loadedReport.getSampleId());
                System.out.println("Создан:    " + loadedReport.getCreatedAt());
                System.out.println("Обновлен:  " + loadedReport.getUpdatedAt());
                System.out.println("Строк:     " + (loadedReport.getLines() != null ? loadedReport.getLines().size() : 0));

                if (loadedReport.getLines() != null && !loadedReport.getLines().isEmpty()) {
                    System.out.println("\n=== СТРОКИ ОТЧЕТА ===");
                    for (ReportLine l : loadedReport.getLines()) {
                        System.out.printf("  - %s: %.2f %s (ID: %s)%n",
                                l.getParam(),
                                l.getValue(),
                                l.getUnit(),
                                l.getId());
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}