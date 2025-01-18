import java.util.*;

public class Email implements Classifiable {
    public static final Set<String> FEATURES = Set.of("wordPercent");
    
    private Map<String, Integer> words;
    private double totalWords;

    public Email(String content) {
        this.words = new HashMap<>();
        parseContent(content);
    }

    private void parseContent(String content) {
        Scanner sc = new Scanner(content);
        while (sc.hasNext()) {
            String word = sc.next();
            if (!words.containsKey(word)) {
                words.put(word, 0);
            }
            words.put(word, words.get(word) + 1);
            totalWords++;
        }
    }

    public Set<String> getFeatures() {
        return FEATURES;
    }

    public double get(String feature) {
        String[] splitted = feature.split(Classifiable.SPLITTER);

        if (!FEATURES.contains(splitted[0])) {
            throw new IllegalArgumentException(
                String.format("Invalid feature [%s], not within possible features [%s]",
                              feature, FEATURES.toString()));
        }

        if (splitted[0].equals("wordPercent")) {
            return getWordPercentage(splitted[1]);
        }
        return 0.0; // Mandatory return statement - should never reach
    }

    private double getWordPercentage(String word) {
        return totalWords == 0 ? 0.0 : this.words.getOrDefault(word, 0) / totalWords;
    }

    public Split partition(Classifiable other) {
        if (!(other instanceof Email)) {
            throw new IllegalArgumentException("Provided 'other' not instance of Email");
        }

        Email otherEmail = (Email) other;

        Set<String> allWords = new HashSet<>(this.words.keySet());
        allWords.addAll(otherEmail.words.keySet());

        String bestWord = null;
        double highestDiff = 0;
        for (String word : allWords) {
            double diff = this.getWordPercentage(word) - otherEmail.getWordPercentage(word);
            diff = Math.abs(diff);
            if (diff > highestDiff) {
                bestWord = word;
                highestDiff = diff;
            }
        }

        double halfway = Split.midpoint(this.getWordPercentage(bestWord),
                                        otherEmail.getWordPercentage(bestWord));

        // Ensure the full feature name is used, including SPLITTER and specific word
        return new Split("wordPercent" + Classifiable.SPLITTER + bestWord, halfway);
    }

    public static Classifiable toClassifiable(List<String> row) {
        return new Email(row.get(1));
    }
}

