package seedu.planner.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import seedu.planner.commons.core.LogsCenter;
import seedu.planner.commons.events.ui.ShowPieChartStatsEvent;
import seedu.planner.commons.events.ui.ShowSummaryTableEvent;

/**
 * This UI component is responsible for displaying the summary requested by the user
 */
//TODO: refactor this into 2 components
public class SummaryDisplay extends UiPart<Region> {

    private static final Logger logger = LogsCenter.getLogger(UiManager.class);

    private static final String FXML = "SummaryDisplay.fxml";

    private ObservableList<SummaryEntry> data = FXCollections.emptyObservableList();

    @FXML
    private TableView<SummaryEntry> table;

    @FXML
    private AnchorPane summaryDisplay;

    @FXML
    private TabPane tabManager;

    @FXML
    private TableColumn dateColumn;

    @FXML
    private TableColumn totalIncomeColumn;

    @FXML
    private TableColumn totalExpenseColumn;

    @FXML
    private TableColumn totalColumn;

    public SummaryDisplay() {
        super(FXML);
        init();
        hide();
        registerAsAnEventHandler(this);
    }

    /**
     * This function links up all the columns of {@code TableView} with the parameters of {@code SummaryEntry}
     */
    private void init() {
        dateColumn.setCellValueFactory(
                new PropertyValueFactory<SummaryEntry, String>("timeStamp"));
        totalIncomeColumn.setCellValueFactory(
                new PropertyValueFactory<SummaryEntry, String>("totalIncome"));
        totalExpenseColumn.setCellValueFactory(
                new PropertyValueFactory<SummaryEntry, String>("totalExpense"));
        totalColumn.setCellValueFactory(
                new PropertyValueFactory<SummaryEntry, String>("total"));
        table.setItems(data);
    }

    private void show() {
        getRoot().toFront();
        summaryDisplay.setVisible(true);
    }

    private void hide() {
        getRoot().toBack();
        summaryDisplay.setVisible(false);
    }

    @Subscribe
    public void handleShowSummaryTableEvent(ShowSummaryTableEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        table.setItems(event.data);
        show();
    }

    @Subscribe
    //TODO: Refactor this to not hardcode
    public void handleShowPieChartStatsEvent(ShowPieChartStatsEvent event) {
        Tab tab = new Tab();
        tab.setText("Category Breakdown for financial activity");
        if (!tabManager.getTabs().stream().anyMatch(t -> t.getText() == "Category Breakdown for financial activity")) {
            tabManager.getTabs().add(tab);
            tabManager.getSelectionModel().select(tab);
        }
    }
}
