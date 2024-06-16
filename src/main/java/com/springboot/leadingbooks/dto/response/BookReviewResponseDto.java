    package com.springboot.leadingbooks.dto.response;

    import com.springboot.leadingbooks.domain.entity.Book;
    import com.springboot.leadingbooks.domain.entity.Review;
    import com.springboot.leadingbooks.domain.enum_.Category;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.Getter;

    import java.util.List;

    @Builder
    @Data
    @AllArgsConstructor
    public class BookReviewResponseDto {
        private String bName;
        private String bWriter;
        private String bPublish;
        private Category bCategory;
        private List<Review> review;

        public static BookReviewResponseDto of(Book book) {
            return BookReviewResponseDto.builder()
                    .bName(book.getBName())
                    .bWriter(book.getBWriter())
                    .bPublish(book.getBPublish())
                    .bCategory(book.getBCategory())
                    .review(book.getReviewList())
                    .build();

        }
    }
