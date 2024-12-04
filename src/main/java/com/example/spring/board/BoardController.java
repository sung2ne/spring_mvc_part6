package com.example.spring.board;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

    private final String uploadPath = "C:/uploads/board";

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
                mav.setViewName("redirect:/board/create");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "파일 업로드 중 오류가 발생했습니다.");
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
        
        try {
            // 기존 게시글 정보 조회
            BoardVo existingBoardVo = boardService.read(boardVo.getSeq());
            if (existingBoardVo == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "게시글을 찾을 수 없습니다.");
                mav.setViewName("redirect:/board/");
                return mav;
            }

            // 파일 처리
            MultipartFile uploadFile = boardVo.getUploadFile();
            String existingFileName = existingBoardVo.getFileName();
            
            // 기존 파일 삭제 처리
            if (boardVo.isDeleteFile() || (uploadFile != null && !uploadFile.isEmpty())) {
                if (existingFileName != null) {
                    File fileToDelete = new File(uploadPath + File.separator + existingFileName);
                    if (fileToDelete.exists()) {
                        fileToDelete.delete();
                    }
                    // 파일 정보 초기화
                    boardVo.setFileName(null);
                    boardVo.setOriginalFileName(null);
                }
            } else {
                // 파일을 삭제하지 않고 유지하는 경우
                boardVo.setFileName(existingFileName);
                boardVo.setOriginalFileName(existingBoardVo.getOriginalFileName());
            }

            // 새 파일 업로드 처리
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

            // 게시글 수정
            boolean updated = boardService.update(boardVo);
            if (updated) {
                redirectAttributes.addFlashAttribute("successMessage", "게시글이 수정되었습니다.");
                mav.setViewName("redirect:/board/" + boardVo.getSeq());
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "게시글이 수정되지 않았습니다.");
                mav.setViewName("redirect:/board/" + boardVo.getSeq() + "/update");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "게시글 수정 중 오류가 발생했습니다.");
            mav.setViewName("redirect:/board/" + boardVo.getSeq() + "/update");
        }
        
        return mav;
    }

    // 게시글 삭제
    @PostMapping("/{seq}/delete")
    public ModelAndView deletePost(BoardVo boardVo, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        
        try {
            // 게시글 정보 조회
            BoardVo existingBoardVo = boardService.read(boardVo.getSeq());
            if (existingBoardVo == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "게시글을 찾을 수 없습니다.");
                mav.setViewName("redirect:/board/");
                return mav;
            }

            // 게시글 삭제
            boolean deleted = boardService.delete(boardVo);
            if (deleted) {
                // 파일 삭제
                if (existingBoardVo.getFileName() != null) {
                    File fileToDelete = new File(uploadPath + File.separator + existingBoardVo.getFileName());
                    if (fileToDelete.exists()) {
                        fileToDelete.delete();
                    }
                }
                redirectAttributes.addFlashAttribute("successMessage", "게시글이 삭제되었습니다.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "게시글이 삭제되지 않았습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "게시글 삭제 중 오류가 발생했습니다.");
        }
        
        mav.setViewName("redirect:/board/");
        return mav;
    }

    // 파일 다운로드
    @GetMapping("/{seq}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable("seq") int seq) {
        try {
            // 게시글 정보 조회
            BoardVo boardVo = boardService.read(seq);
            if (boardVo == null || boardVo.getFileName() == null) {
                return ResponseEntity.notFound().build();
            }

            // 파일 경로 생성
            Path filePath = Paths.get(uploadPath).resolve(boardVo.getFileName());
            Resource resource = new UrlResource(filePath.toUri());

            // 파일이 존재하고 읽을 수 있는지 확인
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            // 다운로드될 파일명 설정 (원본 파일명 사용)
            String downloadName = boardVo.getOriginalFileName();
            
            // 한글 파일명 처리
            String encodedDownloadName = new String(downloadName.getBytes("UTF-8"), "ISO-8859-1");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedDownloadName + "\"")
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}