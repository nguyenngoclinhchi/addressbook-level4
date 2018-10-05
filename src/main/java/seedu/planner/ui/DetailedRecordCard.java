package seedu.planner.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import seedu.planner.commons.core.LogsCenter;
import seedu.planner.commons.events.ui.RecordPanelSelectionChangedEvent;
import seedu.planner.model.record.Record;

/**
 * This UI component is responsible for displaying detailed information for each record
 */
public class DetailedRecordCard extends UiPart<Region> {

    private static String FXML = "DetailedRecordCard.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private final String DATE_LABEL = "Date:";
    private final String MONEYFLOW_LABEL = "Credit(+)/Debit(-):";

    @FXML
    private GridPane card;

    @FXML
    private Label dateLabel;

    @FXML
    private Label moneyFlowLabel;

    @FXML
    private Label name;

    @FXML
    private Label date;

    @FXML
    private Label moneyFlow;

    @FXML
    private FlowPane biggerTags;

    public DetailedRecordCard() {
        super(FXML);
        dateLabel.setText(DATE_LABEL);
        moneyFlowLabel.setText(MONEYFLOW_LABEL);
        card.setVisible(false);
        registerAsAnEventHandler(this);
    }

    /**
     * Loads the detailed record panel with all record information
     * @param record
     */
    private void loadRecordPage(Record record) {
        if (record != null) {
            name.setText(record.getName().toString());
            date.setText(record.getDate().toString());
            moneyFlow.setText("$" + record.getMoneyFlow().toString());
            biggerTags.getChildren().clear();
            record.getTags().forEach(tag -> biggerTags.getChildren().add(new Label(tag.tagName)));
            card.setVisible(true);
        } else {
            card.setVisible(false);
        }
    }

    @Subscribe
    private void handleRecordPanelSelectionChangedEvent(RecordPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadRecordPage(event.getNewSelection());
    }
}
