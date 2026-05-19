package ui.main;

public class MainController {

    private final MainView view;
    private final MainActionHandler actionHandler;

    public MainController(MainView view, MainActionHandler actionHandler) {
        this.view = view;
        this.actionHandler = actionHandler;
        bindActions();
    }

    private void bindActions() {
        view.getRefreshButton().setOnAction(e -> actionHandler.refresh(view));
        view.getCreateButton().setOnAction(e -> actionHandler.createSample(view));
        view.getEditButton().setOnAction(e -> actionHandler.editSelectedReport(view));
        view.getDeleteButton().setOnAction(e -> actionHandler.deleteSelectedReport(view));
        view.getDetailsButton().setOnAction(e -> actionHandler.showDetails(view));

        view.getFinalizeButton().setOnAction(e -> actionHandler.finalizeSelectedReport(view));
        view.getSignButton().setOnAction(e -> actionHandler.signSelectedReport(view));

        view.getShowLinesButton().setOnAction(e -> actionHandler.showReportLines(view));
        view.getAddLineButton().setOnAction(e -> actionHandler.addReportLine(view));
        view.getEditLineButton().setOnAction(e -> actionHandler.editReportLine(view));
        view.getDeleteLineButton().setOnAction(e -> actionHandler.deleteReportLine(view));

    }

    public void onStart() {
        actionHandler.refresh(view);
    }

    public MainView getView() {
        return view;
    }
}