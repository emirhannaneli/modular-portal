package dev.emirman.mp.core.mapper.stock

import dev.emirman.mp.core.dto.stock.read.RBStock
import dev.emirman.mp.core.dto.stock.read.RStock
import dev.emirman.mp.core.dto.stock.update.UStock
import dev.emirman.mp.core.model.stock.Stock
import dev.emirman.mp.core.service.product.ProductService
import net.lubble.util.AppContextUtil
import net.lubble.util.mapper.BaseMapper

interface StockMapper : BaseMapper<Stock, RStock, RBStock, UStock> {
    override fun map(source: Stock): RStock {
        return RStock().apply {
            id = source.id
            sk = source.sk.toKey()
            product = service().map(source.product)
            amount = source.amount
            nonStock = source.nonStock
            createdAt = source.createdAt
            updatedAt = source.updatedAt
        }
    }

    override fun rbMap(source: Stock): RBStock {
        return RBStock().apply {
            id = source.id
            sk = source.sk.toKey()
            product = service().rbMap(source.product)
            amount = source.amount
            nonStock = source.nonStock
            createdAt = source.createdAt
            updatedAt = source.updatedAt
        }
    }

    private fun service(): ProductService = AppContextUtil.bean(ProductService::class.java)
}