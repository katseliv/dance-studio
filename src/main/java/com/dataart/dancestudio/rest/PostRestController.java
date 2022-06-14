package com.dataart.dancestudio.rest;

import com.dataart.dancestudio.model.dto.LikeDto;
import com.dataart.dancestudio.model.dto.PostDto;
import com.dataart.dancestudio.model.dto.view.PostViewDto;
import com.dataart.dancestudio.model.dto.view.ViewListPage;
import com.dataart.dancestudio.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
public class PostRestController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Integer> createPost(@RequestBody @Valid final PostDto postDto) {
        final int id = postService.createPost(postDto);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public PostViewDto getPost(@PathVariable final int id) {
        return postService.getPostViewById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePost(@PathVariable final int id, @RequestBody @Valid final PostDto postDto) {
        postService.updatePostById(id, postDto);
        return new ResponseEntity<>("Post was updated!", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable final int id) {
        postService.deletePostById(id);
    }

    @PostMapping("/{id}")
    public void likePost(@PathVariable final int id, @RequestBody @Valid final LikeDto likeDto) {
        postService.likePostById(id, likeDto);
    }

    @GetMapping
    public ViewListPage<PostViewDto> getPosts(@RequestParam(required = false) final Map<String, String> allParams) {
        return postService.getViewListPage(allParams.get("page"), allParams.get("size"));
    }

}
