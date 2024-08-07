    package com.springboot.leadingbooks.services.dto.response;

    import com.springboot.leadingbooks.domain.entity.Book;
    import com.springboot.leadingbooks.domain.enum_.Category;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import java.util.List;
    import java.util.stream.Collectors;

    @Builder
    @Data
    @AllArgsConstructor
    public class BookReviewResponseDto {
        private Long bId;
        private String bName;
        private String bWriter;
        private String bPublish;
        private Category bCategory;
        private List<ReviewResponseDto> review;

        public static BookReviewResponseDto of(Book book) {
            return BookReviewResponseDto.builder()
                    .bId(book.getId())
                    .bName(book.getBName())
                    .bWriter(book.getBWriter())
                    .bPublish(book.getBPublish())
                    .bCategory(book.getBCategory())
                    .review(book.getReviewList().stream()
                            .map(ReviewResponseDto::of)
                            .collect(Collectors.toList()))
                    .build();

        }
    }
