/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import de.timesnake.basic.bukkit.util.user.scoreboard.ExSideboard.LineId;
import java.util.LinkedList;

public class ExSideboardBuilder extends SideboardBuilder {

    protected LinkedList<LineId<?>> lineIds = new LinkedList<>();
    protected boolean lineSpacer = false;

    @Override
    public ExSideboardBuilder name(String name) {
        return (ExSideboardBuilder) super.name(name);
    }

    @Override
    public ExSideboardBuilder title(String title) {
        return (ExSideboardBuilder) super.title(title);
    }

    @Override
    public ExSideboardBuilder setScore(int line, String text) {
        return (ExSideboardBuilder) super.setScore(line, text);
    }

    public ExSideboardBuilder addLine(LineId<?> lineId) {
        this.lineIds.addLast(lineId);
        return this;
    }

    public ExSideboardBuilder lineSpacer() {
        this.lineSpacer = true;
        return this;
    }

    public LinkedList<LineId<?>> getLineIds() {
        return lineIds;
    }

    public boolean isLineSpacer() {
        return lineSpacer;
    }
}
