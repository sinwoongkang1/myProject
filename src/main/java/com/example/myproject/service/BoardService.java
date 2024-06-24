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

    public void save(Board board) {
        boardRepository.save(board);
    }


    public Board findBoardByTitle(String title) {
        return boardRepository.findByTitle(title);
    }

    public List<Board> findBoardByUser(User user) {
        return boardRepository.findBoardByUser(user);
    }
    public List<Board> findAll(){
        return boardRepository.findAll();
    }

    public Board findByUsernameAndBoardIdAndTemporaryFalse(String username,Long id) {
        return boardRepository.findByUsernameAndBoardIdAndTemporaryFalse(username,id);
    }

    public List<Board> findByTemporaryFalse(){
        return boardRepository.findByTemporaryFalse();
    }
    public List<Board> findByTemporaryTrue() {
        return boardRepository.findByTemporaryTrue();
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

    public Board UpdateBoard(Board board) {
        return boardRepository.save(board);
    }
}
