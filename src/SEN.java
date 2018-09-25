public class SEN extends EXP {
    @Override
    public void execute(Robot robot) {

    }

    @Override
    public int evaluate(Robot robot) {
        return 0;
    }
}

class FUEL_LEFT extends SEN {
    @Override
    public int evaluate(Robot robot) {
        return robot.getFuel();
    }
}

class OPP_LR extends SEN {
    @Override
    public int evaluate(Robot robot) {
        return robot.getOpponentLR();
    }
}

class OPP_FB extends SEN {
    @Override
    public int evaluate(Robot robot) {
        return robot.getOpponentFB();
    }
}

class NUM_BARRELS extends SEN {
    @Override
    public int evaluate(Robot robot) {
        return robot.numBarrels();
    }
}

class BARREL_LR extends SEN {
    private EXP exp;

    public BARREL_LR() {
    }

    public BARREL_LR(EXP exp) {
        this.exp = exp;
    }

    @Override
    public int evaluate(Robot robot) {
        if (this.exp != null) {
            return robot.getBarrelLR(this.exp.evaluate(robot));
        }
        return robot.getClosestBarrelLR();
    }
}

class BARREL_FB extends SEN {
    private EXP exp;

    public BARREL_FB() {
    }

    public BARREL_FB(EXP exp) {
        this.exp = exp;
    }

    @Override
    public int evaluate(Robot robot) {
        if (this.exp != null) {
            return robot.getBarrelFB(this.exp.evaluate(robot));
        }
        return robot.getClosestBarrelFB();
    }
}

class WALL_DIST extends SEN {
    @Override
    public int evaluate(Robot robot) {
        return robot.getDistanceToWall();
    }
}

