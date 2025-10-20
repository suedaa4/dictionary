
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DictionaryManager {

    private static final String FILE_NAME = "personal_dictionary.txt";

    private Map<String, String> dictionary = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        DictionaryManager manager = new DictionaryManager();
        manager.start();
    }

    public void start() {
        loadDictionary();
        displayMenu();
    }

    private void displayMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Personal Dictionary Manager ---");
            System.out.println("1. Add Entry");
            System.out.println("2. Search Entry");
            System.out.println("3. Remove Entry");
            System.out.println("4. List All Entries");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        addEntry();
                        break;
                    case "2":
                        searchEntry();
                        break;
                    case "3":
                        removeEntry();
                        break;
                    case "4":
                        listAllEntries();
                        break;
                    case "5":
                        saveDictionary();
                        running = false;
                        System.out.println("Dictionary saved. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private void addEntry() {
        System.out.print("Enter word to add: ");
        String word = scanner.nextLine().trim().toLowerCase();

        if (dictionary.containsKey(word)) {
            System.out.println("Word already exists. Current meaning: " + dictionary.get(word));
            System.out.print("Do you want to update the meaning? (y/n): ");
            if (!scanner.nextLine().trim().toLowerCase().equals("y")) {
                return;
            }
        }

        System.out.print("Enter meaning for '" + word + "': ");
        String meaning = scanner.nextLine().trim();

        dictionary.put(word, meaning);
        System.out.println("Entry added/updated successfully.");
    }

    private void searchEntry() {
        System.out.print("Enter word to search: ");
        String word = scanner.nextLine().trim().toLowerCase();

        String meaning = dictionary.get(word);
        if (meaning != null) {
            System.out.println("Meaning of '" + word + "': " + meaning);
        } else {
            System.out.println("Word '" + word + "' not found in dictionary.");
        }
    }

    private void removeEntry() {
        System.out.print("Enter word to remove: ");
        String word = scanner.nextLine().trim().toLowerCase();

        if (dictionary.containsKey(word)) {
            dictionary.remove(word);
            System.out.println("Word '" + word + "' successfully removed.");
        } else {
            System.out.println("Word '" + word + "' not found.");
        }
    }

    private void listAllEntries() {
        if (dictionary.isEmpty()) {
            System.out.println("The dictionary is empty.");
            return;
        }

        System.out.println("\n--- All Dictionary Entries ---");

        dictionary.keySet().stream().sorted().forEach(word -> {

            DictionaryEntry entry = new DictionaryEntry(word, dictionary.get(word));
            System.out.println(entry);
        });
        System.out.println("Total entries: " + dictionary.size());
    }

    private void loadDictionary() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {

                if (line.contains(":")) {
                    String[] parts = line.split(":", 2);
                    String word = parts[0].trim().toLowerCase();
                    String meaning = parts[1].trim();
                    dictionary.put(word, meaning);
                }
            }
            System.out.println("Dictionary loaded successfully from " + FILE_NAME + ".");
        } catch (FileNotFoundException e) {
            System.out.println("Dictionary file not found. Starting with an empty dictionary.");
        } catch (IOException e) {
            System.err.println("Error reading dictionary file: " + e.getMessage());
        }
    }

    private void saveDictionary() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, String> entry : dictionary.entrySet()) {

                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }

        } catch (IOException e) {
            System.err.println("Error saving dictionary file: " + e.getMessage());
        }
    }
}
