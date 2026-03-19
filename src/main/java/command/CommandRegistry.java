package command;

import service.ReportService;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private final Map<String, Command> commands = new HashMap<>();

    public CommandRegistry(ReportService service) {
        registerCommand("rep_create_sample", new CreateSampleReportCommand(service));
        registerCommand("rep_addline", new AddReportLineCommand(service));
        registerCommand("rep_list", new ListReportsCommand(service));
        registerCommand("rep_show", new ShowReportCommand(service));
        registerCommand("rep_lines", new ShowReportLinesCommand(service));
        registerCommand("rep_updateline", new UpdateReportLineCommand(service));
        registerCommand("rep_delline", new DeleteReportLineCommand(service));
        registerCommand("rep_finalize", new FinalizeReportCommand(service));
        registerCommand("rep_sign", new SignReportCommand(service));
        registerCommand("rep_export", new ExportReportCommand(service));
        registerCommand("help", new HelpCommand(commands));
        registerCommand("exit", new ExitCommand());
    }

    private void registerCommand(String name, Command command) {
        commands.put(name, command);
    }

    public Command getCommand(String name) {
        return commands.get(name);
    }

}