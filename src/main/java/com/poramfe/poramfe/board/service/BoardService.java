package com.poramfe.poramfe.board.service;

import com.poramfe.poramfe.board.domain.Board;
import com.poramfe.poramfe.board.repository.BoardRepository;
import com.poramfe.poramfe.user.security.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class BoardService {

    @Autowired
    private TokenProvider tokenProvider;
    private final BoardRepository boardRepository;
    public List<Board> findBoards() {
        return boardRepository.findAll();
    }

    public Board findOne(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(NullPointerException::new);
    }

    @Transactional
    public void create(Board board) {

        boardRepository.save(board);
    }

    @Transactional
    public String update(Long id, String title, String content, String writer, HttpServletRequest request) {

        try {
            String token = parseBearerToken(request);
            if (token != null && !token.equalsIgnoreCase("null")) {
                String userNickname = tokenProvider.validate(token);

                if (userNickname == null) {
                    return "잘못된 접근입니다.";
                } else {
                    Optional<Board> boardOptional = boardRepository.findById(id);
                    if (boardOptional.isPresent()) {
                        Board BoardToUpdate = boardOptional.get();

                        if (!BoardToUpdate.getWriter().equals(userNickname)) {
                            return "작성자와 로그인 사용자가 다릅니다.";
                        }
                        Board findBoard = boardRepository.findById(id).orElseThrow(NullPointerException::new); // 영속성 컨텍스트
                        findBoard.setTitle(title); // Dirty Checking
                        findBoard.setContent(content);
                    }
                }
            }
        }catch (Exception exception) {
            exception.printStackTrace();
        }
        return "INFO) Memo update fail";

    }
    @Transactional

    public void delete(Board board, HttpServletRequest request, Long id) {
        boardRepository.delete(board);
        /*
        try {
            String token = parseBearerToken(request);
            if (token != null && !token.equalsIgnoreCase("null")) {
                String userNickname = tokenProvider.validate(token);

                LocalDateTime nowDate = LocalDateTime.now();
                String nowDate_Img = nowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"));

                System.out.println("\n" + nowDate_Img + ") ID : " + id + " 메모 삭제 시도");

                Optional<Board> boardOptional = boardRepository.findById(id);
                if (boardOptional.isPresent()) {
                    String writer = boardOptional.get().getWriter();

                    if (writer.equals(userNickname)) {
                        boardRepository.delete(board);
                        System.out.println("메모 삭제 완료");
                        return "메모 삭제 완료";
                    } else {
                        System.out.println("작성자와 로그인 사용자가 다릅니다.");
                        return "작성자와 로그인 사용자가 다릅니다.";
                    }
                } else {
                    System.out.println("해당 ID의 메모가 없습니다.");
                    return "해당 ID의 메모가 없습니다.";
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "An error occurred";
        */
    }


    private String parseBearerToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");

        //문자를 가지고 있는지 bearerToken이 맞는지 확인, 맞으면 7번째 문자부터 가져온다.
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);
        return null;
    }
}