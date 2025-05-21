package com.game.characters;

import java.util.Random;

public abstract class Character {
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

    // === ДОБАВЬТЕ ЭТИ ГЕТТЕРЫ ===
    public int getStrength() {
        return strength;
    }

    public int getAgility() {
        return agility;
    }
}
