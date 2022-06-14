package com.dataart.dancestudio.service;

import com.dataart.dancestudio.model.dto.LikeDto;
import com.dataart.dancestudio.model.dto.PostDto;
import com.dataart.dancestudio.model.dto.view.PostViewDto;
import com.dataart.dancestudio.model.dto.view.ViewListPage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    int createPost(PostDto postDto);

    PostViewDto getPostViewById(int id);

    void updatePostById(int id, PostDto postDto);

    void deletePostById(int id);

    void likePostById(int id, LikeDto likeDto);

    ViewListPage<PostViewDto> getViewListPage(String page, String size);

    List<PostViewDto> listPosts(Pageable pageable);

    int numberOfPosts();

}
