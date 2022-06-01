package com.dataart.dancestudio.controller;

import com.dataart.dancestudio.model.dto.BookingDto;
import com.dataart.dancestudio.model.dto.LessonDto;
import com.dataart.dancestudio.model.dto.view.*;
import com.dataart.dancestudio.model.entity.Role;
import com.dataart.dancestudio.service.DanceStyleService;
import com.dataart.dancestudio.service.LessonService;
import com.dataart.dancestudio.service.RoomService;
import com.dataart.dancestudio.service.UserService;
import com.dataart.dancestudio.utils.ParseUtils;
import com.dataart.dancestudio.utils.SecurityContextFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/lessons")
public class LessonController {

    @Value("${pagination.defaultPageNumber}")
    private int defaultPageNumber;
    @Value("${pagination.defaultPageSize}")
    private int defaultPageSize;

    private final LessonService lessonService;
    private final UserService userService;
    private final DanceStyleService danceStyleService;
    private final RoomService roomService;
    private final SecurityContextFacade securityContextFacade;

    @PostMapping("/create")
    public String createLesson(@RequestParam(required = false) final Map<String, String> allParams,
                               @ModelAttribute("lesson") @Valid final LessonDto lessonDto, final Model model,
                               final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            prepareModel(allParams, model);
            if (securityContextFacade.getContext().getAuthentication().getAuthorities().contains(Role.TRAINER)) {
                return "forms/trainer_lesson_form";
            } else {
                final ViewListPage<UserForListDto> userViewListPage = userService
                        .getViewListPage(allParams.get("page"), allParams.get("size"));
                model.addAttribute("trainers", userViewListPage.getViewDtoList());
                return "forms/lesson_form";
            }
        }
        final int id = lessonService.createLesson(lessonDto);
        return "redirect:/lessons/" + id;
    }

    @GetMapping("/create")
    public String createLesson(@RequestParam(required = false) final Map<String, String> allParams, final Model model) {
        prepareModel(allParams, model);
        model.addAttribute("lesson", LessonDto.builder().build());
        if (securityContextFacade.getContext().getAuthentication().getAuthorities().contains(Role.TRAINER)) {
            return "forms/trainer_lesson_form";
        } else {
            final ViewListPage<UserForListDto> userViewListPage = userService
                    .getViewListPage(allParams.get("page"), allParams.get("size"));
            model.addAttribute("trainers", userViewListPage.getViewDtoList());
            return "forms/lesson_form";
        }
    }

    @GetMapping("/{id}")
    public String getLesson(@PathVariable final int id, final Model model) {
        model.addAttribute("lesson_view", lessonService.getLessonViewById(id));
        final Authentication authentication = securityContextFacade.getContext().getAuthentication();
        if (authentication.getAuthorities().contains(Role.ADMIN)) {
            return "infos/admin_lesson_info";
        } else {
            return "infos/lesson_info";
        }
    }

    @PutMapping("/{id}")
    public String updateLesson(@RequestParam(required = false) final Map<String, String> allParams,
                               @PathVariable final int id, @ModelAttribute("lesson") @Valid final LessonDto lessonDto,
                               final Model model, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            prepareModel(allParams, model);
            if (securityContextFacade.getContext().getAuthentication().getAuthorities().contains(Role.TRAINER)) {
                return "forms/trainer_lesson_edit";
            } else {
                final ViewListPage<UserForListDto> userViewListPage = userService
                        .getViewListPage(allParams.get("page"), allParams.get("size"));
                model.addAttribute("trainers", userViewListPage.getViewDtoList());
                return "forms/lesson_edit";
            }
        }
        lessonService.updateLessonById(lessonDto, id);
        return "redirect:/lessons/" + id;
    }

    @GetMapping("/{id}/update")
    public String updateLesson(@RequestParam(required = false) final Map<String, String> allParams,
                               @PathVariable final int id, final Model model) {
        prepareModel(allParams, model);
        model.addAttribute("lesson", lessonService.getLessonById(id));
        if (securityContextFacade.getContext().getAuthentication().getAuthorities().contains(Role.TRAINER)) {
            return "forms/trainer_lesson_edit";
        } else {
            final ViewListPage<UserForListDto> userViewListPage = userService
                    .getViewListPage(allParams.get("page"), allParams.get("size"));
            model.addAttribute("trainers", userViewListPage.getViewDtoList());
            return "forms/lesson_edit";
        }
    }

    @DeleteMapping("/{id}")
    public String deleteLesson(@PathVariable final int id) {
        lessonService.deleteLessonById(id);
        if (securityContextFacade.getContext().getAuthentication().getAuthorities().contains(Role.TRAINER)) {
            final Authentication authentication = securityContextFacade.getContext().getAuthentication();
            final String email = authentication.getName();
            final int userId = userService.getUserIdByEmail(email);
            return "redirect:/trainers/" + userId + "/lessons";
        }
        return "redirect:/lessons";
    }

    @GetMapping
    public String getLessons(@RequestParam(required = false) final Map<String, String> allParams, final Model model) {
        final int pageNumber = Optional.ofNullable(allParams.get("page")).map(ParseUtils::parsePositiveInteger).orElse(defaultPageNumber);
        final int pageSize = Optional.ofNullable(allParams.get("size")).map(ParseUtils::parsePositiveInteger).orElse(defaultPageSize);
        final FilteredViewListPage<LessonViewDto> filteredViewListPage = lessonService
                .getFilteredLessonViewListPage(allParams.get("page"), allParams.get("size"),
                        allParams.get("trainerName"), allParams.get("styleName"), allParams.get("date"));
        final List<DanceStyleViewDto> danceStyleViewListPage = danceStyleService
                .listDanceStyles(PageRequest.of(pageNumber, pageSize));

        model.addAttribute("lessonPage", filteredViewListPage);
        model.addAttribute("booking", BookingDto.builder().build());
        model.addAttribute("styles", danceStyleViewListPage);

        if (securityContextFacade.getContext().getAuthentication().getAuthorities().contains(Role.ADMIN)) {
            return "lists/admin_lesson_list";
        } else {
            return "lists/user_lesson_list";
        }
    }

    private void prepareModel(final Map<String, String> allParams, final Model model) {
        final int pageNumber = Optional.ofNullable(allParams.get("page")).map(ParseUtils::parsePositiveInteger).orElse(defaultPageNumber);
        final int pageSize = Optional.ofNullable(allParams.get("size")).map(ParseUtils::parsePositiveInteger).orElse(defaultPageSize);
        final List<DanceStyleViewDto> danceStyleViewListPage = danceStyleService
                .listDanceStyles(PageRequest.of(pageNumber, pageSize));
        final List<RoomViewDto> roomViewListPage = roomService
                .listRooms(PageRequest.of(pageNumber, pageSize));
        model.addAttribute("styles", danceStyleViewListPage);
        model.addAttribute("rooms", roomViewListPage);
    }

}
