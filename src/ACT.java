public class ACT extends STMT{
    @Override
    public void execute(Robot robot) {

    }
}

class MOVE extends ACT {
    private EXP exp;

    public MOVE() {}

    public MOVE(EXP exp) {
        this.exp = exp;
    }
}

class TURNL extends ACT {

}

class TURNR extends ACT {

}

class TAKEFUEL extends  ACT {

}

class WAIT extends ACT {
    private EXP exp;

    public WAIT() {}

    public WAIT(EXP exp) {
        this.exp = exp;
    }
}