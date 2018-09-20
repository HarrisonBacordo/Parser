import java.util.ArrayList;
import java.util.List;

public class PROG implements  RobotProgramNode{
    List<STMT> statements;

    public PROG() {
        this.statements = new ArrayList<>();
    }

    public void addStatement(STMT statement) {
        this.statements.add(statement);
    }

    @Override
    public void execute(Robot robot) {

    }
}
