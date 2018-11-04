package seedu.planner.model.record;

import static java.util.Objects.requireNonNull;
import static seedu.planner.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.planner.commons.util.CompareUtil;
import seedu.planner.logic.commands.SortCommand;
import seedu.planner.model.record.exceptions.DuplicateRecordException;
import seedu.planner.model.record.exceptions.RecordNotFoundException;
import seedu.planner.model.tag.TagMap;

/**
 * A list of records that enforces uniqueness between its elements and does not allow nulls.
 * A record is considered unique by comparing using {@code Record#isSameRecord(Record)}. As such, adding and updating of
 * records uses Record#isSameRecord(Record) for equality so as to ensure that the record being added or updated is
 * unique in terms of identity in the UniqueRecordList. However, the removal of a record uses Record#equals(Object) so
 * as to ensure that the record with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Record#isSameRecord(Record)
 */
public class UniqueRecordList implements Iterable<Record> {

    private final ObservableList<Record> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent record as the given argument.
     */
    public boolean contains(Record toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameRecord);
    }

    /**
     * Adds a record to the list.
     * The record must not already exist in the list.
     */
    public void add(Record toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateRecordException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the record {@code target} in the list with {@code editedRecord}.
     * {@code target} must exist in the list.
     * The record identity of {@code editedRecord} must not be the same as another existing record in the list.
     */
    public void setRecord(Record target, Record editedRecord) {
        requireAllNonNull(target, editedRecord);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new RecordNotFoundException();
        }

        if (!target.isSameRecord(editedRecord) && contains(editedRecord)) {
            throw new DuplicateRecordException();
        }

        internalList.set(index, editedRecord);
    }

    /**
     * Removes the equivalent record from the list.
     * The record must exist in the list.
     */
    public void remove(Record toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new RecordNotFoundException();
        }
    }

    /**
     * Removes the equivalent records from the list.
     * The record must exist in the list.
     */
    public void removeListRecord(List<Record> recordList) {
        requireNonNull(recordList);
        for (int i = internalList.size() - 1; i >= 0; i--) {
            Record record = internalList.get(i);
            if (recordList.contains(record)) {
                remove(record);
                recordList.remove(record);
            }
        }
    }

    /**
     * Set the new UniqueRecordList.
     */
    public void setRecords(UniqueRecordList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code records}.
     * {@code records} must not contain duplicate records.
     */
    public void setRecords(List<Record> records) {
        requireAllNonNull(records);
        if (!recordsAreUnique(records)) {
            throw new DuplicateRecordException();
        }

        internalList.setAll(records);
    }

    /**
     * Sorts the contents of this list by {@code category} and in reverse if {@code ascending} is true.
     * {@code records} must not contain duplicate records.
     */
    public void sortRecords(String category, Boolean ascending) {

        switch (category) {

        case SortCommand.CATEGORY_NAME:
            if (!ascending) {
                internalList.sort(CompareUtil.compareNameAttribute().reversed());
            } else {
                internalList.sort(CompareUtil.compareNameAttribute());
            }
            break;

        case SortCommand.CATEGORY_DATE:
            if (!ascending) {
                internalList.sort(CompareUtil.compareDateAttribute().reversed());
            } else {
                internalList.sort(CompareUtil.compareDateAttribute());
            }
            break;

        case SortCommand.CATEGORY_MONEYFLOW:
        case SortCommand.CATEGORY_MONEY:
            if (!ascending) {
                internalList.sort(CompareUtil.compareMoneyflowAttribute().reversed());
            } else {
                internalList.sort(CompareUtil.compareMoneyflowAttribute());
            }
            break;

        default: break;
        }

    }

    /**
     * Creates a hashmap of the lists' contents' tags that keep tracks of the usage of each different tag
     */
    public HashMap<String, Integer> makeTagMap() {
        TagMap tagMap = new TagMap();
        for (Record record : internalList) {
            tagMap.addRecordToTagMap(record);
        }
        return tagMap.getAsReadOnlyTagMap();
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Record> asUnmodifiableObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<Record> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueRecordList // instanceof handles nulls
                        && internalList.equals(((UniqueRecordList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code records} contains only unique records.
     */
    private boolean recordsAreUnique(List<Record> records) {
        for (int i = 0; i < records.size() - 1; i++) {
            for (int j = i + 1; j < records.size(); j++) {
                if (records.get(i).isSameRecord(records.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}