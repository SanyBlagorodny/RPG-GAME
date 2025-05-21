package com.game.characters;

public class Player extends Character {
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
            System.out.println("Поздравляем! Вы достигли уровня " + level + "!");
            System.out.println("Ваши характеристики улучшились!");
        }
    }
}
