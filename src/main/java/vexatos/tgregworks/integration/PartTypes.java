package vexatos.tgregworks.integration;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Vexatos
 */
public enum PartTypes {
	PickaxeHead			(		"Pickaxe Head",			"_pickaxe_head",		0	),
	ShovelHead			(		"Shovel Head",			"_shovel_head",			1	),
	AxeHead				(		"Axe Head",				"_axe_head",			2	),
	Binding				(		"Tool Binding",			"_binding",				3	),
	ToughBind			(		"Tough Binding",		"_toughbind",			4	),
	ToolRod				(		"Tool Rod",				"_rod",					5	),
	ToughRod			(		"Tough Tool Rod",		"_toughrod",			6	),
	LargePlate			(		"Large Plate",			"_largeplate",			7	),

	SwordBlade			(		"Sword Blade",			"_sword_blade",			8	),
	LargeGuard			(		"Wide Guard",			"_large_guard",			9	),
	MediumGuard			(		"Hand Guard",			"_medium_guard",		10	),
	Crossbar			(		"Crossbar",				"_crossbar",			11	),
	KnifeBlade			(		"Knife Blade",			"_knife_blade",			12	),
	FullGuard			(		"Full Guard",			"_full_guard",			13	),

	FrypanHead			(		"Pan",					"_frypan_head",			14	),
	SignHead			(		"Sign Head",			"_battlesign_head",		15	),
	ChiselHead			(		"Chisel Head",			"_chisel_head",			16	),

	ScytheHead			(		"Scythe Head",			"_scythe_head",			17	),
	LumberHead			(		"Broad Axe Head",		"_lumberaxe_head",		18	),
	ExcavatorHead		(		"Excavator Head",		"_excavator_head",		19	),
	LargeSwordBlade		(		"Large Sword Blade",	"_large_sword_blade",	20	),
	HammerHead			(		"Hammer Head",			"_hammer_head",			21	),

	Chunk				(		"Shard",				"_chunk",				22	);

	public String textureName;
	public String partName;
	public int metaID;
	private static ArrayList<PartTypes> typeList;

	public static PartTypes getFromID(int metaID) {
		if(typeList == null) {
			typeList = new ArrayList<PartTypes>();
			Collections.addAll(typeList, PartTypes.values());
		}
		return typeList.get(metaID);
	}

	private PartTypes(String partName, String textureName, int metaID) {
		this.partName = partName;
		this.textureName = textureName;
		this.metaID = metaID;
	}
}
