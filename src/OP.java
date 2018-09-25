public abstract class OP extends EXP {
    protected EXP exp1;
    protected EXP exp2;

    public OP(EXP exp1, EXP exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }
}

class ADD extends OP {

    public ADD(EXP exp1, EXP exp2) {
        super(exp1, exp2);
    }

    @Override
    public void execute(Robot robot) {
    }

    @Override
    public int evaluate(Robot robot) {
        return this.exp1.evaluate(robot) + this.exp2.evaluate(robot);
    }
}

class SUB extends OP {

    public SUB(EXP exp1, EXP exp2) {
        super(exp1, exp2);
    }

    @Override
    public void execute(Robot robot) {

    }

    @Override
    public int evaluate(Robot robot) {
        return this.exp1.evaluate(robot) - this.exp2.evaluate(robot);
    }
}

class MUL extends OP {

    public MUL(EXP exp1, EXP exp2) {
        super(exp1, exp2);
    }

    @Override
    public void execute(Robot robot) {

    }

    @Override
    public int evaluate(Robot robot) {
        return this.exp1.evaluate(robot) * this.exp2.evaluate(robot);
    }
}

class DIV extends OP {

    public DIV(EXP exp1, EXP exp2) {
        super(exp1, exp2);
    }

    @Override
    public void execute(Robot robot) {

    }

    @Override
    public int evaluate(Robot robot) {
        return this.exp1.evaluate(robot) / this.exp2.evaluate(robot);
    }
}
