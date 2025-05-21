import java.util.Random;
import java.util.Scanner;

// Класс игры
public class Game {
    private Player player;
    private Merchant merchant = new Merchant();
    private Scanner scanner = new Scanner(System.in);
    private Random random = new Random();

    public void start() {
        System.out.println("Добро пожаловать в игру!");
        System.out.print("Введите имя вашего героя: ");
        String name = scanner.nextLine();
        player = new Player(name);
        System.out.println("Персонаж создан: " + player.getName());
        mainMenu();
    }

    private void mainMenu() {
        while (true) {
            System.out.println("\nКуда вы хотите пойти?");
            System.out.println("1. К торговцу");
            System.out.println("2. В тёмный лес");
            System.out.println("3. Показать статистику");
            System.out.println("4. На выход");
            System.out.print("Введите номер: ");

            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    visitMerchant();
                    break;
                case "2":
                    enterForest();
                    break;
                case "3":
                    player.showStats();
                    break;
                case "4":
                    System.out.println("Спасибо за игру! До свидания!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Неверный ввод, попробуйте снова.");
            }
        }
    }

    private void visitMerchant() {
        System.out.println("\nВы пришли к торговцу.");
        merchant.trade(player, scanner);
        System.out.println("Возвращаемся в город...");
    }

    private void enterForest() {
        System.out.println("\nВы входите в тёмный лес...");
        Monster monster = random.nextBoolean() ? new Goblin() : new Skeleton();
        Battle battle = new Battle(player, monster, scanner);
        Thread battleThread = new Thread(battle);
        battleThread.start();
        try {
            battleThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Бой завершён. Возвращаемся в город...");
    }

    public static void main(String[] args) {
        new Game().start();
    }
}
