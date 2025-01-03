package com.example.mybitcoinportolioapp.domain.use_case.investment

import com.example.mybitcoinportolioapp.data.local.entities.InvestmentEntity
import com.example.mybitcoinportolioapp.domain.repository.InvestmentRepository

class GetInvestmentsUseCase(
    private val investmentRepository: InvestmentRepository
) {
    suspend operator fun invoke(): List<InvestmentEntity> {
        return investmentRepository.getAllInvestments()
    }
}
