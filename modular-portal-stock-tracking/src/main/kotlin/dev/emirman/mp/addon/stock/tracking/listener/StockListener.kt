package dev.emirman.mp.addon.stock.tracking.listener

import dev.emirman.mp.addon.notifier.service.mail.MailService
import dev.emirman.mp.addon.stock.tracking.config.StockTrackingConfig
import dev.emirman.mp.addon.stock.tracking.model.Settings
import dev.emirman.mp.addon.stock.tracking.model.Stock
import dev.emirman.mp.core.event.stock.StockUpdatedEvent
import net.lubble.util.AppContextUtil
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class StockListener {

    @EventListener(StockUpdatedEvent::class)
    fun onStockUpdated(event: StockUpdatedEvent) {
        val stock = event.stock
        val settings = StockTrackingConfig.settings

        val isMatch = settings.stocks.any { stock.matchesId(it.id) }

        if (!isMatch) return

        val tracking = settings.stocks.find { stock.matchesId(it.id) } ?: return

        val isLow = stock.amount <= tracking.min.value
        val isHigh = stock.amount >= tracking.max.value

        val minTypes = tracking.min.notification.types
        val maxTypes = tracking.max.notification.types

        val minTemplate = tracking.min.notification.template
        val maxTemplate = tracking.max.notification.template

        if (isLow) notify(event, tracking, settings, minTypes, minTemplate)
        if (isHigh) notify(event, tracking, settings, maxTypes, maxTemplate)
    }

    private fun notify(
        event: StockUpdatedEvent,
        tracking: Stock,
        settings: Settings,
        types: List<Stock.Notification.Type>,
        template: String
    ) {
        val stock = event.stock
        types.forEach { type ->
            when (type) {
                Stock.Notification.Type.SMS -> {
                    // send sms
                }

                Stock.Notification.Type.PUSH -> {
                    // send push
                }

                Stock.Notification.Type.MAIL -> {
                    val addresses = settings.addresses
                    val service = AppContextUtil.bean(MailService::class.java)

                    val data: Map<String, String> = mapOf(
                        "AMOUNT" to stock.amount.toString(),
                        "TRACKING_MIN" to tracking.min.value.toString(),
                        "TRACKING_MAX" to tracking.max.value.toString(),
                        "STOCK_ID" to stock.id.toString(),
                        "STOCK_KEY" to stock.sk.toKey(),
                        "PRODUCT_ID" to stock.product.id.toString(),
                        "PRODUCT_KEY" to stock.product.sk.toKey(),
                        "PRODUCT_TITLE" to stock.product.title,
                        "WAREHOUSE_TITLE" to (stock.warehouse?.title ?: ""),
                    )

                    addresses.forEach { to ->
                        service.send(to, template, data)
                    }
                }
            }
        }
    }
}