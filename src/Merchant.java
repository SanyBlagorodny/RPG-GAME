package com.game.characters;

import java.util.Scanner;

public class Merchant {
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
