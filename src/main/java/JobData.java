import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LaunchCode
 */
public class JobData {

    // Below are variables we're declaring.

    private static final String DATA_FILE = "src/main/resources/job_data.csv";
    private static boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    public static ArrayList<HashMap<String, String>> getAllJobs() {
        return allJobs;
    }

    public static void setAllJobs(ArrayList<HashMap<String, String>> allJobs) {
        JobData.allJobs = allJobs;
    }

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue.toLowerCase())) {
                values.add(aValue);
            }
        }

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        return allJobs;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>(); // creating a new ArrayList to shove search results into

        for (HashMap<String, String> row : allJobs) { // for each row in allJobs

            String aValue = row.get(column); // get the column and call it aValue

            if (aValue.toLowerCase().contains(value.toLowerCase())) { // if the column contains the search term,...
                jobs.add(row); // ... add the row to the new ArrayList
            }
        }

        return jobs; // return the new ArrayList
    }

    /**
     * Search all columns for the given term
     *
     * @param value The search term to look for
     * @return      List of all jobs with at least one field containing the value
     */
    public static ArrayList<HashMap<String, String>> findByValue(String value) {

        // load data, if not already loaded
        loadData();

        // TODO - implement this method
        // Want to search ALL columns for value.

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>(); // creating a new ArrayList to shove search results into

        for (HashMap<String, String> row : allJobs) { // for each row in allJobs

            for (String aValue : row.values()) {// look in every value of the HashMap for the search term

                if (aValue.toLowerCase().contains(value.toLowerCase()) && !jobs.contains(row)) { // if the value of the HashMap contains the search term AND if the row doesn't ALREADY exist in jobs,...
                    jobs.add(row); // ... add the HashMap row to the new ArrayList

                }
            }
        }

        return jobs; // return the new ArrayList

    }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>(); // So is this creating a brand-new hashmap for every single job? Yes, that's exactly what it's doing.

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob); // And then it puts each hashmap into the arraylist.
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}
