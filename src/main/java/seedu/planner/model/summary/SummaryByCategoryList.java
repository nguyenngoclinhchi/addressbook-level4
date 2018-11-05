package seedu.planner.model.summary;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.planner.commons.util.CompareUtil;
import seedu.planner.commons.util.MoneyUtil;
import seedu.planner.model.record.MoneyFlow;
import seedu.planner.model.record.Record;
import seedu.planner.model.tag.Tag;
import seedu.planner.ui.SummaryEntry;
//@@author tenvinc

/**
 * This class represents a list containing all Summary objects computed from a given list of records
 * and a predicate criteria. The internal implementation is a HashMap but it returns a list
 * and implements only list functions
 */
public class SummaryByCategoryList {
    private HashMap<Set<Tag>, Summary<Set<Tag>>> summaryMap = new HashMap<>();

    private MoneyFlow total = new MoneyFlow("-0");
    private MoneyFlow totalIncome = new MoneyFlow("-0");
    private MoneyFlow totalExpense = new MoneyFlow("-0");

    public SummaryByCategoryList(List<Record> recordList) {
        requireNonNull(recordList);
        for (Record r : recordList) {
            addRecordToMap(r);
            updateTotals(r);
        }
    }

    public ObservableList<SummaryEntry> getSummaryList() {

        List<SummaryEntry> list = summaryMap.keySet().stream().sorted(CompareUtil.compareTags())
                .map(k -> SummaryEntry.convertToUiFriendly(summaryMap.get(k)))
                .collect(Collectors.toList());
        return FXCollections.observableList(list);
    }

    public HashMap<Set<Tag>, Summary<Set<Tag>>> getSummaryMap() {
        return summaryMap;
    }

    /** Adds a record to the {@code summaryMap} while following some rules.
     * If there exists a summary with {@code Date} of record, then record is added to the summary.
     * Else, it creates a summary with the details of the record.
     * @param record given record
     * @see Summary#add(Record)
     */
    private void addRecordToMap(Record record) {
        Set<Tag> tags = record.getTags();
        if (summaryMap.containsKey(tags)) {
            summaryMap.get(tags).add(record);
        } else {
            summaryMap.put(tags, new Summary<>(record, tags));
        }
    }

    /** Update the total moneyflow, total income and total expense */
    private void updateTotals(Record record) {
        MoneyFlow money = record.getMoneyFlow();
        if (isExpense(money)) {
            totalExpense = MoneyUtil.add(totalExpense, money);
        } else {
            totalIncome = MoneyUtil.add(totalIncome, money);
        }
        total = MoneyUtil.add(total, money);
    }

    private boolean isExpense(MoneyFlow money) {
        return money.toDouble() < 0;
    }

    public int size() {
        return summaryMap.size();
    }

    public MoneyFlow getTotal() {
        return total;
    }

    public MoneyFlow getTotalIncome() {
        return totalIncome;
    }

    public MoneyFlow getTotalExpense() {
        return totalExpense;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SummaryByCategoryList // instanceof handles nulls
                && summaryMap.equals(((SummaryByCategoryList) other).summaryMap));
    }
}
