/*
 * basic-bukkit.main
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

package de.timesnake.basic.bukkit.util.file;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExToml {

    protected final File file;

    protected Toml toml;
    protected TomlWriter tomlWriter;

    public ExToml(File file) {
        this.file = file;
        this.create();
        this.reload();
    }

    public ExToml(Toml toml) {
        this.file = null;
        this.toml = toml;
    }

    public void create() {
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void reload() {
        if (this.file != null) {
            this.toml = new Toml().read(this.file);
            this.tomlWriter = new TomlWriter();
        }
    }

    public String getString(String key) {return toml.getString(key);}

    public String getString(String key, String defaultValue) {return toml.getString(key, defaultValue);}

    public Long getLong(String key) {return toml.getLong(key);}

    public Long getLong(String key, Long defaultValue) {return toml.getLong(key, defaultValue);}

    public <T> List<T> getList(String key) {return toml.getList(key);}

    public <T> List<T> getList(String key, List<T> defaultValue) {return toml.getList(key, defaultValue);}

    public Boolean getBoolean(String key) {return toml.getBoolean(key);}

    public Boolean getBoolean(String key, Boolean defaultValue) {return toml.getBoolean(key, defaultValue);}

    public Date getDate(String key) {return toml.getDate(key);}

    public Date getDate(String key, Date defaultValue) {return toml.getDate(key, defaultValue);}

    public Double getDouble(String key) {return toml.getDouble(key);}

    public Double getDouble(String key, Double defaultValue) {return toml.getDouble(key, defaultValue);}

    public Toml getTable(String key) {return toml.getTable(key);}

    public List<Toml> getTables(String key) {return toml.getTables(key);}

    public boolean contains(String key) {return toml.contains(key);}

    public boolean containsPrimitive(String key) {return toml.containsPrimitive(key);}

    public boolean containsTable(String key) {return toml.containsTable(key);}

    public boolean containsTableArray(String key) {return toml.containsTableArray(key);}

    public boolean isEmpty() {return toml.isEmpty();}

    public <T> T to(Class<T> targetClass) {return toml.to(targetClass);}

    public Map<String, Object> toMap() {return toml.toMap();}

    public Set<Map.Entry<String, Object>> entrySet() {return toml.entrySet();}


    public boolean write(Object from) {
        try {
            tomlWriter.write(from, this.file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
