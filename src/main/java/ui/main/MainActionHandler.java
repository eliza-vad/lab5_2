package ui.main;

import app.domain.MeasurementParam;
import app.domain.Report;
import app.domain.ReportLine;
import app.service.AuthService;
import app.service.ReportService;
import javafx.concurrent.Task;
import ui.dialog.DialogService;
import ui.task.FxTaskRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class MainActionHandler {

    private final ReportService reportService;
    private final DialogService dialogService;
    private final FxTaskRunner taskRunner;
    private final MainUiState uiState;
    private final AuthService authService;

    public MainActionHandler(
            ReportService reportService,
            DialogService dialogService,
            FxTaskRunner taskRunner,
            MainUiState uiState,
            AuthService authService
    ) {
        this.reportService = reportService;
        this.dialogService = dialogService;
        this.taskRunner = taskRunner;
        this.uiState = uiState;
        this.authService = authService;
    }

    public void refresh(MainView view) {
        Task<List<Report>> task = new Task<>() {
            @Override
            protected List<Report> call() {
                return new ArrayList<>(reportService.getAllReports());
            }
        };

        taskRunner.run(task, view.getProgressIndicator(), () -> {
            uiState.getReports().setAll(task.getValue());
            view.getReportListView().refresh();
        });
    }

    public void createSample(MainView view) {
        if (!authService.isAuthorized()) {
            dialogService.showError("Ошибка: Создавать отчеты могут только авторизованные пользователи.");
            return;
        }

        Optional<String> nameResult = dialogService.askText(
                "Создание отчета", "Создать тестовый отчет", "Введите название отчета:", "Тестовый отчет"
        );
        if (nameResult.isEmpty() || nameResult.get().isBlank()) {
            return;
        }

        String reportName = nameResult.get().trim();
        String username = authService.getCurrentUser().getLogin();
        Integer ownerId = authService.getCurrentUser().getId();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                reportService.createSampleReport(UUID.randomUUID(), reportName, username);
                for(Report r : reportService.getAllReports()) {
                    if(r.getName().equals(reportName) && r.getOwnerId() == null) {
                        r.setOwnerId(ownerId);
                    }
                }
                return null;
            }
        };

        taskRunner.run(task, view.getProgressIndicator(),
                () -> dialogService.showInfo("Отчет создан пользователем " + username + ". Нажми Refresh."));
    }

    public void editSelectedReport(MainView view) {
        Report selected = requireSelectedReport(view);
        if (selected == null) return;

        if (!authService.isAuthorized() || !authService.getCurrentUser().getId().equals(selected.getOwnerId())) {
            dialogService.showError("Ошибка: У вас нет прав на изменение этого отчета.");
            return;
        }

        Optional<String> result = dialogService.askText(
                "Редактирование отчета", "Изменить название отчета", "Новое название:", safe(getName(selected))
        );
        if (result.isEmpty() || result.get().isBlank()) return;

        String newName = result.get().trim();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                reportService.updateReportName(selected.getId(), newName);
                return null;
            }
        };

        taskRunner.run(task, view.getProgressIndicator(),
                () -> dialogService.showInfo("Название изменено. Нажми Refresh."));
    }

    public void deleteSelectedReport(MainView view) {
        Report selected = requireSelectedReport(view);
        if (selected == null) return;

        if (!authService.isAuthorized() || !authService.getCurrentUser().getId().equals(selected.getOwnerId())) {
            dialogService.showError("Ошибка: У вас нет прав на удаление этого отчета.");
            return;
        }

        boolean confirmed = dialogService.confirm("Подтверждение", "Удаление отчета", "Удалить выбранный отчет?");
        if (!confirmed) return;

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                reportService.deleteReport(selected.getId());
                return null;
            }
        };

        taskRunner.run(task, view.getProgressIndicator(),
                () -> dialogService.showInfo("Отчет удален. Нажми Refresh."));
    }

    public void showDetails(MainView view) {
        Report selected = requireSelectedReport(view);
        if (selected == null) return;

        StringBuilder text = new StringBuilder();
        text.append("ID: ").append(selected.getId()).append("\n");
        text.append("Название: ").append(safe(getName(selected))).append("\n");
        text.append("Владелец ID: ").append(selected.getOwnerId() != null ? selected.getOwnerId() : "нет").append("\n");
        text.append("Создатель логин: ").append(selected.getOwnerUsername() != null ? selected.getOwnerUsername() : "нет").append("\n");
        text.append("Статус: ").append(String.valueOf(selected.getStatus())).append("\n");
        text.append("Создан: ").append(String.valueOf(selected.getCreatedAt())).append("\n");
        text.append("Обновлен: ").append(String.valueOf(selected.getUpdatedAt())).append("\n");

        dialogService.showInfo(text.toString());
    }

    public void finalizeSelectedReport(MainView view) {
        Report selected = requireSelectedReport(view);
        if (selected == null) return;

        if (!authService.isAuthorized() || !authService.getCurrentUser().getId().equals(selected.getOwnerId())) {
            dialogService.showError("Ошибка: Только владелец может финализировать отчет.");
            return;
        }

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                reportService.finalizeReport(selected.getId());
                return null;
            }
        };

        taskRunner.run(task, view.getProgressIndicator(),
                () -> dialogService.showInfo("Отчет финализирован. Нажми Refresh."));
    }

    public void signSelectedReport(MainView view) {
        Report selected = requireSelectedReport(view);
        if (selected == null) return;

        Optional<String> result = dialogService.askText(
                "Подписание отчета", "Подписать отчет", "Введите ваше имя для подписи:", "user"
        );
        if (result.isEmpty() || result.get().isBlank()) return;

        String username = result.get().trim();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                reportService.signReport(selected.getId(), username);
                return null;
            }
        };

        taskRunner.run(task, view.getProgressIndicator(),
                () -> dialogService.showInfo("Отчет подписан. Нажми Refresh."));
    }

    public void showReportLines(MainView view) {
        Report selected = requireSelectedReport(view);
        if (selected == null) return;

        try {
            Set<ReportLine> lines = reportService.getLinesByReportId(selected.getId());
            StringBuilder text = new StringBuilder();
            if (lines.isEmpty()) {
                text.append("У отчета нет строк.");
            } else {
                int i = 1;
                for (ReportLine line : lines) {
                    text.append(i++).append(") ID=").append(line.getId())
                            .append(", PARAM=").append(line.getParam())
                            .append(", VALUE=").append(line.getValue())
                            .append(", UNIT=").append(line.getUnit()).append("\n");
                }
            }
            dialogService.showInfo(text.toString());
        } catch (Exception e) {
            dialogService.showError("Не удалось показать строки отчета: " + safeMessage(e));
        }
    }

    public void addReportLine(MainView view) {
        Report selected = requireSelectedReport(view);
        if (selected == null) return;

        if (!authService.isAuthorized() || !authService.getCurrentUser().getId().equals(selected.getOwnerId())) {
            dialogService.showError("Ошибка: У вас нет прав на изменение этого отчета.");
            return;
        }

        MeasurementParam[] params = MeasurementParam.values();
        if (params.length == 0) {
            dialogService.showError("Справочник MeasurementParam пуст.");
            return;
        }

        Optional<MeasurementParam> paramResult = dialogService.askChoice(
                "Добавление строки", "Выбери параметр", "Параметр:", Arrays.asList(params), params[0]
        );
        if (paramResult.isEmpty()) return;

        Optional<String> valueResult = dialogService.askText("Добавление строки", "Введите значение", "Value:", "0");
        if (valueResult.isEmpty() || valueResult.get().isBlank()) return;

        double value;
        try {
            value = Double.parseDouble(valueResult.get().trim());
        } catch (NumberFormatException e) {
            dialogService.showError("Значение должно быть числом.");
            return;
        }

        Optional<String> unitResult = dialogService.askText("Добавление строки", "Введите единицу измерения", "Unit:", "unit");
        if (unitResult.isEmpty() || unitResult.get().isBlank()) return;

        String unit = unitResult.get().trim();
        MeasurementParam param = paramResult.get();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                reportService.addReportLine(selected.getId(), param, value, unit);
                return null;
            }
        };

        taskRunner.run(task, view.getProgressIndicator(), () -> dialogService.showInfo("Строка добавлена."));
    }

    public void editReportLine(MainView view) {
        Report selected = requireSelectedReport(view);
        if (selected == null) return;

        if (!authService.isAuthorized() || !authService.getCurrentUser().getId().equals(selected.getOwnerId())) {
            dialogService.showError("Ошибка: У вас нет прав на изменение этого отчета.");
            return;
        }

        try {
            Set<ReportLine> lines = reportService.getLinesByReportId(selected.getId());
            if (lines.isEmpty()) {
                dialogService.showError("У выбранного отчета нет строк.");
                return;
            }

            List<ReportLine> lineList = new ArrayList<>(lines);
            ReportLine selectedLine = dialogService.askChoice(
                    "Редактирование строки", "Выбери строку", "Строка:", lineList, lineList.get(0)
            ).orElse(null);
            if (selectedLine == null) return;

            MeasurementParam[] params = MeasurementParam.values();
            MeasurementParam newParam = dialogService.askChoice(
                    "Редактирование строки", "Выбери новый параметр", "Параметр:", Arrays.asList(params), selectedLine.getParam()
            ).orElse(null);
            if (newParam == null) return;

            Optional<String> valueResult = dialogService.askText(
                    "Редактирование строки", "Введите новое значение", "Value:", String.valueOf(selectedLine.getValue())
            );
            if (valueResult.isEmpty() || valueResult.get().isBlank()) return;

            double newValue;
            try {
                newValue = Double.parseDouble(valueResult.get().trim());
            } catch (NumberFormatException e) {
                dialogService.showError("Значение должно быть числом.");
                return;
            }

            Optional<String> unitResult = dialogService.askText(
                    "Редактирование строки", "Введите новую единицу измерения", "Unit:", selectedLine.getUnit()
            );
            if (unitResult.isEmpty() || unitResult.get().isBlank()) return;

            String newUnit = unitResult.get().trim();

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    reportService.updateReportLine(selectedLine.getId(), newParam, newValue, newUnit);
                    return null;
                }
            };

            taskRunner.run(task, view.getProgressIndicator(), () -> dialogService.showInfo("Строка изменена."));
        } catch (Exception e) {
            dialogService.showError("Не удалось открыть редактирование строки: " + safeMessage(e));
        }
    }

    public void deleteReportLine(MainView view) {
        Report selected = requireSelectedReport(view);
        if (selected == null) return;

        if (!authService.isAuthorized() || !authService.getCurrentUser().getId().equals(selected.getOwnerId())) {
            dialogService.showError("Ошибка: У вас нет прав на изменение этого отчета.");
            return;
        }

        try {
            Set<ReportLine> lines = reportService.getLinesByReportId(selected.getId());
            if (lines.isEmpty()) {
                dialogService.showError("У выбранного отчета нет строк.");
                return;
            }

            List<ReportLine> lineList = new ArrayList<>(lines);
            ReportLine selectedLine = dialogService.askChoice(
                    "Удаление строки", "Выбери строку для удаления", "Строка:", lineList, lineList.get(0)
            ).orElse(null);
            if (selectedLine == null) return;

            boolean confirmed = dialogService.confirm("Подтверждение", "Удаление строки отчета", "Удалить выбранную строку?");
            if (!confirmed) return;

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    reportService.deleteReportLine(selectedLine.getId());
                    return null;
                }
            };

            taskRunner.run(task, view.getProgressIndicator(), () -> dialogService.showInfo("Строка удалена."));
        } catch (Exception e) {
            dialogService.showError("Не удалось удалить строку: " + safeMessage(e));
        }
    }

    private Report requireSelectedReport(MainView view) {
        Report selected = view.getReportListView().getSelectionModel().getSelectedItem();
        if (selected == null) {
            dialogService.showError("Сначала выбери отчет.");
            return null;
        }
        return selected;
    }

    private String getName(Report report) {
        try {
            return report.getName();
        } catch (Exception e) {
            return "(нет имени)";
        }
    }

    private String safe(String text) {
        return text == null || text.isBlank() ? "(нет значения)" : text;
    }

    private String safeMessage(Throwable e) {
        if (e == null || e.getMessage() == null || e.getMessage().isBlank()) {
            return "Неизвестная ошибка";
        }
        return e.getMessage();
    }
}