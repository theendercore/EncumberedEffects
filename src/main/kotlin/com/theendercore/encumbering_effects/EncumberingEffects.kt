package com.theendercore.encumbering_effects

import me.fzzyhmstrs.fzzy_config.api.ConfigApi
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.AirItem
import net.minecraft.world.item.Item
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(EncumberingEffects.MODID)
object EncumberingEffects {
    const val MODID = "encumbering_effects"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MODID)
    fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MODID, path)
    var CONFIG = ConfigApi.registerAndLoadConfig(::EEConfig)

    init {
        EVENT_BUS.addListener(::onRightClickItem)
        EVENT_BUS.addListener(::onRightClickBlock)
        EVENT_BUS.addListener(::onEntityInteract)
    }


    fun onRightClickItem(event: PlayerInteractEvent.RightClickItem) {
        event.isCanceled = shouldStopUsage(event.entity, event.itemStack.item)
    }

    fun onRightClickBlock(event: PlayerInteractEvent.RightClickBlock) {
        event.isCanceled = shouldStopUsage(event.entity, event.itemStack.item)
    }

    fun onEntityInteract(event: PlayerInteractEvent.EntityInteract) {
        event.isCanceled = shouldStopUsage(event.entity, event.itemStack.item)
    }


    fun shouldStopUsage(entity: LivingEntity, item: Item): Boolean {
        if (item is AirItem) return false
        for (effect in entity.activeEffects) {
            val list = CONFIG.getItems(effect.effect) ?: continue
            if (list.isEmpty()) continue
            if (!list.contains(item)) continue

            return true
        }

        return false
    }
}