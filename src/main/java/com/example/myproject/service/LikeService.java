package com.example.myproject.service;

import com.example.myproject.domain.Board;
import com.example.myproject.domain.Liked;
import com.example.myproject.domain.User;
import com.example.myproject.repository.BoardRepository;
import com.example.myproject.repository.LikeRepository;
import com.example.myproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public void likeBoard(User user, Board board) {
        Liked liked = new Liked();
        liked.setUser(user);
        liked.setBoard(board);
        likeRepository.save(liked);
    }

    public List<Long> getUserIdsByBoardId(Long boardId) {
        return likeRepository.getUserIdsByBoardId(boardId);
    }
    public void unlikeBoard(User user, Board board) {
        Liked liked = likeRepository.findByUserAndBoard(user, board);
        if (liked != null) {
            likeRepository.delete(liked);
        }
    }
}
