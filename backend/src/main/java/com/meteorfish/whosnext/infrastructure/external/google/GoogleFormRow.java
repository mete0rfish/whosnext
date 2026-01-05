package com.meteorfish.whosnext.infrastructure.external.google;

import java.util.List;

public record GoogleFormRow(
        String email,
        String companyName,
        String title,
        String content,
        String tips,
        String rating
) {
    public static GoogleFormRow fromList(List<Object> row) {
        return new GoogleFormRow(
                getSafeString(row, 1), // 이메일
                getSafeString(row, 2), // 회사명
                getSafeString(row, 3), // 제목
                getSafeString(row, 4), // 내용
                getSafeString(row, 5), // 꿀팁
                getSafeString(row, 6)  // 평점
        );
    }

    private static String getSafeString(List<Object> row, int index) {
        if (index >= row.size() || row.get(index) == null) {
            return "";
        }
        return row.get(index).toString();
    }
}
