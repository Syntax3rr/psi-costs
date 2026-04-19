package com.nullpt3r.psicosts

import net.neoforged.neoforge.common.ModConfigSpec

object Config {
    private val BUILDER = ModConfigSpec.Builder()

    val COST_MULTIPLIER: ModConfigSpec.DoubleValue = BUILDER
        .comment(
            "Global multiplier applied to all PSI spell costs after the CAD efficiency formula runs.",
            "1.0 = vanilla cost, 2.0 = double, 0.5 = half."
        )
        .defineInRange("costMultiplier", 1.0, 0.0, 100.0)

    var XP_CONVERSION_ENABLED: ModConfigSpec.BooleanValue
    var XP_CONVERSION_RATIO: ModConfigSpec.DoubleValue
    var XP_CONVERSION_MULTIPLIER: ModConfigSpec.DoubleValue

    var UNSTABLE_CELL_CAPACITY: ModConfigSpec.IntValue

    var CELL_TIER_1_CAPACITY: ModConfigSpec.IntValue
    var CELL_TIER_2_CAPACITY: ModConfigSpec.IntValue
    var CELL_TIER_3_CAPACITY: ModConfigSpec.IntValue
    var CELL_TIER_4_CAPACITY: ModConfigSpec.IntValue

    val SPEC: ModConfigSpec

    init {
        BUILDER.push("xpConversion")

        XP_CONVERSION_ENABLED = BUILDER
            .comment(
                "When true, picking up XP orbs while PSI is not full will convert a portion of",
                "that XP into PSI instead. If PSI is full, all XP is kept normally.",
                "Be careful disabling this: it could lock new players out of Psi entirely!"
            )
            .define("enabled", true)

        XP_CONVERSION_RATIO = BUILDER
            .comment("Fraction of each XP orb's value consumed for PSI (0.0–1.0). 0.75 = 75% taken.")
            .defineInRange("ratio", 0.75, 0.0, 1.0)

        XP_CONVERSION_MULTIPLIER = BUILDER
            .comment("PSI gained per XP point consumed.")
            .defineInRange("multiplier", 150.0, 1.0, 100000.0)

        BUILDER.pop()

        BUILDER.push("unstableCell")

        UNSTABLE_CELL_CAPACITY = BUILDER
            .comment(
                "PSI capacity of a single unstable cell.",
                "Unstable cells are drawn before regular cells and destroyed when empty.",
                "Each cell in the player's inventory expands their effective PSI pool by this amount."
            )
            .defineInRange("capacity", 5000, 1, Int.MAX_VALUE)

        BUILDER.pop()

        BUILDER.push("cellCapacities")
        .comment("PSI cell capacities require a restart to take effect.")

        CELL_TIER_1_CAPACITY = BUILDER
            .comment("PSI capacity of a tier-1 cell (PSI Cell).")
            .defineInRange("tier1", 80000, 1, Int.MAX_VALUE)

        CELL_TIER_2_CAPACITY = BUILDER
            .comment("PSI capacity of a tier-2 cell (PSI-O Cell).")
            .defineInRange("tier2", 240000, 1, Int.MAX_VALUE)

        CELL_TIER_3_CAPACITY = BUILDER
            .comment("PSI capacity of a tier-3 cell (PSI-Dyne Cell).")
            .defineInRange("tier3", 720000, 1, Int.MAX_VALUE)

        CELL_TIER_4_CAPACITY = BUILDER
            .comment("PSI capacity of a tier-4 cell (PSI-Force Cell).")
            .defineInRange("tier4", 2560000, 1, Int.MAX_VALUE)

        BUILDER.pop()

        SPEC = BUILDER.build()
    }
}
