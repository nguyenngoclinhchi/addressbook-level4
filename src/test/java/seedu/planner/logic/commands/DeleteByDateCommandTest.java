package seedu.planner.logic.commands;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static seedu.planner.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.planner.testutil.TypicalRecords.getTypicalFinancialPlanner;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import seedu.planner.commons.core.Messages;
import seedu.planner.commons.core.index.Index;
import seedu.planner.logic.CommandHistory;
import seedu.planner.model.Model;
import seedu.planner.model.ModelManager;
import seedu.planner.model.UserPrefs;
import seedu.planner.model.record.Date;
import seedu.planner.model.record.Record;
import seedu.planner.testutil.TypicalDates;
import seedu.planner.testutil.TypicalIndexes;
import seedu.planner.testutil.TypicalRecords;

//@@author nguyenngoclinhchi

public class DeleteByDateCommandTest {
    private Model model = new ModelManager(getTypicalFinancialPlanner(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validDateUnfilteredList_success() {
        Date targetDate = TypicalDates.DATE_FIRST_INDEX_DATE;
        List<Record> records = filteredRecordList(model);

        DeleteByDateCommand deleteByDateCommand =
                new DeleteByDateCommand(TypicalDates.DATE_FIRST_INDEX_DATE);

        String expectedMessage = String.format(
                DeleteByDateCommand.MESSAGE_DELETE_RECORDS_SUCCESS, TypicalDates.DATE_FIRST_INDEX_DATE);

        ModelManager expectedModel = new ModelManager(model.getFinancialPlanner(), new UserPrefs());
        for (int i = records.size() - 1; i >= 0; i--) {
            if (records.get(i).getDate().equals(targetDate)) {
                expectedModel.deleteRecord(records.get(i));
                expectedModel.commitFinancialPlanner();
            }
        }
        CommandTestUtil.assertCommandSuccess(
                deleteByDateCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        List<Record> recordsToDelete = listAllRecordToDelete(model, TypicalDates.DATE_FIRST_INDEX_DATE);

        for (int i = 0; i < recordsToDelete.size() - 1; i++) {
            //ensure that only one record exists accordingly to the index
            List<Record> records = filteredRecordList(model);
            int targetIndexInt = records.indexOf(recordsToDelete.get(i));

            CommandTestUtil.showRecordAtIndex(model, targetIndexInt);

            Index outOfBoundIndex = TypicalIndexes.INDEX_LAST_RECORD;
            //ensure that outOfBoundIndex is still in bound of address book list
            assertTrue(outOfBoundIndex.getOneBased() < model.getFinancialPlanner().getRecordList().size());

            DeleteByDateCommand deleteByDateCommand = new DeleteByDateCommand(
                    records.get(outOfBoundIndex.getZeroBased()).getDate());

            CommandTestUtil.assertCommandFailure(
                    deleteByDateCommand, model, commandHistory,
                    Messages.MESSAGE_NONEXISTENT_RECORD_DISPLAYED_DATE);
        }
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Date targetDate = TypicalDates.DATE_FIRST_INDEX_DATE;
        List<Record> recordsToDelete = listAllRecordToDelete(model, targetDate);

        DeleteByDateCommand deleteByDateCommand =
                new DeleteByDateCommand(TypicalDates.DATE_FIRST_INDEX_DATE);

        Model expectedModel = new ModelManager(model.getFinancialPlanner(), new UserPrefs());
        expectedModel.deleteListRecord(recordsToDelete);
        expectedModel.commitFinancialPlanner();

        //delete the Records have required date
        deleteByDateCommand.execute(model, commandHistory);

        //undo -> reverts financialPlanner back to the previous state and filtered record list to show all records
        expectedModel.undoFinancialPlanner();
        CommandTestUtil.assertCommandSuccess(
                new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        //redo -> same first record deleted again
        expectedModel.redoFinancialPlanner();
        CommandTestUtil.assertCommandSuccess(
                new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_nonexistentDateUnfilteredList_failure() {
        Date outOfBoundDate = TypicalRecords.OUT_OF_BOUND_DATE;
        DeleteByDateCommand deleteByDateCommand = new DeleteByDateCommand(outOfBoundDate);

        //execution failed -> address book state not added into model
        CommandTestUtil.assertCommandFailure(
                deleteByDateCommand, model, commandHistory, Messages.MESSAGE_NONEXISTENT_RECORD_DISPLAYED_DATE);

        //single address book state in model -> undoCommand and redoCommand fail
        CommandTestUtil.assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        CommandTestUtil.assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Deletes a {@code Record} from a filtered list.
     * 2. Undo the deletion.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously deleted record in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the deletion. This ensures {@code RedoCommand} deletes the record object regardless of indexing.
     */

    @Test
    public void executeUndoRedo_exsistentDateFilteredList_sameRecordDeleted() throws Exception {
        DeleteByDateCommand deleteByDateCommand =
                new DeleteByDateCommand(TypicalDates.DATE_FIRST_INDEX_DATE);
        Model expectedModel = new ModelManager(model.getFinancialPlanner(), new UserPrefs());

        Date date = TypicalDates.DATE_FIRST_INDEX_DATE;

        List<Record> records = filteredRecordList(model);
        List<Record> recordsToDelete = listAllRecordToDelete(model, date);

        for (Record record : recordsToDelete) {
            expectedModel.deleteRecord(record);
        }
        expectedModel.commitFinancialPlanner();

        // delete -> deletes second record in unfiltered record list / first record in filtered record list
        deleteByDateCommand.execute(model, commandHistory);

        // undo -> reverts address book back to previous state and filtered record list to show all records
        expectedModel.undoFinancialPlanner();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(recordsToDelete, model.getFilteredRecordList());

        // redo -> deletes same second record in unfiltered record list
        expectedModel.redoFinancialPlanner();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        DeleteByDateCommand deleteCommandByFirstDateEntry =
                new DeleteByDateCommand(TypicalDates.DATE_FIRST_INDEX_DATE);

        DeleteByDateCommand deleteCommandByLastDateEntry =
                new DeleteByDateCommand(TypicalDates.DATE_LAST_INDEX_DATE);

        DeleteByDateCommand deleteCommandByFirstDateEntryCopy =
                new DeleteByDateCommand(TypicalDates.DATE_FIRST_INDEX_DATE);

        // same object -> returns true
        Assert.assertTrue(
                deleteCommandByFirstDateEntry.equals(deleteCommandByFirstDateEntry));

        // same values -> returns true
        Assert.assertTrue(
                deleteCommandByFirstDateEntry.equals(deleteCommandByFirstDateEntryCopy));

        // different types -> returns false
        Assert.assertFalse(
                deleteCommandByFirstDateEntry.equals(TypicalDates.DATE_LAST_INDEX_DATE));

        // null -> returns false
        Assert.assertFalse(
                deleteCommandByFirstDateEntry.equals(null));

        // different record -> returns false
        Assert.assertFalse(
                deleteCommandByFirstDateEntry.equals(deleteCommandByLastDateEntry));
    }


    public List<Record> filteredRecordList(Model model) {
        return model.getFilteredRecordList();
    }

    /**
     * List all the records whose date is required.
     * @param model model you want to test.
     * @param dateToDelete Date you require.
     * @return the list of records whose date is required.
     */
    public List<Record> listAllRecordToDelete (Model model, Date dateToDelete) {
        List<Record> records = filteredRecordList(model);
        List<Record> recordsToDelete = new ArrayList<>();
        for (Record recordToDelete : records) {
            if (dateToDelete == recordToDelete.getDate()) {
                recordsToDelete.add(recordToDelete);
            }
        }
        return recordsToDelete;
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     * @param model
     */
    public void showNoRecord (Model model) {
        model.updateFilteredRecordList(p -> false);
        assertTrue (model.getFilteredRecordList().isEmpty());
    }
}
