package com.example.spring.board;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardService boardService;

    private String uploadPath = "C:/uploads/board";

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
        
        try {
            // 파일 업로드 처리
            MultipartFile uploadFile = boardVo.getUploadFile();
            if (uploadFile != null && !uploadFile.isEmpty()) {
                String originalFileName = uploadFile.getOriginalFilename();
                String fileName = UUID.randomUUID().toString() + "_" + originalFileName;
                
                // 업로드 디렉토리가 없으면 생성
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                
                // 파일 저장
                File destFile = new File(uploadPath + File.separator + fileName);
                uploadFile.transferTo(destFile);
                
                // 파일 정보 설정
                boardVo.setFileName(fileName);
                boardVo.setOriginalFileName(originalFileName);
            }
            
            // 게시글 저장
            boolean created = boardService.create(boardVo);
            
            if (created) {
                redirectAttributes.addFlashAttribute("successMessage", "게시글이 등록되었습니다.");
                mav.setViewName("redirect:/board/");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "게시글이 등록되지 않았습니다.");
                mav.setViewName("redirect:/board/create/");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "파일 업로드 중 오류가 발생했습니다.");
            mav.setViewName("redirect:/board/create/");
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
