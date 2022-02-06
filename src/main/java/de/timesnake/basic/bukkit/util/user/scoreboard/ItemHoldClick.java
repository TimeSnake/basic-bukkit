package de.timesnake.basic.bukkit.util.user.scoreboard;

public class ItemHoldClick {

    private final Integer clickTime;
    private Long firstClickMilis;
    private Long lastClickMilis;

    public ItemHoldClick(Integer clickTime) {
        this.clickTime = clickTime;
        long currentMilis = System.currentTimeMillis();
        this.firstClickMilis = currentMilis;
        this.lastClickMilis = currentMilis;
    }

    public boolean click() {
        Long currentMilis = System.currentTimeMillis();
        if (currentMilis - lastClickMilis > 300) {
            this.firstClickMilis = currentMilis;
            this.lastClickMilis = currentMilis;
            return false;
        } else if (currentMilis - firstClickMilis >= clickTime) {
            this.firstClickMilis = currentMilis;
            this.lastClickMilis = currentMilis;
            return true;
        } else {
            this.lastClickMilis = currentMilis;
            return false;
        }
    }

    public Long getFirstClickMilis() {
        return firstClickMilis;
    }

    public Long getLastClickMilis() {
        return lastClickMilis;
    }
}
