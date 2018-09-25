public class COND implements RobotProgramNode{
    private CondOp condOp;
    private COND cond1;
    private COND cond2;
    private RelOp relop;
    private EXP exp1;
    private EXP exp2;


    public COND(RelOp relop, EXP exp1, EXP exp2) {
        this.relop = relop;
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    public COND(CondOp condOp, COND cond1, COND cond2) {
        this.condOp = condOp;
        this.cond1 = cond1;
        this.cond2 = cond2;
    }

    public COND(CondOp condOp, COND cond1) {
        assert condOp == CondOp.NOT;
        this.condOp = condOp;
        this.cond1 = cond1;
        this.cond2 = null;
    }

    @Override
    public void execute(Robot robot) {
    }

    public boolean evaluate(Robot robot) {
        if (this.condOp != null) {
            switch (condOp) {
                case AND:
                    return (this.cond1.evaluate(robot) && this.cond2.evaluate(robot));
                case OR:
                    return (this.cond1.evaluate(robot) || this.cond2.evaluate(robot));
                case NOT:
                    return !this.cond1.evaluate(robot);
                default:
                    break;
            }
        } else {
            switch (this.relop) {
                case EQ:
                    return this.exp1.evaluate(robot) == this.exp2.evaluate(robot);
                case GT:
                    return this.exp1.evaluate(robot) > this.exp2.evaluate(robot);
                case LT:
                    return this.exp1.evaluate(robot) < this.exp2.evaluate(robot);

            }
        }
        return false;
    }

    public enum CondOp {
        AND,
        OR,
        NOT
    }

    public enum RelOp {
        LT,
        GT,
        EQ
    }
}
