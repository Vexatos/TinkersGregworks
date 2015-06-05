package vexatos.tgregworks.util;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import tconstruct.library.ActiveToolMod;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.AbilityHelper;
import tconstruct.library.tools.ToolCore;
import vexatos.tgregworks.TGregworks;

import java.util.List;
import java.util.Random;

/**
 * @author SlimeKnights, Vexatos
 */
public class TGregAbilityHelper extends AbilityHelper {

	public static boolean onBlockChanged(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase player, Random random) {
		if(!stack.hasTagCompound()) {
			return false;
		}

		int reinforced = 0;
		NBTTagCompound tags = stack.getTagCompound();

		if(tags.getCompoundTag("InfiTool").hasKey("Unbreaking")) {
			reinforced = tags.getCompoundTag("InfiTool").getInteger("Unbreaking");
		}

		if(random.nextInt(10) < 10 - reinforced) {
			damageTool(stack, 1, tags, player, false);
		}

		return true;
	}

	/* Tool specific */
	public static void damageTool(ItemStack stack, int dam, EntityLivingBase entity, boolean ignoreCharge) {
		NBTTagCompound tags = stack.getTagCompound();
		damageTool(stack, dam, tags, entity, ignoreCharge);
	}

	public static void healTool(ItemStack stack, int dam, EntityLivingBase entity, boolean ignoreCharge) {
		NBTTagCompound tags = stack.getTagCompound();
		damageTool(stack, -dam, tags, entity, ignoreCharge);
	}

	public static void damageTool(ItemStack stack, int dam, NBTTagCompound tags, EntityLivingBase entity, boolean ignoreCharge) {
		if(entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode || tags == null) {
			return;
		}

		if(ignoreCharge || !damageEnergyTool(stack, tags, entity)) {
			boolean damagedTool = false;
			for(ActiveToolMod mod : TConstructRegistry.activeModifiers) {
				if(mod.damageTool(stack, dam, entity)) {
					damagedTool = true;
				}
			}

			if(damagedTool) {
				return;
			}

			int damage = tags.getCompoundTag("InfiTool").getInteger("Damage");
			int damageTrue = damage + dam;
			int maxDamage = tags.getCompoundTag("InfiTool").getInteger("TotalDurability");
			if(damageTrue <= 0) {
				tags.getCompoundTag("InfiTool").setInteger("Damage", 0);
				//stack.setItemDamage(0);
				tags.getCompoundTag("InfiTool").setBoolean("Broken", false);
			} else if(damageTrue > maxDamage) {
				breakTool(stack, tags, entity);
				//stack.setItemDamage(0);
			} else {
				tags.getCompoundTag("InfiTool").setInteger("Damage", damage + dam);
				int toolDamage = (damage * 100 / maxDamage) + 1;
				int stackDamage = stack.getItemDamage();
				if(toolDamage != stackDamage) {
					//stack.setItemDamage((damage * 100 / maxDamage) + 1);
				}
			}
		}
	}

	public static boolean damageEnergyTool(ItemStack stack, NBTTagCompound tags, Entity entity) {

		NBTTagCompound gregTag = TGregUtils.getCompoundTag(tags, "TGregBattery");

		if(!gregTag.hasKey("electric")) {
			return AbilityHelper.damageEnergyTool(stack, tags, entity);
		}
		NBTTagCompound toolTag = stack.getTagCompound().getCompoundTag("InfiTool");

		long energy = gregTag.getLong("currentCharge");
		int durability = toolTag.getInteger("Damage");
		float shoddy = toolTag.getFloat("Shoddy");

		float mineSpeed = toolTag.getInteger("MiningSpeed");
		int heads = 1;
		if(toolTag.hasKey("MiningSpeed2")) {
			mineSpeed += toolTag.getInteger("MiningSpeed2");
			heads++;
		}

		if(toolTag.hasKey("MiningSpeedHandle")) {
			mineSpeed += toolTag.getInteger("MiningSpeedHandle");
			heads++;
		}

		if(toolTag.hasKey("MiningSpeedExtra")) {
			mineSpeed += toolTag.getInteger("MiningSpeedExtra");
			heads++;
		}
		float trueSpeed = mineSpeed / (heads * 100f);
		float stonebound = toolTag.getFloat("Shoddy");
		float bonusLog = (float) Math.log(durability / 72f + 1) * 2 * stonebound;
		trueSpeed += bonusLog;
		trueSpeed *= 6;
		if(energy != -1) {
			ToolCore tool = (ToolCore) stack.getItem();
			// first try charging from the hotbar
			if(entity instanceof EntityPlayer) {
				// workaround for charging flux-capacitors making tools unusable
				chargeEnergyFromHotbar(stack, (EntityPlayer) entity, tags);
				energy = tool.getEnergyStored(stack);
			}

			if(energy < trueSpeed * 2) {
				if(energy > 0) {
					gregTag.setLong("currentCharge", 0);
				}
				return false;
			}

			energy -= trueSpeed * 2;
			gregTag.setLong("currentCharge", Math.max(energy, TGregworks.registry.modifierCharges.get(gregTag.getLong("maxCharge"))));

			//stack.setItemDamage(1 + (tool.getMaxEnergyStored(stack) - energy) * (stack.getMaxDamage() - 1) / tool.getMaxEnergyStored(stack));
		}
		return true;
	}

