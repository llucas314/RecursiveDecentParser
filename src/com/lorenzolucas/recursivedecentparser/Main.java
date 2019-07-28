package com.lorenzolucas.recursivedecentparser;
import javax.swing.*;
import java.io.FileNotFoundException;

/**
 * This program parses, using recursive descent, a GUI definition language defined in an input file
 * and generates the GUI that it defines.
 * The grammar for this language is defined below:
 * <ul>
 *     <li>
 *         gui ::=
 *     <ul>
 *         <li>Window STRING '(' NUMBER ',' NUMBER ')' layout widgets End '.'</li>
 *     </ul>
 *     </li>
 *     <li>
 *         layout ::=
 *         <ul>
 *             <li>
 *                Layout layout_type ':'
 *             </li>
 *         </ul>
 *     </li>
 *     <li>
 *         layout_type ::=
 *         <ul>
 *             <li>
 *                 Flow |
 *             </li>
 *             <li>
 *                 Grid '(' NUMBER ',' NUMBER [',' NUMBER ',' NUMBER] ')'
 *             </li>
 *         </ul>
 *     </li>
 *     <li>
 *         widgets ::=
 *         <ul>
 *             <li>
 *                 widget widgets |
 *             </li>
 *             <li>
 *                 widget
 *             </li>
 *         </ul>
 *     </li>
 *     <li>
 *         widget ::=
 *         <ul>
 *             <li>
 *                 Button STRING ';' |
 *             </li>
 *             <li>
 *                 Group radio_buttons End ';' |
 *             </li>
 *             <li>
 *                 Label STRING ';' |
 *             </li>
 *             <li>
 *                 Panel layout widgets End ';' |
 *             </li>
 *             <li>
 *                 Textfield NUMBER ';'
 *             </li>
 *         </ul>
 *     </li>
 *     <li>
 *         radio_buttons ::=
 *         <ul>
 *             <li>
 *                 radio_button radio_buttons |
 *             </li>
 *             <li>
 *                 radio_button
 *             </li>
 *         </ul>
 *     </li>
 *     <li>
 *         radio_button ::=
 *         <ul>
 *             <li>
 *                 Radio STRING ';'
 *             </li>
 *         </ul>
 *     </li>
 * </ul>
 *
 *
 * @author Lorenzo Lucas
 * @version 1.0
 * @since 2019-06-15
 */
public class Main {
    /**
     * Calls the Parser class which creates the gui.
     * @param args
     */
    public static void main(String[] args) {
        Parser parser;
        try {
             parser = new Parser();
            parser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            parser.setLocationRelativeTo(null);
             parser.gui();
            parser.setVisible(true);
        } catch (FileNotFoundException | ParseError e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
