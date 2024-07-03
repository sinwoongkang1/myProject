package com.example.myproject.service;

import com.example.myproject.domain.Board;
import com.example.myproject.domain.Liked;
import com.example.myproject.domain.User;
import com.example.myproject.repository.BoardRepository;
import com.example.myproject.repository.LikeRepository;
import com.example.myproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public void likeBoard(User user, Board board){
        Liked liked = new Liked(user,board);
        likeRepository.save(liked);
        int userCount = user.getLiked();
        int boardCount = board.getLiked();
        user.setLiked(++userCount);
        board.setLiked(++boardCount);
        userRepository.save(user);
        boardRepository.save(board);
    }


}
