package com.poramfe.poramfe.board.api;

import com.poramfe.poramfe.board.domain.Board;
import com.poramfe.poramfe.board.dto.BoardDeleteDto;
import com.poramfe.poramfe.board.dto.BoardDto;
import com.poramfe.poramfe.board.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
public class BoardApiController {
    private final BoardService boardService; // Autowired로 스프링 빈에 등록

    @GetMapping("/api/board-list")
    public WrapperClass board_list(){
        List<Board> boardList = boardService.findBoards();
        List<BoardDto> boardDtoList = boardList.stream().map(b -> new BoardDto(b)).collect(Collectors.toList());
        return new WrapperClass(boardDtoList);
    }
    @GetMapping("/api/board-detail/{id}")
    public WrapperClass board_detail(@PathVariable("id") Long id){
        Board board = boardService.findOne(id);
        BoardDto boardDto = new BoardDto(board);
        return new WrapperClass(boardDto);
    }
    @PostMapping("/api/create-board")
    public ResponseEntity create_board(@RequestBody BoardDto boardDto){
        System.out.println("create_board/boardDto = " + boardDto);
        HttpHeaders headers = new HttpHeaders();
        Map<String, String> body = new HashMap<>();
        HttpStatus status = HttpStatus.CREATED; // 201 잘 생성되었음을 의미
        try{
            Board board = new Board(
                    boardDto.getId(),
                    boardDto.getTitle(),
                    boardDto.getContent(),
                    boardDto.getWriter()
            );
            boardService.create(board);
        } catch (Exception exception){
            status = HttpStatus.BAD_REQUEST; // 400 에러
            System.out.println("create_board/exception = " + exception);
        }
        return new ResponseEntity(body, headers, status);
    }
    @PutMapping("/api/update-board")
    public ResponseEntity update_board(@RequestBody BoardDto boardDto,HttpServletRequest request){
        System.out.println("update_board/boardDto = " + boardDto);
        HttpHeaders headers = new HttpHeaders();
        Map<String, String> body = new HashMap<>();
        HttpStatus status = HttpStatus.NO_CONTENT; // 204 -> 수정이 정상적으로 완료됬음을 의미
        try{
            boardService.update(boardDto.getId(), boardDto.getTitle(), boardDto.getContent(), boardDto.getWriter(), request);

        } catch (Exception exception){
            status = HttpStatus.BAD_REQUEST; // 400 에러
            System.out.println("update_board/exception = " + exception);
        }
        return new ResponseEntity(body, headers, status);
    }
    @DeleteMapping("/api/delete-board")
    public ResponseEntity delete_board(@RequestBody BoardDeleteDto boardDeleteDto,HttpServletRequest request,Long id){
        System.out.println("delete_board/boardDeleteDto = " + boardDeleteDto);
        HttpHeaders headers = new HttpHeaders();
        Map<String, String> body = new HashMap<>();
        HttpStatus status = HttpStatus.NO_CONTENT; // 204
        try{
            Board board = boardService.findOne(boardDeleteDto.getId());
            boardService.delete(board,request,id);
        } catch (Exception exception){
            status = HttpStatus.BAD_REQUEST;
            System.out.println("delete_board/exception = " + exception);
        }
        return new ResponseEntity(body, headers, status);
    }
}