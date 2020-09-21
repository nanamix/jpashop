package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    //필드Injection
    
    //필드가 변경될 일이 없기에 final 로 두는 것을 권장
    private final MemberRepository memberRepository;

    // 생성자 Injection
//    @Autowired
//    public MemberService(MemberRepository memberRepository){
//        this.memberRepository = memberRepository;
//    }
    /*
    회원가입
     */
    //@Transactional의 Default는 false임.
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member); //중복회원검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //Exception
        List<Member> findMembers = memberRepository.findByName(member.getName());

        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다. ");
        }
    }

    //회원전체조회 , JPA 성능 최적화를 위해 readOnly = true로 둠.

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }
    
    //회원단건조회

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

}
