package net.gravity.puffs.entity.goals;

import net.gravity.puffs.entity.custom.puff.Bombpuff;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class BombpuffRunAroundLikeCrazyGoal extends Goal {
    private final Bombpuff bombpuff;
    private final double speedModifier;
    private double posX;
    private double posY;
    private double posZ;

    public BombpuffRunAroundLikeCrazyGoal(Bombpuff bombpuff, double pSpeedModifier) {
        this.bombpuff = bombpuff;
        this.speedModifier = pSpeedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        if (this.bombpuff.isIgnited()) {
            Vec3 vec3 = DefaultRandomPos.getPos(this.bombpuff, 3, 2);
            if (vec3 == null) {
                return false;
            } else {
                this.posX = vec3.x;
                this.posY = vec3.y;
                this.posZ = vec3.z;
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        this.bombpuff.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse() {
        return this.bombpuff.isIgnited() && !this.bombpuff.getNavigation().isDone();
    }
}
