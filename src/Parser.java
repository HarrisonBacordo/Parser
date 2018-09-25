import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
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
    private static Pattern LOOP_TERMINAL = Pattern.compile("loop");
    private static Pattern IF_ENTRY_TERMINAL = Pattern.compile("if");
    private static Pattern IF_INTERMEDIATE_TERMINALS = Pattern.compile("elif|else");
    private static Pattern WHILE_TERMINAL = Pattern.compile("while");
    private static Pattern ACT_TERMINALS = Pattern.compile("move|turnL|turnR|turnAround|shieldOn|shieldOff|takeFuel|wait");
    private static Pattern SEN_TERMINALS = Pattern.compile("fuelLeft|oppLR|oppFB|numBarrels|barrelLR|barrelFB|wallDist");
    private static Pattern OP_TERMINALS = Pattern.compile("add|sub|mul|div");
    private static Pattern RELOP_TERMINALS = Pattern.compile("gt|lt|eq");
    private static Pattern COND_TERMINALS = Pattern.compile("and|or|not");
    // Useful Patterns
    static Pattern NUMPAT = Pattern.compile("-?\\d+"); // ("-?(0|[1-9][0-9]*)");
    static Pattern OPENPAREN = Pattern.compile("\\(");
    static Pattern CLOSEPAREN = Pattern.compile("\\)");
    static Pattern OPENBRACE = Pattern.compile("\\{");
    static Pattern CLOSEBRACE = Pattern.compile("\\}");
    static Pattern SEMICOLON = Pattern.compile("\\;");

    /**
     * PROG ::= STMT+
     */
    static RobotProgramNode parseProgram(Scanner s) {
        PROG program = new PROG();
        while (s.hasNext()) {
            program.addStatement(parseStatement(s));
        }
        return program;
    }

    private static STMT parseStatement(Scanner scan) {
        if (scan.hasNext(ACT_TERMINALS)) {
            return parseAction(scan);
        } else if (scan.hasNext(LOOP_TERMINAL)) {
            return parseLoop(scan);
        } else if (scan.hasNext(IF_ENTRY_TERMINAL)) {
            return parseIf(scan);
        } else if (scan.hasNext(WHILE_TERMINAL)) {
            return parseWhile(scan);
        } else {
            fail("Incorrect grammar: " + scan.next(), scan);
            return null;
        }
    }

    private static LOOP parseLoop(Scanner scan) {
        require(LOOP_TERMINAL, "Incorrect terminal; LOOP should be \"loop\"", scan);
        BLOCK loopBlock = parseBlock(scan);
        return new LOOP(loopBlock);
    }

    private static IF parseIf(Scanner scan) {
        require(IF_ENTRY_TERMINAL, "Incorrect terminal; IF should be \"if\"", scan);
        require(OPENPAREN, "Missing open parenthesis", scan);
        COND cond = null;
        if (scan.hasNext(RELOP_TERMINALS) || scan.hasNext(COND_TERMINALS)) {
            cond = parseCondition(scan);
        } else {
            fail("Expected condition", scan);
        }
        require(CLOSEPAREN, "Missing close parenthesis", scan);

        BLOCK ifBlock = parseBlock(scan);
        if (checkFor(IF_INTERMEDIATE_TERMINALS, scan)) {
            BLOCK elseBlock = parseBlock(scan);
            return new IF(cond, ifBlock, elseBlock);
        }
        return new IF(cond, ifBlock);
    }

    private static WHILE parseWhile(Scanner scan) {
        require(WHILE_TERMINAL, "Incorrect terminal; WHILE should be \"while\"", scan);
        require(OPENPAREN, "Missing open parenthesis", scan);
//        cond
        COND cond = null;
        if (scan.hasNext(RELOP_TERMINALS) || scan.hasNext(COND_TERMINALS)) {
            cond = parseCondition(scan);
        } else {
            fail("Expected condition", scan);
        }
        require(CLOSEPAREN, "Missing close parenthesis", scan);

//        block
        BLOCK block = parseBlock(scan);

        return new WHILE(cond, block);
    }

    private static ASSGN parseAssign(Scanner scan) {
        return null;
    }

    private static BLOCK parseBlock(Scanner scan) {
        require(OPENBRACE, "Missing open bracket", scan);
        BLOCK block = new BLOCK();
        while (scan.hasNext() && !scan.hasNext(CLOSEBRACE)) {
            block.addStatement(parseStatement(scan));
        }
        require(CLOSEBRACE, "Missing close bracket", scan);
        if (block.getStatements().size() == 0) {
            fail("Empty loops block", scan);
        }
        return block;
    }

    private static ACT parseAction(Scanner scan) {
        String scannedAction = scan.next();
        ACT action = null;

        switch (scannedAction) {
            case "move":
                if (checkFor(OPENPAREN, scan)) {
                    EXP exp = parseExpression(scan);
                    require(CLOSEPAREN, "Missing close parenthesis", scan);
                    action = new MOVE(exp);
                } else {
                    action = new MOVE();
                }
                break;
            case "turnL":
                action = new TURNL();
                break;
            case "turnR":
                action = new TURNR();
                break;
            case "takeFuel":
                action = new TAKEFUEL();
                break;
            case "wait":
                if (checkFor(OPENPAREN, scan)) {
                    EXP exp = parseExpression(scan);
                    require(CLOSEPAREN, "Missing close parenthesis", scan);
                    action = new WAIT(exp);
                } else {
                    action = new WAIT();
                }
                break;
            case "shieldOn":
                action = new SHIELD_ON();
                break;
            case "shieldOff":
                action = new SHIELD_OFF();
                break;
            case "turnAround":
                action = new TURN_AROUND();
                break;
        }
        require(SEMICOLON, "Missing semicolon", scan);
        return action;
    }

    private static EXP parseExpression(Scanner scan) {
        if (scan.hasNext(OP_TERMINALS)) {
            return parseOperator(scan);
        } else if (scan.hasNext(NUMPAT)) {
            return new NUM(requireInt(NUMPAT, "Int required", scan));
        } else if (scan.hasNext(SEN_TERMINALS)) {
            return parseSensor(scan);
        }
        fail("Incorrect grammar for expression", scan);
        return null;
    }

    private static OP parseOperator(Scanner scan) {
        OP op = null;
        EXP exp1;
        EXP exp2;
        switch (scan.next()) {
            case "add":
                require(OPENPAREN, "Missing open parenthesis", scan);
                exp1 = parseExpression(scan);
                require(",", "missing \",\"", scan);
                exp2 = parseExpression(scan);
                op = new ADD(exp1, exp2);
                break;
            case "sub":
                require(OPENPAREN, "Missing open parenthesis", scan);
                exp1 = parseExpression(scan);
                require(",", "missing \",\"", scan);
                exp2 = parseExpression(scan);
                op = new SUB(exp1, exp2);
                break;
            case "mul":
                require(OPENPAREN, "Missing open parenthesis", scan);
                exp1 = parseExpression(scan);
                require(",", "missing \",\"", scan);
                exp2 = parseExpression(scan);
                op = new MUL(exp1, exp2);
                break;
            case "div":
                require(OPENPAREN, "Missing open parenthesis", scan);
                exp1 = parseExpression(scan);
                require(",", "missing \",\"", scan);
                exp2 = parseExpression(scan);
                op = new DIV(exp1, exp2);
                break;
            default:
                fail("Incorrect EXP grammar: ", scan);
        }
        require(CLOSEPAREN, "Missing close parenthesis", scan);
        return op;
    }

    private static SEN parseSensor(Scanner scan) {
        if (scan.hasNext(SEN_TERMINALS)) {
            String s = scan.next();
            switch (s) {
                case "fuelLeft":
                    return new FUEL_LEFT();

                case "oppLR":
                    return new OPP_LR();

                case "oppFB":
                    return new OPP_FB();

                case "numBarrels":
                    return new NUM_BARRELS();

                case "barrelLR":
                    if (checkFor(OPENPAREN, scan)) {
                        EXP exp = parseExpression(scan);
                        require(CLOSEPAREN, "Missing close parenthesis", scan);
                        return new BARREL_LR(exp);
                    }
                    return new BARREL_LR();

                case "barrelFB":
                    if (checkFor(OPENPAREN, scan)) {
                        EXP exp = parseExpression(scan);
                        require(CLOSEPAREN, "Missing close parenthesis", scan);
                        return new BARREL_FB(exp);
                    }
                    return new BARREL_FB();

                case "wallDist":
                    return new WALL_DIST();
                default:
                    fail("Incorrect grammar for SEN", scan);
                    return null;
            }

        }
        fail("Not a valid Sensor", scan);
        return null;
    }

    private static COND parseCondition(Scanner scan) {
        COND cond = null;
        if (scan.hasNext(RELOP_TERMINALS)) {
            EXP exp1;
            EXP exp2;
            switch (scan.next()) {
                case "lt":
                    require(OPENPAREN, "Missing open parenthesis", scan);
                    exp1 = parseExpression(scan);
                    require(",", "missing \",\"", scan);
                    exp2 = parseExpression(scan);
                    cond = new COND(COND.RelOp.LT, exp1, exp2);
                    break;
                case "gt":
                    require(OPENPAREN, "Missing open parenthesis", scan);
                    exp1 = parseExpression(scan);
                    require(",", "missing \",\"", scan);
                    exp2 = parseExpression(scan);
                    cond = new COND(COND.RelOp.GT, exp1, exp2);
                    break;
                case "eq":
                    require(OPENPAREN, "Missing open parenthesis", scan);
                    exp1 = parseExpression(scan);
                    require(",", "missing \",\"", scan);
                    exp2 = parseExpression(scan);
                    cond = new COND(COND.RelOp.EQ, exp1, exp2);
                    break;
                default:
                    fail("Incorrect grammar for COND: ", scan);
            }
        } else if (scan.hasNext(COND_TERMINALS)) {
            COND cond1;
            COND cond2;
            switch (scan.next()) {
                case "and":
                    require(OPENPAREN, "Missing open parenthesis", scan);
                    cond1 = parseCondition(scan);
                    require(",", "missing \",\"", scan);
                    cond2 = parseCondition(scan);
                    cond = new COND(COND.CondOp.AND, cond1, cond2);
                    break;
                case "or":
                    require(OPENPAREN, "Missing open parenthesis", scan);
                    cond1 = parseCondition(scan);
                    require(",", "missing \",\"", scan);
                    cond2 = parseCondition(scan);
                    cond = new COND(COND.CondOp.OR, cond1, cond2);
                    break;
                case "not":
                    require(OPENPAREN, "Missing open parenthesis", scan);
                    cond1 = parseCondition(scan);
                    cond = new COND(COND.CondOp.NOT, cond1);
                    break;
                default:
                    fail("Incorrect grammar for COND: ", scan);
            }
        }
        require(CLOSEPAREN, "Missing close parenthesis", scan);
        return cond;
    }

    private static VAR parseVariable(Scanner scan) {
        return null;
    }

    private static NUM parseNumber(Scanner scan) {
        return new NUM(requireInt(NUMPAT, "Missing int", scan));
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
