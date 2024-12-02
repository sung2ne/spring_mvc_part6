package com.example.spring.board;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardService boardService;

    // 게시글 등록
    @GetMapping("/create")
    public ModelAndView createGet() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("board/create");
        return mav;
    }

    // 게시글 등록
    @PostMapping("/create")
    public ModelAndView createPost(BoardVo boardVo, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        boolean created = boardService.create(boardVo);
        if (created) {
            redirectAttributes.addFlashAttribute("successMessage", "게시글이 등록되었습니다.");
            mav.setViewName("redirect:/board/");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "게시글이 등록되지 않았습니다.");
            mav.setViewName("redirect:/board/create");
        }
        return mav;
    }

    // 게시글 목록
    @GetMapping("/")
    public ModelAndView listWithSearchGet(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(required = false) String searchType,
        @RequestParam(required = false) String searchKeyword
    ) {
        ModelAndView mav = new ModelAndView();
        Map<String, Object> result = boardService.list(page, searchType, searchKeyword);
        mav.addObject("boardVoList", result.get("boardVoList"));
        mav.addObject("pagination", result.get("pagination"));
        mav.addObject("searchType", result.get("searchType"));
        mav.addObject("searchKeyword", result.get("searchKeyword"));
        mav.setViewName("board/list");
        return mav;
    }

    // 게시글 보기
    @GetMapping("/{seq}")
    public ModelAndView readGet(@PathVariable("seq") int seq) {
        ModelAndView mav = new ModelAndView();
        BoardVo boardVo = boardService.read(seq);
        mav.addObject("boardVo", boardVo);
        mav.setViewName("board/read");
        return mav;
    }

    // 게시글 수정
    @GetMapping("/{seq}/update")
    public ModelAndView updateGet(@PathVariable("seq") int seq) {
        ModelAndView mav = new ModelAndView();
        BoardVo boardVo = boardService.read(seq);
        mav.addObject("boardVo", boardVo);
        mav.setViewName("board/update");
        return mav;
    }

    // 게시글 수정
    @PostMapping("/{seq}/update")
    public ModelAndView updatePost(BoardVo boardVo, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        boolean updated = boardService.update(boardVo);
        if (updated) {
            redirectAttributes.addFlashAttribute("successMessage", "게시글이 수정되었습니다.");
            mav.setViewName("redirect:" + boardVo.getSeq());
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "게시글이 수정되지 않았습니다.");
            mav.setViewName("redirect:/board/" + boardVo.getSeq() + "/update");
        }
        return mav;
    }

    // 게시글 삭제
    @PostMapping("/{seq}/delete")
    public ModelAndView deletePost(BoardVo boardVo, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        boolean deleted = boardService.delete(boardVo);
        if (deleted) {
            redirectAttributes.addFlashAttribute("successMessage", "게시글이 삭제되었습니다.");
            mav.setViewName("redirect:/board/");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "게시글이 삭제되지 않았습니다.");
            mav.setViewName("redirect:/board/" + boardVo.getSeq());
        }
        return mav;
    }
}
