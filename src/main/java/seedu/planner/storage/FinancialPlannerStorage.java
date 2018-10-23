package seedu.planner.storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.planner.commons.exceptions.DataConversionException;
import seedu.planner.model.FinancialPlanner;
import seedu.planner.model.ReadOnlyFinancialPlanner;
import seedu.planner.model.record.DateBasedLimitList;
import seedu.planner.model.record.DirectoryPath;
import seedu.planner.model.record.UniqueRecordList;
import seedu.planner.model.summary.SummaryMap;

/**
 * Represents a storage for {@link FinancialPlanner}.
 */
public interface FinancialPlannerStorage {

    /**
     * Returns the file path of the data file of the record list storage.
     */
    Path getRecordListFilePath();

    /**
     * Returns the file path for the data file of the limit list storage.
     */
    Path getLimitListFilePath();

    /**
     * Returns the file path of the data file for SummaryMap storage
     */
    Path getSummaryMapFilePath();

    Path getDirectoryPathFilePath();

    // ================ Financial Planner storage methods ===========================
    /**
     * Returns FinancialPlanner data as a {@link ReadOnlyFinancialPlanner}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyFinancialPlanner> readFinancialPlanner() throws DataConversionException, IOException;

    /**
     * @see #getRecordListFilePath() and #getSummaryMapFilePath
     */
    Optional<ReadOnlyFinancialPlanner> readFinancialPlanner(Path recordListFilePath,
                                                            Path summaryListFilePath,
                                                            Path directoryPath)
            throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyFinancialPlanner} to the storage.
     * @param financialPlanner cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveFinancialPlanner(ReadOnlyFinancialPlanner financialPlanner) throws IOException;

    /**
     * @see #saveFinancialPlanner(ReadOnlyFinancialPlanner)
     */
    void saveFinancialPlanner(ReadOnlyFinancialPlanner financialPlanner, Path recordListFilePath,
                              Path summaryMapFilePath, Path directoryPathFilePath) throws IOException;

    // ================ Record List storage methods ===========================

    /**
     * Returns UniqueRecordList data as a {@link UniqueRecordList}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<UniqueRecordList> readRecordList() throws DataConversionException, IOException;

    /**
     * @see #getRecordListFilePath()
     */
    Optional<UniqueRecordList> readRecordList(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the RecordList of the given {@link ReadOnlyFinancialPlanner} to the storage.
     * @param financialPlanner cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveRecordList(ReadOnlyFinancialPlanner financialPlanner) throws IOException;

    /**
     * @see #saveRecordList(ReadOnlyFinancialPlanner)
     */
    void saveRecordList(ReadOnlyFinancialPlanner financialPlanner, Path filePath) throws IOException;

    // ================ Summary Map storage methods ===========================

    /**
     * Returns SummaryMap data as a {@link SummaryMap}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem reading from the storage.
     */
    Optional<SummaryMap> readSummaryMap() throws DataConversionException, IOException;

    /**
     * @see #getSummaryMapFilePath()
     */
    Optional<SummaryMap> readSummaryMap(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given SummaryMap of {@link ReadOnlyFinancialPlanner} to the storage.
     * @param financialPlanner cannot be null
     * @throws IOException if there was any problem writing to the file.
     */
    void saveSummaryMap(ReadOnlyFinancialPlanner financialPlanner) throws IOException;

    /**
     * @see #saveSummaryMap(ReadOnlyFinancialPlanner)
     */
    void saveSummaryMap(ReadOnlyFinancialPlanner financialPlanner, Path filePath) throws IOException;

    // ================ Limit List storage methods ===========================

    /**
     * Returns limitlist data as a limitlist.
     * @return
     * @throws DataConversionException
     * @throws IOException
     */
    Optional<DateBasedLimitList> readLimitList() throws DataConversionException, IOException;

    /**
     * see previous one
     */
    Optional<DateBasedLimitList> readLimitList(Path filePath) throws DataConversionException, IOException;

    /**
     * save the given limitlist to the storage.
     * @param limitList
     * @throws IOException
     */
    void saveLimitList(ReadOnlyFinancialPlanner limitList) throws IOException;

    /**
     * @see #saveLimitList(ReadOnlyFinancialPlanner, Path)
     */
    void saveLimitList(ReadOnlyFinancialPlanner limitList, Path filePath) throws IOException;

    // ======================================= Directory Storage methods ==============================

    /**
     * Returns DirectoryPath data as a {@link DirectoryPath}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem reading from the storage.
     */
    Optional<DirectoryPath> readDirectoryPath() throws DataConversionException, IOException;

    /**
     * @see #readDirectoryPath(Path)
     */
    Optional<DirectoryPath> readDirectoryPath(Path filePath) throws DataConversionException, IOException;

    /**
     * save the given financialPlanner to the storage.
     * @param financialPlanner
     * @throws IOException
     */
    public void saveDirectoryPath(ReadOnlyFinancialPlanner financialPlanner) throws IOException;

    /**
     * @see #saveDirectoryPath(ReadOnlyFinancialPlanner, Path)
     */
    public void saveDirectoryPath(ReadOnlyFinancialPlanner financialPlanner, Path filePath) throws IOException;
}
