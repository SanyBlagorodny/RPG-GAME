import java.util.Random;
import java.util.Scanner;

// Абстрактный класс персонажа
abstract class Character {
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int strength;
    protected int agility;
    protected int experience;
    protected int gold;

    protected Random random = new Random();

    public Character(String name, int health, int strength, int agility) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.strength = strength;
        this.agility = agility;
        this.experience = 0;
        this.gold = 0;
    }

    public boolean isAlive() {
        return health > 0;
    }

    // Атака с вероятностью промаха и шансом критического удара
    public int attack() {
        int chance = random.nextInt(100);
        if (agility * 3 > chance) {
            // Критический удар с вероятностью 20%
            boolean critical = random.nextInt(100) < 20;
            int damage = critical ? strength * 2 : strength;
            if (critical) {
                System.out.println(name + " наносит КРИТИЧЕСКИЙ удар! Урон: " + damage);
            } else {
                System.out.println(name + " наносит удар. Урон: " + damage);
            }
            return damage;
        } else {
            System.out.println(name + " промахивается!");
            return 0;
        }
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
    }

    public void heal(int amount) {
        health += amount;
        if (health > maxHealth) health = maxHealth;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getExperience() {
        return experience;
    }

    public int getGold() {
        return gold;
    }

    public void addExperience(int exp) {
        experience += exp;
    }

    public void addGold(int g) {
        gold += g;
    }

    public void spendGold(int g) {
        gold -= g;
    }
}

// Класс Игрока
class Player extends Character {
    private int level;
    private int expToNextLevel;

    public Player(String name) {
        super(name, 100, 10, 10);
        this.level = 1;
        this.expToNextLevel = 100;
        this.gold = 50; // стартовое золото
    }

    public int getLevel() {
        return level;
    }

    // Проверка и повышение уровня
    public void checkLevelUp() {
        while (experience >= expToNextLevel) {
            experience -= expToNextLevel;
            level++;
            expToNextLevel += 50; // усложнение для следующего уровня
            maxHealth += 20;
            health = maxHealth;
            strength += 5;
            agility += 3;
            System.out.println("Поздравляем! Вы достигли уровня " + level + "!");
            System.out.println("Ваши характеристики улучшились!");
        }
    }

    public void showStats() {
        System.out.println("=== Статистика игрока ===");
        System.out.println("Имя: " + name);
        System.out.println("Уровень: " + level);
        System.out.println("Здоровье: " + health + "/" + maxHealth);
        System.out.println("Сила: " + strength);
        System.out.println("Ловкость: " + agility);
        System.out.println("Опыт: " + experience + "/" + expToNextLevel);
        System.out.println("Золото: " + gold);
        System.out.println("=========================");
    }
}

// Абстрактный класс монстра
abstract class Monster extends Character {
    protected int expReward;
    protected int goldReward;

    public Monster(String name, int health, int strength, int agility, int expReward, int goldReward) {
        super(name, health, strength, agility);
        this.expReward = expReward;
        this.goldReward = goldReward;
    }

    public int getExpReward() {
        return expReward;
    }

    public int getGoldReward() {
        return goldReward;
    }
}

// Гоблин
class Goblin extends Monster {
    public Goblin() {
        super("Гоблин", 50, 8, 15, 40, 20);
    }
}

// Скелет
class Skeleton extends Monster {
    public Skeleton() {
        super("Скелет", 70, 12, 8, 60, 30);
    }
}

// Торговец
class Merchant {
    private int potionPrice = 20;
    private int potionHealAmount = 50;

    public void trade(Player player, Scanner scanner) {
        System.out.println("Торговец: Добро пожаловать! У меня есть зелья лечения за " + potionPrice + " золота.");
        System.out.println("У вас золота: " + player.getGold());
        System.out.println("Введите количество зелий для покупки (0 - выйти): ");

        while (true) {
            String input = scanner.nextLine();
            int count;
            try {
                count = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите число.");
                continue;
            }
            if (count == 0) {
                System.out.println("Выход из торговли.");
                break;
            }
            int cost = count * potionPrice;
            if (cost > player.getGold()) {
                System.out.println("У вас недостаточно золота!");
            } else {
                player.spendGold(cost);
                player.heal(potionHealAmount * count);
                System.out.println("Вы купили " + count + " зелий и восстановили " + (potionHealAmount * count) + " здоровья.");
                System.out.println("Текущее здоровье: " + player.getHealth() + "/" + player.getMaxHealth());
                System.out.println("Остаток золота: " + player.getGold());
                System.out.println("Если хотите купить ещё, введите количество, иначе 0 для выхода:");
            }
        }
    }
}

// Класс боя
class Battle implements Runnable {
    private Player player;
    private Monster monster;
    private Scanner scanner;

    public Battle(Player player, Monster monster, Scanner scanner) {
        this.player = player;
        this.monster = monster;
        this.scanner = scanner;
    }

    @Override
    public void run() {
        System.out.println("Начинается бой с " + monster.getName() + "!");
        while (player.isAlive() && monster.isAlive()) {
            // Ход игрока
            System.out.println("\nВаш ход. Нажмите Enter, чтобы атаковать...");
            scanner.nextLine();
            int damage = player.attack();
            monster.takeDamage(damage);
            System.out.println(monster.getName() + " осталось здоровья: " + monster.getHealth() + "/" + monster.getMaxHealth());
            if (!monster.isAlive()) {
                System.out.println("Вы победили " + monster.getName() + "!");
                player.addExperience(monster.getExpReward());
                player.addGold(monster.getGoldReward());
                System.out.println("Вы получили " + monster.getExpReward() + " опыта и " + monster.getGoldReward() + " золота.");
                player.checkLevelUp();
                break;
            }

            // Ход монстра
            System.out.println("\nХод " + monster.getName() + ". Нажмите Enter, чтобы продолжить...");
            scanner.nextLine();
            int monsterDamage = monster.attack();
            player.takeDamage(monsterDamage);
            System.out.println("Ваше здоровье: " + player.getHealth() + "/" + player.getMaxHealth());
            if (!player.isAlive()) {
                System.out.println("Вы были убиты " + monster.getName() + ". Игра окончена.");
                System.exit(0);
            }
        }
    }
}
