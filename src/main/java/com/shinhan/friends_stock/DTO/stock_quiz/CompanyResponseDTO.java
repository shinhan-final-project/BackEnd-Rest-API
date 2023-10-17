package com.shinhan.friends_stock.DTO.stock_quiz;

import com.shinhan.friends_stock.domain.entity.InvestItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CompanyResponseDTO {

    private final long companyId;

    private final String companyName;

    private final String companyInfo;

    public static CompanyResponseDTO of(InvestItem company) {
        return new CompanyResponseDTO(
                company.getItemId(),
                company.getCompanyName(),
                company.getCompanyInfo()
        );
    }
}
