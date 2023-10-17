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

    private final int startYear;

    private final int endYear;

    public static CompanyResponseDTO of(InvestItem company) {
        return new CompanyResponseDTO(
                company.getItemId(),
                company.getCompanyName(),
                company.getCompanyInfo(),
                company.getQuizStartYear(),
                company.getQuizStartYear() + 5
        );
    }
}
