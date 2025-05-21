package com.game.battle;

import com.game.characters.Player;
import com.game.characters.Monster;

import java.util.Scanner;

public class Battle implements Runnable {
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
