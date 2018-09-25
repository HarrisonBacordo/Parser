import java.util.ArrayList;
import java.util.List;

public class BLOCK implements RobotProgramNode{
    private List<STMT> statements;

    public BLOCK() {
        this.statements = new ArrayList<>();
    }

    public List getStatements() { return this.statements; }


    public void addStatement(STMT statement) {
        this.statements.add(statement);
    }

    @Override
    public void execute(Robot robot) {

    }
}
