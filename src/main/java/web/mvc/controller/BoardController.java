package web.mvc.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.Board;
import web.mvc.domain.Member;
import web.mvc.dto.BoardReq;
import web.mvc.dto.BoardRes;
import web.mvc.exception.DMLException;
import web.mvc.exception.ErrorCode;
import web.mvc.security.CustomMemberDetails;
import web.mvc.service.BoardService;
import web.mvc.service.BoardServiceImpl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@Tag(name = "BoardController API", description = "Security Swagger 테스트용 API")
public class BoardController {
	private final BoardService boardService;

   /**
	* 전체 게시물 조회
	* */
	@GetMapping("/boards")
	public ResponseEntity<?> findAll(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info("authentication = {}" , authentication);

		//시큐리티에 저장된 정보 조회
		String name = authentication.getName();//아이디
		log.info("Authentication getName =  {} " , name);
		log.info("Authentication  authentication.getPrincipal() =  {} " ,  authentication.getPrincipal());
		if(name!=null && !name.equals("anonymousUser")) {
			CustomMemberDetails customMemberDetails = (CustomMemberDetails) authentication.getPrincipal();
			Member m = customMemberDetails.getMember();
			log.info("customMemberDetails =  {} ,{} ,{} ", m.getId(), m.getName(), m.getRole());


			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

			Iterator<? extends GrantedAuthority> iter = authorities.iterator();
			while (iter.hasNext()) {
				GrantedAuthority auth = iter.next();
				String role = auth.getAuthority();
				log.info("Authentication role =  {} ", role);
			}

		}

		return new ResponseEntity<>(boardService.findAllBoard(),HttpStatus.OK);
	}

	@GetMapping("/boardsTest")
	public List<BoardRes> findAll2(){
		return boardService.findAllBoard();
	}

	/**
	 * 글번호에 해당하는 게시물 조회
	 * */
	@GetMapping("/boards/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id){
		Board board = boardService.findBoard(id);

		//return new ResponseEntity<>(board ,HttpStatus.OK);
		/////////////////////////////////////////////////////
		BoardRes boardRes = new BoardRes(board);
		return new ResponseEntity<>(boardRes ,HttpStatus.OK);
	}
    /**
	 * 게시물 등록
	 * */
	@PostMapping("/boards")
	public ResponseEntity<?> save(@RequestBody BoardReq board  , // 오센티케이션프린시펄 받을수 있음
								  @AuthenticationPrincipal CustomMemberDetails customMemberDetails){

        log.info("board ={}", board);
        log.info("customMemberDetails ={}", customMemberDetails);
		if( customMemberDetails.getMember().getMemberNo() != board.getMemberNo()){
			//로그인정보자 != 글등록자 사용자 번호 다르면 >예외처리
			throw new DMLException(ErrorCode.INSERT_FAILED);
			//로그인정보자 != 글등록자 사용자 번호 다르면 >예외처리
		}

		return new ResponseEntity<>(boardService.addBoard(board) ,HttpStatus.CREATED);//201
	}

	/**
	 * 글번호에 해당하는 게시물 수정
	 */
	@PutMapping("/boards/{id}")
	public ResponseEntity<?> update(@PathVariable Long id,@RequestBody BoardReq board){
		System.out.println("id = " + id);
		Board updatedBoard = boardService.updateBoard(id, board);
		System.out.println("updatedBoard = " + updatedBoard);

		BoardRes br = BoardRes.builder()
				.title(updatedBoard.getTitle())
				.regDate(updatedBoard.getRegDate().toString())
				.content(updatedBoard.getContent())
				.id(updatedBoard.getId()).build();

		return new ResponseEntity<>(br,HttpStatus.OK);
	}
	/**
	 * 글번호에 해당하는 게시물 삭제
	 * */
	@DeleteMapping("/boards/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){		
		return new ResponseEntity<>(boardService.deleteBoard(id),HttpStatus.OK);
	}
}




