package com.example.myproject.service;

import com.example.myproject.domain.Board;
import com.example.myproject.domain.User;
import com.example.myproject.repository.BoardRepository;
import com.example.myproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public Board findById(Long id){
        return boardRepository.findById(id).orElse(null);
    }

    public void save(Board board) {
        boardRepository.save(board);
    }

    public Board findByUsernameAndBoardIdAndTemporaryFalse(String username,Long id) {
        return boardRepository.findByUsernameAndBoardIdAndTemporaryFalse(username,id);
    }

    public Board findByUsernameAndBoardIdAndTemporaryTrue(String username,Long id) {
        return boardRepository.findByUsernameAndBoardIdAndTemporaryTrue(username,id);
    }

    public Board findBoardByUserIdAndContent(Long userId,String content) {
        return boardRepository.findBoardByUserIdAndContent(userId,content);
    }

    public List<Board> findByTemporaryFalse(){
        return boardRepository.findByTemporaryFalse();
    }

    public void saveTemporary(Board board,String username) {
        User user = userRepository.findByUsername(username);
        board.setUser(user);
        board.setTitle(board.getTitle());
        board.setContent(board.getContent());
        board.setTemporary(true);
        boardRepository.save(board);
    }

    public List<Board> findByBoardUserAndTemporaryFalse(User user) {
        return boardRepository.findBoardByUserAndTemporaryFalse(user);
    }

    public List<Board> findByBoardUserAndTemporaryTrue(User user) {
        return boardRepository.findBoardByUserAndTemporaryTrue(user);
    }

    public void deleteBoard(Board board) {
        boardRepository.delete(board);
    }

    public List<Board> findBoardByUserId (Long id) {
    return boardRepository.findBoardByUserId(id);}


}
