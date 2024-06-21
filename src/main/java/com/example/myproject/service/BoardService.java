package com.example.myproject.service;

import com.example.myproject.domain.Board;
import com.example.myproject.domain.User;
import com.example.myproject.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

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
}
