package com.service.ttucktak.dto.solution;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class SolutionDetailResDto {

    @Schema(name = "detailIdx", example = "UUID 문자열", description = "솔루션 디테일 UUID 스트링 문자열")
    private UUID detailIdx;

    @Schema(name = "detailHeader", example = "솔루션 제목", description = "솔루션 제목")
    private String detailHeader;

    @Schema(name = "imageUrls", description = "이미지 URL 스트링 리스트")
    private List<String> imageUrls;

    @Schema(name = "content", example = "Example 솔루션 내용", description = "솔루션 내용")
    private String content;

    @Schema(name = "subContent", example = "Example 솔루션 부연 설명", description = "솔루션 부연 설명")
    private String subContent;

    @Builder
    public SolutionDetailResDto(UUID detailIdx, String detailHeader, List<String> imageUrls, String content,String subContent) {
        this.detailIdx = detailIdx;
        this.detailHeader = detailHeader;
        this.imageUrls = imageUrls;
        this.content = content;
        this.subContent = subContent;
    }
}
