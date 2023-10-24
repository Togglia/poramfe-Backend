package com.poramfe.poramfe.board.dto;

import com.poramfe.poramfe.board.domain.Board;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardDto {
    private Long id;
    private String title;
    private String content;
    private String writer;

    public BoardDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getWriter();
    }
}