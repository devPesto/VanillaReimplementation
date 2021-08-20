package net.minestom.vanilla.entity;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.other.PrimedTntMeta;
import net.minestom.server.instance.block.Block;
import net.minestom.vanilla.instance.VanillaExplosion;

public class PrimedTNT extends Entity {

    private int fuseTime = 0;

    public PrimedTNT() {
        super(EntityType.TNT);
        setGravity(0.025f, getGravityAcceleration());
        setBoundingBox(0.98f, 0.98f, 0.98f);

        PrimedTntMeta meta = (PrimedTntMeta) this.getEntityMeta();
        meta.setFuseTime(fuseTime);
    }

    private void explode() {
        if (shouldRemove()) {
            return;
        }

        remove();

        Block block = instance.getBlock(this.getPosition());

        VanillaExplosion explosion = VanillaExplosion.builder(getPosition(), 5.0f)
                .destroyBlocks(!block.isLiquid())
                .build();

        explosion.trigger(instance);
    }

    @Override
    public void update(long time) {
        super.update(time);
        if (fuseTime-- <= 0) {
            explode();
        }
    }

    public int getFuseTime() {
        return fuseTime;
    }

    public void setFuseTime(int fuseTime) {
        this.fuseTime = fuseTime;
    }
}
