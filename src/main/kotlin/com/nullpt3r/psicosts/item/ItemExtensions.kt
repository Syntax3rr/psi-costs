package com.nullpt3r.psicosts.item

fun PsiCellItem.fullyCharged() = defaultInstance.also { injectCharge(it, Int.MAX_VALUE) }
fun UnstableCellItem.fullyCharged() = defaultInstance.also { injectCharge(it, Int.MAX_VALUE) }
