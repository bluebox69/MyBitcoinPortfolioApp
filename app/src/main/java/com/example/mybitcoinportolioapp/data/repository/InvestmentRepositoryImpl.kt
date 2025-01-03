package com.example.mybitcoinportolioapp.data.repository

import com.example.mybitcoinportolioapp.data.local.dao.InvestmentDao
import com.example.mybitcoinportolioapp.data.local.entities.InvestmentEntity
import com.example.mybitcoinportolioapp.domain.repository.InvestmentRepository

class InvestmentRepositoryImpl(
    private val dao: InvestmentDao
) : InvestmentRepository {
    override suspend fun getAllInvestments(): List<InvestmentEntity> = dao.getAllInvestments()

    override suspend fun addInvestment(investment: InvestmentEntity) {
        dao.insertInvestment(investment)
    }

    override suspend fun clearInvestments() {
        dao.clearInvestments()
    }
}