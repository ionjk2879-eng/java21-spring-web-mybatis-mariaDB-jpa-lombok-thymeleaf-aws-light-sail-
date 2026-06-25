package com.example.exam0625.controller;

import com.example.exam0625.dto.CommentWriteDto;
import com.example.exam0625.entity.Member;
import com.example.exam0625.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/write")
    public String write(@ModelAttribute CommentWriteDto dto, HttpSession session, RedirectAttributes redirectAttributes) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/member/login";
        }
        try {
            commentService.write(dto, loginMember);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/board/detail/" + dto.getBoardId();
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @RequestParam String content,
                       @RequestParam Long boardId, HttpSession session,
                       RedirectAttributes redirectAttributes) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/member/login";
        }
        try {
            commentService.update(id, content, loginMember);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/board/detail/" + boardId;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, @ModelAttribute("boardId") Long boardId,
                         HttpSession session, RedirectAttributes redirectAttributes) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/member/login";
        }
        try {
            commentService.delete(id, loginMember);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/board/detail/" + boardId;
    }
}
