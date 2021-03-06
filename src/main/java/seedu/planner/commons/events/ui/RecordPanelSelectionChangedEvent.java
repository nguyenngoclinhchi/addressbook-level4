package seedu.planner.commons.events.ui;

import seedu.planner.commons.events.BaseEvent;
import seedu.planner.model.record.Record;

/**
 * Represents a selection change in the Record List Panel
 */
public class RecordPanelSelectionChangedEvent extends BaseEvent {


    private final Record newSelection;

    public RecordPanelSelectionChangedEvent(Record newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public Record getNewSelection() {
        return newSelection;
    }
}
