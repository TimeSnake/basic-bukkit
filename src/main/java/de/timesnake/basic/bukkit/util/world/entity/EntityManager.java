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
