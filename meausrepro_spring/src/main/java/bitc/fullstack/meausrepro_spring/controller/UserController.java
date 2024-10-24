package bitc.fullstack.meausrepro_spring.controller;

import bitc.fullstack.meausrepro_spring.model.MeausreProCompany;
import bitc.fullstack.meausrepro_spring.model.MeausreProUser;
import bitc.fullstack.meausrepro_spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/MeausrePro/User")
public class UserController {
    @Autowired
    private UserService userService;

    // 웹페이지 전용
    @PostMapping("/webLogin")
    public MeausreProUser login(@RequestBody MeausreProUser loginUser) {
        System.out.println("\n" + loginUser.getId() + "\n");
        Optional<MeausreProUser> user = userService.findById(loginUser.getId());

        if (user.isPresent()) {
            if (Objects.equals(user.get().getTopManager(), "1")) {
                return user.get();
            } else {
                if (Objects.equals(user.get().getRole(), "0")) {
                    if (user.get().getPass().equals(loginUser.getPass())) {
                        // 작업그룹 상태 확인
                        MeausreProCompany company = user.get().getCompanyIdx(); // 사용자의 소속 작업그룹 정보 가져오기
                        if (company != null && company.getCompanyIng() == 'N') {
                            throw new IllegalArgumentException("작업그룹이 비활성화된 상태입니다. 로그인할 수 없습니다.");
                        }
                        return user.get();
                    }
                    else {
                        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
                    }
                } else {
                    throw new IllegalArgumentException("웹 관리자만 로그인 가능합니다.");
                }
            }
        } else {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
    }

    // 어플 전용
    @PostMapping("/AppLogin")
    public MeausreProUser Applogin(@RequestBody MeausreProUser loginUser) {
        System.out.println("\n" + loginUser.getId() + "\n");
        Optional<MeausreProUser> user = userService.findById(loginUser.getId());

        if (user.isPresent()) {
            if (Objects.equals(user.get().getRole(), "1")) {
                if (user.get().getPass().equals(loginUser.getPass())) {
                    return user.get();
                }
                else {
                    throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
                }
            } else {
                throw new IllegalArgumentException("앱 관리자만 로그인 가능합니다.");
            }
        } else {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
    }

    // 아이디 중복 확인
    @PostMapping("/checkId/{id}")
    public boolean checkId(@PathVariable String id) {
        return userService.checkId(id) == 0;
    }

    // 회원가입
    @PostMapping("/SignUp")
    public MeausreProUser signUp(@RequestBody MeausreProUser signUpUser) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = sdf.format(new Date());

        signUpUser.setCreateDate(formattedDate);
        return userService.signUp(signUpUser);
    }

    // 전체 관리자 겸 웹 관리자 제외 회원정보 보기
    @GetMapping("/notTopManager")
    public List<MeausreProUser> getNotTopManager() {
        List<MeausreProUser> users = userService.getNotTopManager();
        System.out.println("\n" + users.size());
        return users;
    }

    // 비밀번호 확인
    @PostMapping("/checkPassword/{id}/{password}")
    public boolean checkPassword(@PathVariable String id, @PathVariable String password) {
        return userService.checkPasswrod(id, password);
    }

    // 회원정보 수정
    @PutMapping("/update/{idx}")
    public ResponseEntity<String> updateUser(@PathVariable int idx, @RequestBody MeausreProUser updateUser) {
        Optional<MeausreProUser> userOptional = userService.findByIdx(idx);
        
        if (userOptional.isPresent()) {
            MeausreProUser existingUser = userOptional.get();
            
            existingUser.setPass(updateUser.getPass());
            existingUser.setName(updateUser.getName());
            existingUser.setCompanyIdx(updateUser.getCompanyIdx());
            existingUser.setTel(updateUser.getTel());
            existingUser.setRole(updateUser.getRole());
            
            userService.signUp(existingUser);
            
            return ResponseEntity.ok("회원정보 수정 완료");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원정보 수정에 실패하였습니다.");
        }
    }

    // 회원정보 삭제
    @DeleteMapping("/delete/{idx}")
    public ResponseEntity<String> deleteUser(@PathVariable("idx") int idx) {
        return userService.deleteUser(idx);
    }
}
