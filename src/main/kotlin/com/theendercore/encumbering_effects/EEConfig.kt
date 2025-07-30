package com.theendercore.encumbering_effects

import com.theendercore.encumbering_effects.EncumberingEffects.MODID
import com.theendercore.encumbering_effects.EncumberingEffects.id
import me.fzzyhmstrs.fzzy_config.config.Config
import me.fzzyhmstrs.fzzy_config.util.AllowableIdentifiers
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items.*
import net.minecraftforge.registries.ForgeRegistries.ITEMS
import net.minecraftforge.registries.ForgeRegistries.MOB_EFFECTS
import net.minecraftforge.registries.IForgeRegistry

@Suppress("DEPRECATION")
class EEConfig : Config(id(MODID)) {

    var effectsToDisabledItems = ValidatedMap.Builder<ResourceLocation, List<ResourceLocation>>()
        .keyHandler(ofForge(MOB_EFFECTS))
        .valueHandler(ofForge(ITEMS).toList())
        .defaults(*DEFAULTS.toTypedArray())
        .build()

    fun getItems(effect: MobEffect): List<Item>? =
        effectsToDisabledItems[MOB_EFFECTS.getKey(effect)]?.mapNotNull { ITEMS.getValue(it) }


    companion object {
        fun <T : Any> ofForge(registry: IForgeRegistry<T>): ValidatedIdentifier = ValidatedIdentifier(
            registry.defaultKey ?: ResourceLocation.fromNamespaceAndPath("empty", "empty"),
            AllowableIdentifiers({ id -> registry.containsKey(id) }, { registry.keys.toList() }, true)
        )

        var DEFAULTS = mapOf(
            MobEffects.DIG_SLOWDOWN to listOf(
                TNT,
                SNOWBALL,
                ENDER_PEARL
            )
        ).map { MOB_EFFECTS.getKey(it.key)!! to it.value.mapNotNull(ITEMS::getKey) }

    }
}