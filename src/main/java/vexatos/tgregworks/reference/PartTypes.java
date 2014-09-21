package vexatos.tgregworks.reference;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tconstruct.smeltery.TinkerSmeltery;
import tconstruct.tools.TinkerTools;

/**
 * @author Vexatos
 */
public enum PartTypes {
	PickaxeHead		(		"Pickaxe Head",			"_pickaxe_head",		0,	TinkerTools.pickaxeHead,		2,	2	),
	ShovelHead		(		"Shovel Head",			"_shovel_head",			1,	TinkerTools.shovelHead,			3,	2	),
	AxeHead			(		"Axe Head",				"_axe_head",			2,	TinkerTools.hatchetHead,		4,	2	),
	Binding			(		"Tool Binding",			"_binding",				3,	TinkerTools.binding,			9,	1	),
	ToughBind		(		"Tough Binding",		"_toughbind",			4,	TinkerTools.toughBinding,		15,	6	),
	ToolRod			(		"Tool Rod",				"_rod",					5,	TinkerTools.toolRod,			1,	1	),
	ToughRod		(		"Tough Tool Rod",		"_toughrod",			6,	TinkerTools.toughRod,			14,	6	),
	LargePlate		(		"Large Plate",			"_largeplate",			7,	TinkerTools.largePlate,			16,	16	),

	SwordBlade		(		"Sword Blade",			"_sword_blade",			8,	TinkerTools.swordBlade,			5,	2	),
	LargeGuard		(		"Wide Guard",			"_large_guard",			9,	TinkerTools.wideGuard,			6,	1	),
	MediumGuard		(		"Hand Guard",			"_medium_guard",		10,	TinkerTools.handGuard,			7,	1	),
	Crossbar		(		"Crossbar",				"_crossbar",			11,	TinkerTools.crossbar,			8,	1	),
	KnifeBlade		(		"Knife Blade",			"_knife_blade",			12,	TinkerTools.knifeBlade,			12,	1	),
	FullGuard		(		"Full Guard",			"_full_guard",			13,	TinkerTools.fullGuard,			22,	2	),

	FrypanHead		(		"Pan",					"_frypan_head",			14,	TinkerTools.frypanHead,			10,	2	),
	SignHead		(		"Sign Head",			"_battlesign_head",		15,	TinkerTools.signHead,			11,	2	),
	ChiselHead		(		"Chisel Head",			"_chisel_head",			16,	TinkerTools.chiselHead,			13,	2	),

	ScytheHead		(		"Scythe Head",			"_scythe_head",			17,	TinkerTools.scytheBlade,		18,	16	),
	LumberHead		(		"Broad Axe Head",		"_lumberaxe_head",		18,	TinkerTools.broadAxeHead,		17,	16	),
	ExcavatorHead	(		"Excavator Head",		"_excavator_head",		19,	TinkerTools.excavatorHead,		19,	16	),
	LargeSwordBlade	(		"Large Sword Blade",	"_large_sword_blade",	20,	TinkerTools.largeSwordBlade,	20,	16	),
	HammerHead		(		"Hammer Head",			"_hammer_head",			21,	TinkerTools.hammerHead,			21,	16	),

	Chunk			(		"Shard",				"_chunk",				22,	TinkerTools.toolShard,			-1,	1	),

	ArrowHead		(		"Arrow Head",			"_arrowhead",			23,	TinkerTools.arrowhead,			25,	1	);

	public String textureName;
	public String partName;
	public Item counterpart;
	public ItemStack pattern;
	public int price;
	public int id;

	private PartTypes(String partName, String textureName, int id, Item counterpart, int patternNumber, int price) {
		this.partName = partName;
		this.textureName = textureName;
		this.id = id;
		this.counterpart = counterpart;
		this.pattern = new ItemStack(TinkerSmeltery.metalPattern, 0, patternNumber);
		this.price = price;
	}
}
