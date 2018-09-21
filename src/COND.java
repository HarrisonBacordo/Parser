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
