package com.example.exam0625.controller;

import com.example.exam0625.dto.BoardSearchDto;
import com.example.exam0625.dto.BoardWriteDto;
import com.example.exam0625.entity.Board;
import com.example.exam0625.entity.Member;
import com.example.exam0625.service.BoardService;
import com.example.exam0625.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) String searchType,
                       @RequestParam(required = false) String keyword,
                       Model model) {

        if (keyword != null && !keyword.isEmpty()) {
            BoardSearchDto searchDto = new BoardSearchDto();
            searchDto.setSearchType(searchType);
            searchDto.setKeyword(keyword);
            searchDto.setPage(page);
            searchDto.setSize(size);

            int totalCount = boardService.countSearch(searchDto);
            model.addAttribute("boards", boardService.search(searchDto));
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);
            model.addAttribute("totalPages", (int) Math.ceil((double) totalCount / size));
            model.addAttribute("searchType", searchType);
            model.addAttribute("keyword", keyword);
        } else {
            Page<Board> boardPage = boardService.findAll(
                    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
            model.addAttribute("boards", boardPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);
            model.addAttribute("totalPages", boardPage.getTotalPages());
            model.addAttribute("totalCount", boardPage.getTotalElements());
        }

        return "board/list";
    }

    @GetMapping("/write")
    public String writeForm(HttpSession session, Model model) {
        if (session.getAttribute("loginMember") == null) {
            return "redirect:/member/login";
        }
        model.addAttribute("boardWriteDto", new BoardWriteDto());
        return "board/write";
    }

    @PostMapping("/write")
    public String write(@ModelAttribute BoardWriteDto dto, HttpSession session, RedirectAttributes redirectAttributes) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/member/login";
        }
        try {
            boardService.write(dto, loginMember);
            return "redirect:/board/list";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/board/write";
        }
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        boardService.increaseViewCount(id);
        Board board = boardService.findById(id);
        model.addAttribute("board", board);
        model.addAttribute("comments", commentService.findByBoardId(id));
        return "board/detail";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, HttpSession session, Model model) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/member/login";
        }
        Board board = boardService.findById(id);
        if (!board.getMember().getId().equals(loginMember.getId())) {
            return "redirect:/board/list";
        }

        BoardWriteDto dto = new BoardWriteDto();
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        model.addAttribute("boardWriteDto", dto);
        model.addAttribute("boardId", id);
        return "board/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute BoardWriteDto dto,
                       HttpSession session, RedirectAttributes redirectAttributes) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/member/login";
        }
        try {
            boardService.update(id, dto, loginMember);
            return "redirect:/board/detail/" + id;
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/board/list";
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/member/login";
        }
        try {
            boardService.delete(id, loginMember);
            redirectAttributes.addFlashAttribute("message", "게시글이 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/board/list";
    }

    @GetMapping("/my")
    public String myBoards(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           HttpSession session, Model model) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/member/login";
        }
        Page<Board> boardPage = boardService.findByMemberId(loginMember.getId(),
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        model.addAttribute("boards", boardPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", boardPage.getTotalPages());
        return "board/my";
    }
}