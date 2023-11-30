/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.chat;

import de.timesnake.library.extension.util.chat.Chat;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class MessageBlockBuilder {

  private final LinkedList<String> messages = new LinkedList<>();

  public MessageBlockBuilder() {

  }

  public MessageBlockBuilder addLine(String message) {
    this.messages.addLast(message);
    return this;
  }

  public MessageBlockBuilder addLine(boolean predicate, String message) {
    if (predicate) {
      this.addLine(message);
    }
    return this;
  }

  public MessageBlockBuilder addLines(List<String> messages) {
    messages.forEach(this::addLine);
    return this;
  }

  public MessageBlockBuilder replaceLine(int index, String message) {
    this.messages.set(index, message);
    return this;
  }

  public MessageBlockBuilder emptyLine() {
    this.messages.add("");
    return this;
  }

  public MessageBlockBuilder separatorLine() {
    this.messages.add(Chat.getLineTDSeparator());
    return this;
  }

  public MessageBlockBuilder separatorLine(boolean predicate) {
    if (predicate) {
      this.separatorLine();
    }
    return this;
  }

  public void sendTo(Consumer<String> consumer) {
    this.messages.forEach(consumer);
  }

  public List<String> getBlock() {
    return this.messages;
  }
}
