package com.example.parkingbookingsystems.SlotFileUtil;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class SlotFileUtil {

    private static final String FILE_NAME = "selectedSlots.txt";

    // Save selected slots to file
    public static void saveSelectedSlotsToFile(Set<String> selectedSlots) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String slot : selectedSlots) {
                writer.write(slot);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load selected slots from file
    public static Set<String> loadSelectedSlotsFromFile() {
        Set<String> selectedSlots = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                selectedSlots.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return selectedSlots;
    }
}