package app.cli.command;

import app.cli.CommandContext;

public class LoginCommand implements Command {
    @Override
    public String getName() { return "login"; }

    @Override
    public String getDescription() { return "Вход в систему"; }

    @Override
    public void execute(CommandContext context, String[] args) {
        System.out.print("Введите логин: ");
        String login = context.getScanner().nextLine().trim();
        System.out.print("Введите пароль: ");
        String password = context.getScanner().nextLine().trim();

        try {
            boolean success = context.getAuthService().login(login, password);
            if (success) {
                System.out.println("Успешно: Вы вошли как " + context.getAuthService().getCurrentUser().getLogin());
            } else {
                System.out.println("Ошибка: Неверный логин или пароль.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}