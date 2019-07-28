package com.lorenzolucas.recursivedecentparser;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * File: Parser.java
 * Creates a parser that, using recursive descent, parses a GUI.
 *
 *
 * @author Lorenzo Lucas
 * @version 1.0
 * @since 2019-06-15
 */
public class Parser extends JFrame {

    private static String windowBoarder;
    private static int windowWidth;
    private static int windowHeight;
    private static int flowLayout;
    private static int gridLayout;
    private static int gridRows;
    private static int gridColumns;
    private static int horizontalGap;
    private static int verticalGap;
    private static String labelName;
    private static int textfieldWidth;
    private static StringTokenizer tokenizer;
    private static String token;
    //Creates counters to track current items
    private static int panelCount = 0;
    private static int buttonCount = 0;
    private static int radioButtonCount = 0;
    private static int currentRadioButton = 0;
    private static JPanel currentPanel;
    //Lists to track the created panels, radio buttons, and button names
    private static ArrayList<JPanel> jPanelArrayList;
    private static ArrayList<JRadioButton> jRadioButtonArrayList;
    private static ArrayList<String> radioButtonLabel;
    private static ArrayList<String> buttonName;
    private ButtonGroup buttonGroup;
    private String inputFile;

    Parser() throws FileNotFoundException, ParseError {

        String expressionFromFile = null;
        buttonName = new ArrayList<>();
        radioButtonLabel = new ArrayList<>();
        jRadioButtonArrayList = new ArrayList<>();
        jPanelArrayList = new ArrayList<>();
        //Allows user to input a file
        inputFile = JOptionPane.showInputDialog(null, "Enter A Text File");
        if (!inputFile.contains(".txt")) {
            throw new ParseError("The Was An Error In Your File Name");
        }
        //Creates new file
        File file = new File(inputFile);
        //Creates scanner
        Scanner scanner = new Scanner(file);
        //Reads the selected file
        while (scanner.hasNext()) {
            expressionFromFile += " " + scanner.next();
        }
        //Separates the string from the file into tokens
        tokenizer = new StringTokenizer(expressionFromFile);
    }

    /**
     * Reads in the tokenized file string and creates the gui layout.
     * @throws ParseError  identifies incorrect syntax errors
     */
    public void gui() throws ParseError {
        while (tokenizer.hasMoreTokens()) {
            token = tokenizer.nextToken();
            if (token.equals("Window")) {
                token = tokenizer.nextToken();
                windowBoarder = token.replaceAll("[\"]", "");
                setTitle(windowBoarder);
                token = tokenizer.nextToken();
                if (!token.contains("(")){
                    throw new ParseError("Incorrect GUI Syntax");
                }
                windowWidth = Integer.parseInt(token.replaceAll("[(,]", ""));
                token = tokenizer.nextToken();
                if (!token.contains(")")){
                    throw new ParseError("Incorrect GUI Syntax");
                }
                windowHeight = Integer.parseInt(token.replaceAll("[,)]", ""));
                setSize(windowWidth, windowHeight);
                token = tokenizer.nextToken();
                layout(token);
                //Sets the layout for the initial window
                if (flowLayout == 1) {
                    setLayout(new FlowLayout());
                } else if (gridLayout == 1) {
                    setLayout(new GridLayout(gridRows, gridColumns, horizontalGap, verticalGap));
                }
                token = tokenizer.nextToken();
                widgets(token);
                if (!token.equals("End.")) {
                    throw new ParseError("Incorrect gui Syntax");
                }
            }
        }
    }

    //Checks for correct "Layout" syntax
    private void layout(String layoutToken) throws ParseError {
        if (layoutToken.equals("Layout")) {
            layoutFormat();
        } else {
            throw new ParseError("Incorrect layout Syntax");
        }
    }

