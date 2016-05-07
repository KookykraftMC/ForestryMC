/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.core.entities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityFXColoredDripParticle extends EntityFX {

	/**
	 * The height of the current bob
	 */
	private int bobTimer;

	public EntityFXColoredDripParticle(World world, double x, double y, double z, float red, float green, float blue) {
		super(world, x, y, z);
		this.particleRed = red;
		this.particleGreen = green;
		this.particleBlue = blue;

		this.setParticleTextureIndex(113);
		this.setSize(0.01F, 0.01F);
		this.particleGravity = 0.06F;
		this.bobTimer = 40;
		this.particleMaxAge = (int) (64.0D / (Math.random() * 0.8D + 0.2D));
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		this.ySpeed -= this.particleGravity;

		if (this.bobTimer-- > 0) {
			this.xSpeed *= 0.02D;
			this.ySpeed *= 0.02D;
			this.zSpeed *= 0.02D;
			this.setParticleTextureIndex(113);
		} else {
			this.setParticleTextureIndex(112);
		}

		this.moveEntity(this.xSpeed, this.ySpeed, this.zSpeed);
		this.xSpeed *= 0.9800000190734863D;
		this.ySpeed *= 0.9800000190734863D;
		this.zSpeed *= 0.9800000190734863D;

		if (this.particleMaxAge-- <= 0) {
			this.setExpired();
		}

		if (this.isCollided) {
			this.setParticleTextureIndex(114);

			this.xSpeed *= 0.699999988079071D;
			this.zSpeed *= 0.699999988079071D;
		}

		IBlockState state = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)));
		Block block = state.getBlock();
		Material material = block.getMaterial(state);

		if (material.isLiquid() || material.isSolid()) {
			double d0 = MathHelper.floor_double(this.posY) + 1 - BlockLiquid.getLiquidHeightPercent(block.getMetaFromState(state));

			if (this.posY < d0) {
				this.setExpired();
			}
		}
	}
}