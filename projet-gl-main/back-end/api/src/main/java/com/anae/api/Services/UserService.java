package com.anae.api.Services;

import com.anae.api.Entities.Course;
import com.anae.api.Entities.UserData;
import com.anae.api.Enums.UserRole;
import com.anae.api.Repository.UserDataRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder encoder;

    private final UserDataRepository userDataRepository;

    public UserService(PasswordEncoder encoder, UserDataRepository userDataRepository) {
        this.encoder = encoder;
        this.userDataRepository = userDataRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String schoolEmail) throws UsernameNotFoundException {
        return userDataRepository.findBySchoolEmail(schoolEmail).orElseThrow(() -> new UsernameNotFoundException("user is not valid"));
    }


    public boolean changePassword(String oldPwd, String newPwd) {
        //First check if the oldPwd correspond to his current password and then change if it's the case
        return false;
    }

    public UserData getTeacher(Integer matricule) {
        return userDataRepository.findByMatriculeAndAuthoritiesAuthority(matricule, UserRole.TEACHER).orElseThrow();
    }

    public boolean affectTeacher(
            Integer matricule,
            Course course
    ) {
        try {
            var teacher = getTeacher(matricule);
            teacher.getTaughtCourses().add(course);
            course.getTeachers().add(teacher);

            course.getTeachers().add(teacher);

            userDataRepository.save(teacher);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public UserData load(Integer matricule) {
        return userDataRepository.findByMatricule(matricule).orElseThrow();
    }

    public void save(UserData user) {
        userDataRepository.save(user);
    }
}
