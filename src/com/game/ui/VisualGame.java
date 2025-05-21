package com.game.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Random;
import java.util.Scanner;

public class VisualGame extends JFrame {
    private JTextArea logArea;
    private JLabel lblName, lblLevel, lblHealth, lblStrength, lblAgility, lblExp, lblGold;
    private JButton btnMerchant, btnForest, btnStats, btnExit;
    private Player player;
    private Merchant merchant = new Merchant();
    private Random random = new Random();

    public VisualGame() {
        setTitle("RPG Game");
        setSize(900, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(40, 40, 40));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font logFont = new Font("Consolas", Font.PLAIN, 14);

        // Левая панель - статистика героя
        JPanel statsPanel = new JPanel();
        statsPanel.setPreferredSize(new Dimension(250, 0));
        statsPanel.setBackground(new Color(60, 63, 65));
        statsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Статистика героя", 0, 0, labelFont, Color.WHITE));
        statsPanel.setLayout(new GridLayout(8, 1, 5, 5));

        lblName = createStatLabel("Имя: ", labelFont);
        lblLevel = createStatLabel("Уровень: ", labelFont);
        lblHealth = createStatLabel("Здоровье: ", labelFont);
        lblStrength = createStatLabel("Сила: ", labelFont);
        lblAgility = createStatLabel("Ловкость: ", labelFont);
        lblExp = createStatLabel("Опыт: ", labelFont);
        lblGold = createStatLabel("Золото: ", labelFont);

        statsPanel.add(lblName);
        statsPanel.add(lblLevel);
        statsPanel.add(lblHealth);
        statsPanel.add(lblStrength);
        statsPanel.add(lblAgility);
        statsPanel.add(lblExp);
        statsPanel.add(lblGold);

        // Центр - лог и кнопки
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(50, 53, 55));
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        logArea = new JTextArea();
        logArea.setFont(logFont);
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setBackground(new Color(30, 30, 30));
        logArea.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        btnPanel.setBackground(new Color(50, 53, 55));

        btnMerchant = createButton("К торговцу");
        btnForest = createButton("В тёмный лес");
        btnStats = createButton("Показать статистику");
        btnExit = createButton("Выход");

        btnPanel.add(btnMerchant);
        btnPanel.add(btnForest);
        btnPanel.add(btnStats);
        btnPanel.add(btnExit);

        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(btnPanel, BorderLayout.SOUTH);

        add(statsPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);

        // Обработчики кнопок
        btnMerchant.addActionListener(e -> visitMerchant());
        btnForest.addActionListener(e -> enterForest());
        btnStats.addActionListener(e -> showStatsDialog());
        btnExit.addActionListener(e -> exitGame());

        SwingUtilities.invokeLater(this::startGame);
    }

    private JLabel createStatLabel(String text, Font font) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(font);
        return lbl;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(75, 110, 175));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void log(String text) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(text + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    private void updateStats() {
        SwingUtilities.invokeLater(() -> {
            lblName.setText("Имя: " + player.getName());
            lblLevel.setText("Уровень: " + player.getLevel());
            lblHealth.setText(String.format("Здоровье: %d / %d", player.getHealth(), player.getMaxHealth()));
            lblStrength.setText("Сила: " + player.getStrength());
            lblAgility.setText("Ловкость: " + player.getAgility());
            lblExp.setText("Опыт: " + player.getExperience());
            lblGold.setText("Золото: " + player.getGold());
        });
    }

    private void startGame() {
        String name = null;
        while (name == null || name.trim().isEmpty()) {
            name = JOptionPane.showInputDialog(this, "Введите имя героя:", "Создание персонажа", JOptionPane.PLAIN_MESSAGE);
            if (name == null) System.exit(0);
        }

        String[] options = {"Маг", "Танк", "Ловкач"};
        String[] descriptions = {
                "Маг: мало здоровья, большой урон.",
                "Танк: много здоровья и защиты, маленький урон.",
                "Ловкач: среднее здоровье, высокий урон, частые критические удары."
        };

        int classChoice = JOptionPane.showOptionDialog(
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

        if (classChoice == -1) System.exit(0);

        switch (classChoice) {
            case 0 -> player = new Player(name.trim(), 80, 15, 12);
            case 1 -> player = new Player(name.trim(), 130, 12, 8);
            case 2 -> player = new Player(name.trim(), 100, 10, 18);
            default -> player = new Player(name.trim(), 100, 10, 10);
        }

        log("Добро пожаловать, " + player.getName() + " класс " + options[classChoice] + "!");
        log("Описание: " + descriptions[classChoice]);
        updateStats();
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
                Thread.sleep(700);

                int damage = player.attack();
                if (damage == 0) {
                    log(player.getName() + " промахивается!");
                } else {
                    monster.takeDamage(damage);
                    log(player.getName() + " наносит " + damage + " урона " + monster.getName() +
                            ". Здоровье монстра: " + monster.getHealth() + "/" + monster.getMaxHealth());
                    // Критический урон выводится внутри attack()
                }
                updateStats();

                if (!monster.isAlive()) {
                    log("Вы победили " + monster.getName() + "!");
                    player.addExperience(monster.getExpReward());
                    player.addGold(monster.getGoldReward());
                    player.checkLevelUp();
                    updateStats();
                    break;
                }

                Thread.sleep(700);

                damage = monster.attack();
                if (damage == 0) {
                    log(monster.getName() + " промахивается!");
                } else {
                    player.takeDamage(damage);
                    log(monster.getName() + " наносит " + damage + " урона " + player.getName() +
                            ". Ваше здоровье: " + player.getHealth() + "/" + player.getMaxHealth());
                }
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
                "Сила: " + player.getStrength() + "\n" +
                "Ловкость: " + player.getAgility() + "\n" +
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
        SwingUtilities.invokeLater(() -> {
            btnMerchant.setEnabled(false);
            btnForest.setEnabled(false);
            btnStats.setEnabled(false);
            btnExit.setEnabled(false);
        });
    }

    private void enableButtons() {
        SwingUtilities.invokeLater(() -> {
            btnMerchant.setEnabled(true);
            btnForest.setEnabled(true);
            btnStats.setEnabled(true);
            btnExit.setEnabled(true);
        });
    }

    // --- Классы персонажей и монстров ---

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
                }
                return damage;
            } else {
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

        public int getStrength() {
            return strength;
        }

        public int getAgility() {
            return agility;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VisualGame game = new VisualGame();
            game.setVisible(true);
        });
    }
}
