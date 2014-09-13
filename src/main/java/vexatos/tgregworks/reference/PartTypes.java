package vexatos.tgregworks.reference;

import net.minecraft.item.Item;
import tconstruct.tools.TinkerTools;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Vexatos
 */
public enum PartTypes {
	PickaxeHead			(		"Pickaxe Head",			"_pickaxe_head",		0	,	TinkerTools.pickaxeHead		),
	ShovelHead			(		"Shovel Head",			"_shovel_head",			1	,	TinkerTools.shovelHead		),
	AxeHead				(		"Axe Head",				"_axe_head",			2	,	TinkerTools.hatchetHead		),
	Binding				(		"Tool Binding",			"_binding",				3	,	TinkerTools.binding			),
	ToughBind			(		"Tough Binding",		"_toughbind",			4	,	TinkerTools.toughBinding	),
	ToolRod				(		"Tool Rod",				"_rod",					5	,	TinkerTools.toolRod			),
	ToughRod			(		"Tough Tool Rod",		"_toughrod",			6	,	TinkerTools.toughRod		),
	LargePlate			(		"Large Plate",			"_largeplate",			7	,	TinkerTools.largePlate		),

	SwordBlade			(		"Sword Blade",			"_sword_blade",			8	,	TinkerTools.swordBlade		),
	LargeGuard			(		"Wide Guard",			"_large_guard",			9	,	TinkerTools.wideGuard		),
	MediumGuard			(		"Hand Guard",			"_medium_guard",		10	,	TinkerTools.handGuard		),
	Crossbar			(		"Crossbar",				"_crossbar",			11	,	TinkerTools.crossbar		),
	KnifeBlade			(		"Knife Blade",			"_knife_blade",			12	,	TinkerTools.knifeBlade		),
	FullGuard			(		"Full Guard",			"_full_guard",			13	,	TinkerTools.fullGuard		),

	FrypanHead			(		"Pan",					"_frypan_head",			14	,	TinkerTools.frypanHead		),
	SignHead			(		"Sign Head",			"_battlesign_head",		15	,	TinkerTools.signHead		),
	ChiselHead			(		"Chisel Head",			"_chisel_head",			16	,	TinkerTools.chiselHead		),

	ScytheHead			(		"Scythe Head",			"_scythe_head",			17	,	TinkerTools.scytheBlade		),
	LumberHead			(		"Broad Axe Head",		"_lumberaxe_head",		18	,	TinkerTools.broadAxeHead	),
	ExcavatorHead		(		"Excavator Head",		"_excavator_head",		19	,	TinkerTools.excavatorHead	),
	LargeSwordBlade		(		"Large Sword Blade",	"_large_sword_blade",	20	,	TinkerTools.largeSwordBlade	),
	HammerHead			(		"Hammer Head",			"_hammer_head",			21	,	TinkerTools.hammerHead		),

	Chunk				(		"Shard",				"_chunk",				22	,	TinkerTools.toolShard		),

	ArrowHead			(		"Arrow Head",			"_arrowhead",			23	,	TinkerTools.arrowhead		);

	public String textureName;
	public String partName;
	public int metaID;
	public Item counterpart;
	private static ArrayList<PartTypes> typeList;

	public static PartTypes getFromID(int metaID) {
		if(typeList == null) {
			typeList = new ArrayList<PartTypes>();
			Collections.addAll(typeList, PartTypes.values());
		}
		return typeList.get(metaID);
	}

	private PartTypes(String partName, String textureName, int metaID, Item counterpart) {
		this.partName = partName;
		this.textureName = textureName;
		this.metaID = metaID;
		this.counterpart = counterpart;
	}
}
