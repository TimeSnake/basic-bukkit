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

package de.timesnake.basic.bukkit.util.world.entity;

import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.world.ExWorld;

import java.util.Collection;
import java.util.Set;

public interface EntityManager {

    /**
     * Registers a {@link PacketEntity} for all users.
     *
     * @param entity The {@link PacketEntity} to register
     */
    void registerEntity(PacketEntity entity);

    /**
     * Registers a {@link PacketEntity} for the given users.
     *
     * @param entity The {@link PacketEntity} to register
     * @param users  The users to spawn for
     */
    void registerEntity(PacketEntity entity, Collection<? extends User> users);

    /**
     * Registers a {@link PacketEntity} for the given users.
     *
     * @param entity The {@link PacketEntity} to register
     * @param users  The users to spawn for
     */
    void registerEntity(PacketEntity entity, User... users);

    /**
     * Unregisters a {@link PacketEntity}. The entity will be removed for all users.
     *
     * @param entity The {@link PacketEntity} to remove
     */
    void unregisterEntity(PacketEntity entity);

    /**
     * Gets all {@link PacketEntity}s in the given world
     *
     * @param world The {@link ExWorld}
     * @return the set of {@link PacketEntity}s
     */
    Set<PacketEntity> getEntitiesByWorld(ExWorld world);

    /**
     * Gets all {@link PacketEntity}s in the given world
     *
     * @param world        The {@link ExWorld}
     * @param entityClass  The class of the entity
     * @param <EntityType> The type of the entity
     * @return the set of {@link PacketEntity}s
     */
    <EntityType extends PacketEntity> Set<EntityType> getEntitiesByWorld(ExWorld world, Class<EntityType> entityClass);
}
