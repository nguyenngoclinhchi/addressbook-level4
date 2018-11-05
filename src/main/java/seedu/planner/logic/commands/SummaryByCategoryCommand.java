package seedu.planner.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.planner.logic.parser.CliSyntax.PREFIX_DATE;

import java.util.function.Predicate;

import seedu.planner.commons.core.EventsCenter;
import seedu.planner.commons.events.ui.ShowSummaryTableEvent;
import seedu.planner.logic.CommandHistory;
import seedu.planner.model.Model;
import seedu.planner.model.record.Date;
import seedu.planner.model.record.DateIsWithinIntervalPredicate;
import seedu.planner.model.record.Record;
import seedu.planner.model.summary.SummaryByCategoryList;

//@@author tenvinc

/** List all the summary of records within a period of time specified */
public class SummaryByCategoryCommand extends SummaryCommand {

    public static final String COMMAND_MODE_WORD = "category";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists the summary for each category for a "
            + "period of time.\n A category refers to any set of tags that is assigned to a record.\n"
            + " Parameters: "
            + PREFIX_DATE + "START_DATE " + "END_DATE "
            + "Example: " + COMMAND_WORD + " " + COMMAND_MODE_WORD + " "
            + PREFIX_DATE + "1-1-2018 " + "12-12-2018 ";

    public static final String MESSAGE_SUCCESS = "Listed summary for %d categories";

    private final Date startDate;
    private final Date endDate;
    private final Predicate<Record> predicate;

    public SummaryByCategoryCommand(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        predicate = new DateIsWithinIntervalPredicate(startDate, endDate);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.updateFilteredRecordList(predicate);
        SummaryByCategoryList summaryList = new SummaryByCategoryList(model.getFilteredRecordList());
        EventsCenter.getInstance().post(new ShowSummaryTableEvent(summaryList.getSummaryList(),
                summaryList.getTotalExpense(), summaryList.getTotalIncome(), summaryList.getTotal(), TOTAL_LABEL));
        return new CommandResult(String.format(MESSAGE_SUCCESS, summaryList.size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SummaryByCategoryCommand // instanceof handles nulls
                && startDate.equals(((SummaryByCategoryCommand) other).startDate)
                && endDate.equals(((SummaryByCategoryCommand) other).endDate));
    }
}
