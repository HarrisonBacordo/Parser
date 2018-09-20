import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * The parser and interpreter. The top level parse function, a main method for
 * testing, and several utility methods are provided. You need to implement
 * parseProgram and all the rest of the parser.
 */
public class Parser {

    /**
     * Top level parse method, called by the World
     */
    static RobotProgramNode parseFile(File code) {
        Scanner scan = null;


        try {
            scan = new Scanner(code);

            // the only time tokens can be next to each other is
            // when one of them is one of (){},;
            scan.useDelimiter("\\s+|(?=[{}(),;])|(?<=[{}(),;])");

            RobotProgramNode n = parseProgram(scan); // You need to implement this!!!

            scan.close();
            return n;
        } catch (FileNotFoundException e) {
            System.out.println("Robot program source file not found");
        } catch (ParserFailureException e) {
            System.out.println("Parser error:");
            System.out.println(e.getMessage());
            scan.close();
        }
        return null;
    }

    /**
     * For testing the parser without requiring the world
     */

    public static void main(String[] args) {
        if (args.length > 0) {
            for (String arg : args) {
                File f = new File(arg);
                if (f.exists()) {
                    System.out.println("Parsing '" + f + "'");
                    RobotProgramNode prog = parseFile(f);
                    System.out.println("Parsing completed ");
                    if (prog != null) {
                        System.out.println("================\nProgram:");
                        System.out.println(prog);
                    }
                    System.out.println("=================");
                } else {
                    System.out.println("Can't find file '" + f + "'");
                }
            }
        } else {
            while (true) {
                JFileChooser chooser = new JFileChooser(".");// System.getProperty("user.dir"));
                int res = chooser.showOpenDialog(null);
                if (res != JFileChooser.APPROVE_OPTION) {
                    break;
                }
                RobotProgramNode prog = parseFile(chooser.getSelectedFile());
                System.out.println("Parsing completed");
                if (prog != null) {
                    System.out.println("Program: \n" + prog);
                }
                System.out.println("=================");
            }
        }
        System.out.println("Done");
    }

//    Constants
    static Pattern LOOP_TERMINAL = Pattern.compile("loop");
    static Pattern IF_ENTRY_TERMINAL = Pattern.compile("if");
    static Pattern IF_INTERMEDIATE_TERMINALS = Pattern.compile("elif|else");
    static Pattern WHILE_TERMINAL = Pattern.compile("while");
    static Pattern ACT_TERMINALS = Pattern.compile("move|turnL|turnR|turnAround|shieldOn|shieldOff|takeFuel|wait");
    static Pattern SEN_TERMINALS = Pattern.compile("fuelLeft|oppLR|oppFB|numBarrels|barrelLR|barrelFB|wallDist");
    static Pattern OP_TERMINALS = Pattern.compile("add|sub|mul|div");
    static Pattern RELOP_TERMINALS = Pattern.compile("gt|lt|eq");
    // Useful Patterns
    static Pattern NUMPAT = Pattern.compile("-?\\d+"); // ("-?(0|[1-9][0-9]*)");
    static Pattern OPENPAREN = Pattern.compile("\\(");
    static Pattern CLOSEPAREN = Pattern.compile("\\)");
    static Pattern OPENBRACE = Pattern.compile("\\{");
    static Pattern CLOSEBRACE = Pattern.compile("\\}");

    /**
     * PROG ::= STMT+
     */
    static RobotProgramNode parseProgram(Scanner s) {
        RobotProgramNode robotProgramNode;
        PROG program = new PROG()
        while (s.hasNext()) {
            program.addStatement(parseStatement(s));
        }
        if (!s.hasNext()) { return null; }
        return null;
    }

    private static STMT parseStatement(Scanner scan) {
        STMT statement = null;
        if (scan.hasNext(ACT_TERMINALS)) { return parseAction(scan); }
        else if (scan.hasNext(LOOP_TERMINAL)) { return parseLoop(scan); }
        else if (scan.hasNext(IF_ENTRY_TERMINAL)) { return parseIf(scan); }
        else if (scan.hasNext(WHILE_TERMINAL)) { return parseWhile(scan); }
        else if (scan.hasNext(ASS)) { return parseIf(scan); }
    }

    private static LOOP parseLoop(Scanner scan) {
        return null;
    }

    private static IF parseIf(Scanner scan) {
        return null;
    }

    private static WHILE parseWhile(Scanner scan) {
        return null;
    }

    private static ASSGN parseAssign(Scanner scan) {
        return null;
    }

    private static BLOCK parseBlock(Scanner scan) {
        return null;
    }

    private static ACT parseAction(Scanner scan) {
        return null;
    }

    private static EXP parseExpression(Scanner scan) {
        return null;
    }

    private static SEN parseSensor(Scanner scan) {
        return null;
    }

    private static OP parseOperation(Scanner scan) {
        return null;
    }

    private static COND parseCondition(Scanner scan) {
        return null;
    }

    private static RELOP parseRelationalOperator(Scanner scan) {
        return null;
    }

    private static VAR parseVariable(Scanner scan) {
        return null;
    }

    private static NUM parseNumber(Scanner scan) {
        return null;
    }




    // utility methods for the parser

    /**
     * Report a failure in the parser.
     */
    static void fail(String message, Scanner s) {
        String msg = message + "\n   @ ...";
        for (int i = 0; i < 5 && s.hasNext(); i++) {
            msg += " " + s.next();
        }
        throw new ParserFailureException(msg + "...");
    }

    /**
     * Requires that the next token matches a pattern if it matches, it consumes
     * and returns the token, if not, it throws an exception with an error
     * message
     */
    static String require(String p, String message, Scanner s) {
        if (s.hasNext(p)) {
            return s.next();
        }
        fail(message, s);
        return null;
    }

    static String require(Pattern p, String message, Scanner s) {
        if (s.hasNext(p)) {
            return s.next();
        }
        fail(message, s);
        return null;
    }

    /**
     * Requires that the next token matches a pattern (which should only match a
     * number) if it matches, it consumes and returns the token as an integer if
     * not, it throws an exception with an error message
     */
    static int requireInt(String p, String message, Scanner s) {
        if (s.hasNext(p) && s.hasNextInt()) {
            return s.nextInt();
        }
        fail(message, s);
        return -1;
    }

    static int requireInt(Pattern p, String message, Scanner s) {
        if (s.hasNext(p) && s.hasNextInt()) {
            return s.nextInt();
        }
        fail(message, s);
        return -1;
    }

    /**
     * Checks whether the next token in the scanner matches the specified
     * pattern, if so, consumes the token and return true. Otherwise returns
     * false without consuming anything.
     */
    static boolean checkFor(String p, Scanner s) {
        if (s.hasNext(p)) {
            s.next();
            return true;
        } else {
            return false;
        }
    }

    static boolean checkFor(Pattern p, Scanner s) {
        if (s.hasNext(p)) {
            s.next();
            return true;
        } else {
            return false;
        }
    }

}

// You could add the node classes here, as long as they are not declared public (or private)
