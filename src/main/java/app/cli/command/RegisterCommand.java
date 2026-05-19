package app.cli.command;

import app.cli.CommandContext;

public class RegisterCommand implements Command {
    @Override
    public String getName() { return "register"; }

    @Override
    public String getDescription() { return "Регистрация нового пользователя"; }

    @Override
    public void execute(CommandContext context, String[] args) {
        System.out.print("Введите логин: ");
        String login = context.getScanner().nextLine().trim();
        System.out.print("Введите пароль: ");
        String password = context.getScanner().nextLine().trim();

        try {
            boolean success = context.getAuthService().register(login, password);
            if (success) {
                System.out.println("Успешно: Пользователь зарегистрирован!");
            } else {
                System.out.println("Ошибка: Логин уже занят.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}
