import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents job data management.
 */
public class JobData {

    // The path to the CSV data file
    private static final String DATA_FILE = "src/main/resources/job_data.csv";

    // Flag to check if data has already been loaded
    private static boolean isDataLoaded = false;

    // List to store all job data as HashMaps
    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch a list of all values from loaded data for a given column, without duplicates.
     *
     * @param field The column to retrieve values from
     * @return List of all unique values in the given field
     */
    public static ArrayList<String> findAll(String field) {

        // Load data if not already loaded
        loadData();

        // List to store unique values in the specified field
        ArrayList<String> values = new ArrayList<>();

        // Iterate through all job data
        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            // Add the value to the list if it's not already present
            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        return values;
    }

    /**
     * Fetch a list of all jobs.
     *
     * @return List of all job data as HashMaps
     */
    public static ArrayList<HashMap<String, String>> findAll() {

        // Load data if not already loaded
        loadData();

        return allJobs;
    }

    /**
     * Search for jobs based on a specific column and a search term.
     *
     * @param column The column to search in
     * @param value  The search term
     * @return List of jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // Load data if not already loaded
        loadData();

        // List to store matching jobs
        ArrayList<HashMap<String, String>> matchingJobs = new ArrayList<>();

        // Iterate through all job data
        for (HashMap<String, String> job : allJobs) {
            String columnValue = job.get(column);

            // Check if the column value contains the search term (case-insensitive)
            if (columnValue != null && columnValue.toLowerCase().contains(value.toLowerCase())) {
                matchingJobs.add(job);
            }
        }

        return matchingJobs;
    }

    /**
     * Search all columns for the given term.
     *
     * @param value The search term to look for
     * @return List of all jobs with at least one field containing the value
     */
    public static ArrayList<HashMap<String, String>> findByValue(String value) {

        // Load data if not already loaded
        loadData();

        // List to store matching jobs
        ArrayList<HashMap<String, String>> matchingJobs = new ArrayList<>();

        // Iterate through all job data
        for (HashMap<String, String> job : allJobs) {
            // Create a flag to indicate if the entire job record contains the search term
            boolean containsValue = false;

            // Iterate over all columns in the job record
            for (String column : job.keySet()) {
                String columnValue = job.get(column).toLowerCase(); // Convert to lowercase
                if (columnValue.contains(value.toLowerCase())) { // Case-insensitive comparison
                    containsValue = true;
                    break; // No need to continue checking other columns
                }
            }

            // If the entire job record contains the search term, add the job to the results
            if (containsValue) {
                matchingJobs.add(job);
            }
        }

        return matchingJobs;
    }

    /**
     * Read data from a CSV file and store it in a list of HashMaps.
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {
            // Open the CSV file and set up CSV parsing
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            // Initialize the list to store job data
            allJobs = new ArrayList<>();

            // Put the records into a more friendly format (as HashMaps)
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                // Map each header to its corresponding value in the record
                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                // Add the job data to the list
                allJobs.add(newJob);
            }

            // Set the data loaded flag to true to avoid loading data again
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }
}
