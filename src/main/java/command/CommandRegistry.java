package command;

import service.ReportService;
import storage.FileStorage;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CommandRegistry {
    private final Map<String, Command> commands;
    private final ReportService reportService;
    private final FileStorage fileStorage;

    public CommandRegistry(ReportService reportService, FileStorage fileStorage) {
        this.reportService = Objects.requireNonNull(reportService, "reportService must not be null");
        this.fileStorage = Objects.requireNonNull(fileStorage, "fileStorage must not be null");
        this.commands = new HashMap<>();
        registerAllCommands();
    }

    private void registerAllCommands() {
        register("help", new HelpCommand(commands, fileStorage));
        register("exit", new ExitCommand());

        register("rep_list", new ListReportsCommand(reportService));
        register("rep_show", new ShowReportCommand(reportService));
        register("rep_create_sample", new CreateSampleReportCommand(reportService));
        register("rep_delete", new DeleteReportCommand(reportService));
        register("rep_finalize", new FinalizeReportCommand(reportService));
        register("rep_sign", new SignReportCommand(reportService));
        register("rep_export", new ExportReportCommand(reportService));

        register("rep_addline", new AddReportLineCommand(reportService));
        register("rep_lines", new ShowReportLinesCommand(reportService));
        register("rep_delline", new DeleteReportLineCommand(reportService));
        register("rep_upd_line", new UpdateReportLineCommand(reportService));

        register("save", new SaveCommand(fileStorage, reportService));
        register("load", new LoadCommand(fileStorage, reportService));
    }

    public void register(String name, Command command) {
        String normalizedName = normalize(name);

        if (commands.containsKey(normalizedName)) {
            throw new IllegalArgumentException("Команда уже зарегистрирована: " + normalizedName);
        }

        commands.put(normalizedName, Objects.requireNonNull(command, "command must not be null"));
    }

    public Command getCommand(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        return commands.get(normalize(name));
    }

    public Map<String, Command> getAllCommands() {
        return Map.copyOf(commands);
    }

    private String normalize(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя команды не должно быть пустым");
        }
        return name.trim().toLowerCase(Locale.ROOT);
    }
}