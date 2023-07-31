package com.sparta.spring_lv4.aop;

import com.sparta.spring_lv4.entity.Comment;
import com.sparta.spring_lv4.entity.Post;
import com.sparta.spring_lv4.entity.User;
import com.sparta.spring_lv4.entity.UserRoleEnum;
import com.sparta.spring_lv4.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.concurrent.RejectedExecutionException;

@Slf4j(topic = "UserCheckAop")
@Aspect
/*
* Aspect 클래스는 포인트컷(Pointcut)과 어드바이스(Advice)로 구성된다.
* 포인트컷 (Pointcut) : 어떤 메서드가 어드바이스를 적용할 대상인지를 정의.
*                    : 특정 메서드 실행 시점을 지정하기 위한 표현식이나 메서드 패턴을 정의할 수 있다.
* 어드바이스 (Advice) : 포인트컷이 지정한 시점에 실행되는 로직을 정의
*                   : 메서드 실행 전(before), 후(after), 예외 발생 시(after-throwing),
*                     정상 종료 시(after-returning) 등에 실행될 수 있다.
*/
@Component // 빈으로 등록되도록 지정하는 애노테이션
public class UserCheckAop {

    /*
    * execution(modifiers-pattern? return-type-pattern declaring-type-pattern? method-name-pattern(param-pattern) throws-pattern?)
    * execution(..) : 메서드 실행 시점을 지정하는 키워드
    * modifiers-pattern : 메서드의 접근 제어자를 지정 ex) public, private, protected, * 등
    * return-type-pattern : 메서드의 반환 타입 지정 ex) void, String, * 등
    * declaring-type-pattern : 메서드를 선언한 클래스의 패키지와 클래스 이름을 지정 ex) com.sparta.spring_lv4.service.*
    * method-name-pattern : 메서드의 이름을 지정 ex) updatePost, * 등
    * param-pattern : 메서드의 파라미터 타입을 지정 ex) (String, int) 등 특정 파라미터를 지정하거나 (..)와 같이 모든 파라미터를 선택할 수 있다.
    * throws-pattern : 메서드가 던질 수 있는 예외 타입을 지정
    */

    @Pointcut("execution(* com.sparta.spring_lv4.service.PostService.updatePost(..))")
    /*
    * : 모든 반환 타입
    com.sparta.spring_lv4.service.PostService 클래스의 updatePost 메서드를 지정
    (..) : 모든 파라미터를 의미. 즉, 파라미터 개수와 타입에 대해 일치하는 모든 메서드를 선택
    정리 : @Pointcut 은 com.sparta.spring_lv4.service.PostService 클래스의 updatePost 메서드를 실행 시점으로 지정하며,
          해당 메서드가 호출될 때 어드바이스를 실행하게 된다.
    */
    private void updatePost() {}

    @Pointcut("execution(* com.sparta.spring_lv4.service.PostService.deletePost(..))")
    private void deletePost() {}

    @Pointcut("execution(* com.sparta.spring_lv4.service.CommentService.updateComment(..))")
    private void updateComment() {}

    @Pointcut("execution(* com.sparta.spring_lv4.service.CommentService.deleteComment(..))")
    private void deleteComment() {}


    // updatePost() 메서드 또는 deletePost() 메서드가 실행되는 시점에 Around Advice를 적용
    @Around("updatePost() || deletePost()")

    // updatePost() 메서드 또는 deletePost() 메서드가 실행되기 전과 후에 실행되는 메서드
    // 이 메서드를 통해 사용자 권한 체크하여 예외 발생시키거나, joinPoint.proceed()를 호출하여 실제 메서드 실행
    public Object executePostRoleCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        // ProceedingJoinPoint : AOP의 Advice 에서 핵심 기능을 호출할 때 사용하는 객체
        //                       proceed() 메서드를 호출하여 실제 핵심 기능 실행

        // joinPoint.getArgs() 를 통해서 메서드의 인자들을 가져온다.
        // 첫번째 매개변수로 게시글 받아옴
        Post post = (Post)joinPoint.getArgs()[0];

        // Spring Security 의 SecurityContextHolder를 통해 현재 인증 정보를 가져온다.
        // 로그인 회원이 없는 경우, 수행시간 기록하지 않음
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 존재하고, 인증 주체가 UserDetailsImpl 클래스의 인스턴스인지 체크
        if (auth != null && auth.getPrincipal().getClass() == UserDetailsImpl.class) {
            // 현재 인증 주체에서 사용자 정보를 가져온다.
            UserDetailsImpl userDetails = (UserDetailsImpl)auth.getPrincipal();
            User loginUser = userDetails.getUser();

            // 게시글 작성자(post.user) 와 요청자(user) 가 같은지 또는 Admin 인지 체크 (아니면 예외발생)
            if (!(loginUser.getRole().equals(UserRoleEnum.ADMIN) || post.getUser().equals(loginUser))) {
                log.warn("[AOP] 작성자만 게시글을 수정/삭제 할 수 있습니다.");
                throw new RejectedExecutionException();
            }
        }

        // 실제 updatePost() 또는 deletePost() 메서드를 실행
        // 핵심기능 수행
        return joinPoint.proceed();
    }

    @Around("updateComment() || deleteComment()")
    public Object executeCommentRoleCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        // 첫번째 매개변수로 게시글 받아옴
        Comment comment = (Comment)joinPoint.getArgs()[0];

        // 로그인 회원이 없는 경우, 수행시간 기록하지 않음
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal().getClass() == UserDetailsImpl.class) {
            // 로그인 회원 정보
            UserDetailsImpl userDetails = (UserDetailsImpl)auth.getPrincipal();
            User loginUser = userDetails.getUser();

            // 댓글 작성자(comment.user) 와 요청자(user) 가 같은지 또는 Admin 인지 체크 (아니면 예외발생)
            if (!(loginUser.getRole().equals(UserRoleEnum.ADMIN) || comment.getUser().equals(loginUser))) {
                log.warn("[AOP] 작성자만 댓글을 수정/삭제 할 수 있습니다.");
                throw new RejectedExecutionException();
            }
        }

        // 핵심기능 수행
        return joinPoint.proceed();
    }
}