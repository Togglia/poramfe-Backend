package com.poramfe.poramfe.board.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import jakarta.persistence.Table;


@Entity(name = "board")
@Data // @Getter @Setter
@Table(name = "board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {
    @Id @GeneratedValue
    private Long id;

    private String title;
    private String content;
    private String writer;

    public Board(Long id, String title, String content,String writer) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
    }
}