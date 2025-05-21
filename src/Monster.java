package com.game.characters;

public abstract class Monster extends Character {
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
