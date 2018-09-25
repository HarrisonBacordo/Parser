public class ACT extends STMT {
    @Override
    public void execute(Robot robot) {

    }
}

class MOVE extends ACT {
    private EXP exp;

    public MOVE() {
    }

    public MOVE(EXP exp) {
        this.exp = exp;
    }

    @Override
    public void execute(Robot robot) {
        if (this.exp != null) {
            for (int i = 0; i < this.exp.evaluate(robot); i++) {
                robot.move();
            }
        } else {
            robot.move();
        }
    }
}

class TURNL extends ACT {
    @Override
    public void execute(Robot robot) {
        robot.turnLeft();
    }
}

class TURNR extends ACT {
    @Override
    public void execute(Robot robot) {
        robot.turnRight();
    }
}

class TAKEFUEL extends ACT {
    @Override
    public void execute(Robot robot) {
        robot.takeFuel();
    }
}

class WAIT extends ACT {
    private EXP exp;

    public WAIT() {
    }

    public WAIT(EXP exp) {
        this.exp = exp;
    }

    @Override
    public void execute(Robot robot) {
        try {
            if(this.exp != null) {
                for (int i = 0; i < this.exp.evaluate(robot); i++) {
                    robot.idleWait();
                }
            }else {
                robot.idleWait();
            }
        }catch (Exception e) {
            System.out.println(e);
        }
    }
}

class SHIELD_ON extends ACT{

    @Override
    public void execute(Robot robot) {
        robot.setShield(true);
    }
}

class SHIELD_OFF extends ACT{

    @Override
    public void execute(Robot robot) {
        robot.setShield(false);
    }
}

class TURN_AROUND extends ACT{

    @Override
    public void execute(Robot robot) {
        robot.turnAround();
    }
}