	public static boolean onLeftClickEntity(ItemStack stack, EntityLivingBase player, Entity entity, ToolCore tool, int baseDamage) {
		if(entity.canAttackWithItem() && stack.hasTagCompound()) {
			if(!entity.hitByEntity(player)) // can't attack this entity
			{
				NBTTagCompound tags = stack.getTagCompound();
				NBTTagCompound toolTags = stack.getTagCompound().getCompoundTag("InfiTool");
				int damage = toolTags.getInteger("Attack") + baseDamage;
				boolean broken = toolTags.getBoolean("Broken");

				int durability = tags.getCompoundTag("InfiTool").getInteger("Damage");
				float stonebound = tags.getCompoundTag("InfiTool").getFloat("Shoddy");

				float stoneboundDamage = (float) Math.log(durability / 72f + 1) * -2 * stonebound;

				int earlyModDamage = 0;
				for(ActiveToolMod mod : TConstructRegistry.activeModifiers) {
					earlyModDamage = mod.baseAttackDamage(earlyModDamage, damage, tool, tags, toolTags, stack, player, entity);
				}
				damage += earlyModDamage;

				if(player.isPotionActive(Potion.damageBoost)) {
					damage += 3 << player.getActivePotionEffect(Potion.damageBoost).getAmplifier();
				}

				if(player.isPotionActive(Potion.weakness)) {
					damage -= 2 << player.getActivePotionEffect(Potion.weakness).getAmplifier();
				}

				float knockback = 0;
				float enchantDamage = 0;

				if(entity instanceof EntityLivingBase) {
					enchantDamage = EnchantmentHelper.getEnchantmentModifierLiving(player, (EntityLivingBase) entity);
					knockback += EnchantmentHelper.getKnockbackModifier(player, (EntityLivingBase) entity);
				}

				damage += stoneboundDamage;
				if(damage < 1) {
					damage = 1;
				}

				if(player.isSprinting()) {
					knockback++;
					float lunge = tool.chargeAttack();
					if(lunge > 1f) {
						knockback += lunge - 1.0f;
						damage *= lunge;
					}
				}

				float modKnockback = 0f;
				for(ActiveToolMod mod : TConstructRegistry.activeModifiers) {
					modKnockback = mod.knockback(modKnockback, knockback, tool, tags, toolTags, stack, player, entity);
				}
				knockback += modKnockback;

				int modDamage = 0;
				for(ActiveToolMod mod : TConstructRegistry.activeModifiers) {
					modDamage = mod.attackDamage(modDamage, damage, tool, tags, toolTags, stack, player, entity);
				}
				damage += modDamage;

				if(damage > 0 || enchantDamage > 0) {
					boolean criticalHit = player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(Potion.blindness) && player.ridingEntity == null && entity instanceof EntityLivingBase;

					for(ActiveToolMod mod : TConstructRegistry.activeModifiers) {
						if(mod.doesCriticalHit(tool, tags, toolTags, stack, player, entity)) {
							criticalHit = true;
						}
					}

					if(criticalHit) {
						damage += random.nextInt(damage / 2 + 2);
					}

					damage += enchantDamage;

					if(tool.getDamageModifier() != 1f) {
						damage *= tool.getDamageModifier();
					}
					boolean var6 = false;
					int fireAspect = EnchantmentHelper.getFireAspectModifier(player);

					if(entity instanceof EntityLivingBase && fireAspect > 0 && !entity.isBurning()) {
						var6 = true;
						entity.setFire(1);
					}

					if(broken) {
						if(baseDamage > 0) {
							damage = baseDamage;
						} else {
							damage = 1;
						}
					}
					boolean causedDamage = false;
					if(tool.pierceArmor() && !broken) {
						if(player instanceof EntityPlayer) {
							causedDamage = entity.attackEntityFrom(causePlayerPiercingDamage((EntityPlayer) player), damage);
						} else {
							causedDamage = entity.attackEntityFrom(causePiercingDamage(player), damage);
						}
					} else {
						if(player instanceof EntityPlayer) {
							causedDamage = entity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) player), damage);
						} else {
							causedDamage = entity.attackEntityFrom(DamageSource.causeMobDamage(player), damage);
						}
					}

					if(causedDamage) {
						int reinforced = 0;
						if(toolTags.hasKey("Unbreaking")) {
							reinforced = tags.getCompoundTag("InfiTool").getInteger("Unbreaking");
						}

						if(random.nextInt(10) < 10 - reinforced) {
							damageTool(stack, 1, tags, player, false);
						}
						// damageTool(stack, 1, player, false);
						tool.onEntityDamaged(player.worldObj, player, entity);
						if(!necroticUHS || (entity instanceof IMob && entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getHealth() <= 0)) {
							int drain = toolTags.getInteger("Necrotic") * 2;
							if(drain > 0) {
								player.heal(random.nextInt(drain + 1));
							}
						}

						if(knockback > 0) {
							entity.addVelocity((double) (-MathHelper.sin(player.rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F), 0.1D, (double) (MathHelper.cos(player.rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F));
							player.motionX *= 0.6D;
							player.motionZ *= 0.6D;
							player.setSprinting(false);
						}

						if(player instanceof EntityPlayer) {
							if(criticalHit) {
								((EntityPlayer) player).onCriticalHit(entity);
							}

							if(enchantDamage > 0) {
								((EntityPlayer) player).onEnchantmentCritical(entity);
							}

							if(damage >= 18) {
								((EntityPlayer) player).triggerAchievement(AchievementList.overkill);
							}
						}

						player.setLastAttacker(entity);

						if(entity instanceof EntityLivingBase) {
							DamageSource.causeThornsDamage(entity);// (((EntityLivingBase)player,
							// (EntityLivingBase)
							// entity);
						}
					}

					if(entity instanceof EntityLivingBase) {
						if(entity instanceof EntityPlayer) {
							stack.hitEntity((EntityLivingBase) entity, (EntityPlayer) player);
							if(entity.isEntityAlive()) {
								alertPlayerWolves((EntityPlayer) player, (EntityLivingBase) entity, true);
							}

							((EntityPlayer) player).addStat(StatList.damageDealtStat, damage);
						} else {
							stack.getItem().hitEntity(stack, (EntityLivingBase) entity, player);
						}

						if((fireAspect > 0 || toolTags.hasKey("Fiery") || toolTags.hasKey("Lava")) && causedDamage) {
							fireAspect *= 4;
							if(toolTags.hasKey("Fiery")) {
								fireAspect += toolTags.getInteger("Fiery") / 5 + 1;
							}
							if(toolTags.getBoolean("Lava")) {
								fireAspect += 3;
							}
							entity.setFire(fireAspect);
						} else if(var6) {
							entity.extinguish();
						}
					}

					if(entity instanceof EntityPlayer) {
						((EntityPlayer) player).addExhaustion(0.3F);
					}
					if(causedDamage) {
						return true;
					}
				}
			}
		}
		return false;
	}

	static void alertPlayerWolves(EntityPlayer player, EntityLivingBase living, boolean par2) {
		if(!(living instanceof EntityCreeper) && !(living instanceof EntityGhast)) {
			if(living instanceof EntityWolf) {
				EntityWolf var3 = (EntityWolf) living;

				if(var3.isTamed() && player.getDisplayName().equals(var3.func_152113_b())) {
					return;
				}
			}

			if(!(living instanceof EntityPlayer) || player.canAttackPlayer((EntityPlayer) living)) {
				List var6 = player.worldObj.getEntitiesWithinAABB(EntityWolf.class, AxisAlignedBB.getBoundingBox(player.posX, player.posY, player.posZ, player.posX + 1.0D, player.posY + 1.0D, player.posZ + 1.0D).expand(16.0D, 4.0D, 16.0D));

				for(Object aVar6 : var6) {
					EntityWolf var5 = (EntityWolf) aVar6;

					if(var5.isTamed() && var5.getEntityToAttack() == null && player.getDisplayName().equals(var5.func_152113_b()) && (!par2 || !var5.isSitting())) {
						var5.setSitting(false);
						var5.setTarget(living);
					}
				}
			}
		}
	}

	public static boolean hoeGround(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, Random random) {
		if(!player.canPlayerEdit(x, y, z, side, stack)) {
			return false;
		} else {
			UseHoeEvent event = new UseHoeEvent(player, stack, world, x, y, z);
			if(MinecraftForge.EVENT_BUS.post(event)) {
				return false;
			}

			if(event.getResult() == Event.Result.ALLOW) {
				damageTool(stack, 1, player, false);
				return true;
			}

			Block block = world.getBlock(x, y, z);

			if(side != 0 && world.getBlock(x, y + 1, z).isAir(world, x, y + 1, z) && (block == Blocks.grass || block == Blocks.dirt)) {
				Block block1 = Blocks.farmland;
				world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), block1.stepSound.getStepResourcePath(), (block1.stepSound.getVolume() + 1.0F) / 2.0F, block1.stepSound.getPitch() * 0.8F);

				if(world.isRemote) {
					return true;
				} else {
					world.setBlock(x, y, z, block1);
					damageTool(stack, 1, player, false);
					return true;
				}
			} else {
				return false;
			}
		}
	}
}
