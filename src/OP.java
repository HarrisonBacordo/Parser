public abstract class OP extends EXP {
    private EXP exp1;
    private EXP exp2;

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
}

class SUB extends OP {

    public SUB(EXP exp1, EXP exp2) {
        super(exp1, exp2);
    }

    @Override
    public void execute(Robot robot) {

    }
}

class MUL extends OP {

    public MUL(EXP exp1, EXP exp2) {
        super(exp1, exp2);
    }

    @Override
    public void execute(Robot robot) {

    }
}

class DIV extends OP {

    public DIV(EXP exp1, EXP exp2) {
        super(exp1, exp2);
    }

    @Override
    public void execute(Robot robot) {

    }
}
