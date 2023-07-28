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

    @Schema(name = "detailIdx", example = "UUID String", description = "디테일 UUID 스트링")
    private UUID detailIdx;

    @Schema(name = "detailHeader", example = "String Header", description = "디테일 헤더")
    private String detailHeader;

    //@Schema(name = "desc_header", example = "IMG URL String List", description = "이미지 URL 스트링 리스트")
    private List<String> imageUrls;

    @Schema(name = "content", example = "Example content", description = "main content")
    private String content;

    @Schema(name = "subContent", example = "Example subContent", description = "sub content")
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
