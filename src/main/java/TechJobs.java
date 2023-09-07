import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The main class for LaunchCode's TechJobs App.
 */
public class TechJobs {

    static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {

        // Initialize a map to store column choices and their descriptions
        HashMap<String, String> columnChoices = new HashMap<>();
        columnChoices.put("core competency", "Skill");
        columnChoices.put("employer", "Employer");
        columnChoices.put("location", "Location");
        columnChoices.put("position type", "Position Type");
        columnChoices.put("all", "All");

        // Initialize a map to store action choices and their descriptions
        HashMap<String, String> actionChoices = new HashMap<>();
        actionChoices.put("search", "Search");
        actionChoices.put("list", "List");

        // Display a welcome message
        System.out.println("Welcome to LaunchCode's TechJobs App!");

        // Allow the user to interact with the application until they choose to quit
        while (true) {

            // Prompt the user to choose an action (search or list)
            String actionChoice = getUserSelection("View jobs by (type 'x' to quit):", actionChoices);

            if (actionChoice == null) {
                // Exit the program if the user types 'x'
                break;
            } else if (actionChoice.equals("list")) {

                // If the user chooses to list, prompt them to select a column
                String columnChoice = getUserSelection("List", columnChoices);

                if (columnChoice.equals("all")) {
                    // If the user chooses to list all, print all job data
                    printJobs(JobData.findAll());
                } else {

                    // Retrieve and print a list of specific column values
                    ArrayList<String> results = JobData.findAll(columnChoice);

                    System.out.println("\n*** All " + columnChoices.get(columnChoice) + " Values ***");

                    // Print list of skills, employers, etc
                    for (String item : results) {
                        System.out.println(item);
                    }
                }

            } else { // Choice is "search"

                // Prompt the user to choose a search field (e.g., by skill or employer)
                String searchField = getUserSelection("Search by:", columnChoices);

                // Prompt the user to enter their search term
                System.out.println("\nSearch term:");
                String searchTerm = in.nextLine();

                if (searchField.equals("all")) {
                    // If the user chooses to search in all columns, print matching jobs
                    ArrayList<HashMap<String, String>> matchingJobs = JobData.findByValue(searchTerm);
                    printJobs(matchingJobs);
                } else {
                    // Search for jobs based on the selected column and search term
                    printJobs(JobData.findByColumnAndValue(searchField, searchTerm));
                }
            }
        }
    }

    /**
     * Helper method to get the user's selection from a list of choices.
     * @param menuHeader The header to display for the menu.
     * @param choices A map of available choices.
     * @return The user's selected choice.
     */
    private static String getUserSelection(String menuHeader, HashMap<String, String> choices) {

        int choiceIdx = -1;
        Boolean validChoice = false;
        String[] choiceKeys = new String[choices.size()];

        // Put the choices in an ordered structure so we can
        // associate an integer with each one
        int i = 0;
        for (String choiceKey : choices.keySet()) {
            choiceKeys[i] = choiceKey;
            i++;
        }

        do {

            System.out.println("\n" + menuHeader);

            // Print available choices
            for (int j = 0; j < choiceKeys.length; j++) {
                System.out.println("" + j + " - " + choices.get(choiceKeys[j]));
            }

            if (in.hasNextInt()) {
                choiceIdx = in.nextInt();
                in.nextLine();
            } else {
                String line = in.nextLine();
                boolean shouldQuit = line.equals("x");
                if (shouldQuit) {
                    return null;
                }
            }

            // Validate user's input
            if (choiceIdx < 0 || choiceIdx >= choiceKeys.length) {
                System.out.println("Invalid choice. Try again.");
            } else {
                validChoice = true;
            }

        } while (!validChoice);

        return choiceKeys[choiceIdx];
    }

    /**
     * Print a list of jobs with their details.
     * @param someJobs The list of jobs to print.
     */
    private static void printJobs(ArrayList<HashMap<String, String>> someJobs) {
        if (someJobs.isEmpty()) {
            System.out.println("No Results");
        } else {
            for (HashMap<String, String> job : someJobs) {
                System.out.println("*****");
                for (Map.Entry<String, String> entry : job.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    System.out.println(key + ": " + value);
                }
                System.out.println("*****\n");
            }
        }
    }
}
