package command;

import java.util.Scanner;

public interface Command {
    void execute(String[] args, Scanner scanner) throws Exception;
    String getDescription();
    String getUsage();
}