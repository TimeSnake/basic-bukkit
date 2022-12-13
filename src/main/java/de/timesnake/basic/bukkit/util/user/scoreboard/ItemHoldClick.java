/*
 * workspace.basic-bukkit.main
 * Copyright (C) 2022 timesnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; If not, see <http://www.gnu.org/licenses/>.
 */

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
        if (currentMilis - lastClickMilis > 400) {
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