    private void layoutFormat() throws ParseError {
        token = tokenizer.nextToken();
        if (token.equals("Flow:")) {
            flowLayout = 1;
            if (currentPanel != null) {
                currentPanel.setLayout(new FlowLayout());
            }
        } else if (token.equals("Grid")) {
            gridLayout = 1;
            token = tokenizer.nextToken();
            if (!token.contains("(")){
                throw new ParseError("Incorrect Grid Layout Syntax");
            }
            gridRows = Integer.parseInt(token.replaceAll("[(),]", ""));
            token = tokenizer.nextToken();
            if (!token.contains(":")) {
                gridColumns = Integer.parseInt(token.replaceAll("[(),:]", ""));
                token = tokenizer.nextToken();
                horizontalGap = Integer.parseInt(token.replaceAll("[(),:]", ""));
                token = tokenizer.nextToken();
                if (!token.contains(")")){
                    throw new ParseError("Incorrect Grid Layout Syntax");
                }
                verticalGap = Integer.parseInt(token.replaceAll("[(),:]", ""));
                if (currentPanel != null) {
                    currentPanel.setLayout(new GridLayout(gridRows, gridColumns, horizontalGap, verticalGap));
                }
                if (!token.contains(":")) {
                    throw new ParseError("Grid layout Gaps Syntax Error");
                }
            } else if (token.contains(":")) {
                if (!token.contains(")")){
                    throw new ParseError("Incorrect Grid Layout Syntax");
                }
                gridColumns = Integer.parseInt(token.replaceAll("[(),:]", ""));
                if (currentPanel != null) {
                    currentPanel.setLayout(new GridLayout(gridRows, gridColumns));
                }
            } else {
                throw new ParseError("Grid layout Syntax Error");
            }
        } else {
            throw new ParseError("Incorrect layout Format Syntax");
        }
    }

    //Checks if the next token is a "Widgets" nonterminal
    private void widgets(String widgetsToken) throws ParseError {
        if (widgetsToken.matches("Button|Group|Label|Panel|Textfield")) {
            widget();
            token = tokenizer.nextToken();
            widgets(token);
        }
    }

    //Provides actions for a widget
    private void widget() throws ParseError {
        switch (token) {
            case "Button":
                token = tokenizer.nextToken();
                if (!token.contains(";")) {
                    throw new ParseError("Incorrect Button Syntax");
                }
                buttonName.add(token.replaceAll("[\";]", ""));
                JButton newButton = new JButton(buttonName.get(buttonCount));
                buttonCount++;
                currentPanel.add(newButton);
                break;
            case "Group":
                token = tokenizer.nextToken();
                buttonGroup = new ButtonGroup();
                //Adds radio buttons to current group
                radioButtons(token);
                if (!token.equals("End;")) {
                    throw new ParseError("Incorrect Group Syntax");
                }
                break;
            case "Label":
                token = tokenizer.nextToken();
                labelName = token.replaceAll("[\";]", "");
                JLabel label = new JLabel(labelName);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                currentPanel.add(label);
                break;
            case "Panel":
                JPanel newPanel = new JPanel();
                newPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
                jPanelArrayList.add(newPanel);
                currentPanel = jPanelArrayList.get(panelCount);
                token = tokenizer.nextToken();
                layout(token);
                //Adds layout to current panels
                if (currentPanel == jPanelArrayList.get(0)) {
                    add(currentPanel);
                } else {
                    //Adds the current panel to the previous panel in the ArrayList
                    jPanelArrayList.get(panelCount - 1).add(currentPanel);
                }
                panelCount++;
                token = tokenizer.nextToken();
                widgets(token);
                panelCount = 0;
                jPanelArrayList.clear();
                if (!token.equals("End;")) {
                    throw new ParseError("Incorrect Panel Syntax");
                }
                break;
            case "Textfield":
                token = tokenizer.nextToken();
                textfieldWidth = Integer.parseInt(token.replaceAll("[\";]", ""));
                JTextField newJTextField = new JTextField(textfieldWidth);
                //Makes sure the text field is added to the correct window or panel
                if (currentPanel != null) {
                    currentPanel.add(newJTextField);
                } else {
                    add(newJTextField);
                }
                if (!token.contains(";")) {
                    throw new ParseError("Incorrect Textfield Syntax");
                }
                break;
            default:
                throw new ParseError("Incorrect Widget Syntax");
        }
    }

    //Creates radio buttons adds them to the panel/window and adds them to a button group
    private void radioButtons(String radioButtonsToken) throws ParseError {
        if (radioButtonsToken.equals("Radio")) {
            token = tokenizer.nextToken();
            radioButton(token);
            JRadioButton radioButton = new JRadioButton(radioButtonLabel.get(currentRadioButton));
            jRadioButtonArrayList.add(radioButton);
            buttonGroup.add(jRadioButtonArrayList.get(currentRadioButton));
            if (currentPanel != null) {
                currentPanel.add(radioButton);
            } else {
                add(radioButton);
            }
            if (radioButtonCount == 0) {
                radioButton.setSelected(true);
            }
            radioButtonCount++;
            currentRadioButton++;
            token = tokenizer.nextToken();
            radioButtons(token);
            radioButtonCount = 0;
        }
    }

    //Method that creates radio button names
    private void radioButton(String radioButtonToken) throws ParseError {
        radioButtonLabel.add(radioButtonToken.replaceAll("[\";]", ""));
        if (!radioButtonToken.contains(";")) {
            throw new ParseError("Incorrect Radio Button Syntax");
        }
    }
}
