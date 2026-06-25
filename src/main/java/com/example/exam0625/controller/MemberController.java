package com.example.exam0625.controller;

import com.example.exam0625.dto.MemberLoginDto;
import com.example.exam0625.dto.MemberRegisterDto;
import com.example.exam0625.entity.Member;
import com.example.exam0625.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("memberRegisterDto", new MemberRegisterDto());
        return "member/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute MemberRegisterDto dto, RedirectAttributes redirectAttributes) {
        try {
            memberService.register(dto);
            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");
            return "redirect:/member/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/member/register";
        }
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("memberLoginDto", new MemberLoginDto());
        return "member/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberLoginDto dto, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Member member = memberService.login(dto.getUsername(), dto.getPassword());
            session.setAttribute("loginMember", member);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/member/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String mypage(HttpSession session, Model model) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/member/login";
        }
        Member member = memberService.findById(loginMember.getId());
        model.addAttribute("member", member);
        return "member/mypage";
    }
}