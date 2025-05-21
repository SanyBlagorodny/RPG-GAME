import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Random;

public class VisualGame extends JFrame {
    private JTextArea logArea;
    private JLabel lblName, lblLevel, lblHealth, lblStrength, lblAgility, lblExp, lblGold;
    private JButton btnMerchant, btnForest, btnStats, btnExit;
    private Player player;
    private Merchant merchant = new Merchant();
    private Random random = new Random();

    public VisualGame() {
        setTitle("RPG Game");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Font font = new Font("Consolas", Font.PLAIN, 14);

        // Левая панель - статистика героя
        JPanel statsPanel = new JPanel();
        statsPanel.setPreferredSize(new Dimension(220, 0));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Статистика героя"));
        statsPanel.setLayout(new GridLayout(8, 1, 5, 5));
        statsPanel.setFont(font);

        lblName = new JLabel("Имя: ");
        lblLevel = new JLabel("Уровень: ");
        lblHealth = new JLabel("Здоровье: ");
        lblStrength = new JLabel("Сила: ");
        lblAgility = new JLabel("Ловкость: ");
        lblExp = new JLabel("Опыт: ");
        lblGold = new JLabel("Золото: ");

        for (JLabel lbl : new JLabel[]{lblName, lblLevel, lblHealth, lblStrength, lblAgility, lblExp, lblGold}) {
            lbl.setFont(font);
            statsPanel.add(lbl);
        }

        // Правая панель - лог и кнопки
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        logArea = new JTextArea();
        logArea.setFont(font);
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(logArea);

        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        btnMerchant = new JButton("К торговцу");
        btnForest = new JButton("В тёмный лес");
        btnStats = new JButton("Показать статистику");
        btnExit = new JButton("Выход");

        btnMerchant.setToolTipText("Купить зелья лечения");
        btnForest.setToolTipText("Сразиться с монстром");
        btnStats.setToolTipText("Показать текущие характеристики");
        btnExit.setToolTipText("Выйти из игры");

        btnPanel.add(btnMerchant);
        btnPanel.add(btnForest);
        btnPanel.add(btnStats);
        btnPanel.add(btnExit);

        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.add(btnPanel, BorderLayout.SOUTH);

        add(statsPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        // Кнопки
        btnMerchant.addActionListener(e -> visitMerchant());
        btnForest.addActionListener(e -> enterForest());
        btnStats.addActionListener(e -> showStatsDialog());
        btnExit.addActionListener(e -> exitGame());

        // Ввод имени
        String name = null;
        while (name == null || name.trim().isEmpty()) {
            name = JOptionPane.showInputDialog(this, "Введите имя героя:", "Создание персонажа", JOptionPane.PLAIN_MESSAGE);
            if (name == null) {
                System.exit(0);
            }
        }

        // Выбор класса с описанием
        String[] options = {"Маг", "Танк", "Ловкач"};
        String[] descriptions = {
                "Маг: мало здоровья, большой урон.",
                "Танк: много здоровья и защиты, маленький урон.",
                "Ловкач: среднее здоровье, высокий урон, частые критические удары."
        };

        int classChoice = -1;
        while (classChoice == -1) {
            classChoice = JOptionPane.showOptionDialog(
                    this,
                    "Выберите класс героя:\n\n" +
                            "1. Маг - мало здоровья, большой урон.\n" +
                            "2. Танк - много здоровья и защиты, маленький урон.\n" +
                            "3. Ловкач - среднее здоровье, высокий урон, частые критические удары.\n\n" +
                            "Выберите класс:",
                    "Выбор класса",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (classChoice == -1) {
                int res = JOptionPane.showConfirmDialog(this, "Вы действительно хотите выйти?", "Выход", JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        }

        switch (classChoice) {
            case 0:
                player = new Player(name.trim(), 80, 15, 12);
                break;
            case 1:
                player = new Player(name.trim(), 130, 12, 8);
                break;
            case 2:
                player = new Player(name.trim(), 100, 10, 18);
                break;
            default:
                player = new Player(name.trim(), 100, 10, 10);
        }

        log("Добро пожаловать, " + player.getName() + " класс " + options[classChoice] + "!");
        log("Описание: " + descriptions[classChoice]);
        updateStats();
    }

    private void log(String text) {
        logArea.append(text + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void updateStats() {
        lblName.setText("Имя: " + player.getName());
        lblLevel.setText("Уровень: " + player.getLevel());
        lblHealth.setText(String.format("Здоровье: %d / %d", player.getHealth(), player.getMaxHealth()));
        lblStrength.setText("Сила: " + player.strength);
        lblAgility.setText("Ловкость: " + player.agility);
        lblExp.setText("Опыт: " + player.getExperience());
        lblGold.setText("Золото: " + player.getGold());
    }

    private void visitMerchant() {
        if (!player.isAlive()) {
            log("Вы мертвы и не можете торговать.");
            return;
        }
        String input = JOptionPane.showInputDialog(this,
                "У вас золота: " + player.getGold() +
                        "\nЦена зелья: 20 золота\nСколько зелий хотите купить?");
        if (input == null) {
            log("Вы отказались от покупки.");
            return;
        }
        try {
            int count = Integer.parseInt(input.trim());
            if (count <= 0) {
                log("Покупка отменена.");
                return;
            }
            int cost = count * 20;
            if (cost > player.getGold()) {
                log("Недостаточно золота!");
            } else {
                player.spendGold(cost);
                player.heal(50 * count);
                log("Вы купили " + count + " зелий и восстановили здоровье.");
                updateStats();
            }
        } catch (NumberFormatException ex) {
            log("Некорректный ввод.");
        }
    }

    private void enterForest() {
        if (!player.isAlive()) {
            log("Вы мертвы и не можете сражаться.");
            return;
        }
        Monster monster = random.nextBoolean() ? new Goblin() : new Skeleton();
        log("\nВ лесу появился " + monster.getName() + "!");
        disableButtons();
        new Thread(() -> battle(monster)).start();
    }

    private void battle(Monster monster) {
        try {
            while (player.isAlive() && monster.isAlive()) {
                Thread.sleep(1000);
                int damage = player.attack();
                monster.takeDamage(damage);
                log(player.getName() + " наносит " + damage + " урона " + monster.getName() +
                        ". Здоровье монстра: " + monster.getHealth() + "/" + monster.getMaxHealth());
                updateStats();
                if (!monster.isAlive()) {
                    log("Вы победили " + monster.getName() + "!");
                    player.addExperience(monster.getExpReward());
                    player.addGold(monster.getGoldReward());
                    player.checkLevelUp();
                    updateStats();
                    break;
                }

                Thread.sleep(1000);
                damage = monster.attack();
                player.takeDamage(damage);
                log(monster.getName() + " наносит " + damage + " урона " + player.getName() +
                        ". Ваше здоровье: " + player.getHealth() + "/" + player.getMaxHealth());
                updateStats();
                if (!player.isAlive()) {
                    log("Вы были убиты. Игра окончена.");
                    JOptionPane.showMessageDialog(this, "Вы погибли. Игра окончена.");
                    System.exit(0);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            enableButtons();
        }
    }

    private void showStatsDialog() {
        String stats = "Имя: " + player.getName() + "\n" +
                "Уровень: " + player.getLevel() + "\n" +
                "Здоровье: " + player.getHealth() + "/" + player.getMaxHealth() + "\n" +
                "Сила: " + player.strength + "\n" +
                "Ловкость: " + player.agility + "\n" +
                "Опыт: " + player.getExperience() + "\n" +
                "Золото: " + player.getGold();
        JOptionPane.showMessageDialog(this, stats, "Статистика игрока", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exitGame() {
        int confirm = JOptionPane.showConfirmDialog(this, "Вы действительно хотите выйти?", "Выход", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void disableButtons() {
        btnMerchant.setEnabled(false);
        btnForest.setEnabled(false);
        btnStats.setEnabled(false);
        btnExit.setEnabled(false);
    }

    private void enableButtons() {
        btnMerchant.setEnabled(true);
        btnForest.setEnabled(true);
        btnStats.setEnabled(true);
        btnExit.setEnabled(true);
    }

    // --- Классы персонажей ---

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

        public int attack() {
            int chance = random.nextInt(100);
            if (agility * 3 > chance) {
                boolean critical = random.nextInt(100) < 20;
                int damage = critical ? strength * 2 : strength;
                if (critical) {
                    log(name + " наносит КРИТИЧЕСКИЙ удар! Урон: " + damage);
                } else {
                    log(name + " наносит удар. Урон: " + damage);
                }
                return damage;
            } else {
                log(name + " промахивается!");
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

    class Player extends Character {
        private int level;
        private int expToNextLevel;

        public Player(String name, int health, int strength, int agility) {
            super(name, health, strength, agility);
            this.level = 1;
            this.expToNextLevel = 100;
            this.gold = 50;
        }

        public int getLevel() {
            return level;
        }

        public void checkLevelUp() {
            while (experience >= expToNextLevel) {
                experience -= expToNextLevel;
                level++;
                expToNextLevel += 50;
                maxHealth += 20;
                health = maxHealth;
                strength += 5;
                agility += 3;
                log("Поздравляем! Вы достигли уровня " + level + "!");
                log("Ваши характеристики улучшились!");
            }
        }
    }

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

    class Goblin extends Monster {
        public Goblin() {
            super("Гоблин", 50, 8, 15, 40, 20);
        }
    }

    class Skeleton extends Monster {
        public Skeleton() {
            super("Скелет", 70, 12, 8, 60, 30);
        }
    }

    class Merchant {
        private int potionPrice = 20;
        private int potionHealAmount = 50;
    }

    // Точка входа
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VisualGame game = new VisualGame();
            game.setVisible(true);
        });
    }
}
