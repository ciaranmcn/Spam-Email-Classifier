# Spam Email Classifier

This project implements a machine learning classifier for determining whether emails are spam or ham (non-spam) using decision trees. The implementation is in Java and follows an object-oriented design.

## Features

- **Email Parsing**: Processes email content to calculate word frequency percentages.
- **Binary Classification**: Classifies emails as "spam" or "ham."
- **Decision Tree Model**: Utilizes a decision tree for classification, with functionality to train, test, and save the model.
- **Customizable Features**: Easily extendable to include additional classification features.

## Project Structure

- **Source Files**:
  - `Email.java`: Represents an email and computes features like word frequency percentages.
  - `Classifiable.java`: Interface for data types that can be classified.
  - `Split.java`: Represents a split criterion for the decision tree.
  - `DataLoader.java`: Loads and shuffles data from CSV files.
  - `ClassificationTree.java`: Implements the decision tree classifier.
  - `Classifier.java`: Abstract class providing methods for classification and accuracy calculation.
  - `CsvReader.java`: Utility class for reading CSV files.
  - `Client.java`: Provides a command-line interface for interacting with the classifier.

- **Data Files**:
  - `train.csv`: Training dataset with labeled examples.
  - `test.csv`: Testing dataset for evaluating the model.
  - `simple.txt`, `medium.txt`, `large.txt`: Example saved decision trees.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 11 or higher
- A text editor or IDE for Java development (e.g., IntelliJ IDEA, Eclipse)

### Running the Program

1. **Compile the Project**:
   ```bash
   javac *.java

