package com.metroflow.model.service;

import com.metroflow.model.dto.Board;
import com.metroflow.model.dto.BoardDTO;
import com.metroflow.model.dto.User;
import com.metroflow.repository.BoardRepository;
import com.metroflow.repository.SubwayStationRepository;
import com.metroflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository BOARDREPOSITORY;
    private final SubwayStationRepository SUBWAYSTATIONREPOSITORY;
    private final UserRepository USERREPOSITORY;

    public Map<String, List<String>> getStationNames() {
        List<String> lineOne =  SUBWAYSTATIONREPOSITORY.findByStationName("1");
        List<String> lineTwo =  SUBWAYSTATIONREPOSITORY.findByStationName("2");
        List<String> lineThree =  SUBWAYSTATIONREPOSITORY.findByStationName("3");
        List<String> lineFour =  SUBWAYSTATIONREPOSITORY.findByStationName("4");
        List<String> lineFive =  SUBWAYSTATIONREPOSITORY.findByStationName("5");
        List<String> lineSix =  SUBWAYSTATIONREPOSITORY.findByStationName("6");
        List<String> lineSeven =  SUBWAYSTATIONREPOSITORY.findByStationName("7");
        List<String> lineEight =  SUBWAYSTATIONREPOSITORY.findByStationName("8");
        List<String> lineNine =  SUBWAYSTATIONREPOSITORY.findByStationName("9");
        Map<String, List<String>> lineMap = new HashMap<>();
        lineMap.put("lineOne", lineOne);
        lineMap.put("lineTwo", lineTwo);
        lineMap.put("lineThree", lineThree);
        lineMap.put("lineFour", lineFour);
        lineMap.put("lineFive", lineFive);
        lineMap.put("lineSix", lineSix);
        lineMap.put("lineSeven", lineSeven);
        lineMap.put("lineEight", lineEight);
        lineMap.put("lineNine", lineNine);
        return lineMap;
    }

    @Transactional(readOnly = true)
    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 8; // 한 페이지에 보여줄 글 갯수
        // 한 페이지당 8개씩 글을 보여주고 정렬 기준은 boardNo 기준으로 내림차순 정렬
        // page 위치에 있는 값은 0부터 시작
        Page<Board> boards =
                BOARDREPOSITORY.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "boardNo")));
        System.out.println("boards.getContent() = " + boards.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boards.getTotalElements() = " + boards.getTotalElements()); // 전체 글갯수
        System.out.println("boards.getNumber() = " + boards.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boards.getTotalPages() = " + boards.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boards.getSize() = " + boards.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boards.hasPrevious() = " + boards.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boards.isFirst() = " + boards.isFirst()); // 첫 페이지 여부
        System.out.println("boards.isLast() = " + boards.isLast()); // 마지막 페이지 여부

        // 목록 : No, userId, title, createdTime
        // 엔티티 객체를 DTO 객체로 옮겨담음
        return boards.map(board -> new BoardDTO(board.getBoardNo(),
                BOARDREPOSITORY.findById(board.getBoardNo()).get().getUser(),
                board.getStationLine(), board.getTitle(), board.getCreatedTime(), board.getThumbsUp(),
                board.getView()));
    }
    // 보드 내용 얻어오기
    public Board getInfo(Long no) {
        String userId = BOARDREPOSITORY.findById(no).get().getUser().getUserId();
        User user = USERREPOSITORY.findByUserId(userId).get();
        Board board = BOARDREPOSITORY.findById(no).get();
        board.setUser(user);
        return board;
    }
}