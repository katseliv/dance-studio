package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.EntityCreationException;
import com.dataart.dancestudio.exception.EntityNotFoundException;
import com.dataart.dancestudio.mapper.PostMapper;
import com.dataart.dancestudio.model.Role;
import com.dataart.dancestudio.model.dto.LikeDto;
import com.dataart.dancestudio.model.dto.PostDto;
import com.dataart.dancestudio.model.dto.view.PostViewDto;
import com.dataart.dancestudio.model.dto.view.ViewListPage;
import com.dataart.dancestudio.model.entity.LikeEntity;
import com.dataart.dancestudio.model.entity.LikeId;
import com.dataart.dancestudio.model.entity.PostEntity;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.repository.LikeRepository;
import com.dataart.dancestudio.repository.PostRepository;
import com.dataart.dancestudio.repository.UserRepository;
import com.dataart.dancestudio.utils.ParseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService, PaginationService<PostViewDto> {

    @Value("${pagination.defaultPageNumber}")
    private int defaultPageNumber;
    @Value("${pagination.defaultPageSize}")
    private int defaultPageSize;

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    @Transactional
    public int createPost(final PostDto postDto) {
        final Optional<UserEntity> userEntity = userRepository.findById(postDto.getUserAdminId());
        userEntity.ifPresentOrElse(
                (user) -> {
                    if (user.getRole() != Role.ADMIN) {
                        log.warn("Post hasn't been created.");
                        throw new EntityCreationException("User doesn't have role ADMIN!");
                    }
                    log.info("User has been found.");
                },
                () -> {
                    log.warn("User hasn't been found.");
                    throw new EntityNotFoundException("User not found!");
                }
        );
        final PostEntity postEntity = Optional.of(postDto)
                .map(postMapper::postDtoToPostEntity)
                .map(post -> {
                    post.setCreationDatetime(LocalDateTime.now());
                    return post;
                })
                .map(postRepository::save)
                .orElseThrow(() -> new EntityCreationException("Post hasn't been created!"));
        log.info("Post with id = {} has been created.", postEntity.getId());
        return postEntity.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public PostViewDto getPostViewById(final int id) {
        final Optional<PostEntity> postEntity = postRepository.findById(id);
        postEntity.ifPresentOrElse(
                (post) -> log.info("Post with id = {} has been found.", post.getId()),
                () -> log.warn("Post hasn't been found."));
        return postEntity.map(postMapper::postEntityToPostViewDto)
                .orElseThrow(() -> new EntityNotFoundException("Post not found!"));
    }

    @Override
    @Transactional
    public void updatePostById(final int id, final PostDto postDto) {
        final Optional<PostEntity> postEntity = postRepository.findById(id);
        postEntity.ifPresentOrElse(
                (post) -> log.info("Post with id = {} has been found.", post.getId()),
                () -> {
                    log.warn("Post hasn't been found.");
                    throw new EntityNotFoundException("Post not found!");
                });
        Optional.of(postDto)
                .map(postMapper::postDtoToPostEntity)
                .map((post) -> {
                    post.setId(id);
                    return postRepository.save(post);
                });
        log.info("Post with id = {} has been updated.", id);
    }

    @Override
    @Transactional
    public void deletePostById(final int id) {
        final Optional<PostEntity> postEntity = postRepository.findById(id);
        postEntity.ifPresentOrElse(
                (post) -> log.info("Post with id = {} has been found.", post.getId()),
                () -> {
                    log.warn("Post hasn't been found.");
                    throw new EntityNotFoundException("Post not found!");
                });
        postRepository.markAsDeletedById(id);
        log.info("Post with id = {} has been deleted.", id);
    }

    @Override
    @Transactional
    public void likePostById(final int id, final LikeDto likeDto) {
        final LikeId likeId = LikeId.builder().postId(id).userId(likeDto.getUserId()).build();
        final LikeEntity likeEntity = LikeEntity.builder().id(likeId).build();
        likeRepository.save(likeEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public ViewListPage<PostViewDto> getViewListPage(final String page, final String size) {
        final int pageNumber = Optional.ofNullable(page).map(ParseUtils::parsePositiveInteger).orElse(defaultPageNumber);
        final int pageSize = Optional.ofNullable(size).map(ParseUtils::parsePositiveInteger).orElse(defaultPageSize);

        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        final List<PostViewDto> listPosts = listPosts(pageable);
        final int totalAmount = numberOfPosts();

        return getViewListPage(totalAmount, pageSize, pageNumber, listPosts);
    }

    @Override
    public List<PostViewDto> listPosts(final Pageable pageable) {
        final List<PostEntity> postEntities = postRepository.findAll(pageable).getContent();
        log.info("There have been found {} posts.", postEntities.size());
        return postMapper.postEntitiesToPostViewDtoList(postEntities);
    }

    @Override
    public int numberOfPosts() {
        final int numberOfPosts = (int) postRepository.count();
        log.info("There have been found {} posts.", numberOfPosts);
        return numberOfPosts;
    }

}
