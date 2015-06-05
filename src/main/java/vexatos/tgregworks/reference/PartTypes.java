package vexatos.tgregworks.reference;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tconstruct.tools.TinkerTools;
import tconstruct.weaponry.TinkerWeaponry;

import static vexatos.tgregworks.reference.Pattern.MetalPatterns.*;
import static vexatos.tgregworks.reference.Pattern.WeaponryPatterns.*;

/**
 * @author Vexatos
 */
public enum PartTypes {
	PickaxeHead		(		"Pickaxe Head",			"_pickaxe_head",		0,	TinkerTools.pickaxeHead,		pickaxe,		2	),
	ShovelHead		(		"Shovel Head",			"_shovel_head",			1,	TinkerTools.shovelHead,			shovel,			2	),
	AxeHead			(		"Axe Head",				"_axe_head",			2,	TinkerTools.hatchetHead,		axe,			2	),
	Binding			(		"Tool Binding",			"_binding",				3,	TinkerTools.binding,			binding,		1	),
	ToughBind		(		"Tough Binding",		"_toughbind",			4,	TinkerTools.toughBinding,		toughbinding,	6	),
	ToolRod			(		"Tool Rod",				"_rod",					5,	TinkerTools.toolRod,			rod,			1	),
	ToughRod		(		"Tough Tool Rod",		"_toughrod",			6,	TinkerTools.toughRod,			largerod,		6	),
	LargePlate		(		"Large Plate",			"_largeplate",			7,	TinkerTools.largePlate,			largeplate,		16	),

	SwordBlade		(		"Sword Blade",			"_sword_blade",			8,	TinkerTools.swordBlade,			swordblade,		2	),
	LargeGuard		(		"Wide Guard",			"_large_guard",			9,	TinkerTools.wideGuard,			largeguard,		1	),
	MediumGuard		(		"Hand Guard",			"_medium_guard",		10,	TinkerTools.handGuard,			mediumguard,	1	),
	Crossbar		(		"Crossbar",				"_crossbar",			11,	TinkerTools.crossbar,			crossbar,		1	),
	KnifeBlade		(		"Knife Blade",			"_knife_blade",			12,	TinkerTools.knifeBlade,			knifeblade,		1	),
	FullGuard		(		"Full Guard",			"_full_guard",			13,	TinkerTools.fullGuard,			fullguard,		2	),

	FrypanHead		(		"Pan",					"_frypan_head",			14,	TinkerTools.frypanHead,			frypan,			2	),
	SignHead		(		"Sign Head",			"_battlesign_head",		15,	TinkerTools.signHead,			sign,			2	),
	ChiselHead		(		"Chisel Head",			"_chisel_head",			16,	TinkerTools.chiselHead,			chisel,			2	),

	ScytheHead		(		"Scythe Head",			"_scythe_head",			17,	TinkerTools.scytheBlade,		scythe,			16	),
	LumberHead		(		"Broad Axe Head",		"_lumberaxe_head",		18,	TinkerTools.broadAxeHead,		broadaxe,		16	),
	ExcavatorHead	(		"Excavator Head",		"_excavator_head",		19,	TinkerTools.excavatorHead,		excavator,		16	),
	LargeSwordBlade	(		"Large Sword Blade",	"_large_sword_blade",	20,	TinkerTools.largeSwordBlade,	largeblade,		16	),
	HammerHead		(		"Hammer Head",			"_hammer_head",			21,	TinkerTools.hammerHead,			hammerhead,		16	),

	Chunk			(		"Shard",				"_chunk",				22,	TinkerTools.toolShard,			null,			1	),

	ArrowHead		(		"Arrow Head",			"_arrowhead",			23,	TinkerWeaponry.arrowhead,		arrowhead,		1	),
	Shuriken		(		"Shuriken",				"_shuriken",			24, TinkerWeaponry.partShuriken,	shuriken,		1	),
	CrossbowLimb	(		"Crossbow Limb",		"_crossbow_limb",		25, TinkerWeaponry.partCrossbowLimb,crossbowlimb,	8	),
	CrossbowBody	(		"Crossbow Body",		"_crossbow_body",		26, TinkerWeaponry.partCrossbowBody,crossbowbody,	10	),
	BowLimb			(		"Bow Limb",				"_bow_limb",			27, TinkerWeaponry.partBowLimb,		bowlimb,		3	);

	public String textureName;
	public String partName;
	public Item counterpart;
	public ItemStack pattern;
	public int price;
	public int id;

	private PartTypes(String partName, String textureName, int id, Item counterpart, Pattern pattern, int price) {
		this.partName = partName;
		this.textureName = textureName;
		this.id = id;
		this.counterpart = counterpart;
		this.pattern = pattern != null ? new ItemStack(pattern.getPatternItem(), 0, pattern.ordinal()) : null;
		this.price = price;
	}
}
